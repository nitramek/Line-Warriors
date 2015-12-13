package cz.nitramek.linewarriors.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cz.nitramek.linewarriors.R;
import cz.nitramek.linewarriors.util.Role;

public class MainActivity extends AppCompatActivity {

    public static final int CONNECT_AS_SERVER = 1;
    public static final String EXTRA_CONNECT_AS = "CONNECT_AS";
    public static final int CONNECT_AS_CLIENT = 2;
    public static final int CONNECT_AS_NONE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button clientButton = (Button) findViewById(R.id.main_btn_start_as_client);
        Button serverButton = (Button) findViewById(R.id.main_btn_start_as_server);
        Button trainingButton = (Button) findViewById(R.id.main_btn_start_training);
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra(EXTRA_CONNECT_AS, Role.CLIENT);
                Toast.makeText(MainActivity.this, "Hledám klienty", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra(EXTRA_CONNECT_AS, Role.SERVER);
                Toast.makeText(MainActivity.this, "Hledám servery", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.welcome);
        mediaPlayer.start();
        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra(EXTRA_CONNECT_AS, Role.NONE);
                startActivity(intent);
            }
        });
        TextView tv = (TextView) findViewById(R.id.main_tv_kills);
        final SharedPreferences preferences = this.getPreferences(MODE_PRIVATE);
        tv.setText(String.format("Max kills in traing mode: %d", preferences.getInt("KILLS", 0)));
    }

}
