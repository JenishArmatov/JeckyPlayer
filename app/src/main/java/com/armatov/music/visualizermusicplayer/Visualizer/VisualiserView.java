
package com.armatov.music.visualizermusicplayer.Visualizer;


import android.content.Context;

import android.graphics.Canvas;

import android.util.AttributeSet;
import android.view.View;


public class VisualiserView extends View {
    private static final String TAG = "VisualiserView";
    private Context context;
    private Draw draw;


    public VisualiserView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs);
        this.context = context;

        init();


    }




    public VisualiserView(Context context, AttributeSet attrs)
    {

        this(context, attrs, 0);
        this.context = context;
        init();


    }

    public VisualiserView(Context context)
    {
        this(context, null, 0);
        this.context = context;
        init();

    }

    public void init() {
        if(draw == null){
            draw = new Draw();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw.draw(canvas);
        invalidate();
    }

}


