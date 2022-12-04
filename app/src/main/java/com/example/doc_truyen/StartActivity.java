package com.example.doc_truyen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageView imgstart = findViewById(R.id.img_start);
        imgstart.setOnClickListener(view -> {
            startActivity( new Intent(this,LoginActivity.class));
        });
    }
}