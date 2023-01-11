package com.example.whack_a_mole_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnRanking;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.start();

        btnPlay = findViewById(R.id.btnPlay);
        btnRanking = findViewById(R.id.btnRanking);
    }

    public void onClick(View view) {
        if(view == btnPlay) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            mediaPlayer.stop();
        }
        if(view == btnRanking){
            Intent intent = new Intent(this, RankingActivity.class);
            startActivity(intent);
            mediaPlayer.stop();
        }
    }
}