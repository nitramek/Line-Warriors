package cz.nitramek.linewarriors.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cz.nitramek.linewarriors.game.objects.Enemy;
import cz.nitramek.linewarriors.game.objects.Mage;
import cz.nitramek.linewarriors.game.objects.MainCharacter;
import cz.nitramek.linewarriors.game.objects.Spell;
import cz.nitramek.linewarriors.game.objects.Sprite;
import cz.nitramek.linewarriors.game.objects.Square;
import cz.nitramek.linewarriors.game.utils.Constants;
import cz.nitramek.linewarriors.game.utils.GameRendererListener;
import cz.nitramek.linewarriors.game.utils.GameStateListener;
import cz.nitramek.linewarriors.game.utils.Monster;
import cz.nitramek.linewarriors.game.utils.OnAbilityCast;
import cz.nitramek.linewarriors.game.utils.SpellManager;
import cz.nitramek.linewarriors.game.utils.TextureKey;
import cz.nitramek.linewarriors.game.utils.TextureManager;
import cz.nitramek.linewarriors.game.utils.Vector;

/**
 * Must be instantiated
 */
public class GameWorld implements Runnable, OnAbilityCast, StateChangedListener {
    public static final float LINE_Y = -0.75f;
    public static final int CAPACITY = 5;
    private final GameRendererListener listener;
    private final List<Spell> spells;
    /**
     * Reprezents enemies in line, one enemy per given width
     */
    private Enemy[] enemies;
    private MainCharacter mainCharacter;
    /**
     * Reprezents square with 1/4 UV mapping
     */
    private Square square4;

    private Thread worldThread;
    private boolean running;
    private int gold = Constants.STARTING_GOLD;
    private int deathTimer = Constants.RESPAWN_TIME;

    private GameStateListener gameStateListener;

    public GameWorld(final GameRendererListener listener) {
        this.listener = listener;
        this.enemies = new Enemy[CAPACITY];
        this.square4 = new Square(4, 4);
        final Sprite mainCharacterSprite = new Sprite(square4, 4, 4, TextureManager.getInstance().getTextureId(TextureKey.MAGE));
        mainCharacterSprite.getModelMatrix().scale(0.15f, 0.15f * this.listener.getRatio());
        this.mainCharacter = new Mage(mainCharacterSprite, this, this);
        synchronized (this) {
            this.notifyAll();
        }
        this.listener.addDrawable(mainCharacterSprite);
        this.addEnemy(Monster.JENOVA);
//        this.addEnemy();
//        this.addEnemy();
        this.worldThread = new Thread(this);
        this.worldThread.setDaemon(true);
        this.worldThread.start();
        this.spells = new CopyOnWriteArrayList<>();

    }

    public void addEnemy(Monster monster) {
        int freeIndex = findFreeSpot();
        if (freeIndex > -1) {
            this.gold -= monster.reward;
            this.fireGoldChanged();
            final Sprite enemySprite = new Sprite(square4, 4, 4, TextureManager.getInstance().getTextureId(TextureKey.valueOf(monster.toString())));
            enemySprite.getModelMatrix().scale(0.15f, 0.15f * this.listener.getRatio());
            enemySprite.getModelMatrix().setPosition(-0.75f + freeIndex * 1.5f / CAPACITY, 0.75f);
            Enemy e = new Enemy(enemySprite, monster, this);
            enemies[freeIndex] = e;
            this.listener.addDrawable(enemySprite);
        }

    }

    private void fireGoldChanged() {
        if (this.gameStateListener != null) {
            this.gameStateListener.goldChanged(this.gold);
        }
    }

    private void fireHealthChanged() {
        if (this.gameStateListener != null) {
            this.gameStateListener.healthChanged(this.mainCharacter.getHealth());
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
                if (e != null) {
                    synchronized (this.spells) {
                        for (Spell s : spells) {
                            s.animate();
                            if (s.collide(e)) {
                                s.requestRemoval();
                            }

                        }
                    }
                    //pokud kolidují dávají damage
                    if (mainCharacter.isDead() || !e.collide(mainCharacter)) {
                        e.move(new Vector(0f, -1f));

                    } else {
                        collision = true;
                        this.fireHealthChanged();

                    }
                    if (e.behindLine(LINE_Y)) {
                        e.requestRemoval();
                        enemies[i] = null;
                    }
                    if (e.isDead()) {
                        e.requestRemoval();
                        this.gold += e.getMonster().reward;
                        this.fireGoldChanged();
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

    @Override
    public void onCast(SpellManager.SpellType type) {
        switch (type) {
            case FIRE:
                //TODO rework, this is completely unacceptable
                final Sprite spellSprite = new Sprite(
                        new Square(1, 4),
                        1,
                        4,
                        TextureManager.getInstance().getTextureId(TextureKey.EXPLOSION)
                );
                spellSprite.getModelMatrix().scale(0.15f, 0.15f * this.listener.getRatio());
                spellSprite.getModelMatrix().setPosition(
                        this.mainCharacter.getBoundingBox().left + 0.1f,
                        this.mainCharacter.getBoundingBox().top + 0.1f);
                Spell s = new Spell(spellSprite, 5) {
                    int maxY = 4;
                    int currentY = 0;

                    @Override
                    public void animate() {
                        this.currentY = ++this.currentY;
                        if (currentY > maxY) {
                            this.requestRemoval();
                            GameWorld.this.spells.remove(this);
                        }
                        this.sprite.setSpriteY(this.currentY);
                    }
                };
                this.listener.addDrawable(spellSprite);
                synchronized (this.spells) {
                    this.spells.add(s);
                }
                break;
        }
    }

    @Override
    public void onDeath(boolean enemy) {
        this.mainCharacter.requestRemoval();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < GameWorld.this.deathTimer; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                final Sprite mainCharacterSprite = new Sprite(square4, 4, 4, TextureManager.getInstance().getTextureId(TextureKey.MAGE));
                mainCharacterSprite.getModelMatrix().scale(0.15f, 0.15f * GameWorld.this.listener.getRatio());
                GameWorld.this.mainCharacter.setSprite(mainCharacterSprite);
                GameWorld.this.listener.addDrawable(mainCharacterSprite);
                GameWorld.this.fireHealthChanged();
            }
        }).start();
    }


    public void setGameStateListener(GameStateListener gameStateListener) {
        this.gameStateListener = gameStateListener;
    }
}

