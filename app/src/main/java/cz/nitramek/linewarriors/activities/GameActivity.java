package cz.nitramek.linewarriors.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cz.nitramek.linewarriors.R;
import cz.nitramek.linewarriors.game.Controller;
import cz.nitramek.linewarriors.game.GameView;
import cz.nitramek.linewarriors.game.GameWorld;
import cz.nitramek.linewarriors.game.utils.Constants;
import cz.nitramek.linewarriors.game.utils.GameStateListener;
import cz.nitramek.linewarriors.game.utils.Monster;
import cz.nitramek.linewarriors.networking.Networker;
import cz.nitramek.linewarriors.networking.NsdHelper;
import cz.nitramek.linewarriors.util.Role;
import cz.nitramek.linewarriors.util.Skin;

public class GameActivity extends Activity implements GameStateListener, NsdHelper.DiscoveryListener, Networker.GameActivityListener {

    public static final int GOLD_MSG = 0;
    public static final int HEALTH_MSG = 1;
    public static final int REMAINING_ESCAPES_MGS = 2;
    public static final int GAME_WON = 3;

    private GameView gameView;
    private Controller controller;
    private RelativeLayout rl;
    private TextView goldView;
    private LinearLayout bestiary;
    private Handler handler;
    private TextView healthView;
    private TextView escapesView;
    private Networker networker;
    private NsdHelper nsdHelper;
    private Role role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networker = new Networker(this);
        nsdHelper = new NsdHelper(this, networker);

        final Intent intent = super.getIntent();
        role = (Role) intent.getSerializableExtra(MainActivity.EXTRA_CONNECT_AS);
        final Skin selectedSkin = (Skin) intent.getSerializableExtra(Skin.SKIN_TAG);
        switch (role) {
            case SERVER:
                this.nsdHelper.registerService();
                break;
            case CLIENT:
                this.nsdHelper.startDiscovery();
                break;
        }
        handler = new MyHandler(Looper.getMainLooper(), this);

