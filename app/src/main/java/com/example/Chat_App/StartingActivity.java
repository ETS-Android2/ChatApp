package com.example.Chat_App;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        // Enabling Firebase Persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Intent Main = new Intent(StartingActivity.this,LogInActivity.class);
        startActivity(Main);
        finish();
    }
}