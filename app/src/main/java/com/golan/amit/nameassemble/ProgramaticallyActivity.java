package com.golan.amit.nameassemble;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProgramaticallyActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    TextView[] up, down;
    ImageButton ibRollback;
    int heightAbs = -1;
    int widthAbs = -1;

    float initialX = -1;
    float initialY = -1;

    MediaPlayer mp;
    SeekBar sb;
    AudioManager am;

    NamingHelper nh;

    Rect[] upPositionRectArray;
    private SharedPreferences sp;

    public enum DemoState {
        YES, NO
    }
    private DemoState demoState = DemoState.YES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programatically);

        init();

        setListeners();

        PlayNameAsync();

    }

    private void PlayNameAsync() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    PlayName();
                } catch (Exception e) {
                }
                return null;
            }
        }.execute();
    }

    private void PlayName() {
        nh.generate_random_name();
        for (int i = 0; i < nh.getRnd_name().length; i++) {
            setText(down[i], nh.getNameCharByIndex(i));
            SystemClock.sleep(150);
        }

    }

    private void setText(final TextView textView, final String nameCharByIndex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(nameCharByIndex);
            }
        });
    }

    private void setListeners() {
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "in setlisteners");
        }

        for(int i = 0; i < down.length; i++) {
            down[i].setOnTouchListener(this);
        }
        ibRollback.setOnClickListener(this);
    }

    private void init() {
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "in init");
        }
        up = new TextView[] {
                findViewById(R.id.tvUp0), findViewById(R.id.tvUp1), findViewById(R.id.tvUp2),
                findViewById(R.id.tvUp3), findViewById(R.id.tvUp4)
        };
        down = new TextView[] {
                findViewById(R.id.tvDown0), findViewById(R.id.tvDown1), findViewById(R.id.tvDown2),
                findViewById(R.id.tvDown3), findViewById(R.id.tvDown4)
        };

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightAbs = displayMetrics.heightPixels;
        widthAbs = displayMetrics.widthPixels;

        nh = new NamingHelper();

        sb = findViewById(R.id.sb);
        mp = MediaPlayer.create(this, R.raw.timetunnel);
        mp.start();

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        sb.setMax(max / 4);
        sb.setMax(max);
        sb.setProgress(max / 4);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, max / 4, 0);
        sb.setOnSeekBarChangeListener(this);

        upPositionRectArray = new Rect[down.length];
        ibRollback = findViewById(R.id.ibRollbackId);

        sp = getSharedPreferences("nameassemble", MODE_PRIVATE);

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "in onWindowFocusChanged");
        }

        ibRollback.setY(100);
        ibRollback.setX(20);

        sb.setY(350);
        sb.setX(105);

        int startXpos = 150;
        for (int i = 0; i < up.length; i++) {
            up[i].setY(25);
            up[i].setX(startXpos);

            down[i].setY(heightAbs - 800);
            down[i].setX(startXpos);

            int width = up[i].getWidth();
            int height = up[i].getHeight();
            float x = up[i].getX();
            float y = up[i].getY();
            Rect r = new Rect((int) x,
                    (int) y,
                    (int) (x + width),
                    (int) (y + height));
            upPositionRectArray[i] = new Rect(r);
            startXpos += up[i].getWidth() + 10;
        }

        if(demoState.equals(DemoState.YES))
            displayDemoAsync();

