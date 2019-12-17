package com.updesigns.moneycircle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    String chkpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.pass);
    }

    //On clicking create account. Opens create account page
    public void createAccount(View v){
        Intent intent = new Intent(LoginActivity.this,CreateAccount.class);
        startActivity(intent);
    }

    public void logIn(View v){
        String userName = username.getText().toString();
        String pass = password.getText().toString();
        chkpass = MainActivity.database.MyDao().getPassword(userName);      //this would be null if username is invalid
        if (pass.equals(chkpass)){          //checks if password is valid
            Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
            Bundle b = new Bundle();
            b.putString("usern", userName);     //passes the username to next screen
            intent.putExtras(b);
            startActivity(intent);              //opens homescreen
            Toast.makeText(this,"Login Successful", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "INCORRECT USERNAME OR PASSWORD", Toast.LENGTH_LONG).show();
            username.setText("");
            password.setText("");
        }
    }

    @Override
    public void onBackPressed() {           //To disable back button
    }
}
