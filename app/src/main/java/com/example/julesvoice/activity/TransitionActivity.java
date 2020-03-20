package com.example.julesvoice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.julesvoice.R;

public class TransitionActivity extends AppCompatActivity
{

    private Button btnMusic, btnVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        btnMusic = findViewById(R.id.buttonMusic);
        btnVideo = findViewById(R.id.buttonVideo);

        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransitionActivity.this, LectureMusiqueActivity.class);
                intent.putExtra("type", "musique");
                startActivity(intent);
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransitionActivity.this, LectureMusiqueActivity.class);
                intent.putExtra("type", "video");
                startActivity(intent);
            }
        });
    }
}
