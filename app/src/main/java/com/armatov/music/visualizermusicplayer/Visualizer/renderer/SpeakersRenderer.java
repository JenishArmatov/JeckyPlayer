package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.armatov.music.visualizermusicplayer.JniBitmapHolder;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.interfaces.Renderer;

public class SpeakersRenderer implements Renderer {
    private int red = 250;
    private int green = 0;
    private int blue = 0;
    private Bitmap b;
    private Paint p;
    private final float[] x = new float[1024*4];
    private Matrix matrix;



    private void calcolateColor(int i){
        if(i < 26 && i > 0){
            green = i * 10;
        }
        if(i > 25 && i < 51){
            red = red - 10;

        }
        if(i > 50 && i < 76){
            blue = blue + 10;
            green = green - 5;
            red = red + 1;

        }
        if(i > 75 && i < 99){
            green = green - 5;
            red = red + 3;
            blue= blue - 2;
        }
    }

    @Override
    public void draw(Canvas canvas, float[] mFftBytes, float[] data){
        Rect rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
        int column = 100;
        if (matrix == null){
            b = Bitmap.createBitmap(canvas.getWidth(),canvas.getHeight(), Bitmap.Config.ARGB_8888);
            matrix = new Matrix();
            p = new Paint();
            rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());

            //p.setShadowLayer(30, 0, 0, Color.RED);
            //  p.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL));

        }
        int graund = (int) (canvas.getHeight() / 2);
        canvas.drawColor(Color.argb(255,0,0,0));
        final int w=canvas.getWidth(),height=canvas.getHeight();
        final JniBitmapHolder bitmapHolder = new JniBitmapHolder(b);
        b.recycle();

        bitmapHolder.cropBitmap(0, 6,w/2,height);

        b=bitmapHolder.getBitmapAndFree();

        float[] newbytes = new float[column];
        float width = (canvas.getWidth())/(column);
        float xStart = rect.width()/2;
        p.setStrokeWidth(4);



        Canvas canvas1 = new Canvas(b);
        matrix.reset();
        matrix.setRotate(180,rect.width()/2, rect.height()/2);
        // matrix.postTranslate(0,rect.height()/2);
        canvas1.setMatrix(matrix);

        for(int i = 0; i < column; i++) {

            newbytes[i] = graund -  mFftBytes[i] ;

            if (newbytes[i] > graund) {
                newbytes[i] = graund;
            }
            x[i] = (x[i] + x[i] + newbytes[i])/3;

            if (i > 0 && x[i] < x[i - 1]) {
                x[i - 1] = x[i];
            }
            if (x[i] < x[i + 1]) {
                x[i + 1] = x[i];
            }
            if (x[i] < 0 ) {
                x[i] = 0;
            }
            if (i > 2 && i < 98) {
                x[i - 1] = ((x[i] + x[i-3])/2 + (x[i]))/2;
                x[i - 2] = ((x[i] + x[i-3])/2 + (x[i - 3]))/2;
            }
            if (i < column - 3) {
                x[i + 1] = ((x[i] + x[i+3])/2 + (x[i]))/2;
                x[i + 2] = ((x[i] + x[i+3])/2 + (x[i + 3]))/2;
            }
            if (x[i] > graund) {
                x[i] = graund;
            }
        }
        canvas1.drawColor(Color.argb(25,0,0,0));

        for(int i = 0; i < column; i++) {
            calcolateColor(i);

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.argb(255, red, green, blue));

            if(i < column - 1){
                canvas1.drawLine(xStart,x[i],
                        xStart + width, x[i + 1],p);
                //      canvas1.drawLine(xStart,graund,
                //               xStart + width, graund,p);
            }



            width = width - width/(60);
            xStart = xStart + width;
        }
        red = 250;
        green = 0;
        blue = 0;
        matrix.reset();
        matrix.setRotate(180,rect.width()/2, rect.height()/2);
        //matrix.postTranslate(rect.width(),rect.height()/2);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(b,0,0,p);

        matrix.reset();
        matrix.setScale(1.0f, -1.0f);
        matrix.postTranslate(0,rect.height());
        canvas.setMatrix(matrix);
        canvas.drawBitmap(b,0,0,p);


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
