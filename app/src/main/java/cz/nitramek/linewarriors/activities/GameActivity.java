package cz.nitramek.linewarriors.activities;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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

public class GameActivity extends Activity {

    private GameView gameView;
    private Controller controller;
    private RelativeLayout rl;
    private TextView scoreView;
    private LinearLayout enemiesView;
    private TextView bestiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            gameView = new GameView(this, new GameView.InitiationCallback() {
                @Override
                public void onInitiation(GameView view) {
                    GameActivity.this.controller.init(
                            GameActivity.this.gameView.getWorld().getMainCharacter(),
                            GameActivity.this.gameView.getWidth(),
                            GameActivity.this.gameView.getHeight());
                    GameActivity.this.gameView.setOnTouchListener(GameActivity.this.controller);
                }
            });
        } catch (IOException e) {
            Log.e(GameActivity.class.getName(), "Error in loading shaders", e);
            this.finish();
        }
        GameActivity.this.controller = new Controller(GameActivity.this);
        this.rl = new RelativeLayout(this);
        this.rl.addView(this.gameView);
        this.scoreView = new TextView(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        bestiary = new TextView(this);
        bestiary.setLayoutParams(lp);
        bestiary.setText(R.string.bestiary);
        bestiary.setBackgroundColor(Color.WHITE);
        bestiary.setTextColor(Color.BLACK);
//        scoreView.setLayoutParams(lp);
//        scoreView.setText(R.string.Shop);
//        scoreView.setBackgroundColor(Color.WHITE);
//        scoreView.setTextColor(Color.BLACK);
//        rl.addView(scoreView);

        bestiary.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                GameActivity.this.enemiesView.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                return true;
            }
        });
        LayoutInflater inflater = this.getLayoutInflater();
        this.enemiesView = (LinearLayout) inflater.inflate(R.layout.enemy_selection, null);
        this.enemiesView.setLayoutParams(lp);
        this.enemiesView.setVisibility(View.GONE);
        this.rl.addView(this.bestiary);
        this.rl.addView(this.enemiesView);
        setContentView(this.rl);
        for (int i = 0; i < enemiesView.getChildCount(); i++) {
            ImageView iv = (ImageView) enemiesView.getChildAt(i);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameActivity.this.gameView.getWorld().addEnemy();
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
}
