package com.example.whack_a_mole_02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class walkActivity  extends View{

    private Bitmap character;
    private int frames = 1;
    private int currentFrame = 0;
    private int x, y;
    private int targetX, targetY;


    public walkActivity(Context context) {
        super(context);

        character = BitmapFactory.decodeResource(getResources(), R.drawable.mole_down);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect srcRect = new Rect();
        srcRect.top = 0;
        srcRect.left = currentFrame * (character.getWidth() / 6);
        srcRect.right = srcRect.left + (character.getWidth() / 6);
        srcRect.bottom = character.getHeight();

        Rect dstRect = new Rect();
        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = dstRect.left + (character.getWidth() / 6);
        dstRect.bottom = dstRect.top + character.getHeight();

        canvas.drawBitmap(character, srcRect, dstRect, null);
    }
    public void update(){
        float deltaX = targetX - x;
        float deltaY = targetY - y;

        float mag = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        deltaX /= mag;
        deltaY /= mag;

        x += deltaX * 20;
        y += deltaY * 20;
    }

}
