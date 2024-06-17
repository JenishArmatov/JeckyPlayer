package com.armatov.music.visualizermusicplayer.Visualizer.renderer;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.armatov.music.visualizermusicplayer.JniBitmapHolder;

public class Subopod {
    private int red = 250;
    private int green = 0;
    private int blue = 0;
    private Bitmap b;
    private Matrix matrix;
    private Paint p;
    private  float[] x = new float[1024*4];
    private float angle;
    private boolean right = true;

    public void draw(Canvas canvas, float[] mFftBytes,  Rect rect, int column) {
        rect = new Rect(0,0,canvas.getWidth()/2,canvas.getHeight()/2);
        int graund = (int) (rect.height() / 2);

        canvas.drawColor(Color.argb(255,0,0,0));
        if (b == null){
            b = Bitmap.createBitmap(rect.width(),rect.height(), Bitmap.Config.ARGB_4444);

            matrix = new Matrix();
            p = new Paint();
            //p.setShadowLayer(30, 0, 0, Color.RED);
            //  p.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL));

        }

        final Bitmap output = Bitmap.createBitmap(rect.width(),rect.height(), Bitmap.Config.ARGB_4444);

        final int w = rect.width(), height = rect.height();
        final JniBitmapHolder bitmapHolder = new JniBitmapHolder();

        bitmapHolder.cropBitmap(b,output);


        float[] newbytes = new float[column];
        float[][] arrayXYARGB = new float[3][column];
        float width = (rect.width())/(column);
        float xStart = rect.width()/2;
        p.setStrokeWidth(2);



        Canvas canvas1 = new Canvas(output);
        matrix.reset();
        matrix.setRotate(180);
        matrix.postTranslate(rect.width(),rect.height());
        if(right){
            if(angle > 358){
                right = false;
            }
            angle = angle + 0.1f;
            angle = (angle + mFftBytes[3]/15);

        }
        if(!right){
            if(angle < 0){
                right = true;
            }
            angle = angle - 0.1f;
            angle = (angle - mFftBytes[3]/15);

        }
        matrix.preRotate(angle,rect.width()/2, height/2);
     //   matrix.preScale(angle/360,angle/360, w-angle/360, height-angle/360);

        canvas1.setMatrix(matrix);

        for(int i = 0; i < column; i++) {

            newbytes[i] = graund -  mFftBytes[i] ;

            if (newbytes[i] > graund) {
                newbytes[i] = graund;
            }
            x[i] = (x[i] + x[i] + x[i] + newbytes[i])/4;

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
            arrayXYARGB[0][i] = xStart;
        }
        canvas1.drawColor(Color.argb(20,0,0,0));

        for(int i = 0; i < column; i++) {
            calcolateColor(i);

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.argb(255, red, green, blue));
            p.setAlpha(255);

            if(i < column - 1){
                canvas1.drawLine(xStart,x[i],
                        xStart + width, x[i + 1],p);

            }


            if(i < column - 1){
                canvas1.drawLine(rect.width()/2 - (xStart - rect.width()/2),x[i],
                        rect.width()/2 - (xStart - rect.width()/2) - width, x[i + 1],p);

            }

            if(i < column - 1){
                canvas1.drawLine(xStart,graund*2 - x[i],
                        xStart + width, graund*2 - x[i + 1],p);

            }


            if(i < column - 1){
                canvas1.drawLine(rect.width()/2 - (xStart - rect.width()/2),graund*2 - x[i],
                        rect.width()/2 - (xStart - rect.width()/2) - width, graund*2 - x[i + 1],p);

            }

            width = width - width/(70);
            xStart = xStart + width;
        }
        red = 250;
        green = 0;
        blue = 0;
        matrix.reset();
        matrix.setRotate(180);
        matrix.postTranslate(rect.width(),rect.height());
        //matrix.setScale(angle/300,angle/300, w, height);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(output, rect, new Rect(0-rect.width(),0-rect.height(), rect.width(),rect.height()), p);
        b = Bitmap.createBitmap(output, 0,0,rect.width(),rect.height());

    }
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
}