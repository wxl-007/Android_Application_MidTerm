package com.midterm.microproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GuessNumberGameActivity extends AppCompatActivity {

    ListView lv_numberHistory;
    TextView txt_TimesLeft;
    TextView txt_Score;
    EditText etxt_InputNumber;
    GuessingNumberGame curGame;
    TextView txt_GameErrorMsg;
    Button  btn_Guess;
    Boolean isStarted;
    List<String> inputHistory;
    String curName;
    String curScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_number_game);
        lv_numberHistory = (ListView)findViewById(R.id.lv_guessNumberHistory);
        txt_TimesLeft = (TextView) findViewById(R.id.txt_RemainingTimes);
        txt_Score = (TextView) findViewById(R.id.txt_ScorePart);
        lv_numberHistory = (ListView)findViewById(R.id.lv_guessNumberHistory);
        etxt_InputNumber = (EditText)findViewById(R.id.etxt_guessNumber);
        btn_Guess = (Button)findViewById(R.id.btn_GuessTheNum);
        txt_GameErrorMsg = (TextView) findViewById(R.id.txt_GameErrorMsg);
        setTitle("Guess The Number");
        Intent tIntent  = getIntent();
        curName = tIntent.getStringExtra("Name");
        curScore = tIntent.getStringExtra("Score");
        txt_Score.setText(curName+" score : "+ curScore);
        InitGame();
    }


    void InitGame(){
        curGame = new GuessingNumberGame(10);
        inputHistory = new ArrayList<String>(10);
        isStarted = true;
        txt_TimesLeft.setText("Times Left : "+curGame.GetRemainingTimes());
    }
    public void GuessNumber(View view){
        String result ="";
        if(curGame.GetRemainingTimes()>0){

            if(ValidateInput()){
                result =  curGame.GuessNumber(etxt_InputNumber.getText().toString());
                if(result.equals("1")){
                    //win
                    btn_Guess.setText("Retart");
                    Toast.makeText(this,R.string.guessnumberlose,Toast.LENGTH_LONG);
                    isStarted = false;
                    SetScoreForUser();

                }else if(result.equals("-1")){
                    //lose
                    btn_Guess.setText("Retart");
                    Toast.makeText(this,R.string.guessnumberwin,Toast.LENGTH_LONG);
                    isStarted = false;
                }else{
                    inputHistory.add(result);
                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inputHistory);
                    lv_numberHistory.setAdapter(itemsAdapter);
                    etxt_InputNumber.getText().clear();
                }
                txt_TimesLeft.setText("Times Left : "+ curGame.GetRemainingTimes());
            }
        }else{
            //restart
            btn_Guess.setText("Guess");
            InitGame();
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inputHistory);
            lv_numberHistory.setAdapter(itemsAdapter);
        }

    }

    boolean ValidateInput(){
        if(etxt_InputNumber.getText().toString().length()<4 || etxt_InputNumber.getText().toString().length()>4){
            return false;
        }
        return true;
    }

    void SetScoreForUser(){
        if(curName =="Guest") return;
        SQLiteDatabase userDB = openOrCreateDatabase("UserDB",MODE_PRIVATE,null);
        int newScore = curGame.GetRemainingTimes()*20 + Integer.parseInt(curScore);
        userDB.execSQL("UPDATE UserTable SET Score = ? WHERE Username = ?;",new String[]{String.valueOf(newScore),curName});
        curScore = String.valueOf(newScore);
        txt_Score.setText(curName+ " score : "+ curScore);
    }


}
