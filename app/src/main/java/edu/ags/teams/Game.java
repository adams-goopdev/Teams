package edu.ags.teams;

import static edu.ags.teams.MainActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class Game extends AppCompatActivity {

    public static final String TAG = "myDebug";
    int width;
    int height;
    Board board;
    String[][] cellValues = new String[Board.BOARDSIZE][Board.BOARDSIZE];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getScreenDims();
        board = new Board(width);

        initialSetup();
        board.cellValues = cellValues;
        setContentView(new DrawView(this));

    }

    private void initialSetup() {

        for(int row = 0; row < cellValues[0].length; row ++)
        {
            for(int col = 0; col < cellValues[1].length; col ++)
            {
                if(row < 2) //Add Red Circles
                {
                    cellValues[row][col] = "1";
                }
                else if (row >= Board.BOARDSIZE - 2 ) // Add Blue Circles;
                {
                    cellValues[row][col] = "2";
                }
                else // Add empty strings
                {
                    cellValues[row][col] = "";
                }
            }
        }
    }

    private void getScreenDims(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        Log.d(TAG, "getScreenDims: " + width + ":" + height);
    }

    private class DrawView extends View implements View.OnTouchListener {

        String turn = "1";

        public DrawView(Game game) {
            super(game);
            this.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            Point pt = new Point();

            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            {
                pt.x = (int)motionEvent.getX();
                pt.y = (int)motionEvent.getY();

                if(board.hitTest(pt, turn, this.getContext()) != "-1")
                {
                    turn = (turn == "1") ? "2" : "1";
                }
                else
                {
                    return false;
                }
                invalidate();
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            canvas.drawColor(Color.DKGRAY);
            board.Draw(canvas);
        }

    }
}