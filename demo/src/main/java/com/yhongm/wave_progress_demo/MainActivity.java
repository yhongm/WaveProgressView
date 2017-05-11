package com.yhongm.wave_progress_demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.yhongm.wave_progress_view.WaveProgressView;

public class MainActivity extends Activity {
    WaveProgressView waveProgresView;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waveProgresView = (WaveProgressView) findViewById(R.id.wave_progress_view);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                waveProgresView.setProgress(progress / (seekBar.getMax() + 0.5f));
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
