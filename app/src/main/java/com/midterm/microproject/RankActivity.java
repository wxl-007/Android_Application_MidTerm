package com.midterm.microproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity {


    ListView lv_rank;
    List<String> rankData;
    SQLiteDatabase userDB ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        lv_rank = (ListView) findViewById(R.id.lv_rankList);
        userDB = openOrCreateDatabase("UserDB",MODE_PRIVATE,null);
        InitAllUser();
    }


    void InitAllUser(){
        Cursor cursor =  userDB.rawQuery("SELECT Username,Score " +
                "FROM  UserTable " +
                "ORDER BY Score DESC;",null);
        cursor.moveToFirst();
        rankData = new ArrayList<String>();
        rankData.add("NO \t\t\t Username \t\t\t Score");
        int count =1;
        rankData.add( count +" \t\t\t\t "+ cursor.getString(0) +" \t\t\t\t "+cursor.getString(1) );
        while (cursor.moveToNext()){
            count++;
            rankData.add( count+" \t\t\t\t "+ cursor.getString(0) +" \t\t\t\t "+cursor.getString(1) );
        }
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, rankData);
        lv_rank.setAdapter(itemsAdapter);
    }
}
