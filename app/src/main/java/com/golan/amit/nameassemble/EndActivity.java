package com.golan.amit.nameassemble;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPlayAgain;
    SharedPreferences sp;
    TextView tvDisplay;
    ImageView ivStatus;
    Animation animation;
    SoundPool soundPool;
    int sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        init();
    }

    private void init() {
        ivStatus = findViewById(R.id.ivStatus);
        tvDisplay = findViewById(R.id.tvInfoDisplay);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_slideup);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnPlayAgain.setOnClickListener(this);

        /**
         * Sound
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME).build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10).setAudioAttributes(aa).build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        sp = getSharedPreferences("nameassemble", MODE_PRIVATE);
        String name = sp.getString("name", null);
        final Boolean won = sp.getBoolean("state", false);

        int[] winSounds = new int[] {
                R.raw.applause, R.raw.cheering, R.raw.wine
        };

        sound = won ? (soundPool.load(this, winSounds[((int)(Math.random() * winSounds.length))], 1)) : (soundPool.load(this, R.raw.failtrombone, 1));

        /*if(won) {
            sound = soundPool.load(this, winSounds[((int)(Math.random() * winSounds.length))], 1);
        } else {
            sound = soundPool.load(this, R.raw.failtrombone, 1);
        }*/

        int picResource = won ? R.mipmap.thumbupgreen : R.mipmap.thumbdownred;
        if (name != null) {
            String toDisplay = "הצלחת להרכיב את השם " + name;
            if(!won) {
                toDisplay = "לא " + toDisplay;
            }
            tvDisplay.setText(toDisplay);
            ivStatus.setImageResource(picResource);
            ivStatus.startAnimation(animation);
        }

        soundPool.setOnLoadCompleteListener(
                new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        soundPool.play(sampleId, 1, 1, 0, 1, 1);
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        if(v == btnPlayAgain) {
            soundPool.release();
            Intent i = new Intent(this, ProgramaticallyActivity.class);
            startActivity(i);
        }
    }
}
