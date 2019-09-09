package com.borobudur.w.borobudur;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class Setting extends AppCompatActivity {

    private SeekBar seekBar;
    private AudioManager audioManager;
    private SwitchCompat switchsnackbar;
    private boolean statesnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekBar = (SeekBar) findViewById(R.id.seekbarvolume);
        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#0D6A73"), PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.parseColor("#0D6A73"), PorterDuff.Mode.SRC_IN);
        iniBar(seekBar, AudioManager.STREAM_MUSIC);


        ImageButton buttonback = (ImageButton) findViewById(R.id.backhome1);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        SharedPreferences prefsnackbar = getSharedPreferences("prefsnackbar", 0);
        statesnackbar = prefsnackbar.getBoolean("keysnackbar", false);
        switchsnackbar = (SwitchCompat) findViewById(R.id.switchsnackbar);
        switchsnackbar.setChecked(statesnackbar);
        switchsnackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statesnackbar = !statesnackbar;
                switchsnackbar.setChecked(statesnackbar);
                SharedPreferences.Editor editor = prefsnackbar.edit();
                editor.putBoolean("keysnackbar", statesnackbar);
                editor.apply();
            }
        });

    }

    private void iniBar(SeekBar seekBar, int streamMusic) {
        seekBar.setMax(audioManager.getStreamMaxVolume(streamMusic));
        seekBar.setProgress(audioManager.getStreamVolume(streamMusic));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(streamMusic, i, AudioManager.FLAG_PLAY_SOUND);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
