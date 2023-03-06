package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Cubics {
    private  float[] x = new float[1024*4];

    public  void draw(Canvas canvas, float[] mFftBytes,
                      Rect rect) {

        canvas.drawColor(Color.argb(255,0,0,0));
        float width = (rect.width())/11 - 8;
        float height = (rect.height())/22 - 16;


        int graund = (int) (canvas.getHeight()/2) + (canvas.getHeight()/6);

        float xStart = width*2;

        Paint p = new Paint();
        p.setStrokeWidth(width);

        for(int ww = 0; ww < 9; ww++){
            for(int w = 1; w < 10; w++){
                p.setColor(Color.GREEN);
                if (w > 5) {
                    p.setColor(Color.YELLOW);
                }
                if (w > 7) {
                    p.setColor(Color.RED);
                }
                p.setAlpha(100);

                canvas.drawLine(xStart, graund - height*w - 2, xStart , graund - height*(w+1),p);
                p.setAlpha(50);

                canvas.drawLine(xStart, graund + (height*w + 2 - height - 1)/2, xStart , graund + (height*(w+1)  - height - 1)/2,p);


            }
            xStart = xStart + width + 5;


        }



////////////////////////
        xStart = width*2;

        float oldD = 0;
        for(int ww = 0; ww < 10; ww++){
            float nd = (mFftBytes[ww])/40;
            if(nd > oldD){
                oldD = nd;
            }

        }
        if (oldD <= 2){oldD = 2;}
        if (oldD > 9){oldD = 9;}

        for(int w = 1; w < oldD; w++){
            p.setColor(Color.GREEN);
            if (w > 5) {
                p.setColor(Color.YELLOW);
            }
            if (w > 7) {
                p.setColor(Color.RED);
            }
            canvas.drawLine(xStart, graund - height*w - 2, xStart , graund - height*(w+1),p);

            p.setAlpha(120);
            canvas.drawLine(xStart, graund + (height*w + 2 - height - 1)/2, xStart , graund + (height*(w+1)  - height - 1)/2,p);
            p.setAlpha(255);


        }

/////////////////////
        xStart = xStart + width + 5;
        oldD = 0;
        for(int ww = 11; ww < 30; ww++){
            float nd = (mFftBytes[ww])/40;
            if(nd > oldD){
                oldD = nd;
            }

        }
        if (oldD <= 2){oldD = 2;}
        if (oldD > 10){oldD = 10;}

        for(int w = 1; w < oldD; w++){
            p.setColor(Color.GREEN);
            if (w > 5) {
                p.setColor(Color.YELLOW);
            }
            if (w > 7) {
                p.setColor(Color.RED);
            }
            canvas.drawLine(xStart, graund - height*w - 2, xStart , graund - height*(w+1),p);
            p.setAlpha(120);
            canvas.drawLine(xStart, graund + (height*w + 2 - height - 1)/2, xStart , graund + (height*(w+1)  - height - 1)/2,p);
            p.setAlpha(255);
        }

        /////////////////////
        xStart = xStart + width + 5;
        oldD = 0;
        for(int ww = 31; ww < 50; ww++){
            float nd = (mFftBytes[ww])/30;
            if(nd > oldD){
                oldD = nd;
            }

        }
        if (oldD <= 2){oldD = 2;}
        if (oldD > 10){oldD = 10;}

        for(int w = 1; w < oldD; w++){
            p.setColor(Color.GREEN);
            if (w > 5) {
                p.setColor(Color.YELLOW);
            }
            if (w > 7) {
                p.setColor(Color.RED);
            }
            canvas.drawLine(xStart, graund - height*w - 2, xStart , graund - height*(w+1),p);
            p.setAlpha(120);
            canvas.drawLine(xStart, graund + (height*w + 2 - height - 1)/2, xStart , graund + (height*(w+1)  - height - 1)/2,p);
            p.setAlpha(255);
        }
        /////////////////////
        xStart = xStart + width + 5;
        oldD = 0;
        for(int ww = 52; ww < 70; ww++){
            float nd = (mFftBytes[ww])/30;
            if(nd > oldD){
                oldD = nd;
            }

        }
        if (oldD <= 2){oldD = 2;}
        if (oldD > 10){oldD = 10;}

        for(int w = 1; w < oldD; w++){
            p.setColor(Color.GREEN);
            if (w > 5) {
                p.setColor(Color.YELLOW);
            }
            if (w > 7) {
                p.setColor(Color.RED);
            }
            canvas.drawLine(xStart, graund - height*w - 2, xStart , graund - height*(w+1),p);
            p.setAlpha(120);
            canvas.drawLine(xStart, graund + (height*w + 2 - height - 1)/2, xStart , graund + (height*(w+1)  - height - 1)/2,p);
            p.setAlpha(255);
        }
        /////////////////////
        xStart = xStart + width + 5;
        oldD = 0;
        for(int ww = 75; ww < 100; ww++){
            float nd = (mFftBytes[ww])/20;
            if(nd > oldD){
                oldD = nd;
            }

        }
        if (oldD <= 2){oldD = 2;}
        if (oldD > 10){oldD = 10;}

        for(int w = 1; w < oldD; w++){
            p.setColor(Color.GREEN);
            if (w > 5) {
                p.setColor(Color.YELLOW);
            }
            if (w > 7) {
                p.setColor(Color.RED);
            }
            canvas.drawLine(xStart, graund - height*w - 2, xStart , graund - height*(w+1),p);
            p.setAlpha(120);
            canvas.drawLine(xStart, graund + (height*w + 2 - height - 1)/2, xStart , graund + (height*(w+1)  - height - 1)/2,p);
            p.setAlpha(255);
        }

        /////////////////////
        xStart = xStart + width + 5;
        oldD = 0;
        for(int ww = 102; ww < 140; ww++){
            float nd = (mFftBytes[ww])/20;
            if(nd > oldD){
                oldD = nd;
            }

        }
        if (oldD <= 2){oldD = 2;}
        if (oldD > 10){oldD = 10;}

        for(int w = 1; w < oldD; w++){
            p.setColor(Color.GREEN);
            if (w > 5) {
                p.setColor(Color.YELLOW);
            }
            if (w > 7) {
                p.setColor(Color.RED);
            }
            canvas.drawLine(xStart, graund - height*w - 2, xStart , graund - height*(w+1),p);
            p.setAlpha(120);
            canvas.drawLine(xStart, graund + (height*w + 2 - height - 1)/2, xStart , graund + (height*(w+1)  - height - 1)/2,p);
            p.setAlpha(255);
        }
        /////////////////////
        xStart = xStart + width + 5;
        oldD = 0;
        for(int ww = 142; ww < 300; ww++){
            float nd = (mFftBytes[ww])/20;
            if(nd > oldD){
                oldD = nd;
            }

        }
        if (oldD <= 2){oldD = 2;}
        if (oldD > 10){oldD = 10;}

        for(int w = 1; w < oldD; w++){
            p.setColor(Color.GREEN);
            if (w > 5) {
                p.setColor(Color.YELLOW);
            }
            if (w > 7) {
                p.setColor(Color.RED);
            }
            canvas.drawLine(xStart, graund - height*w - 2, xStart , graund - height*(w+1),p);
            p.setAlpha(120);
            canvas.drawLine(xStart, graund + (height*w + 2 - height - 1)/2, xStart , graund + (height*(w+1)  - height - 1)/2,p);
            p.setAlpha(255);
        }
        /////////////////////
        xStart = xStart + width + 5;
        oldD = 0;
        for(int ww = 300; ww < 500; ww++){
            float nd = (mFftBytes[ww])/20;
            if(nd > oldD){
                oldD = nd;
            }

        }
        if (oldD <= 2){oldD = 2;}
        if (oldD > 10){oldD = 10;}

        for(int w = 1; w < oldD; w++){
            p.setColor(Color.GREEN);
            if (w > 5) {
                p.setColor(Color.YELLOW);
            }
            if (w > 7) {
                p.setColor(Color.RED);
            }
            canvas.drawLine(xStart, graund - height*w - 2, xStart , graund - height*(w+1),p);
            p.setAlpha(120);
            canvas.drawLine(xStart, graund + (height*w + 2 - height - 1)/2, xStart , graund + (height*(w+1)  - height - 1)/2,p);
            p.setAlpha(255);
        }

        /////////////////////
        xStart = xStart + width + 5;
        oldD = 0;
        for(int ww = 500; ww < 1500; ww++){
            float nd = (mFftBytes[ww])/40;
            if(nd > oldD){
                oldD = nd;
            }

        }
        if (oldD <= 2){oldD = 2;}
        if (oldD > 10){oldD = 10;}

        for(int w = 1; w < oldD; w++){
            p.setColor(Color.GREEN);
            if (w > 5) {
                p.setColor(Color.YELLOW);
            }
            if (w > 7) {
                p.setColor(Color.RED);
            }
            canvas.drawLine(xStart, graund - height*w - 2, xStart , graund - height*(w+1),p);
            p.setAlpha(120);
            canvas.drawLine(xStart, graund + (height*w + 2 - height - 1)/2, xStart , graund + (height*(w+1)  - height - 1)/2,p);
            p.setAlpha(255);
        }
    }

}
