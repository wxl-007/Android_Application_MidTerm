package com.midterm.microproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    GuessingGame game;
    Button btn_guess,btn_start;
    TextView txt_time,txt_score;
    ListView lv_history;
    Integer curInput = Integer.MIN_VALUE;
    List<String> inputHistory;
    Boolean isGameOver;
//    ProgressBar pb_timer;
    ImageView img_btn_Setting;
    private Timer gameTimer;
    int totalTime = 50;
    int curTimeLeft = 0;
    Dialog dialog ;
    GridView gv_numBtnGrid;
    TextView txt_timeLeft;
    String curName;
    String curScore;
    // init all components

    // player part
    public MediaPlayer mPlayer;
    private TextView title, songName, leftTime;
    private SeekBar musicBar;
    private ImageView musicCover;
    private Button stop;
    private Spinner musicList;
    private int oTime =0, sTime =0, eTime =0, lTime=0;
    private ArrayAdapter<CharSequence> adapter;
    private Handler hdlr = new Handler();
    public static final String EXTRA_MESSAGE = "com.midterm.microproject.MESSAGE";

    private ImageView runBoy;
    private AnimationDrawable frameAnimation;
    private ImageView fightBoy;
    private AnimationDrawable frameAnimation1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = (Button)findViewById(R.id.btn_Start);
        txt_time = (TextView)findViewById(R.id.txt_times);
        txt_score = (TextView)findViewById(R.id.txt_score);
//        pb_timer = (ProgressBar)findViewById(R.id.pb_timer);
        img_btn_Setting = (ImageView) findViewById(R.id.img_setting);
        gv_numBtnGrid = (GridView) findViewById(R.id.txt_grid);
        txt_timeLeft = (TextView)findViewById(R.id.txt_timeLeft);
        btn_start.setEnabled(true);
        isGameOver=true;
        dialog = new Dialog(this);
        Intent tIntent  = getIntent();
        curName = tIntent.getStringExtra("Name");
        curScore = tIntent.getStringExtra("Score");
        txt_score.setText(curName+" score : "+ curScore);

        musicBar=(SeekBar) findViewById(R.id.seekBarMusic);
        musicCover=(ImageView) findViewById(R.id.imageViewCover);
        mPlayer=MediaPlayer.create(this, R.raw.brightside);
        musicCover.setImageResource(R.drawable.pauseicon);
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
//                        musicCover.setImageResource(R.drawable.cover1);
//                        songName.setText("Bright Side");
                        PlayMusic();
                        break;
                    case 1:
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.nightlife);
//                        musicCover.setImageResource(R.drawable.cover2);
//                        songName.setText("Night Life");
                        PlayMusic();
                        break;
                    case 2:
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.romantic);
//                        musicCover.setImageResource(R.drawable.cover3);
//                        songName.setText("Romantic Arc");
                        PlayMusic();
                        break;
                    case 3:
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.pumping);
//                        musicCover.setImageResource(R.drawable.cover4);
//                        songName.setText("Pumping");
                        PlayMusic();
                        break;
                    case 4:
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bigdeal);
//                        musicCover.setImageResource(R.drawable.cover5);
//                        songName.setText("A Big Deal");
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

        runBoy = (ImageView) findViewById(R.id.runboy);
        fightBoy = (ImageView) findViewById(R.id.fight);

    }
    // init game needs
    public void Init(){
        Integer tTimes = 10;
        game = new GuessingGame(tTimes);
        gameTimer = new Timer();
        curTimeLeft = totalTime;
        isGameOver = false;
        inputHistory = new ArrayList<String>();
        txt_timeLeft.setEnabled(true);
        gameTimer.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {
                TimerInterval();
            }
        }, 0, 1000);
        UpdateUI();
    }
    // restart game
    public void StartGame(View view){
        if(gameTimer!=null) gameTimer.cancel();
        Init();
        InitGrid();
        btn_start.setText("RESTART");
        InitAnim(1);
    }
    //guess btn method
     void CheckNumber(View tView){
        byte result;
        if(game.GetRemainingTimes()>0) {
            result = game.GuessNum(curInput);
            Resources  res =  getResources();
            if(result==-1) {
                tView.setBackgroundColor(Color.YELLOW);
                tView.setEnabled(false);
            }
            else if(result ==1){
                tView.setBackgroundColor(Color.RED);
                tView.setEnabled(false);
            }
            else {
                GameOver(true);
                tView.setBackgroundColor(Color.GREEN);
                tView.setEnabled(false);

                return;
            }
            UpdateUI();
        }else GameOver(false);
        if(game.GetRemainingTimes()==0) GameOver(false);
        else InitAnim(3);
    }

