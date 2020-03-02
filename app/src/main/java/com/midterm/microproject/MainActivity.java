package com.midterm.microproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    GuessingGame game;
    EditText txt_input;
    Button btn_guess,btn_start;
    TextView txt_time,txt_score;
    ListView lv_history;
    Integer curInput = Integer.MIN_VALUE;
    List<String> inputHistory;
    Boolean isGameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_guess = (Button)findViewById(R.id.btn_guess);
        btn_start = (Button)findViewById(R.id.btn_Start);
        txt_input = (EditText)findViewById(R.id.etxt_input);
        txt_time = (TextView)findViewById(R.id.txt_times);
        txt_score = (TextView)findViewById(R.id.txt_score);
        lv_history = (ListView)findViewById(R.id.listView);
        btn_start.setEnabled(true);
        btn_guess.setEnabled(false);
        txt_input.setEnabled(false);
    }

    public void Init(){
        Integer tTimes = 10;
        game = new GuessingGame(tTimes);
        isGameOver = false;
        inputHistory = new ArrayList<String>();
        UpdateUI();
    }

    public void StartGame(View view){
        Init();
        btn_guess.setEnabled(true);
        txt_input.setEnabled(true);
        btn_start.setText("RESTART");
    }

    public void CheckNumber(View view){
        byte result;
        if(CheckInput()) {
            if(game.GetRemainingTimes()>0) {
                result = game.GuessNum(curInput);
                Resources  res =  getResources();
                if(result==-1) inputHistory.add(curInput+" "+res.getString(R.string.lessMessage));
                else if(result ==1)inputHistory.add(curInput+" "+res.getString(R.string.greatMessage));
                else {
                    GameOver(true);
                    return;
                }
                UpdateUI();
            }else GameOver(false);
        }
        if(game.GetRemainingTimes()==0) GameOver(false);

    }


    void GameOver(boolean isWin){
        isGameOver = true;
        btn_guess.setEnabled(false);
        txt_input.setEnabled(false);
        btn_start.setText("START");
        Resources  res =  getResources();
        txt_time.setText(res.getString(R.string.startMessage));
        if(isWin) {
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
            Toast.makeText(this,res.getString(R.string.winMessage),Toast.LENGTH_LONG);
        }else{
            Toast.makeText(this,res.getString(R.string.loseMessage),Toast.LENGTH_LONG);
        }

    }

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

    void UpdateUI(){
        txt_time.setText(getResources().getString(R.string.Chance) +game.GetRemainingTimes() );
        txt_score.setText(getResources().getString(R.string.score) +game.GetRemainingTimes()*10 );
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inputHistory);
        lv_history.setAdapter(itemsAdapter);
    }
}
