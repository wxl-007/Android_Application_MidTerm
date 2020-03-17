package com.midterm.microproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    GuessingGame game;
    EditText txt_input;
    Button btn_start;
    TextView txt_time,txt_score;
    ListView lv_history;
    Integer curInput = Integer.MIN_VALUE;
    List<String> inputHistory;
    Boolean isGameOver;
    ProgressBar pb_timer;
    ImageView img_btn_Setting;
    private Timer gameTimer;
    int totalTime = 100;
    int curTimeLeft = 0;
    Dialog dialog ;
    MediaPlayer mPlayer;
    SeekBar musicBar;
    String musicMessage="";
    // init all components
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button)findViewById(R.id.btn_Start);
        txt_time = (TextView)findViewById(R.id.txt_times);
        txt_score = (TextView)findViewById(R.id.txt_score);
        pb_timer = (ProgressBar)findViewById(R.id.pb_timer);
        img_btn_Setting = (ImageView) findViewById(R.id.img_setting);
        btn_start.setEnabled(true);
        isGameOver=true;
        dialog = new Dialog(this);
        musicBar=(SeekBar)findViewById(R.id.musicBar);

/*
        Intent intentMusic = getIntent();
        musicMessage = intentMusic.getStringExtra(SelectMusicActivity.EXTRA_MESSAGE);
        switch(musicMessage) {
            case "Bright Side":
                mPlayer.stop();
                mPlayer.release();
                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.brightside);
                PlayMusic();
                break;
            case "Night Life":
                mPlayer.stop();
                mPlayer.release();
                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.nightlife);
                PlayMusic();
                break;
            case "Romantic Arc":
                mPlayer.stop();
                mPlayer.release();
                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.romantic);
                PlayMusic();
                break;
            case "Pumping":
                mPlayer.stop();
                mPlayer.release();
                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.pumping);
                PlayMusic();
                break;
            case "A Big Deal":
                mPlayer.stop();
                mPlayer.release();
                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bigdeal);
                PlayMusic();
                break;
            default:
                mPlayer.stop();
                mPlayer.release();
        }//set the music player
*/
    }

    // init game needs
    public void Init(){
        Integer tTimes = 10;
        game = new GuessingGame(tTimes);
        gameTimer = new Timer();
        curTimeLeft = totalTime;
        isGameOver = false;
        inputHistory = new ArrayList<String>();

        gameTimer.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {
                TimerInterval();
            }
        }, 0, 1000);
        pb_timer.setMax(totalTime);
        pb_timer.setProgress(curTimeLeft);

        UpdateUI();
    }
    // restart game
    public void StartGame(View view){
        Init();
        txt_input.setEnabled(true);
        btn_start.setText("RESTART");
    }
    //guess btn method
    public void CheckNumber(View view){
        byte result;
        if(CheckInput()) {
            if(game.GetRemainingTimes()>0) {
                result = game.GuessNum(curInput);
                Resources  res =  getResources();
                if(result==-1) {
                    String tStr = curInput+" "+res.getString(R.string.lessMessage);
                    inputHistory.add(tStr);
                    Toast.makeText(this,tStr,Toast.LENGTH_SHORT).show();
                }
                else if(result ==1){
                    String tStr = curInput+" "+res.getString(R.string.greatMessage);
                    Toast.makeText(this,tStr,Toast.LENGTH_SHORT).show();
                    inputHistory.add(tStr);
                }
                else {
                    GameOver(true);
                    return;
                }
                UpdateUI();
            }else GameOver(false);
        }
        if(game.GetRemainingTimes()==0) GameOver(false);

    }

//game over no matter win or lose
    void GameOver(boolean isWin){
        isGameOver = true;
        txt_input.setEnabled(false);
        btn_start.setText("START");
        Resources  res =  getResources();
        txt_time.setText(res.getString(R.string.startMessage));
        if(isWin) {
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
            Toast.makeText(this,res.getString(R.string.winMessage),Toast.LENGTH_LONG).show();
            gameTimer.cancel();
        }else{
            gameTimer.cancel();
            pb_timer.setProgress(0);
            Toast.makeText(this,res.getString(R.string.loseMessage),Toast.LENGTH_LONG).show();
        }
    }

    //input validation for dumb user
    Boolean CheckInput(){
        String tError = "";
        Integer tAns = Integer.MIN_VALUE;
        String tInput = txt_input.getText().toString();
        if(tInput.isEmpty()) tError = "Please input a number to guess";
        try
        {
            tAns = Integer.parseInt(tInput.trim());
        }
        catch (NumberFormatException nfe)
        {
            tAns=Integer.MIN_VALUE;
        }
        if(tAns == Integer.MIN_VALUE) tError = "Invalid input! guess range is 0-100";
        if(!tError.isEmpty()) {
            Toast.makeText(this,tError,Toast.LENGTH_SHORT);
            return false;
        }
        curInput = tAns;
        return true;
    }

    // update some ui change after every input
    void UpdateUI(){
        txt_time.setText(getResources().getString(R.string.Chance) +game.GetRemainingTimes() );
        txt_score.setText(getResources().getString(R.string.score) +game.GetRemainingTimes()*10 );
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inputHistory);
        lv_history.setAdapter(itemsAdapter);
    }


    void TimerInterval(){
        this.runOnUiThread(Timer_Tick);
    }
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
//            Log.i("TIMER","time left " + curTimeLeft);
            if(curTimeLeft>0) {
                curTimeLeft--;
                pb_timer.setProgress(curTimeLeft);
            }else{
                GameOver(false);
            }
        }
    };

    public void Setting(View view){
        if(!isGameOver) return;
        dialog.setContentView(R.layout.dialogbox);
        dialog.setTitle("Setting");
        TextView text = (TextView) dialog.findViewById(R.id.txt_dialogTitle);
        text.setText("Input game duation");
        Button btn_Submit = (Button) dialog.findViewById(R.id.btn_Submit);
        final EditText inputTime = (EditText) dialog.findViewById(R.id.etxt_duation);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  input = Integer.MIN_VALUE;
                 try {
                    input = Integer.parseInt(inputTime.getText().toString());
                } catch (NumberFormatException nfe)
                {
                    input=Integer.MIN_VALUE;
                }

                if (input != Integer.MIN_VALUE && input > 0 && input < Integer.MAX_VALUE) {
                    totalTime = input;
                    dialog.dismiss();
                } else {
                    Toast.makeText(dialog.getContext(), "Invalid input!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();
    }

    public void SelectMusic(View view){
        Intent intent = new Intent(this, SelectMusicActivity.class);
        startActivity(intent);
    }

    public void PlayMusic(){
        mPlayer.start();
        int oTime=0,eTime=0;
        eTime=mPlayer.getDuration();
        if (oTime == 0) {
            musicBar.setMax(eTime);
            oTime = 1;
        }
        int sTime=mPlayer.getCurrentPosition();
        musicBar.setProgress(sTime);
    }
}
