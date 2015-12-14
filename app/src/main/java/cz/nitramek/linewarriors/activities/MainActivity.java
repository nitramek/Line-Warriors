package cz.nitramek.linewarriors.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cz.nitramek.linewarriors.R;
import cz.nitramek.linewarriors.game.utils.Constants;
import cz.nitramek.linewarriors.util.Role;
import cz.nitramek.linewarriors.util.Skin;

public class MainActivity extends Activity {
    public static final String EXTRA_CONNECT_AS = "CONNECT_AS";

    private ImageView iv;

    private Skin skins[] = Skin.values();
    private int selectedSkinIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        final TextView headLine = (TextView) findViewById(R.id.main_tv_headline);
        headLine.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/wolf.ttf"));
        final Button clientButton = (Button) findViewById(R.id.main_btn_start_as_client);
        final Button serverButton = (Button) findViewById(R.id.main_btn_start_as_server);
        final Button trainingButton = (Button) findViewById(R.id.main_btn_start_training);
        final ImageButton previousHeroBtn = (ImageButton) findViewById(R.id.main_btn_previous_hero);
        final ImageButton nextHeroBtn = (ImageButton) findViewById(R.id.main_btn_next_hero);
        final TextView tv = (TextView) findViewById(R.id.main_tv_kills);

        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(Role.CLIENT, R.string.clientSart);
            }
        });
        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(Role.SERVER, R.string.serverStart);
            }
        });
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.welcome);
        mediaPlayer.start();
        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(Role.NONE, R.string.trainingStart);
            }
        });
        iv = (ImageView) findViewById(R.id.main_iv_hero_selection);

        final SharedPreferences preferences = this.getSharedPreferences(Constants.SP_STATS, MODE_PRIVATE);
        tv.setText(String.format("Max kills in traing mode: %d", preferences.getInt(Constants.SP_KEY_KILLS, 0)));


        previousHeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.selectedSkinIndex--;
                if (MainActivity.this.selectedSkinIndex <= 0) {
                    v.setVisibility(View.INVISIBLE);
                }
                nextHeroBtn.setVisibility(View.VISIBLE);
                iv.setImageResource(skins[selectedSkinIndex].headDrawableId);
                iv.invalidate();
            }
        });

        nextHeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.selectedSkinIndex++;
                if (MainActivity.this.selectedSkinIndex >= MainActivity.this.skins.length - 1) {
                    v.setVisibility(View.INVISIBLE);
                }
                previousHeroBtn.setVisibility(View.VISIBLE);
                iv.setImageResource(skins[selectedSkinIndex].headDrawableId);
                iv.invalidate();
            }
        });
    }

    private void startGame(Role role, @StringRes int messageId) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra(EXTRA_CONNECT_AS, role);
        intent.putExtra(Skin.SKIN_TAG, skins[this.selectedSkinIndex]);
        Toast.makeText(MainActivity.this, messageId, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        TextView tv = (TextView) findViewById(R.id.main_tv_kills);
        final SharedPreferences preferences = this.getSharedPreferences(Constants.SP_STATS, MODE_PRIVATE);
        tv.setText(String.format("Max kills in traing mode: %d", preferences.getInt(Constants.SP_KEY_KILLS, 0)));
        super.onResume();
    }
}
