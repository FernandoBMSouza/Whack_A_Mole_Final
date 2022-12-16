package com.example.whack_a_mole_02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {

    Button btnReturn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        btnReturn = findViewById(R.id.btnReturn);
    }

    public void onClick(View view){
        if(view == btnReturn){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
