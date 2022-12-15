package com.example.wack_a_mole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class GameActivity extends Activity {

    GameLoop gameLoop;
    TextView textPoints;
    Button btnFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameLoop = new GameLoop(this);
        setContentView(R.layout.activity_game);

        //textPoints.setText("Points: " + gameLoop.points);
        textPoints = findViewById(R.id.txtPoints);
        btnFinish = findViewById(R.id.btnFinish);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            gameLoop.startNewGame();
        }
        catch (Exception e)
        {
            Log.i("GameLoopError", "Erro_GameLoop: " + e);
        }
    }


    public void onClick(View view) {
        if(view == btnFinish)
        {
            Intent intent = new Intent(this, FinalActivity.class);
            startActivity(intent);
        }
    }
}
