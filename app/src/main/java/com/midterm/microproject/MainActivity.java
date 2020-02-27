package com.midterm.microproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    GuessingGame game;
    EditText txt_input;
    Button btn_guess;
    ListView lv_history;
    Integer curInput = Integer.MIN_VALUE;
    List<String> inputHistory;
    Boolean isGameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
    }

    public void Init(){
        game = new GuessingGame(10);
        isGameOver = true;
        txt_input = (EditText)findViewById(R.id.etxt_input);
        btn_guess = (Button)findViewById(R.id.btn_guess);
        lv_history = (ListView)findViewById(R.id.listView);
        inputHistory = new ArrayList<String>();
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inputHistory);
    }

    public void CheckNumber(View view){
        byte result;
        if(CheckInput()) {
            if(game.GetRemainingTimes()>0) {
                result = game.GuessNum(curInput);
            }else Toast.makeText(this,"Game over",Toast.LENGTH_SHORT);
        }
        if(game.GetRemainingTimes()==0) GameOver();

    }


    void GameOver(){
        isGameOver = true;
    }

    Boolean CheckInput(){
        String tError = "";
        Integer tAns = Integer.MIN_VALUE;
        String tInput = txt_input.getText().toString();
        if(tInput.isEmpty()) tError = "Please input a number to guess";
        tAns = Integer.getInteger(tInput,Integer.MIN_VALUE);
        if(tAns == Integer.MIN_VALUE) tError = "Invalid input! guess range is 0-100";
        if(!tError.isEmpty()) {
            Toast.makeText(this,tError,Toast.LENGTH_SHORT);
            return false;
        }
        curInput = tAns;
        return true;
    }
}
