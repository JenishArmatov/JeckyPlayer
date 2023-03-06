package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.armatov.music.visualizermusicplayer.JniBitmapHolder;

public class SpeakersRenderer {
    private float[] mPoints;
    private Bitmap b;
    public void draw( Canvas canvas, float[] data, Rect rect,Paint mPaint) {

        b = Bitmap.createBitmap(200,200, Bitmap.Config.ARGB_8888);
        final int w=canvas.getWidth(),height=canvas.getHeight();
// store the bitmap in the JNI "world"
        final JniBitmapHolder bitmapHolder = new JniBitmapHolder(b);
// no need for the bitmap on the java "world", since the operations are done on the JNI "world"

//        BarGraphRenderer.bitmap = Bitmap.createBitmap(b);
        mPaint.setStrokeWidth(2);

        mPoints = new float[data.length];
        for (int i = 0; i < data.length/4 - 1; i++) {
            float[] cartPoint = {
                    (float) i / (data.length/4-1),
                    rect.height() / 2 + ( (data[i]*50 + 128)) * (rect.height() / 2) / 128
            };

            float[] polarPoint = toPolar(cartPoint, rect);

            mPoints[i * 4] = polarPoint[0];
            mPoints[i * 4 + 1] = polarPoint[1];



            float[] cartPoint2 = {
                    (float) (i + 1) / (data.length/4-1),
                    rect.height() / 2 + ( (data[i + 1]*50 + 128)) * (rect.height() / 2) / 128
            };

            float[] polarPoint2 = toPolar(cartPoint2, rect);

            mPoints[i * 4 + 2] = polarPoint2[0];
            mPoints[i * 4 + 3] = polarPoint2[1];


        }
        // Controls the pulsing rate

        Canvas canvas1 = new Canvas(b);
        canvas1.drawLines(mPoints, mPaint);
        canvas.drawBitmap(b,0,0,mPaint);



    }


    float aggresive = 0.9f;

    private float[] toPolar(float[] cartesian, Rect rect) {
        double cX = rect.width() / 2;
        double cY = rect.height() / 2;
        double angle = (cartesian[0]) * 2 * Math.PI;
        double radius = ((rect.width() / 3) + cartesian[1])/10 ;
        float[] out = {
                (float) (cX + radius * Math.sin(angle)),
                (float) (cY + radius * Math.cos(angle))
        };
        return out;
    }

/*
     public void draw( Canvas canvas, float[] data, Rect rect,Paint mPaint) {
//        rect = new Rect(0,0,canvas.getWidth() + 10,canvas.getHeight()+10);
        int speedWidth = rect.width()/45;
        int speedHeight = rect.height()/45;
        canvas.drawColor(Color.argb(255,0,0,0));

        if(b == null){
            b = Bitmap.createBitmap(50,50, Bitmap.Config.ARGB_8888);
        }
        final int w=canvas.getWidth(),height=canvas.getHeight();
// store the bitmap in the JNI "world"
        final JniBitmapHolder bitmapHolder = new JniBitmapHolder(b);
// no need for the bitmap on the java "world", since the operations are done on the JNI "world"
        b.recycle();


// crop a center square from the bitmap, from (0.25,0.25) to (0.75,0.75) of the bitmap.
        bitmapHolder.cropBitmap(0,5,w,height);
//rotate the bitmap:
//get the output java bitmap , and free the one on the JNI "world"
        b=bitmapHolder.getBitmapAndFree();
//        BarGraphRenderer.bitmap = Bitmap.createBitmap(b);
        mPaint.setStrokeWidth(2);

        mPoints = new float[data.length];
        for (int i = 0; i < data.length/4 - 1; i++) {
            float[] cartPoint = {
                    (float) i / (data.length/4-1),
                    rect.height() / 2 + ( (data[i]*50 + 128)) * (rect.height() / 2) / 128
            };

            float[] polarPoint = toPolar(cartPoint, rect);

            mPoints[i * 4] = polarPoint[0];
            mPoints[i * 4 + 1] = polarPoint[1];



            float[] cartPoint2 = {
                    (float) (i + 1) / (data.length/4-1),
                    rect.height() / 2 + ( (data[i + 1]*50 + 128)) * (rect.height() / 2) / 128
            };

            float[] polarPoint2 = toPolar(cartPoint2, rect);

            mPoints[i * 4 + 2] = polarPoint2[0];
            mPoints[i * 4 + 3] = polarPoint2[1];


        }
        // Controls the pulsing rate

        Canvas canvas1 = new Canvas(b);
        canvas1.drawLines(mPoints, mPaint);
        canvas.drawBitmap(b,0,0,mPaint);



    }


    float aggresive = 0.9f;

    private float[] toPolar(float[] cartesian, Rect rect) {
        double cX = rect.width() / 2;
        double cY = rect.height() / 2;
        double angle = (cartesian[0]) * 2 * Math.PI;
        double radius = ((rect.width() / 3) + cartesian[1])/10 ;
        float[] out = {
                (float) (cX + radius * Math.sin(angle)),
                (float) (cY + radius * Math.cos(angle))
        };
        return out;
    }


* */

}
