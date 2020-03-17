package com.midterm.microproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class SelectMusicActivity extends AppCompatActivity {

    public MediaPlayer mPlayer;
    private TextView title, songName, leftTime;
    private SeekBar musicBar;
    private ImageView musicCover;
    private Button stop;
    private Spinner musicList;
    private static int oTime =0, sTime =0, eTime =0, lTime=0;
    private ArrayAdapter<CharSequence> adapter;
    private Handler hdlr = new Handler();
    public static final String EXTRA_MESSAGE = "com.midterm.microproject.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_music);
        title = (TextView) findViewById(R.id.title);
        title.setText("Select Your Background Music");
        songName=(TextView) findViewById(R.id.songName);
        stop=(Button) findViewById(R.id.stop);
        leftTime=(TextView) findViewById(R.id.leftTime);
        musicBar=(SeekBar) findViewById(R.id.seekBarMusic);
        musicCover=(ImageView) findViewById(R.id.imageViewCover);
        mPlayer=MediaPlayer.create(this, R.raw.brightside);
        musicCover.setImageResource(R.drawable.cover1);
        musicList=(Spinner) findViewById(R.id.spinnerMusicList);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.musicList_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        musicList.setAdapter(adapter);
        musicList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.brightside);
                        musicCover.setImageResource(R.drawable.cover1);
                        songName.setText("Bright Side");
                        PlayMusic();
                        break;
                    case 1:
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.nightlife);
                        musicCover.setImageResource(R.drawable.cover2);
                        songName.setText("Night Life");
                        PlayMusic();
                        break;
                    case 2:
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.romantic);
                        musicCover.setImageResource(R.drawable.cover3);
                        songName.setText("Romantic Arc");
                        PlayMusic();
                        break;
                    case 3:
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.pumping);
                        musicCover.setImageResource(R.drawable.cover4);
                        songName.setText("Pumping");
                        PlayMusic();
                        break;
                    case 4:
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bigdeal);
                        musicCover.setImageResource(R.drawable.cover5);
                        songName.setText("A Big Deal");
                        PlayMusic();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPlayer.stop();
                mPlayer.release();
            }
        });

    }

    public void PlayMusic(){
        mPlayer.start();
        eTime=mPlayer.getDuration();
        sTime=mPlayer.getCurrentPosition();
        lTime=eTime-sTime;
        if (oTime == 0) {
            musicBar.setMax(eTime);
            oTime = 1;
        }
        leftTime.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(lTime),
                TimeUnit.MILLISECONDS.toSeconds(lTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lTime))));
        musicBar.setProgress(sTime);
        hdlr.postDelayed(UpdateSongTime, 100);
    }

    private Runnable UpdateSongTime=new Runnable() {
        @Override
        public void run() {
            eTime=mPlayer.getDuration();
            sTime=mPlayer.getCurrentPosition();
            lTime=eTime-sTime;
            long leftMin=TimeUnit.MILLISECONDS.toMinutes(lTime);
            long leftSec=TimeUnit.MILLISECONDS.toSeconds(lTime)-
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lTime));
            leftTime.setText(String.format("%d:%d", leftMin,leftSec));
            musicBar.setProgress(sTime);
            hdlr.postDelayed(this,100);
        }
    };

    public void StopPlayingMusic(View view){
        mPlayer.stop();
    }

    public void BacktoGame(View view){
        Intent intent = new Intent(this, MainActivity.class);
        String message=songName.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        mPlayer.stop();
    }

}
