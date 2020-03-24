package com.golan.amit.nameassemble;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class NamingActivity extends AppCompatActivity implements View.OnTouchListener {

    TextView[] tvLetters, tvBase;
    private NamingHelper nh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);
        
        init();

        setListeners();

        play();
    }

    private void setListeners() {
        for(int i = 0; i < tvBase.length; i++) {
            tvBase[i].setOnTouchListener(this);
        }
    }

    private void init() {
        tvLetters = new TextView[]{
                findViewById(R.id.tvLetter0), findViewById(R.id.tvLetter1),
                findViewById(R.id.tvLetter2), findViewById(R.id.tvLetter3),
                findViewById(R.id.tvLetter4)
        };

        tvBase = new TextView[]{
                findViewById(R.id.tvBase0), findViewById(R.id.tvBase1),
                findViewById(R.id.tvBase2), findViewById(R.id.tvBase3),
                findViewById(R.id.tvBase4)
        };
        nh = new NamingHelper();
    }

    private void play() {

        displayBaseAsync();

        demoAsync();

    }

    private void demoAsync() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    demo();
                } catch (Exception e) {
                }
                return null;
            }
        }.execute();
    }

    private void demo() {
        int theCharRndindex = (int)(Math.random() * tvBase.length);
        float initY = tvBase[theCharRndindex].getY();
        for(int i = 0; i < 80; i++) {
            setPositionY(tvBase[theCharRndindex], tvBase[theCharRndindex].getY() -5);
            SystemClock.sleep(10);
        }
        SystemClock.sleep(500);
        tvBase[theCharRndindex].setY(initY);
    }

    private void setPositionY(final TextView tvBase, final float pos) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    tvBase.setY(pos);
                } catch (Exception e) {
                }
            }
        });
    }



    private void displayBaseAsync() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    displayBase();
                } catch (Exception e) {

                }
                return null;
            }
        }.execute();
    }

    private void displayBase() {
        nh.generate_random_name();
        for(int i = 0; i < nh.getRnd_name().length; i++) {
            setText(tvBase[i], nh.getNameCharByIndex(i));
            SystemClock.sleep(100);
        }
    }


    private void setText(final TextView tvLetter, final String nameCharByIndex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLetter.setText(nameCharByIndex);
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        for(int i = 0; i < tvLetters.length; i++) {
            int width = tvLetters[i].getWidth();
            int height = tvLetters[i].getHeight();
            float x = tvLetters[i].getX(); 
            float y = tvLetters[i].getY();
            String infoStr = "<=>" + i + ": width: " + width + ", height: " + height + ", x: " + x + ", y: " + y;
            
            Log.d(MainActivity.DEBUGTAG, infoStr);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TextView curr = null;
        int textViewid = -1;

        for(int i = 0; i < tvBase.length; i++) {
            if(v == tvBase[i]) {
                curr = tvBase[i];
                textViewid = i;
                break;
            }
        }

        if(textViewid != -1 && curr != null) {
            Log.d(MainActivity.DEBUGTAG, "button " + textViewid + " was touched");
        } else {
            Log.d(MainActivity.DEBUGTAG, "problem with detected touch");
            return true;
        }

        if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            Log.d(MainActivity.DEBUGTAG, "DOWN: time to take initial position of cell " + textViewid);
        } else if(event.getAction() == (MotionEvent.ACTION_UP)) {
            Log.d(MainActivity.DEBUGTAG, "UP: time to evaluate the position for cell " + textViewid);
        } else if(event.getAction() == (MotionEvent.ACTION_MOVE)) {
            Log.d(MainActivity.DEBUGTAG, "MOVE: location is: " + event.getX() +"," + event.getY());

            curr.setX(event.getX());
            curr.setY(event.getY());


        } else {
            Log.d(MainActivity.DEBUGTAG, "un handled / needed event for " + textViewid);
        }


        return true;
    }
}
