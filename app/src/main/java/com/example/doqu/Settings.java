package com.example.doqu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doqu.Main.MainActivity;
import com.example.doqu.Main.MainPage;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    DBHelper db;
    Button deleteAccountButton,deleteAllQuestions;
    String username;
    Intent intentToMain,intentToMainPage;
    ImageView turkishFlag,englishFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = new DBHelper(this);
        username = getIntent().getStringExtra("USERNAME");
        intentToMain = new Intent(this,MainActivity.class);
        intentToMainPage = new Intent(this,MainPage.class);


        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);

        deleteAccountButton = findViewById(R.id.deleteAccountButton);
        deleteAllQuestions = findViewById(R.id.deleteAllQuestionButton);
        turkishFlag = findViewById(R.id.turkishflag);
        englishFlag = findViewById(R.id.usflag);

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.deleteUser(username)){
                    startActivity(intentToMain);
                }
            }
        });

        deleteAllQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.deleteQuestions(username)){
                    Toast.makeText(getApplicationContext(),R.string.deleted_questions,Toast.LENGTH_SHORT).show();
                    startActivity(intentToMain);
                }else {
                    Toast.makeText(getApplicationContext(),R.string.went_wrong,Toast.LENGTH_SHORT).show();
                }
            }
        });

        turkishFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("tr");
            }
        });

        englishFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
            }
        });
    }

    public void setLocale(String l){
        Locale locale = new Locale(l);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("Language",l);
        editor.apply();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}