package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class ClassicRenderer {
    private int red = 250;
    private int green = 0;
    private int blue = 0;
    int column = 100;
    private  float[] x = new float[1024*4];

    public void draw(Canvas canvas, float[] mFftBytes, Rect rect) {

        canvas.drawColor(Color.argb(255,0,0,0));
        Paint p = new Paint();
        p.setStrokeWidth(3);

        int graund = (canvas.getHeight() / 2);

        float[] newbytes = new float[column];
        float[][] arrayXYARGB = new float[3][column];
        float width = (canvas.getWidth())/(column - column/3);
        float xStart = 0;
        int k = 1;
        int lastIndex = 0;
        int index = 1;
        for(int i = 0; i < column; i++) {
            float highSample = 0;
            for(int j = lastIndex; j <= index; j++){
                if(mFftBytes[j] > highSample){
                    highSample = mFftBytes[j];
                }
            }
            lastIndex = index;
            if(i > 85){
                index = index + k + i;
            }else{
                index = index + k + i/30;

            }
            newbytes[i] = graund - (highSample - highSample/3) ;

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

            if (x[i] > graund) {
                x[i] = graund;
            }
            arrayXYARGB[0][i] = xStart;
            arrayXYARGB[1][i] = x[i];
        }
        for(int i = 0; i < column; i++) {
            calcolateColor(i);

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.argb(255, red, green, blue));
            p.setAlpha(255);

            if(i < column - 1){
                canvas.drawLine(xStart + canvas.getWidth()/2,arrayXYARGB[1][i],
                        xStart + canvas.getWidth()/2 + width, arrayXYARGB[1][i + 1],p);
                canvas.drawLine(xStart + canvas.getWidth()/2,graund,
                        xStart + canvas.getWidth()/2 + width, arrayXYARGB[1][i + 1],p);
            }

            if(i == column - 1){
                canvas.drawLine(xStart + canvas.getWidth()/2,arrayXYARGB[1][i],
                        xStart + canvas.getWidth()/2 + width, graund,p);
            }
            if(i < column - 1){
                canvas.drawLine(canvas.getWidth()/2 - xStart,arrayXYARGB[1][i],
                        canvas.getWidth()/2 - xStart - width, arrayXYARGB[1][i + 1],p);
                canvas.drawLine(canvas.getWidth()/2 - xStart,graund,
                        canvas.getWidth()/2 - xStart - width, arrayXYARGB[1][i + 1],p);
            }

            if(i == column - 1){
                canvas.drawLine(canvas.getWidth()/2 - xStart,arrayXYARGB[1][i],
                        canvas.getWidth()/2 - xStart - width, graund,p);
            }
            /////////////////////////////////////


            if(i < column - 1){
                canvas.drawLine(xStart + canvas.getWidth()/2, graund + (graund - arrayXYARGB[1][i]),
                        xStart + canvas.getWidth()/2 + width, graund + (graund - arrayXYARGB[1][i + 1]),p);
                canvas.drawLine(xStart + canvas.getWidth()/2, graund,
                        xStart + canvas.getWidth()/2 + width, graund + (graund - arrayXYARGB[1][i + 1]),p);
            }
            if(i == column - 1){
                canvas.drawLine(xStart + canvas.getWidth()/2,graund + (graund - arrayXYARGB[1][i]),
                        xStart + canvas.getWidth()/2 + width, graund,p);
            }
            if(i < column - 1){
                canvas.drawLine(canvas.getWidth()/2 - xStart,graund + (graund - arrayXYARGB[1][i]),
                        canvas.getWidth()/2 - xStart - width, graund + (graund - arrayXYARGB[1][i + 1]),p);
                canvas.drawLine(canvas.getWidth()/2 - xStart, (graund),
                        canvas.getWidth()/2 - xStart - width, graund + (graund - arrayXYARGB[1][i + 1]),p);
            }
            if(i == column - 1){
                canvas.drawLine(canvas.getWidth()/2 - xStart,graund + (graund - arrayXYARGB[1][i]),
                        canvas.getWidth()/2 - xStart - width, graund,p);
            }

            width = width - width/(35);
            if (width < 2) {
                width = 2;
            }
            xStart = xStart + width;
        }
        red = 250;
        green = 0;
        blue = 0;

    }
    private  void calcolateColor(int i){
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
}