//game over no matter win or lose
    void GameOver(boolean isWin){
        isGameOver = true;
        btn_start.setText("START");
        Resources  res =  getResources();
        txt_time.setText(res.getString(R.string.startMessage));
        if(isWin) {
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
            Toast.makeText(this,res.getString(R.string.winMessage),Toast.LENGTH_LONG).show();
            SetScoreForUser();
            InitAnim(2);
            gv_numBtnGrid.setOnItemClickListener(null);
            gameTimer.cancel();
        }else{
            gameTimer.cancel();
            InitAnim(4);
            gv_numBtnGrid.setOnItemClickListener(null);

            Toast.makeText(this,res.getString(R.string.loseMessage),Toast.LENGTH_LONG).show();
        }
    }
    void SetScoreForUser(){
        if(curName =="Guest") return;
        SQLiteDatabase userDB = openOrCreateDatabase("UserDB",MODE_PRIVATE,null);
        int newScore = game.GetRemainingTimes()*10 + Integer.parseInt(curScore);
        userDB.execSQL("UPDATE UserTable SET Score = ? WHERE Username = ?;",new String[]{String.valueOf(newScore),curName});
        curScore = String.valueOf(newScore);
        txt_score.setText(curName+ " score : "+ curScore);
    }

    // update some ui change after every input
    void UpdateUI(){
        txt_time.setText( getResources().getString(R.string.Chance) +game.GetRemainingTimes() );
        txt_score.setText(curName+" score : "+ curScore);
    }


    void TimerInterval(){
        this.runOnUiThread(Timer_Tick);
    }
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            if(curTimeLeft>0) {
                curTimeLeft--;
//                pb_timer.setProgress(curTimeLeft);
                int seconds = curTimeLeft;
                int minutes = seconds / 60;
                seconds     = seconds % 60;
                txt_timeLeft.setText(String.format("%d:%02d", minutes, seconds) );

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


     void InitGrid(){
        List<Integer> tArray = new ArrayList<Integer>(120);
        for (int i=1;i<101;i++){
            tArray.add(i);
        }
        gv_numBtnGrid.setNumColumns(10);
        ListAdapter adapter = new ArrayAdapter<Integer>(this,
                R.layout.griditem, tArray);
        gv_numBtnGrid.setAdapter(adapter);
        gv_numBtnGrid.setContextClickable(true);
        gv_numBtnGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(v.isEnabled()) {
                    curInput =  position+1;
                    CheckNumber(v);
                }
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
        musicBar.setProgress(sTime);
        hdlr.postDelayed(UpdateSongTime, 100);
    }

    private Runnable UpdateSongTime=new Runnable() {
        @Override
        public void run() {
                    eTime = mPlayer.getDuration();
                    sTime = mPlayer.getCurrentPosition();
                    lTime = eTime - sTime;
                    long leftMin = TimeUnit.MILLISECONDS.toMinutes(lTime);
                    long leftSec = TimeUnit.MILLISECONDS.toSeconds(lTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lTime));
                    musicBar.setProgress(sTime);
                    hdlr.postDelayed(this, 100);

        }
    };

    public void PlayStopMusic(View view){
        if(mPlayer!= null){
            if(mPlayer.isPlaying()) {
                mPlayer.pause();
                musicCover.setImageResource(R.drawable.playicon);
            }else {
                mPlayer.start();
                musicCover.setImageResource(R.drawable.pauseicon);
            }
        }
    }

    void InitAnim(int type){
        switch (type){
            case 1:
                runBoy.setBackgroundResource(R.drawable.running_animation);
                frameAnimation = (AnimationDrawable) runBoy.getBackground();
                frameAnimation.start();

                break;
            case 2:
                runBoy.setBackgroundResource(R.drawable.win_animation);
                frameAnimation = (AnimationDrawable) runBoy.getBackground();
                frameAnimation.start();
                break;
            case 3:
                runBoy.setBackgroundResource(R.drawable.dying_animation);
                fightBoy.setBackgroundResource(R.drawable.fight_animation);
                frameAnimation = (AnimationDrawable) runBoy.getBackground();
                frameAnimation1 = (AnimationDrawable) fightBoy.getBackground();
                fightBoy.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        fightBoy.setBackground(null);
                        InitAnim(1);
                    }
                },1000);
                frameAnimation.start();
                frameAnimation1.start();
                break;
            case 4:
                runBoy.setBackgroundResource(R.drawable.dying_animation);
                frameAnimation = (AnimationDrawable) runBoy.getBackground();
                frameAnimation.start();
                break;
                default:
                    break;
        }
    }

    @Override
    public void onBackPressed() {
        // your code.
        super.onBackPressed();
        mPlayer.stop();
        hdlr.removeCallbacks(UpdateSongTime);
        hdlr = null;
        mPlayer = null;
    }


}
