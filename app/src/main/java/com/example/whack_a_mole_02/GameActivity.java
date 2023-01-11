package com.example.whack_a_mole_02;

import android.app.AlertDialog;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private final List<Snakes> snakesList = new ArrayList<>();
    private static final int pointSize = 10;
    private static final int defaultTalePoints = 3;
    private static final int snakeColor = Color.GREEN;
    public int speed = 500;
    private int move = 0;
    MediaPlayer mediaPlayer;
    SoundPool soundPool;
    AudioAttributes audioAttributes;

    private SurfaceView srfView;
    private TextView scoreTxt;
    private Timer timer;
    private int score;
    private int positionX, positionY;
    private SurfaceHolder surfaceHolder;
    int snakePositionX = 50;
    int snakePositionY = 50;
    private Canvas canvas = null;
    private Paint paint = null;
    int headPositionX = snakesList.get(0).getPositionX();
    int headPositionY = snakesList.get(0).getPositionY();
    private AssistStructure.ViewNode surfaceView;

    /*
    0 = top
    1 = right
    2 = bottom
    3 = left
    */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        srfView = findViewById(R.id.surfaceView);
        scoreTxt= findViewById(R.id.scoreTxt);
        final AppCompatImageButton topBtn = findViewById(R.id.topBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.leftBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.rightBtn);
        final AppCompatImageButton botBtn = findViewById(R.id.bottomBtn);

        srfView.getHolder().addCallback(this);

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();



        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();



        topBtn.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View view) {
                move = 0;
            }
    });
        leftBtn.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View view) {
                move = 1;
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View view) {
                move = 2;
            }
        });
        botBtn.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View view) {
                move = 3;
            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;

        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
    private void init()    {
       snakesList.clear();
       scoreTxt.setText("0");
       score = 0;
       move = 0;
       int startPositionX = 30;
       int startPositionY = 30;

       for (int i = 0; i < defaultTalePoints; i++)
        {
            Snakes snakes = new Snakes(startPositionX, pointSize);
            snakesList.add(snakes);

            startPositionX = startPositionX - (pointSize * 2);
        }

        addApples();
        moveSnake();

    }

    private void addApples(){

        //int surfaceWidth = surfaceView.getWidth() - (pointSize * 2);
        //int surfaceHeight = surfaceView.getHeight() - (pointSize * 2);

        int randomXPosition = new Random().nextInt (50);
        int randomYPosition = new Random().nextInt (50);

        if((randomXPosition % 2) != 0){
            randomXPosition = randomXPosition + 1;
        }
        if((randomYPosition % 2) != 0){
            randomYPosition = randomYPosition + 1;
        }

        positionX = (pointSize * randomXPosition) + pointSize;
        positionY = (pointSize * randomYPosition) + pointSize;

        scoreTxt.setText("5");
    }
    private void moveSnake(){

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {



                if(headPositionX == positionX && headPositionY == positionY){
                    grow();
                    addApples();
                }


                switch (move){
                    case 0:
                        snakesList.get(0).setPositionX(headPositionX);
                        snakesList.get(0).setPositionY(headPositionY + (pointSize * 2));
                        break;
                    case 1:
                        snakesList.get(0).setPositionX(headPositionX + (pointSize * 2));
                        snakesList.get(0).setPositionY(headPositionY);
                        break;
                    case 2:
                        snakesList.get(0).setPositionX(headPositionX);
                        snakesList.get(0).setPositionY(headPositionY - (pointSize * 2));
                        break;
                    case 3:
                        snakesList.get(0).setPositionX(headPositionX - (pointSize * 2));
                        snakesList.get(0).setPositionY(headPositionY);
                        break;
                }

                if(endGame(headPositionX, headPositionY)){
                    timer.purge();
                    timer.cancel();

                    AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                    builder.setMessage("ur score = " + score);
                    builder.setTitle("game over");
                    builder.setCancelable(false);
                    builder.setPositiveButton("start again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            init();
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                    //Intent intent = new Intent(this, EndActivity.class);
                   // startActivity(intent);
                }
                else {
                    onDraw();

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }, 1000 - speed, 1000 - speed);
    }
    private void grow(){

        Snakes snakes = new Snakes(0, 0);
        snakesList.add(snakes);
        score++;
        soundPool.load(this, R.raw.martelada, 1);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreTxt.setText(String.valueOf(score));
            }
        });
    }
    private void onDraw() {
        canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
        canvas.drawCircle(snakesList.get(0).getPositionX(), snakesList.get(0).getPositionY(), pointSize, createPointColorSnake());
        canvas.drawCircle(positionX, positionY, pointSize, createPointColorSnake());
        for (int i = 1; i < snakesList.size(); i++) {
            int axl1 = snakesList.get(i).getPositionX();
            int axl2 = snakesList.get(i).getPositionY();

            snakesList.get(i).setPositionX(headPositionX);
            snakesList.get(i).setPositionY(headPositionY);
            canvas.drawCircle(snakesList.get(i).getPositionX(), snakesList.get(i).getPositionY(), pointSize, createPointColorSnake());

            headPositionX = axl1;
            headPositionY = axl2;
        }
    }
    private boolean endGame(int headPositionX, int headPositionY)    {
        boolean gameOver = false;

        if(snakesList.get(0).getPositionX() <= 0 || snakesList.get(0).getPositionX() >= surfaceView.getWidth() || snakesList.get(0).getPositionY() <= 0 || snakesList.get(0).getPositionY() >= surfaceView.getHeight()){
            gameOver = true;
        }
        else{
            for (int i = 0; i < snakesList.size(); i++){
                if (headPositionX == snakesList.get(i).getPositionX() || headPositionY == snakesList.get(i).getPositionY()){
                    gameOver = true;
                }
            }
        }


        return gameOver;
    }

    private Paint createPointColorSnake(){
        if(paint == null){
            paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
        }
        return paint;
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






    /*Button btnFinish;
    Button[] btnDirections;
    Handler handler = new Handler();
    Chronometer chronometer;
    TextView txtScore;
    Boolean isSaved = false;

    private int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnFinish = findViewById(R.id.btnFinish);
        chronometer = findViewById(R.id.chronometer);
        txtScore = findViewById(R.id.text_score);


        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        maxStreams = 2;

        isSaved = false;
        mp = MediaPlayer.create(this, R.raw.music);
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(maxStreams)
                .build();

        snd_score = soundPool.load(this, R.raw.mole_hit, 1);
        snd_hammer = soundPool.load(this, R.raw.martelada, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(this, DELAY_START);
        mp.start();
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
            mp.stop();
            Intent intent = new Intent(this, EndActivity.class);
            startActivity(intent);
        }
    }

    public void onClickMole(View view)
    {
        soundPool.play(snd_hammer, 100, 100, 1, 0, 1);
    }

    private int ChooseRandomMole(){
    }

    private void MoleShow(int randomMole){
    }

     */



}
