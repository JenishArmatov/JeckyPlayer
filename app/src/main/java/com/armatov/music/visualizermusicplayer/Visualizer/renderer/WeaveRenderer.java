package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class WeaveRenderer {
    private static float[] newbytes = new float[1024*4];
    private static float[] lastFft = new float[300];
    private  float[][] arrayXYARGB = new float[5][300];
    private  float[] x = new float[100*4];
    Paint p;

    private int step = 0;


    public void draw(Canvas canvas, float[] mFftBytes,
                            Rect rect) {


        canvas.drawColor(Color.argb(255,0,0,0));
        float width = (rect.width())/300f;


        int graund = (canvas.getHeight() / 2) + (canvas.getHeight() / 5);

        float xStart = 20f;
        if(p == null){
            p = new Paint();
            p.setStrokeWidth(width);
        }




        int red = 250;
        int green = 0;
        int blue = 0;
        int k = 1;
        int lastIndex = 0;
        int index = 1;


        for (int i = 0; i < 100; i++) {
            float highSample = 0;
            for (int j = lastIndex; j <= index; j++) {
                if (mFftBytes[j] > highSample) {
                    highSample = mFftBytes[j];
                }
            }

            lastIndex = index;
            if (i > 87) {
                if (i < 98) {
                    highSample = highSample - highSample / 3;
                }
                index = index + i;
            } else {

                index = index + k + i / 30;
            }
            if (i > 87) {
                for (int f = i - 5; f < 100; f++) {
                    if ((lastFft[f] > lastFft[f - 1])) {
                        lastFft[f - 1] = lastFft[f] - lastFft[f] / 5;
                    }
                }
            }
            lastFft[i] = highSample;
        }
        for (int f = 82; f < 95; f++) {
            for (int i = 0; i < 5; i++) {
                if (lastFft[f + i] > lastFft[f + i + 1]) {
                    lastFft[f + i + 1] = lastFft[f + i] - lastFft[(f + i)] / 5;
                }
            }
        }
        for (int i = 0; i < 297; i++) {


            newbytes[i] = graund - (lastFft[i/3] - lastFft[i/3] / 5);

            if (newbytes[i] > graund - 6) {
                newbytes[i] = graund - 6;
            }
            x[i] = (x[i] + x[i]  + newbytes[i]) / 3;


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

        }
/*
        for(int g = 0; g < 1; g++){
            xStart = 20f;

            switch (g){
                case 1:
                    red = 150;
                    green = 150;
                    blue = 255;
                    break;
                case 2:
                    red = 255;
                    green = 100;
                    blue = 255;
                    break;
                case 3:
                    red = 0;
                    green = 0;
                    blue = 255;
                    break;

                case 4:
                    red = 0;
                    green = 255;
                    blue = 0;
                    break;



            }

            for(int i = 0; i < 99; i++) {



                p.setStrokeWidth(width);
                p.setColor(Color.argb(255, red, green, blue));
                canvas.drawLine(xStart, graund - width/2,
                        xStart, arrayXYARGB[g][stepForDraw][i] - width/2, p);

                p.setColor(Color.argb(120, red, green, blue));
                p.setStrokeWidth(width);

                canvas.drawLine(xStart, graund + width/2,
                        xStart, graund + (graund - (arrayXYARGB[g][stepForDraw][i]))/2 + width/2, p);

/////////////////////////////////////////////////////


                p.setStrokeWidth(width);
                p.setColor(Color.argb(255, red, green, blue));
                p.setStyle(Paint.Style.FILL);

                canvas.drawLine(xStart, arrayXYARGB[g][stepForDraw][i]-width/2,
                        xStart + width, arrayXYARGB[g][stepForDraw][i+1]-width/2, p);
                p.setStrokeWidth(width/2);


                canvas.drawCircle(xStart, arrayXYARGB[g][stepForDraw][i]-width/2,width/2,p);
                canvas.drawCircle(xStart + width, arrayXYARGB[g][stepForDraw][i+1]-width/2,width/2, p);


                p.setStrokeWidth(width);



                p.setColor(Color.argb(120, red, green, blue));
                p.setStrokeWidth(width/2);
                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));



                canvas.drawLine(xStart, graund + (graund - (arrayXYARGB[g][stepForDraw][i]))/2 + width/2,
                        xStart + width, graund + (graund - (arrayXYARGB[g][stepForDraw][i+1]))/2 + width/2, p);
                p.setStrokeWidth(width/2);
                canvas.drawCircle(xStart, graund + (graund - (arrayXYARGB[g][stepForDraw][i]))/2 + width/2, width/4, p);
                canvas.drawCircle(xStart + width, graund + (graund - (arrayXYARGB[g][stepForDraw][i+1]))/2 + width/2, width/4, p);

                xStart = xStart + width;

            }
            stepForDraw ++;
            if (stepForDraw > 2){
                stepForDraw = 1;
            }

        }
*/
        for (int i = 0; i < 294; i++) {


            if (i > 2 ) {
                x[i - 1] = ((x[i] + x[i-3])/2 + (x[i]))/2;
                x[i - 2] = ((x[i] + x[i-3])/2 + (x[i - 3]))/2;
            }


            x[i + 1] = ((x[i] + x[i+3])/2 + (x[i]))/2;
            x[i + 2] = ((x[i] + x[i+3])/2 + (x[i + 3]))/2;

        }


        for(int i = 0; i < 297; i++) {

            if(i < 70 && i > 0){
                green = i * 3;
            }
            if(i > 70 && i < 140){
                red = red - 3;

            }
            if(i > 140 && i < 220){
                blue = blue + 3;
                green = green - 1;
                red = red + 1;

            }
            if(i > 220){
                green = green - 1;
                red = red + 1;
               // blue = blue - 1;
            }


            p.setStrokeWidth(width);
            p.setColor(Color.argb(255, red, green, blue));
            canvas.drawLine(xStart, graund - width/2,
                    xStart, x[i] - width/2, p);

            p.setColor(Color.argb(120, red, green, blue));
            p.setStrokeWidth(width);

            canvas.drawLine(xStart, graund + width/2,
                    xStart, graund + (graund - (x[i]))/2 + width/2, p);

/////////////////////////////////////////////////////


            p.setStrokeWidth(width);
            p.setColor(Color.argb(255, red, green, blue));
            p.setStyle(Paint.Style.FILL);

            canvas.drawLine(xStart, x[i]-width/2,
                    xStart + width, x[i+1]-width/2, p);
            p.setStrokeWidth(width);


            canvas.drawCircle(xStart, x[i]-width/2,width/2,p);
            canvas.drawCircle(xStart + width, x[i+1]-width/2,width/2, p);


            p.setStrokeWidth(width);



            p.setColor(Color.argb(120, red, green, blue));
            p.setStrokeWidth(width);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));



            canvas.drawLine(xStart, graund + (graund - (x[i]))/2 + width/2,
                    xStart + width, graund + (graund - (x[i+1]))/2 + width/2, p);
            p.setStrokeWidth(width);
            canvas.drawCircle(xStart, graund + (graund - (x[i]))/2 + width/2, 1, p);
            canvas.drawCircle(xStart + width, graund + (graund - (x[i+1]))/2 + width/2, 1, p);

            xStart = xStart + width;



        }



        step++;
        if(step > 4){
            step = 0;
        }

    }

}