//        displayInitPositions();
    }

    private void displayDemoAsync() {
        demoState = DemoState.NO;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    displayDemo();
                } catch (Exception e) {
                    Log.e(MainActivity.DEBUGTAG, "exception while calling display demo: " + e);
                }
                return null;
            }
        }.execute();
    }

    private void displayDemo() {
        for(int u = 0; u < 7; u++) {
            int cell = (int)(Math.random() * down.length);
            float initY = down[cell].getY();
            float endY = 450;
            for (int y = (int) initY; y > (int)endY; y--) {
//            setYposition(down[cell], (float) y);
                down[cell].setY(y);
            }
            SystemClock.sleep(250);
            for (int y = (int)endY; y < (int) initY; y++) {
//            setYposition(down[cell], (float) y);
                down[cell].setY(y);
            }
            SystemClock.sleep(250);
        }
    }

    private void setYposition(final TextView tv, final float yPos ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setY(yPos);
                SystemClock.sleep(20);
            }
        });
    }




    private void displayInitPositions() {
        if (MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "in displayInitPositions");
        }
        for (int i = 0; i < upPositionRectArray.length; i++) {
            Rect r = new Rect(upPositionRectArray[i]);
            if (MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "index: " + i + ")., left: " + r.left +
                        ", right: " + r.right +
                        ", top: " + r.top + ", bottom: " + r.bottom);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int currId = -1;
        TextView currTv = null;
        for(int i = 0; i < down.length; i++) {
            if(v == down[i]) {
                currId = i;
                currTv = down[i];
            }
        }
        if(currId == -1 || currTv == null) {
            if (MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "wrong touch");
            }
            return true;
        }
        if(currTv.getText().toString().equalsIgnoreCase("")) {
            Log.d(MainActivity.DEBUGTAG, "empty source cell");
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float evX = event.getRawX();
                float evY = event.getRawY();
                Rect r = new Rect((int)evX, (int)currTv.getY(), (int)(evX + currTv.getWidth()), (int)(currTv.getY()) + currTv.getHeight());
                int cellId = -1;
                cellId = matchAndReturnCellId(r);
                if(initialX != -1) {
                    currTv.setX(initialX);
                    initialX = -1;
                }
                if(initialY != -1) {
                    currTv.setY(initialY);
                    initialY = -1;
                }
                if(cellId == -1) {
                    if(MainActivity.DEBUG) {
                        Log.d(MainActivity.DEBUGTAG, "NO CELL DETECTED");
                    }
                    return true;
                }

                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "We detected cell: " + cellId);
                }
                String tmpCellVal = null;
                if(up[cellId].getText() == null) {
                    if(MainActivity.DEBUG) {
                        Log.d(MainActivity.DEBUGTAG, "get text is null ");
                    }
                    return true;
                }
                try {
                    tmpCellVal = up[cellId].getText().toString();
                } catch (Exception e) {
                    return true;
                }
                if(!tmpCellVal.equalsIgnoreCase("")) {
//                    Log.d(MainActivity.DEBUGTAG, "get text: " + tmpCellVal);
                    return true;
                }
                String tmpTextChar = null;
                try {
                    tmpTextChar = currTv.getText().toString();
                } catch (Exception e) {
                    return true;
                }
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "get text of up target cell is enpty");
                }

                //  Let the fun begin
                ibRollback.setVisibility(View.VISIBLE);
                up[cellId].setText(tmpTextChar);
                currTv.setText("");

                nh.increaseName_counter();
                if(nh.getName_counter() == nh.getCurr_name().length()) {    //  finished
                    if(MainActivity.DEBUG) {
                        Log.d(MainActivity.DEBUGTAG, "finished ");
                    }
                    ibRollback.setVisibility(View.INVISIBLE);
                    boolean won = won_evaluated();
                    String endgame_state = won ? "WON" : "LOST";
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("name", nh.getCurr_name());
                    editor.putBoolean("state", won);
                    editor.commit();
                    Intent i = new Intent(this, EndActivity.class);
                    startActivity(i);
                    return true;
                }
                nh.push_stack(currId);
                nh.push_stack_u(cellId);
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "stack size of source (down) cells is now: " + nh.getSti().size());
                    Log.d(MainActivity.DEBUGTAG, "stack size of target (up) cells is now: " + nh.getStu().size());
                }

                break;
            case MotionEvent.ACTION_DOWN:
                initialX = currTv.getX();
                initialY = currTv.getY();
//                Log.d(MainActivity.DEBUGTAG, "down was touched. setting initials to: x=" + initialX + ", y=" + initialY);
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d(MainActivity.DEBUGTAG, "move was touched");
                handleMove(currTv, event);

                break;
            case MotionEvent.ACTION_MASK:
//                Log.d(MainActivity.DEBUGTAG, "mask was touched");
                break;
        }

        return true;
    }

    private boolean won_evaluated() {
        StringBuilder sb = new StringBuilder();
        for(int i = (up.length - 1); i >= 0; i-- ) {
            sb.append(up[i].getText().toString());
        }
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "comparing {" + sb.toString() + "} to {" + nh.getCurr_name() + "}");
        }
        return sb.toString().equalsIgnoreCase(nh.getCurr_name());
    }

    private int matchAndReturnCellId(Rect incoming) {
        int cellId = -1;

        for(int i = 0; i < upPositionRectArray.length; i++) {
            Rect r = new Rect(upPositionRectArray[i]);
//            Log.d(MainActivity.DEBUGTAG, "cell " + i + "). r-left: " + r.left + ", r-right: " + r.right +
//                    ", r-top: " + r.top + ", r-bottom: " + r.bottom);

            if(r.contains(incoming.left, incoming.centerY())) {
                cellId = i;
                break;
            }
        }
        return cellId;
    }

    private void handleMove(TextView currTv, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float evX = event.getRawX();
            float evY = event.getRawY();
//            Log.d(MainActivity.DEBUGTAG, "while moving: evX: " + evX + ", evY: " + evY +
//                    ", absolute width: " + widthAbs + ", absolute height: " + heightAbs);
            if (evX > 10 && evX < (widthAbs - 250)) {
                currTv.setX(evX);
            }
            if (evY > 250 && evY < heightAbs) {
                currTv.setY(evY - currTv.getHeight()*2);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v == ibRollback) {
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "rollback delete last button was clicked");
            }
            nh.decreaseName_counter();
            if(nh.getName_counter() == 0)
                ibRollback.setVisibility(View.INVISIBLE);
            int tmpCurrPosition = -1;
            int tmpCurrUpPosition = -1;
            if(nh.getSti().size() > 0) {
                tmpCurrPosition = nh.pop_stack();
            }
            if(nh.getStu().size() > 0) {
                tmpCurrUpPosition = nh.pop_stack_u();
            }
            String tmpChar = null;
            try {
                tmpChar = up[tmpCurrUpPosition].getText().toString();
                up[tmpCurrUpPosition].setText("");
                down[tmpCurrPosition].setText(tmpChar);
            } catch (Exception e) {
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mp != null) {
            try {
                mp.start();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
