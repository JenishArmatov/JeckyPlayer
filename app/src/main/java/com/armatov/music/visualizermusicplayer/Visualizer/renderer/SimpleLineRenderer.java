package com.armatov.music.visualizermusicplayer.Visualizer.renderer;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by User on 10.06.2020.
 */

public class SimpleLineRenderer {
    int red = 250;
    int green = 0;
    int blue = 0;
    private float[] newbytes = new float[1024];
    private float[][] arrayXYARGB = new float[3][100];
    private float[] x = new float[1024*4];
    public void draw(float[] mFftBytes,  Rect rect, Canvas canvas) {

        canvas.drawColor(Color.argb(255,0,0,0));
        float width = (rect.width() - 100f)/40;


        int graund = (canvas.getHeight() / 2);

        float xStart = 20f;

        Paint p = new Paint();
        p.setStrokeWidth(width);
        Paint paint = new Paint();

        int red = 250;
        int green = 0;
        int blue = 0;


        for(int i = 0; i < 40; i++) {
            newbytes[i] = graund - (mFftBytes[i] - mFftBytes[i] / 4);

            x[i] = (x[i] + x[i] + newbytes[i])/3;

            if (i > 0 && x[i] < x[i - 1]) {
                x[i-1] = x[i];
            }
            if (x[i] < x[i + 1]) {
                x[i+1] = x[i];
            }


            if (x[i] < 0 ) {
                x[i] = 0;
            }

            if (x[i] > graund) {
                x[i] = graund;
            }

            p.setColor(Color.argb(255, red, green, blue));
            p.setStrokeWidth(width);
            p.setStyle(Paint.Style.STROKE);
            arrayXYARGB[0][i] = xStart;
            arrayXYARGB[1][i] = x[i];



        }

        for(int i = 0; i < 40; i++) {

            if(i < 11 && i > 0){
                green = i * 20;
            }
            if(i > 10 && i < 21){
                red = red - 20;

            }
            if(i > 20 && i < 31){
                blue = blue + 20;
                green = green - 10;
                red = red + 2;

            }
            if(i > 30 && i < 40){
                green = green - 10;
                red = red + 7;
                blue= blue - 4;
            }
            p.setStrokeWidth(width);
            p.setColor(Color.argb(255, red, green, blue));
            canvas.drawLine(xStart, graund,
                    xStart, arrayXYARGB[1][i], p);
            paint.setColor(Color.argb(255, red, green, blue));
            canvas.drawCircle(xStart,arrayXYARGB[1][i],width/2,paint);

            canvas.drawLine(xStart, graund,
                    xStart, graund + (graund - (arrayXYARGB[1][i])), p);
            canvas.drawCircle(xStart,graund + (graund - (arrayXYARGB[1][i])),width/2,paint);

            xStart = xStart + width + 2;

        }


    }
/*
    public void draw(Canvas canvas, float[] mFftBytes,  Rect rect) {

        canvas.drawColor(Color.argb(255,0,0,0));
        float width = (rect.width() - 100f)/40;


        int graund = (canvas.getHeight() / 2);

        float xStart = 20f;

        Paint p = new Paint();
        p.setStrokeWidth(width);
        Paint paint = new Paint();

        int red = 250;
        int green = 0;
        int blue = 0;


        for(int i = 0; i < 40; i++) {
            newbytes[i] = graund - (mFftBytes[i] - mFftBytes[i] / 4);

            x[i] = (x[i] + x[i] + newbytes[i])/3;

            if (i > 0 && x[i] < x[i - 1]) {
                     x[i-1] = x[i];
            }
            if (x[i] < x[i + 1]) {
                    x[i+1] = x[i];
            }


            if (x[i] < 0 ) {
                x[i] = 0;
            }

            if (x[i] > graund) {
                x[i] = graund;
            }

            p.setColor(Color.argb(255, red, green, blue));
            p.setStrokeWidth(width);
            p.setStyle(Paint.Style.STROKE);
            arrayXYARGB[0][i] = xStart;
            arrayXYARGB[1][i] = x[i];



        }

        for(int i = 0; i < 40; i++) {

            if(i < 11 && i > 0){
                green = i * 20;
            }
            if(i > 10 && i < 21){
                red = red - 20;

            }
            if(i > 20 && i < 31){
                blue = blue + 20;
                green = green - 10;
                red = red + 2;

            }
            if(i > 30 && i < 40){
                green = green - 10;
                red = red + 7;
                blue= blue - 4;
            }
            p.setStrokeWidth(width);
            p.setColor(Color.argb(255, red, green, blue));
            canvas.drawLine(xStart, graund,
                    xStart, arrayXYARGB[1][i], p);
            paint.setColor(Color.argb(255, red, green, blue));
            canvas.drawCircle(xStart,arrayXYARGB[1][i],width/2,paint);

            canvas.drawLine(xStart, graund,
                    xStart, graund + (graund - (arrayXYARGB[1][i])), p);
            canvas.drawCircle(xStart,graund + (graund - (arrayXYARGB[1][i])),width/2,paint);

            xStart = xStart + width + 2;

        }


    }
 */
}
