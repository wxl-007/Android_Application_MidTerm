package com.midterm.microproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {


    Button btn_Login,btn_Register;
    EditText etxt_username,etxt_password;
    ImageView btn_Rank;
    SQLiteDatabase userDB;
    String errorMsg;
    TextView txt_error;
    String curName;
    String curScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_Login = (Button)findViewById(R.id.btn_login);
        btn_Register= (Button)findViewById(R.id.btn_rRegister);
        btn_Rank = (ImageView)findViewById(R.id.btn_Rank);
        etxt_username = (EditText)findViewById(R.id.etxt_username);
        etxt_password= (EditText)findViewById(R.id.etxt_psd);
        userDB = openOrCreateDatabase("UserDB",MODE_PRIVATE,null);
        txt_error = (TextView) findViewById(R.id.txt_Error);
        curName = "Guest";
        curScore = "0";
        InitDatabase();
    }

    void InitDatabase(){
        userDB.execSQL("CREATE TABLE IF NOT EXISTS UserTable(Username VARCHAR(30),Password VARCHAR(30),Score INT(10));");
        if(TableIsEmpty())
            userDB.execSQL("INSERT INTO UserTable('Username','Password','Score') VALUES('admin','admin123',100);");
    }

    boolean TableIsEmpty(){
        String count = "SELECT count(*) FROM UserTable ; ";
        Cursor mcursor = userDB.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        Log.i("DB",icount + " count ");
        return icount <= 0;
    }

    public void Register( View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void Login( View view){
        if(LoginValidation()){
            txt_error.setEnabled(false);
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("Name",curName);
            intent.putExtra("Score",curScore);

            startActivity(intent);
        }
    }

    boolean LoginValidation(){
        String psd = etxt_password.getText().toString();
        String userName = etxt_username.getText().toString();
        if(!psd.equals("") && !userName.equals("") && psd != null && userName !=null){
            try {
                Cursor cursor = userDB.rawQuery("SELECT Username , Password , Score " +
                        "FROM UserTable " +
                        "WHERE Username =?  AND Password = ?; ", new String[]{userName,psd});
                cursor.moveToFirst();
                if(cursor.getCount() == 0){
                    errorMsg = "cannot find user!";
                }
                else {
                    errorMsg = "";
                    Log.i("DB","count : "+cursor.getCount());
                    curName = userName;
                    curScore = cursor.getString(2);
                    return true;
                }
            }catch (Exception e){
                errorMsg = "cannot find user!";
                txt_error.setEnabled(true);
                txt_error.setText(errorMsg);
                return false;
            }
        }else{
            errorMsg = "Please input username and password!";
        }

        if(errorMsg != ""){
            txt_error.setEnabled(true);
            txt_error.setText(errorMsg);
        }
        return false;
    }


    public void RankList( View view){
        Intent intent = new Intent(this,RankActivity.class);
        startActivity(intent);
    }

    public void PlayGame( View view){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("Name",curName);
        intent.putExtra("Score",curScore);
        startActivity(intent);
    }
}
