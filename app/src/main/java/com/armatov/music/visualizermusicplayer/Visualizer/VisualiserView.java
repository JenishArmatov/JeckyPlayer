
package com.armatov.music.visualizermusicplayer.Visualizer;


import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.armatov.music.service.MultiPlayer;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.Draw;

import com.h6ah4i.android.media.IMediaPlayerFactory;
import com.h6ah4i.android.media.audiofx.IHQVisualizer;
import com.h6ah4i.android.media.opensl.audiofx.OpenSLHQVisualizer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


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
            draw = new Draw(context);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw.draw(canvas);
        invalidate();
    }

}


