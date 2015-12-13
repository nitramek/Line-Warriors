package cz.nitramek.linewarriors.activities;


import android.app.Activity;
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

import java.io.IOException;

import cz.nitramek.linewarriors.R;
import cz.nitramek.linewarriors.game.Controller;
import cz.nitramek.linewarriors.game.GameView;
import cz.nitramek.linewarriors.game.GameWorld;
import cz.nitramek.linewarriors.game.utils.Constants;
import cz.nitramek.linewarriors.game.utils.GameStateListener;
import cz.nitramek.linewarriors.game.utils.Monster;

public class GameActivity extends Activity implements GameStateListener {

    public static final int GOLD_MSG = 0;
    public static final int HEALTH_MSG = 1;
    public static final int REMAINING_ESCAPES_MGS = 2;
    private GameView gameView;
    private Controller controller;
    private RelativeLayout rl;
    private TextView goldView;
    private LinearLayout bestiary;
    private Handler handler;
    private TextView healthView;
    private TextView escapesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GOLD_MSG:
                        GameActivity.this.goldView.setText(String.format("G: %s", String.valueOf(msg.arg1)));
                        break;
                    case HEALTH_MSG:
                        GameActivity.this.healthView.setText(String.format("H: %s", String.valueOf(msg.arg1)));
                        break;
                    case REMAINING_ESCAPES_MGS:
                        GameActivity.this.escapesView.setText(String.format("H: %s", String.valueOf(msg.arg1)));
                        break;

                }
            }
        };

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
                    GameActivity.this.gameView.getWorld().addEnemy(Monster.valueOf((String) v.getTag()));
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
        super.onPause();
        if (gameView != null) {
            gameView.onPause();
            final GameWorld world = gameView.getWorld();
            if ((world != null)) {
                world.onPause();
            }
        }
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
}
