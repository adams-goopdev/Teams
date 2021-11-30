package edu.ags.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class Graphics extends AppCompatActivity {

    public static final String TAG = "myDebug";
    RectF targetRect = new RectF(100,100,400, 200);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ShowScreen(this));
        this.setTitle("Graphics");

    }

    class ShowScreen extends View implements View.OnTouchListener{

        int lastTouchX, lastTouchY; //Last Location
        int posX = 600 , posY = 240; //CurrentLocation
        int dX, dY; //Difference between last and current
        Boolean isDragging = false; //Is the mouse still down

        Bitmap aceOfSpades = BitmapFactory.decodeResource(getResources(), R.drawable.acespades);


        public ShowScreen(Context context) {
            super(context);
            this.setOnTouchListener(this);
        }

        @Override
        protected void onDraw(Canvas canvas){
            Log.d(TAG, "onDraw: First Step");

            // Change the background color
            canvas.drawColor(0xFFB0B0B0);

            //Set up the paint for the text on the button
            Paint paintFore = new Paint();
            paintFore.setFakeBoldText(true);
            paintFore.setTextSize(48);
            paintFore.setColor(Color.WHITE);
            paintFore.setTextAlign(Paint.Align.CENTER);

            float centerX = targetRect.centerX();
            float centerY = targetRect.centerY();

            //Setup the paint for the rectangle
            Paint paintBack = new Paint();
            paintBack.setColor(0xFF606060);


            //Draw the rectangle
            canvas.drawRoundRect(targetRect, 12, 12, paintBack);
            canvas.drawText("Click Me!", centerX, centerY + 15, paintFore);


            //Create a rectangle
            Random generator = new Random();
            int red = generator.nextInt(256);
            int green = generator.nextInt(256);
            int blue = generator.nextInt(256);

            Paint  paint = new Paint();
            paint.setColor(Color.rgb(red,green,blue));

            Rect rect = new Rect();
            rect.left = 40;
            rect.top  = 240;
            rect.right = 500;
            rect.bottom = 780;

            canvas.drawRect(rect,paint);

            canvas.drawBitmap(aceOfSpades, posX, posY, null);
        }


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            //Get where I clicked.
            float x = motionEvent.getX();
            float y = motionEvent.getY();

            //Check to see if the click is in the rectangle and a down click
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            {
                if(targetRect.contains(x,y))
                {
                    //Change the screen state to force a redraw\
                    invalidate();

                    Log.d(TAG, "onTouch: " + x + ":" + y);
                }

                Rect box = new Rect(posX, posY, posX + 90, posY + 135);
                if(box.contains((int)x,(int)y))
                {
                    isDragging = true;
                    lastTouchX = (int)x;
                    lastTouchY = (int)y;
                }


            }

            if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                Log.d(TAG, "onTouch: Move");
                if(isDragging)
                {
                    dX = (int)x - lastTouchX;
                    dY = (int)y - lastTouchY;

                    lastTouchX = (int)x;
                    lastTouchY = (int)y;

                    posX+=dX;
                    posY+=dY;
                    invalidate();
                }
            }

            if(motionEvent.getAction() == MotionEvent.ACTION_UP)
            {
                isDragging = false;
            }

            return true;
        }
    }
}