package com.example.whack_a_mole_02;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;

public class RankingActivity extends AppCompatActivity {

    Button btnReturn;
    TextView txtRanking; //TODO: Lógica para alterar o texto do ranking

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        btnReturn = findViewById(R.id.btnReturn);
        txtRanking = findViewById(R.id.text_ranking);
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*TODO:
        - Pegar os últimos 10 scores
        - Fazer um bubble sort para organizar em ordem decrescente
        */

        ArrayList<Integer> scores = new ArrayList<Integer>();

        try {
            FileInputStream fis = openFileInput("score.txt");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis));

            String line = br.readLine();
            //scores.add(Integer.parseInt(line));

            while (line != null) {
                int v = Integer.parseInt(line);
                scores.add(v);
                Log.i("Score", "Line: " + line);
                line = br.readLine();
            }

            br.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = scores.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (scores.get(j) < scores.get(j + 1)) {
                    // Faz a troca de numeros sem usar auxiliar
                    scores.set(j, scores.get(j) + scores.get(j + 1));
                    scores.set(j + 1, scores.get(j) - scores.get(j + 1));
                    scores.set(j, scores.get(j) - scores.get(j + 1));
                }
            }
        }

        String txt = "";
        int size = scores.size();

        if(size > 10)
            size = 10;

        for(int i = 0; i < size; i++)
        {
            txt += "" + scores.get(i) + "\n";
        }
        Log.i("Score", "Tamanho: " + scores.size());
        txtRanking.setText(txt);
    }

    public void onClick(View view){
        if(view == btnReturn){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
