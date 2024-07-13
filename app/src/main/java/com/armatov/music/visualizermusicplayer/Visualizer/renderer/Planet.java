package com.armatov.music.visualizermusicplayer.Visualizer.renderer;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.armatov.music.visualizermusicplayer.Visualizer.renderer.interfaces.Renderer;

public class Planet implements Renderer {

    private float[] newbytes = new float[1024];
    private  float[] x = new float[1024*4];
    private Paint paint;

//    private float[][] stars;



    private float[] toPolar(float[] cartesian, Rect rect) {
        double cX = rect.width() / 2;
        double cY = rect.height() / 2;
        double angle = (cartesian[0]) * 2 * Math.PI;
        double radius = (cartesian[1] / 2);
        float[] out = {
                (float) (cX - radius * Math.sin(angle)),
                (float) (cY - radius * Math.cos(angle))
        };
        return out;
    }


    @Override
    public void draw(Canvas canvas, float[] mFftBytes, float[] data)  {
        Rect rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
        initColorChange();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        canvas.drawColor(Color.argb(255,0,0,0));
        int graund = (canvas.getHeight() / 2);

        Paint p = new Paint();
        p.setStrokeWidth(2);
        p.setStyle(Paint.Style.STROKE);
        Paint pShadow = new Paint();
        pShadow.setStyle(Paint.Style.STROKE);
        pShadow.setColor(paint.getColor());
        float magAlpha = 0;

        for(int i = 1; i < 8; i++) {
            if((mFftBytes[i])/5 > magAlpha){
                magAlpha = (mFftBytes[i])/5;
            }
        }
        int red = 250;
        int green = 0;
        int blue = 0;
        int k = 1;
        int lastIndex = 0;
        int index = 1;

        for(int i = 0; i < 100; i++) {
            float highSample = 0;
            if(i < 15 && i > 0){
                if((graund - x[i-1])/3-50 > magAlpha){
                    magAlpha = (graund - x[i-1])/3-50;
                }
                pShadow.setAlpha((int) (magAlpha = (magAlpha) < 200 ? (int) magAlpha : 200));
                pShadow.setStrokeWidth(i*7);
                canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
                        rect.height() / 7 - (rect.height() / 36), pShadow);

            }

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
            newbytes[i] = graund - highSample ;

            if (newbytes[i] > graund) {
                newbytes[i] = graund - (newbytes[i] - graund);
            }
            x[i] = (x[i] + x[i] + newbytes[i])/3;
            if (i>0 && x[i] < x[i - 1] - 10) {
                x[i-1] = x[i];
            }
            if (x[i] < x[i + 1] - 10) {
                x[i+1] = x[i];
            }

            if (x[i] > graund) {
                x[i] = graund;
            }




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

            p.setColor(Color.argb(255, red, green, blue));
            pShadow.setColor(paint.getColor());


            float[] cartPoint = {
                    (float) (i) / (196),
                    rect.height() / 4 + ((graund - x[i])/4) * (rect.height() / 2) / 128
            };

            float[] polarPoint = toPolar(cartPoint, new Rect(0,0,canvas.getWidth(),canvas.getHeight()));


            //       canvas.drawCircle(polarPoint[0], polarPoint[1],8, paintCircle);
            float[] cartPoint1 = {
                    (float) (i) / (-196),
                    rect.height() / 4 + ((graund - x[i])/4) * (rect.height() / 2) / 128
            };

            float[] polarPoint1 = toPolar(cartPoint1,  new Rect(0,0,canvas.getWidth(),canvas.getHeight()));


            canvas.drawCircle(polarPoint[0], polarPoint[1],rect.height()/100, p);
            canvas.drawCircle(polarPoint1[0], polarPoint1[1], rect.height()/100, p);

        }

        paint.setStyle(Paint.Style.FILL);
        //    canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
        //           rect.height() / 7 - (rect.height() / 36), paint);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);

        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
                rect.height() / 7 - (rect.height() / 16) + magAlpha/2, p);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(6);

        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
                rect.height() / 7 - (rect.height() / 12) + magAlpha/2, p);
    }
    private int red = 250;
    private int green = 0;
    private int blue = 0;
    private int i = 0;
    private void initColorChange() {
        paint = new Paint();

        paint.setARGB(255, red,green,blue);
        if(i < 26 && i > 0){
            red = 250;
            green = i * 10;
            blue = 0;
        }
        if(i > 25 && i < 51){
            red = 250 - ((i - 25)*10);
            blue = 0;
            green = 250;

        }
        if(i > 50 && i < 76){
            blue = (i - 50) * 10;
            green = 250 - ((i - 50)*10);
            red = 0;

        }
        if(i > 75 && i < 99){
            green = 0;
            red = (i - 75) * 10;
            blue = 250;
        }
        i = (int) (i + 1.5f);
        if(i > 100){i = 0;}

        paint.setARGB(255, red,green,blue);
    }
}
