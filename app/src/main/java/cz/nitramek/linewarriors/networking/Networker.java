package cz.nitramek.linewarriors.networking;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import cz.nitramek.linewarriors.game.utils.Constants;
import cz.nitramek.linewarriors.game.utils.Monster;

public class Networker implements NsdHelper.ServiceListener {

    public static final String HELLO_MESSAGE = "hello";
    private final ListeningThread listeningThread;
    private final Context context;

    private SendThread sendingThread;

    private InetAddress other;
    private int otherPort;


    private InetAddress thisAddress;
    private int thisPort;
    private GameActivityListener gameActivityListener;


    public Networker(Context context) {
        this.context = context;
        listeningThread = new ListeningThread();
    }

    @Override
    public void connectTo(InetAddress address, int port) {
        this.otherPort = port;
        this.other = address;
        this.sendMessage(HELLO_MESSAGE);
        Log.i("Networker", String.format("connectTo: %s,%d", other, port));
        Toast.makeText(this.context, "Server nalezen", Toast.LENGTH_SHORT).show();
    }

    @Override
    public InetSocketAddress startAsServer() {
        listeningThread.setDaemon(true);
        listeningThread.start();
        synchronized (listeningThread) {
            try {
                listeningThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("Networking", String.format("Started server as %s, %d", thisAddress.toString(), thisPort));
        return new InetSocketAddress(this.thisAddress, this.thisPort);
    }

    public void sendMessage(String message) {
        if (this.other != null) {
            this.sendingThread = new SendThread();
            this.sendingThread.message = message;
            this.sendingThread.start();
        }
    }

    @Override
    public void stop() {
        try {
            listeningThread.running = false;
            listeningThread.join();
            if (sendingThread != null) {
                sendingThread.join();
            }
        } catch (InterruptedException e) {
            Log.e(Networker.class.getName(), "", e);
        }
    }


    public void setOnMonsterListener(GameActivityListener monsterListener) {
        this.gameActivityListener = monsterListener;
    }


    public interface GameActivityListener {
        void onMonsterRecieved(Monster monster);

        void gameOver();
    }

    class ListeningThread extends Thread {
        boolean running;

        @Override
        public void run() {
            running = true;
            ServerSocket server = null;
            try {
                server = new ServerSocket(0);
                thisAddress = server.getInetAddress();
                thisPort = server.getLocalPort();
                synchronized (this) {
                    this.notifyAll();
                }
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }
            while (running) {
                try {
                    final Socket client = server.accept();
                    ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
                    final String message = (String) ois.readObject();
                    if (message.contains(HELLO_MESSAGE)) {
                        other = client.getInetAddress();
                        otherPort = ois.readInt();
                        Log.d("Networking", String.format("Got hello message: %s, %d", other.toString(), otherPort));
                        Toast.makeText(Networker.this.context, "Klient nalezen", Toast.LENGTH_SHORT).show();

                    }else if(message.contains(Constants.GAME_OVER_MSS)){
                        gameActivityListener.gameOver();
                    }
                    else {
                        Monster m = Monster.valueOf(message);
                        if (gameActivityListener != null) {
                            gameActivityListener.onMonsterRecieved(m);
                        }

                    }
                    ois.close();
                    //add monster
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    running = false;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (server != null) {
                    server.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    class SendThread extends Thread {
        boolean running;
        String message;

        @Override
        public void run() {
            try {
                Socket socket = new Socket(other, otherPort);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(message);
                if (message.contains(HELLO_MESSAGE)) {
                    oos.writeInt(thisPort);
                }
                oos.flush();
                oos.close();


                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }

        }
    }
}
