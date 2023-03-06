package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;


public class Tesla {
    private  float[] newbytes = new float[1024*4];
    private  float[][] arrayXYARGB = new float[3][100];
    private  float[] x = new float[1024*4];

    private  Paint paint = new Paint();
    public  void draw(Canvas canvas, float[] mFftBytes,
                            Rect rect) {

        canvas.drawColor(Color.argb(255,0,0,0));
        float width = (rect.width()-40)/100f;


        int graund = (rect.height() / 2);

        float xStart = 20f;

        Paint p = new Paint();
        Paint paintLine = new Paint();
        Paint paintMirror = new Paint();
        Paint paintUp = new Paint();

        paintMirror.setStrokeWidth(width);
        paintUp.setStrokeWidth(width);
        p.setStrokeWidth(width);

        paintUp.setColor(Color.WHITE);
        paintLine.setColor(Color.WHITE);
        paintLine.setStrokeWidth(3);
        int red = 250;
        int green = 0;
        int blue = 0;
        int k = 1;
        int lastIndex = 0;
        int index = 1;
        for(int i = 0; i < 100; i++) {
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
            newbytes[i] = graund - (highSample - highSample/4) ;

            if (newbytes[i] > graund - 6) {
                newbytes[i] = graund - 6;
            }
            x[i] = (x[i] + x[i] + x[i] + newbytes[i])/4;

            if (i > 0 && x[i] < x[i - 1]) {
                x[i-1] = x[i];
            }
            if (x[i] < x[i + 1]) {
                x[i+1] = x[i];
            }
            if (i > 0 && x[i] < x[i - 1]) {
                x[i-1] = x[i];
            }
            if (x[i] < x[i + 1]) {
                x[i+1] = x[i];
            }
            if (x[i] < 0 ) {
                x[i] = 0;
            }

            if (x[i] > graund - 6) {
                x[i] = graund - 6;
            }



            p.setColor(Color.argb(255, red, green, blue));
            p.setStrokeWidth(width);
            p.setStyle(Paint.Style.STROKE);

            arrayXYARGB[0][i] = xStart;
            arrayXYARGB[1][i] = x[i];

        }


        for(int i = 0; i < 99; i++) {
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

            p.setStrokeWidth(width);
            p.setColor(Color.argb(255, red, green, blue));
            p.setStyle(Paint.Style.FILL);

            if (i > 0){
                canvas.drawLine(xStart, arrayXYARGB[1][i-1],
                        xStart + width, arrayXYARGB[1][i], p);
                p.setStrokeWidth(width/2);


                canvas.drawCircle(xStart, arrayXYARGB[1][i-1],width/2,p);
                canvas.drawCircle(xStart + width, arrayXYARGB[1][i],width/2, p);

            }
            p.setStrokeWidth(width);



            paint.setColor(Color.argb(120, red, green, blue));
            paint.setStrokeWidth(width);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

            if (i > 0){

                canvas.drawLine(xStart, graund + (graund - (arrayXYARGB[1][i-1]))/2 + 6,
                        xStart + width, graund + (graund - (arrayXYARGB[1][i]))/2 + 6, paint);
                paint.setStrokeWidth(width/2);
                canvas.drawCircle(xStart, graund + (graund - (arrayXYARGB[1][i-1]))/2 + 6, width/2, paint);
                canvas.drawCircle(xStart + width, graund + (graund - (arrayXYARGB[1][i]))/2 + 6, width/2, paint);
            }

            xStart = xStart + width;

        }


    }

}
