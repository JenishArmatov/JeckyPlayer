package com.armatov.music.visualizermusicplayer.Visualizer.renderer;


import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;

import android.graphics.Rect;

import java.util.Random;


public class Subopod2 {
    private float[] x = new float[400];
    public float[][] stars;

    float magAlpha;
    native void calculateData(float[] mFftBytes,float[] x,float[] GroundWidthHeight);
    public void draw(float[] mFftBytes, Rect rect, Canvas canvas, Paint paint) {
        Random random = new Random();
        if(stars == null){
            stars = new float[5][500];
            for(int i = 0 ; i < 500; i++){
                int xRandom = random.nextInt(rect.width()/2);
                stars[0][i] = xRandom;
                int yRandom = random.nextInt(rect.width()/2);
                stars[3][i] = yRandom;
                stars[1][i] = 1;
                stars[2][i] = 0.08f;
                stars[4][i] = 0.5f;

            }
        }

        canvas.drawColor(Color.argb(255,0,0,0));
        float width = (canvas.getWidth() + canvas.getHeight())/300;
        int graund = (int) ((canvas.getHeight() / 2));
        float xStart = 20f;

        Paint p = new Paint();
        p.setStrokeWidth(width);


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
        for(int i = 0 ; i < 500; i++){

            p.setColor(Color.WHITE);
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
            stars[1][i] = stars[1][i] + magAlpha/500 + 0.02f;
            float[] cartPoint = {
                    (float) (i) / (300),
                    (rect.centerX() + rect.centerY()) / 4 + ((stars[0][i])) * ((rect.centerX() + rect.centerY()) / 2) / 128
            };
            float[] polarPoint = toPolar(cartPoint, canvas.getWidth()/2, canvas.getHeight()/2);
            canvas.drawCircle(polarPoint[0],polarPoint[1], stars[1][i],p);
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
                float[] polarPoint1 = toPolar(cartPoint1, canvas.getWidth()/2, canvas.getHeight()/2);
                canvas.drawCircle(polarPoint1[0],polarPoint1[1],1,p);
            }

        }
        graund = (canvas.getHeight() / 4);
        float [] gwh = {graund, canvas.getWidth()/2, canvas.getHeight()/2};
        width = 4;
        x = getData(mFftBytes, x, gwh);
        //  calculateData(mFftBytes, x, gwh);





        p = new Paint();



        //  p.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.OUTER));


        p.setColor(paint.getColor());
        //    p.setShader(new SweepGradient(canvas.getWidth() / 2, canvas.getHeight() / 2, COLORS2, null));


        p.setStrokeWidth(width);
        //   p.setShader(new BitmapShader(BarGraphRenderer.bitmapLine, Shader.TileMode.CLAMP, Shader.TileMode.MIRROR));
        canvas.drawLines(x, p);







    }
    final int[] COLORS2 = new int[]{Color.parseColor("#33004c"), Color.parseColor("#4600d2"),
            Color.parseColor("#0000ff"), Color.parseColor("#0099ff"),
            Color.parseColor("#00eeff"),Color.parseColor("#00FF7F"),
            Color.parseColor("#48FF00"),Color.parseColor("#B6FF00"),
            Color.parseColor("#FFD700"),Color.parseColor("#ff9500"),
            Color.parseColor("#FF6200"),Color.parseColor("#FF0000")};

    float newbytes[] = new float[1024*4];
    float lastFft[] = new float[1024*4];
    float[] x1 = new float[400];

    public float[] getData(float mFftBytes[], float data[], float GroundWidthHeight[]){
        float graund = GroundWidthHeight[0];
        float width = GroundWidthHeight[1];
        float height = GroundWidthHeight[2];





        for (int i = 0; i < 100; i++) {


            newbytes[i] = graund - (mFftBytes[i]);


            x1[i] = (x1[i] + x1[i] + newbytes[i]) / 3;
            if (i > 0 && x1[i] < x1[i - 1]) {
                x1[i - 1] = x1[i];
            }
            if (x1[i] < x1[i + 1]) {
                x1[i + 1] = x1[i];
            }
            if (x1[i] < 0) {
                x1[i] = 0;
            }

            if (x1[i] > graund - 6) {
                x1[i] = graund - 6;
            }

        }





        for (int i = 0; i < 100; i++) {





            if(i <= 50){
                float  cartPoint1[] = new float [2];
                cartPoint1[0] = (float) (i) / (-100);
                cartPoint1[1] = height / 5 + ((graund - x1[i])) * (height / 2) / 128;

                float polarPoint1[] = toPolar(cartPoint1, width, height);
                data[i * 4] = width;
                data[i * 4 + 1] = height;
                data[i * 4 + 2] = polarPoint1[0];
                data[i * 4 + 3] = polarPoint1[1];
            }
            if(i > 50){
                float cartPoint[] = new float [2];
                cartPoint[0] = (float) (i-50) / (100);
                cartPoint[1] = height / 5 + (graund - x1[i-50]) * (height / 2) / 128;

                float  polarPoint[] = toPolar(cartPoint, width,height);
                data[i * 4] = width;
                data[i * 4 + 1] = height;
                data[i * 4 + 2] = polarPoint[0];
                data[i * 4 + 3] = polarPoint[1];
            }








        }


        return data;

    }
    float[] toPolar(float[]  cartesian, double  cX, double  cY) {

        double angle = (cartesian[0]) * 2 * Math.PI;
        double radius = (cartesian[1] / 2);
        float  []out = new float [2];

        double x = cX - radius * sin(angle);
        double y =  cY - radius * cos(angle);

        out[0] = (float) x;
        out[1] = (float) y;

        return out;
    }
}