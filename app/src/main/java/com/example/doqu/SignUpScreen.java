package com.example.doqu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doqu.Main.MainActivity;

public class SignUpScreen extends AppCompatActivity {

    Button signup;
    EditText name, username, password, confirmPassword;
    TextView usernameWarning, passwordWarning, checkPasswordWarning, nameWarning;
    Boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        createNotificationChannel();

        signup = findViewById(R.id.signUp);
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        usernameWarning = (TextView) findViewById(R.id.usernameWarning);
        passwordWarning = (TextView) findViewById(R.id.passwordWarning);
        checkPasswordWarning = (TextView) findViewById(R.id.checkPasswordWarning);
        nameWarning = (TextView) findViewById(R.id.nameWarning);

        DBHelper db = new DBHelper(this);
        Intent intent = new Intent(this, MainActivity.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DoQu")
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle("Welcome!")
                .setContentText("Hey newbie. Welcome to the club!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().isEmpty()) {
                    isValid = false;
                }
            }

            ;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid = checkUsername(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    isValid = false;
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().isEmpty()) {
                    isValid = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid = checkPassword(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    isValid = false;
                }
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().isEmpty()) {
                    isValid = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.getText().toString().equals(s.toString())) {
                    checkPasswordWarning.setVisibility(View.INVISIBLE);
                    isValid = true;
                } else {
                    isValid = false;
                    checkPasswordWarning.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    isValid = false;
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().isEmpty()) {
                    isValid = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid = checkName(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    isValid = false;
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid && check()) {
                    if (db.insertUser(db.getWritableDatabase(), name.getText().toString(), username.getText().toString(), password.getText().toString())) {
                        notificationManager.notify(100, builder.build());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.fail_create_account, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.fail_create_account, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "personChannel";
            String description = "Channel for person notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("DoQu", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean check() {
        if (name.getText().toString().isEmpty() || username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean checkUsername(CharSequence s) {
        if (!s.toString().isEmpty()) {

            if (s.toString().matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) {
                usernameWarning.setVisibility(View.INVISIBLE);
                return true;
            } else {
                usernameWarning.setVisibility(View.VISIBLE);
                return false;

            }
        }
        usernameWarning.setVisibility(View.VISIBLE);
        return false;
    }

    private boolean checkPassword(CharSequence s) {
        if (!s.toString().isEmpty()) {
            if (s.toString().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")) {
                passwordWarning.setVisibility(View.INVISIBLE);
                return true;
            } else {
                passwordWarning.setVisibility(View.VISIBLE);
                return false;
            }
        }
        passwordWarning.setVisibility(View.VISIBLE);
        return false;
    }

    private boolean checkName(CharSequence s) {
        if (!s.toString().isEmpty()) {
            if (s.toString().matches("^[a-zA-Z]{3,18}+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")) {
                nameWarning.setVisibility(View.INVISIBLE);
                return true;
            } else {
                nameWarning.setVisibility(View.VISIBLE);
                return false;
            }
        }
        nameWarning.setVisibility(View.VISIBLE);
        return false;
    }
}