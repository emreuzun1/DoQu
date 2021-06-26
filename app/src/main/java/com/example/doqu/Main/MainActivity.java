package com.example.doqu.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doqu.DBHelper;
import com.example.doqu.R;
import com.example.doqu.SignUpScreen;

public class MainActivity extends AppCompatActivity {

    Button signin,signup;
    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signin = findViewById(R.id.signIn);
        signup = findViewById(R.id.signUp);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        Context context = this;

        DBHelper db = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUp();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.findUser(username.getText().toString(),password.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(),MainPage.class);
                    intent.putExtra("USERNAME",username.getText().toString());
                    startActivity(intent);
                }else {
                    Toast.makeText(context,R.string.wrong_entry,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void goToSignUp() {
        Intent intent = new Intent(this, SignUpScreen.class);
        startActivity(intent);
    }


}