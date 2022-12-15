package com.example.wack_a_mole;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class RankingActivity extends AppCompatActivity {
    Button btnReturn;
    TextView txtRanking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        btnReturn = findViewById(R.id.btnReturn);
        txtRanking = findViewById(R.id.txtRanking);
    }

    @Override
    protected void onStart() {
        super.onStart();

        int score = 0;

        try {
            FileInputStream fis = openFileInput("score.txt");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis));

            String line = br.readLine();
            score = Integer.parseInt(line);

            while ( line != null){
                score = Integer.parseInt(line);
                line = br.readLine();
            }

            br.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        txtRanking.setText(String.valueOf(score));
    }

    public void onClick(View view){
        if(view == btnReturn){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
