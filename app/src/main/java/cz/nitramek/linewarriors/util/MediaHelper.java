package cz.nitramek.linewarriors.util;


import android.content.Context;
import android.media.MediaPlayer;

public class MediaHelper {
    public static Context context;

    public static void playSound(int rawId) {
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, rawId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
    }
}
