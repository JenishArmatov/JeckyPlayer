package com.armatov.music.visualizermusicplayer.Visualizer.renderer;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

import java.util.Random;

public class CircleWater {
    private float newbytes[] = new float[1024];
    private float[][] arrayXYARGB = new float[3][200];
    private float magAlpha = 0;
    public static float[][] stars;
    private float[][] starsColor = new float[3][500];
    private  float[] x = new float[1024*4];

    public void draw(Canvas canvas, float[] mFftBytes,
                     Rect rect) {
        Random random = new Random();
        if(stars == null){
            stars = new float[5][500];
            starsColor = new float[3][500];

        }

        canvas.drawColor(Color.argb(255,0,0,0));
        float width = (canvas.getWidth() + canvas.getHeight())/100;
        int graund = (int) ((canvas.getHeight() / 2));
        float xStart = 20f;

        Paint p = new Paint();
        p.setStrokeWidth(width);
   //     p.setStyle(Paint.Style.FILL_AND_STROKE);

        int red = 250;
        int green = 0;
        int blue = 0;
        magAlpha = 0;
        float db = 0;
        for(int i = 6; i < 18; i++) {
            if((mFftBytes[i])/3 > magAlpha){
                magAlpha = (mFftBytes[i])/3;
            }
            if((mFftBytes[i])*(rect.height() / (60 + i*10)) > db){
                db = (mFftBytes[i])*(rect.height() / (60 + i*10));
            }
        }

        p.setColor(Color.WHITE);
       // p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(2);
        for(int i = 0 ; i < 300; i++){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                p.setColor(Color.rgb( starsColor[0][i], starsColor[1][i], starsColor[2][i]));
            }else {
                p.setColor(Color.rgb(random.nextInt(250), random.nextInt(200), random.nextInt(250)));

            }
            if(stars[0][i] > rect.centerX()){
                int xRandom = random.nextInt(rect.width()/2);
                stars[0][i] = xRandom;
                int yRandom = random.nextInt(rect.width()/2);
                stars[3][i] = yRandom;
                stars[1][i] = 1;
                stars[2][i] = 0.08f;
                stars[4][i] = 0.5f;
            }

            if(i < 199 && stars[0][i] == stars[0][i+1]){
                stars[0][i] = random.nextInt(rect.height()/2);

            }
            stars[2][i] = stars[2][i] + 0.08f;
            stars[0][i] = stars[0][i] + magAlpha/6 + stars[2][i];
            stars[1][i] = stars[1][i] + magAlpha/200 + 0.2f;
            float[] cartPoint = {
                    (float) (i) / (300),
                    (rect.centerX() + rect.centerY()) / 4 + ((stars[0][i])) * ((rect.centerX() + rect.centerY()) / 2) / 128
            };
            float[] polarPoint = toPolar(cartPoint, new Rect(0,0,canvas.getWidth(),canvas.getHeight()));
            canvas.drawCircle(polarPoint[0],polarPoint[1],stars[1][i],p);
            if(i < 200){
                if(stars[3][i] > rect.centerX()){
                    stars[3][i] = 0;
                    stars[4][i] = 0.5f;
                }
                stars[3][i] = stars[3][i] + magAlpha/80 + stars[4][i];
                p.setColor(Color.WHITE);
                float[] cartPoint1 = {
                        (float) (i) / (100),
                        rect.height() / 4 + ((stars[3][i])) * (rect.height() / 2) / 128
                };
                float[] polarPoint1 = toPolar(cartPoint1, new Rect(0,0,canvas.getWidth(),canvas.getHeight()));
                canvas.drawCircle(polarPoint1[0],polarPoint1[1],1,p);
            }

        }
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(width);
        red = 250;
        green = 0;
        blue = 0;
        for(int i = 0; i < 100; i++) {


            newbytes[i] = graund - (mFftBytes[i]) ;
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

            if (x[i] > graund) {
                x[i] = graund;
            }




            arrayXYARGB[0][i] = xStart;
            arrayXYARGB[1][i] = x[i];


        }
        for(int i = 0; i < 100; i++) {
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

            float[] cartPoint = {
                    (float) (i) / (199),
                    rect.height() / 4 + ((graund - arrayXYARGB[1][i])/4) * (rect.height() / 2) / 128
            };
            float[] polarPoint = toPolar(cartPoint, new Rect(0,0,canvas.getWidth(),canvas.getHeight()));

            float[] cartPoint1 = {
                    (float) (i) / (-199),
                    rect.height() / 4 + ((graund - arrayXYARGB[1][i])/4) * (rect.height() / 2) / 128
            };
            float[] polarPoint1 = toPolar(cartPoint1, new Rect(0,0,canvas.getWidth(),canvas.getHeight()));


            
            canvas.drawLine(canvas.getWidth()/2, canvas.getHeight()/2, polarPoint[0], polarPoint[1], p);
            p.setColor(Color.argb(255, red, green, blue));
            canvas.drawCircle(polarPoint[0], polarPoint[1],width/2,p);
            if(i < 99){
                float[] cartPoint2 = {
                        (float) (i+1) / (199),
                        rect.height() / 4 + ((graund - arrayXYARGB[1][i + 1])/4) * (rect.height() / 2) / 128
                };
                float[] polarPoint2 = toPolar(cartPoint2, new Rect(0,0,canvas.getWidth(),canvas.getHeight()));
                canvas.drawLine(polarPoint[0], polarPoint[1],
                        polarPoint2[0], polarPoint2[1],p);
            }
            canvas.drawLine(canvas.getWidth()/2, canvas.getHeight()/2, polarPoint1[0], polarPoint1[1], p);

            canvas.drawCircle(polarPoint1[0], polarPoint1[1],width/2,p);
            if(i < 99){
                float[] cartPoint2 = {
                        (float) (i+1) / (-199),
                        rect.height() / 4 + ((graund - arrayXYARGB[1][i + 1])/4) * (rect.height() / 2) / 128
                };
                float[] polarPoint2 = toPolar(cartPoint2, new Rect(0,0,canvas.getWidth(),canvas.getHeight()));
                canvas.drawLine(polarPoint1[0], polarPoint1[1],
                        polarPoint2[0], polarPoint2[1],p);
            }
            if(i == 99){
                float[] cartPoint2 = {
                        (float) (i+1) / (-199),
                        rect.height() / 4 + ((graund - arrayXYARGB[1][i])/4) * (rect.height() / 2) / 128
                };
                float[] polarPoint2 = toPolar(cartPoint2, new Rect(0,0,canvas.getWidth(),canvas.getHeight()));
                canvas.drawLine(polarPoint[0], polarPoint[1],
                        polarPoint2[0], polarPoint2[1],p);
            }
          //  xStart = xStart + width;


        }

        p.setColor(Color.BLACK);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
                rect.height() / 7 - (rect.height() / 12) + db/50, p);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(4);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
                rect.height() / 7 - (rect.height() / 10) + db/50, p);

    }



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


}
