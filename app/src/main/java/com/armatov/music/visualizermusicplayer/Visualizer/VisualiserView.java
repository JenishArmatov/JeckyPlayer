
package com.armatov.music.visualizermusicplayer.Visualizer;


import android.content.Context;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;



import com.armatov.music.visualizermusicplayer.Visualizer.renderer.Draw;

import com.h6ah4i.android.media.IMediaPlayerFactory;
import com.h6ah4i.android.media.audiofx.IHQVisualizer;
import com.h6ah4i.android.media.opensl.audiofx.OpenSLHQVisualizer;


public class VisualiserView extends View {
    private static final String TAG = "VisualiserView";
    private Context context;
    public static float[] lastFFt = new float[1024*4];
    private OpenSLHQVisualizer v;
    private Visualizer visualiserView;
    public static float[] data = new float[1024*4];
    public Draw draw;


    public VisualiserView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
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
        draw = new Draw(context);



    }

    public void link(MediaPlayer factory)
    {
        visualiserView = new Visualizer(factory.getAudioSessionId());
        Visualizer.OnDataCaptureListener listener = new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                for(int i = 1; i < waveform.length; i++) {
                }
            }
            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                lastFFt = new float[1024*4];
                for(int i = 21; i < fft.length/2; i++) {
                    float w = ((fft[(i - 20)*2]*fft[(i - 20)*2]));
                    float q = ((fft[(i - 20)*2+1]*fft[(i - 20)*2+1]));
                    float res = (w+q)/2;
                    res = res > 1 ? (int) ( 30* Math.log10(res*res)) : 1;
                    lastFFt[i] = res;
                }
            }
        };

        visualiserView.setDataCaptureListener(listener,
                Visualizer.getMaxCaptureRate(), true, true);
        visualiserView.setEnabled(true);

    }
    public void link(IMediaPlayerFactory factory)
    {
        v = (OpenSLHQVisualizer) factory.createHQVisualizer();;
        v.setCaptureSize(1024*4);






        IHQVisualizer.OnDataCaptureListener listener = new IHQVisualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(IHQVisualizer visualizer, float[] waveform, int numChannels, int samplingRate) {
                data = waveform;

            }

            @Override
            public void onFftDataCapture(IHQVisualizer visualizer, float[] fft, int numChannels, int samplingRate) {
                lastFFt = fft;

            }

        };
        v.setDataCaptureListener(listener,
                Visualizer.getMaxCaptureRate(), true, true);
        v.setEnabled(true);

    }
    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        draw.draw(canvas);
        invalidate();
    }

    public void release()
    {

        if (v != null){
            v.release();
        }
        if (visualiserView != null){
            visualiserView.release();
        }

    }

/*
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("///Visualizer", "surfaceCreated");

     //   barGraphRenderer = new BarGraphRenderer(getHolder(), context);
     //   barGraphRenderer.setRunning(true);
    //    barGraphRenderer.start();



    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("///Visualizer", "surfaceChanged");

     //   BarGraphRenderer.drawing = false;
      //  holder.setFormat(format);
     //   holder.setFixedSize(width,height);
     //   BarGraphRenderer.drawing = true;
        //когда view меняет свой размер
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //когда view исчезает из поля зрения
        BarGraphRenderer.drawing = false;
        Classic.stars = null;
        CircleWater.stars = null;
        barGraphRenderer.setRunning(false); //останавливает процесс
        Log.d("///Visualizer", "surfaceDestroyed");
        boolean retry = true;
        while(retry) {
            try {
                barGraphRenderer.join();
                retry = false;
            }
            catch (InterruptedException e) {
                //не более чем формальность
            }
        }
        BarGraphRenderer.drawing = true;
    }
*/
}


