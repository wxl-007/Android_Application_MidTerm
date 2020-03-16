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

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {


    Button btn_Login,btn_Register;
    EditText etxt_username,etxt_password;
    ImageView btn_Rank;
    SQLiteDatabase userDB;
    String errorMsg;
    TextView txt_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_Login = (Button)findViewById(R.id.btn_login);
        btn_Register= (Button)findViewById(R.id.btn_register);
        btn_Rank = (ImageView)findViewById(R.id.btn_Rank);
        etxt_username = (EditText)findViewById(R.id.etxt_username);
        etxt_password= (EditText)findViewById(R.id.etxt_psd);
        userDB = openOrCreateDatabase("UserDB",MODE_PRIVATE,null);
        txt_error = (TextView) findViewById(R.id.txt_error);
        InitDatabase();
    }

    void InitDatabase(){
        userDB.execSQL("CREATE TABLE IF NOT EXISTS UserTable(Username VARCHAR(30),Password VARCHAR(30),Score INT(10));");
        if(TableIsEmpty())
            userDB.execSQL("INSERT INTO UserTable('Username','Password','Score') VALUES('admin','admin123',100);");
    }

    boolean TableIsEmpty(){
        try {
            String tQuery = "SELECT Username " +
                    "FROM UserTable " +
                    "WHERE Username='admin' ;";
            Cursor c = userDB.rawQuery(tQuery, null);
            c.moveToFirst();
            Log.i("DB", c.getString(1));
            return c.getCount() == 0;
        }catch (Exception ex){
            return false;
        }
    }

    public void Register( View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void Login( View view){
        if(LoginValidation()){
            txt_error.setEnabled(false);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

    boolean LoginValidation(){
        String psd = etxt_password.getText().toString();
        String userName = etxt_username.getText().toString();
        if(psd!= "" && userName!=""){
            try {
                Cursor cursor = userDB.rawQuery("SELECT Username,Password " +
                        "FROM Usertable " +
                        "WHERE Username=" + userName + " AND Password=" + psd + ";", null);

                if(cursor.getCount() == 0){
                    errorMsg = "cannot find user!";
                }
                else {
                    errorMsg = "";
                    Log.i("DB","count : "+cursor.getCount());
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
        startActivity(intent);
    }
}
