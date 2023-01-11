package com.example.whack_a_mole_02;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class game extends AppCompatActivity {

    walkActivity wA;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        wA = new walkActivity(this);
        setContentView(wA);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wA.update();
                wA.invalidate();

                new Handler().postDelayed(this, 300);
            }
        },300);
    }
}
