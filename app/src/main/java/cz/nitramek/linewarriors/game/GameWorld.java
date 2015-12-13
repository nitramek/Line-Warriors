package cz.nitramek.linewarriors.game;

import java.util.ArrayList;
import java.util.List;

import cz.nitramek.linewarriors.game.objects.Enemy;
import cz.nitramek.linewarriors.game.objects.Mage;
import cz.nitramek.linewarriors.game.objects.MainCharacter;
import cz.nitramek.linewarriors.game.objects.Spell;
import cz.nitramek.linewarriors.game.objects.Sprite;
import cz.nitramek.linewarriors.game.objects.Square;
import cz.nitramek.linewarriors.game.utils.GameRendererListener;
import cz.nitramek.linewarriors.game.utils.TextureKey;
import cz.nitramek.linewarriors.game.utils.TextureManager;
import cz.nitramek.linewarriors.game.utils.Vector;

/**
 * Must be instantiated
 */
public class GameWorld implements Runnable {
    public static final float LINE_Y = -0.75f;
    private final GameRendererListener listener;
    /**
     * Reprezents enemies in line, one enemy per given width
     */
    private Enemy[] enemies;
    private MainCharacter mainCharacter;

    private List<Spell> spells;

    /**
     * Reprezents square with 1/4 UV mapping
     */
    private Square square4;

    private Thread worldThread;
    private boolean running;


    public GameWorld(final GameRendererListener listener) {
        this.listener = listener;
        this.enemies = new Enemy[3];
        this.square4 = new Square(4);
        final Sprite mainCharacterSprite = new Sprite(square4, 4, 4, TextureManager.getInstance().getTextureId(TextureKey.MAGE));
        mainCharacterSprite.getModelMatrix().scale(0.15f, 0.15f * this.listener.getRatio());
        this.mainCharacter = new Mage(mainCharacterSprite);
        synchronized (this) {
            this.notifyAll();
        }
        this.listener.addDrawable(mainCharacterSprite);
        this.addEnemy();
//        this.addEnemy();
//        this.addEnemy();
        this.worldThread = new Thread(this);
        this.worldThread.setDaemon(true);
        this.worldThread.start();
        this.spells = new ArrayList<>();

    }

    public void addEnemy() {
        int freeIndex = findFreeSpot();
        if (freeIndex > -1) {
            final Sprite enemySprite = new Sprite(square4, 4, 4, TextureManager.getInstance().getTextureId(TextureKey.JENOVA));
//            enemySprite.getModelMatrix().translate(-0.75f / 0.15f + freeIndex * 0.75f / 0.22f, 1.3f / 0.15f * this.listener.getRatio());
            enemySprite.getModelMatrix().scale(0.15f, 0.15f * this.listener.getRatio());
            enemySprite.getModelMatrix().setPosition(-0.75f, 0.75f);
            Enemy e = new Enemy(5, 10, enemySprite);
            enemies[freeIndex] = e;
            this.listener.addDrawable(enemySprite);
        }

    }

    private int findFreeSpot() {
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i] == null) {
                return i;
            }
        }
        return -1;
    }


    public MainCharacter getMainCharacter() {
        synchronized (this) {
            if (this.mainCharacter == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return mainCharacter;
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            for (int i = 0; i < enemies.length; i++) {
                Enemy e = enemies[i];
                boolean collision = false;
                for (Spell s : spells) {
                    //při kolizi způsobí damage
                    s.collide(e);
                }
                if (e != null) {
                    //pokud kolidují dávají damage
                    if (!e.collide(mainCharacter)) {
                        e.move(new Vector(0f, -1f));

                    } else {
                        collision = true;
                    }
                    if (e.behindLine(LINE_Y)) {
                        e.requestRemoval();
                        enemies[i] = null;
                    }
                    //TODO povoilit zpětné pohyby
                    mainCharacter.setMovable(!collision);
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onPause() {
        this.running = false;
        try {
            worldThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        worldThread = new Thread(this);
        this.worldThread.setDaemon(true);
        this.worldThread.start();
    }
}

