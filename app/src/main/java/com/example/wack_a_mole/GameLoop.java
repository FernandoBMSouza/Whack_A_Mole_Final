package com.example.wack_a_mole;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class GameLoop extends View implements Runnable {
    long time;
    final int DELAY_MOLE_UP = 800;
    final int END_TIME = 30000;
    final int DELAY_BETWEEN_MOLE = 1500;
    final int DELAY_START = 1000;
    Chronometer t;
    Handler handler;
    public int points = 0;

    Button[] btnMoles = new Button[9];

    public GameLoop(Context context) {
        super(context);

        t = (Chronometer)findViewById(R.id.chronometer);
    }

    @Override
    public void run() {
        update();
        invalidate();

        handler = new Handler();
        handler.postDelayed(this, DELAY_START);
    }

    public void startNewGame(){
        points = 0;

        btnMoles[0] = findViewById(R.id.btnMole01);
        btnMoles[1] = findViewById(R.id.btnMole02);
        btnMoles[2] = findViewById(R.id.btnMole03);
        btnMoles[3] = findViewById(R.id.btnMole04);
        btnMoles[4] = findViewById(R.id.btnMole05);
        btnMoles[5] = findViewById(R.id.btnMole06);
        btnMoles[6] = findViewById(R.id.btnMole07);
        btnMoles[7] = findViewById(R.id.btnMole08);
        btnMoles[8] = findViewById(R.id.btnMole09);

        startChronometer();
    }

    private void update()
    {
        time = SystemClock.elapsedRealtime()-t.getBase();

        if(time < END_TIME)
        {
            MoleShow(ChooseRandomMole());
        }

        if(time >= END_TIME)
        {
            ((Chronometer) findViewById(R.id.chronometer)).stop();
        }
        handler.postDelayed(this, DELAY_BETWEEN_MOLE);
    }

    private void MoleShow(int randomMole) {
        //btnMoles[randomMole].setBackground(getDrawable(R.drawable.selector_btn_mole));
        btnMoles[randomMole].setBackgroundResource(R.drawable.selector_btn_mole);

        handler.postDelayed(() -> {

            if(btnMoles[randomMole].isPressed())
                points++;

            //btnMoles[randomMole].setBackground(getDrawable(R.drawable.mole_down));
            btnMoles[randomMole].setBackgroundResource(R.drawable.mole_down);
        }, DELAY_MOLE_UP);
    }

    private int ChooseRandomMole() {
        int randomId = new Random().nextInt(9);

        for (int i = 0; i < btnMoles.length; i++)
        {
            if(randomId == i)
            {
                return i;
            }

            continue;
        }

        return 0;
    }

    public void startChronometer() {
        ((Chronometer) findViewById(R.id.chronometer)).start();
    }

    private void saveScore() {
        try {
            FileOutputStream fos = getContext().openFileOutput("score.txt", Context.MODE_APPEND);
            String strScore = String.valueOf(points).concat("\n");
            fos.write (strScore.getBytes());
            fos.close();
        } catch (FileNotFoundException fnf){
            fnf.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

}
