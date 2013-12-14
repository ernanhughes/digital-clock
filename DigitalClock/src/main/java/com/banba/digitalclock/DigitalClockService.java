package com.banba.digitalclock;

import java.util.Random;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.graphics.Point;
import android.service.dreams.DreamService;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.service.dreams.DreamService;

/**
 * Created by Ernan on 13/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class DigitalClockService extends DreamService implements OnClickListener {

    private Button dismissBtn;
    private ImageView[] robotImgs;
    private AnimatorSet[] robotSets;
    private final int ROWS_COLS=5;
    private final int NUM_ROBOTS=ROWS_COLS*ROWS_COLS;
    private int randPosn;

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        for(int r=0; r<NUM_ROBOTS; r++){
            if(r!=randPosn)
                robotSets[r].start();
        }
    }

    @Override
    public void onDreamingStopped(){
        for(int r=0; r<NUM_ROBOTS; r++){
            if(r!=randPosn)
                robotSets[r].cancel();
        }
        super.onDreamingStopped();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        Random rand = new Random();
        randPosn = rand.nextInt(NUM_ROBOTS);
        GridLayout ddLayout = new GridLayout(this);
        ddLayout.setColumnCount(ROWS_COLS);
        ddLayout.setRowCount(ROWS_COLS);
        robotSets = new AnimatorSet[NUM_ROBOTS];
        robotImgs = new ImageView[NUM_ROBOTS];
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        int robotWidth = screenSize.x/ROWS_COLS;
        int robotHeight = screenSize.y/ROWS_COLS;

        for (int r=0; r<NUM_ROBOTS; r++){
            GridLayout.LayoutParams ddP = new GridLayout.LayoutParams();
            ddP.width=robotWidth;
            ddP.height=robotHeight;
            if (r==randPosn) {
                dismissBtn = new Button(this);
                dismissBtn.setText("stop");
                dismissBtn.setBackgroundColor(Color.WHITE);
                dismissBtn.setTextColor(Color.RED);
                dismissBtn.setOnClickListener(this);
                dismissBtn.setLayoutParams(ddP);
                ddLayout.addView(dismissBtn);
            } else{
                robotImgs[r] = new ImageView(this);
                robotImgs[r].setImageResource(R.drawable.ic_launcher);
                ddLayout.addView(robotImgs[r], ddP);
                robotSets[r] = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.anim.spin);

            }
        }
        setContentView(ddLayout);
    }

    @Override
    public void onDetachedFromWindow() {
        for(int r=0; r<NUM_ROBOTS; r++){
            if(r!=randPosn)
                robotImgs[r].setOnClickListener(null);
        }
        super.onDetachedFromWindow();
    }

    public void onClick(View v){
        if(v instanceof Button && (Button)v==dismissBtn){
            this.finish();
        } else {
            for(int r=0; r<NUM_ROBOTS; r++){
                if((ImageView)v==robotImgs[r]){
                    if(robotSets[r].isStarted()) robotSets[r].cancel();
                    else robotSets[r].start();
                    break;
                }
            }
        }
    }

}
