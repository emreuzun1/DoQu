package com.example.doqu.Main;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doqu.DBHelper;
import com.example.doqu.Profile.ProfileActivity;
import com.example.doqu.R;
import com.example.doqu.Settings;
import com.google.android.material.navigation.NavigationView;


public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ImageView exitImage;
    private TextView navName,navUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        exitImage = header.findViewById(R.id.exitImage);
        navName = header.findViewById(R.id.nav_name);
        navUsername = header.findViewById(R.id.nav_username);

        Cursor res = new DBHelper(this).getProfile(getIntent().getStringExtra("USERNAME"));

        while(res.moveToNext()){
            navName.setText(res.getString(1));
            navUsername.setText("@" + res.getString(0));
        }

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        exitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = null;

        switch(item.getItemId()){
            case R.id.nav_home:
                intent = new Intent(this,MainPage.class);
                break;
            case R.id.nav_profile:
                intent = new Intent(this, ProfileActivity.class);
                break;
            case R.id.nav_settings:
                intent = new Intent(this, Settings.class);
                break;
            case R.id.nav_share:
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"You have to come an join us! -DoQu");
                intent.setType("text/plain");
                startActivity(intent);
                break;
        }

        if(intent != null){
            intent.putExtra("USERNAME",getIntent().getStringExtra("USERNAME"));
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}