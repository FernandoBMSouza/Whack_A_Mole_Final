package com.example.whack_a_mole_02;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements Runnable {

    Button btnFinish;
    Button[] btnMoles = new Button[9];
    Handler handler = new Handler();
    Chronometer chronometer;
    TextView txtScore;
    Boolean isSaved = false;

    final int DELAY_START = 500;
    final int DELAY_MOLE_UP = 800;
    final int DELAY_BETWEEN_MOLE = 1500;
    final int END_TIME = 3 * 10000;

    private int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnFinish = findViewById(R.id.btnFinish);
        chronometer = findViewById(R.id.chronometer);
        txtScore = findViewById(R.id.text_score);

        btnMoles[0] = findViewById(R.id.btnMole01);
        btnMoles[1] = findViewById(R.id.btnMole02);
        btnMoles[2] = findViewById(R.id.btnMole03);
        btnMoles[3] = findViewById(R.id.btnMole04);
        btnMoles[4] = findViewById(R.id.btnMole05);
        btnMoles[5] = findViewById(R.id.btnMole06);
        btnMoles[6] = findViewById(R.id.btnMole07);
        btnMoles[7] = findViewById(R.id.btnMole08);
        btnMoles[8] = findViewById(R.id.btnMole09);
        isSaved = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(this, DELAY_START);
        chronometer.start();
    }

    @Override
    public void run() {
        long time = SystemClock.elapsedRealtime() - chronometer.getBase();
        txtScore.setText("Score: " + score);

        if(time < END_TIME){
            MoleShow(ChooseRandomMole());
        }
        else {

            if(!isSaved) {
                saveScore();
                isSaved = true;
            }

            chronometer.stop();
        }

        handler.postDelayed(this, DELAY_BETWEEN_MOLE);
    }

    public void onClick(View view){
        if(view == btnFinish){
            Intent intent = new Intent(this, EndActivity.class);
            startActivity(intent);
        }
    }

    private int ChooseRandomMole(){
        int randomId = new Random().nextInt(btnMoles.length);

        for (int i = 0; i < btnMoles.length; i++)
        {
            if(randomId == i)
            {
                return i;
            }
        }
        return 0;
    }

    private void MoleShow(int randomMole){
        btnMoles[randomMole].setBackground(getDrawable(R.drawable.selector_btn_mole));

        handler.postDelayed(() -> {
            if(btnMoles[randomMole].isPressed())
                score++;
            btnMoles[randomMole].setBackground(getDrawable(R.drawable.mole_down));
        }, DELAY_MOLE_UP);
    }

    private void saveScore() {

        try {
            FileOutputStream fos = GameActivity.this.openFileOutput("score.txt", Context.MODE_APPEND);
            String strScore = String.valueOf(score).concat("\n");
            fos.write (strScore.getBytes());
            fos.close();
        } catch (FileNotFoundException fnf){
            fnf.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