        try {
            gameView = new GameView(this, new GameView.InitiationCallback() {
                @Override
                public void onInitiation(GameView view) {
                    GameActivity.this.controller.init(
                            GameActivity.this.gameView.getWorld().getMainCharacter(),
                            GameActivity.this.gameView.getWidth(),
                            GameActivity.this.gameView.getHeight());
                    GameActivity.this.gameView.setOnTouchListener(GameActivity.this.controller);
                    GameActivity.this.gameView.getWorld().setGameStateListener(GameActivity.this);
                    GameActivity.this.gameView.getWorld().setSkin(selectedSkin);
                    GameActivity.this.networker.setOnMonsterListener(GameActivity.this);
                }
            });
        } catch (IOException e) {
            Log.e(GameActivity.class.getName(), "Error in loading shaders", e);
            this.finish();
        }
        GameActivity.this.controller = new Controller(GameActivity.this);
        initTopUi();
        setContentView(this.rl);


    }

    private void initTopUi() {
        this.rl = new RelativeLayout(this);
        this.rl.addView(this.gameView);


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_TOP);
        lp.addRule(RelativeLayout.ALIGN_LEFT);
        this.goldView = new TextView(this);
        goldView.setLayoutParams(lp);
        this.goldChanged(Constants.STARTING_GOLD);
        goldView.setBackgroundColor(Color.DKGRAY);
        goldView.setTextColor(Color.YELLOW);
        goldView.setId(View.generateViewId());
        rl.addView(goldView);


        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, goldView.getId());
        this.healthView = new TextView(this);
        this.healthView.setLayoutParams(lp);
        this.healthChanged(Constants.STARTING_HEALTH);
        healthView.setBackgroundColor(Color.DKGRAY);
        healthView.setTextColor(Color.RED);
        this.healthView.setId(View.generateViewId());
        rl.addView(this.healthView);


        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, this.healthView.getId());
        this.escapesView = new TextView(this);
        this.escapesView.setLayoutParams(lp);
        this.escapedChanged(Constants.STARTING_ESCAPES);
        escapesView.setBackgroundColor(Color.DKGRAY);
        escapesView.setTextColor(Color.BLUE);
        rl.addView(this.escapesView);

        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        TextView bestiaryLink = new TextView(this);
        bestiaryLink.setLayoutParams(lp);
        bestiaryLink.setText(R.string.bestiary);
        bestiaryLink.setBackgroundColor(Color.WHITE);
        bestiaryLink.setTextColor(Color.BLACK);

        bestiaryLink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                GameActivity.this.bestiary.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                return true;
            }
        });
        LayoutInflater inflater = this.getLayoutInflater();
        this.bestiary = (LinearLayout) inflater.inflate(R.layout.enemy_selection, null);
        this.bestiary.setLayoutParams(lp);
        this.bestiary.setVisibility(View.GONE);
        this.rl.addView(bestiaryLink);
        this.rl.addView(this.bestiary);

        for (int i = 0; i < this.bestiary.getChildCount(); i++) {
            ImageView iv = (ImageView) this.bestiary.getChildAt(i);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Monster monster = Monster.valueOf((String) v.getTag());
                    if (GameActivity.this.gameView.getWorld().payForMonster(monster)) {
                        if (role.equals(Role.NONE)) {
                            GameActivity.this.gameView.getWorld().addEnemy(monster);
                        } else {
                            GameActivity.this.networker.sendMessage(monster.toString());
                        }
                    } else {
                        Toast.makeText(GameActivity.this, "Nemáš zlaté na tuhle potvoru", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.onResume();
            final GameWorld world = gameView.getWorld();
            if ((world != null)) {
                world.onResume();
            }
        }
    }

    @Override
    protected void onPause() {

        if (gameView != null) {
            gameView.onPause();
            final GameWorld world = gameView.getWorld();
            if ((world != null)) {
                world.onPause();
            }
        }
        this.nsdHelper.unregister();
        super.onPause();
    }

    @Override
    public void goldChanged(int gold) {
        Message.obtain(handler, GOLD_MSG, gold, 0).sendToTarget();
    }

    @Override
    public void healthChanged(int health) {
        Message.obtain(handler, HEALTH_MSG, health, 0).sendToTarget();
    }

    @Override
    public void escapedChanged(int remainingEscapes) {
        Message.obtain(handler, REMAINING_ESCAPES_MGS, remainingEscapes, 0).sendToTarget();

    }

    @Override
    public void killedChanged(int killed) {
        final SharedPreferences.Editor edit = this.getSharedPreferences(Constants.SP_STATS, MODE_PRIVATE).edit();
        edit.putInt(Constants.SP_KEY_KILLS, killed);
        edit.apply();
    }

    @Override
    public void found() {
        Toast.makeText(this, "Found server, you can now send enemies to your opponent!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMonsterRecieved(Monster monster) {
        GameActivity.this.gameView.getWorld().addEnemy(monster);
    }

    @Override
    public void gameOver() {
        Message.obtain(this.handler, GAME_WON).sendToTarget();
    }

    static class MyHandler extends Handler {
        private final GameActivity activity;

        public MyHandler(Looper looper, GameActivity activity) {
            super(looper);
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOLD_MSG:
                    activity.goldView.setText(String.format("G: %s", String.valueOf(msg.arg1)));
                    break;
                case HEALTH_MSG:
                    activity.healthView.setText(String.format("H: %s", String.valueOf(msg.arg1)));
                    break;
                case REMAINING_ESCAPES_MGS:
                    activity.escapesView.setText(String.format("H: %s", String.valueOf(msg.arg1)));
                    //finish since he lost
                    if (msg.arg1 <= 0) {
                        Toast.makeText(activity, R.string.game_over_lose, Toast.LENGTH_SHORT).show();
                        activity.networker.sendMessage(Constants.GAME_OVER_MSS);
                        activity.finish();
                    }
                    break;
                case GAME_WON:
                    Toast.makeText(activity, R.string.game_over_win, Toast.LENGTH_SHORT).show();
                    activity.finish();
                    break;


            }
        }

    }
}
