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
import android.widget.Toast;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    Button btn_Register;
    EditText etxt_username;
    EditText etxt_password;
    EditText etxt_confirmPassword;
    SQLiteDatabase userDB ;
    TextView txt_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_Register= (Button)findViewById(R.id.btn_register);
        etxt_username = (EditText)findViewById(R.id.etxt_username);
        etxt_password= (EditText)findViewById(R.id.etxt_psd);
        etxt_confirmPassword=(EditText)findViewById(R.id.etxt_confirmPsd);
        txt_error = (TextView)findViewById(R.id.txt_error);
        userDB = openOrCreateDatabase("UserDB",MODE_PRIVATE,null);
    }

    public void Register( View view){
        if(RegisterValidation()){
            txt_error.setEnabled(false);
            RegisterInDatabase();
        }
    }

    void RegisterInDatabase(){
        String psd = etxt_password.getText().toString();
        String username = etxt_username.getText().toString();

        String cpsd = etxt_confirmPassword.getText().toString();
        userDB.execSQL("INSERT INTO UserTable VALUES(username,psd)");
        Toast.makeText(this,"register success!",Toast.LENGTH_SHORT);
        Log.i("DB","register success");

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    boolean RegisterValidation(){
        String psd = etxt_password.getText().toString();
        String cpsd = etxt_confirmPassword.getText().toString();
        if(psd != cpsd){
            txt_error.setText(" password not match!");
            txt_error.setEnabled(true);
            return false;
        }
        String userName = etxt_username.getText().toString();
        if(psd!= "" && userName!=""){
            Cursor cursor = userDB.rawQuery("SELECT Username" +
                    "FROM Usertable" +
                    "WHERE Username=" + userName+";" ,null);
            if(cursor.getCount() == 0) return true;
            else {
                txt_error.setText("user name already used!");
                txt_error.setEnabled(true);
                return false;
            }
        }else{
            txt_error.setText("please fill out all column");
            txt_error.setEnabled(true);
        }
        return false;
    }

    // for taking pic as profile image
    public void TakePic(View view){

    }
}
