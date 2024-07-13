package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.armatov.music.visualizermusicplayer.Visualizer.renderer.interfaces.Renderer;

import java.util.Random;

public class Classic implements Renderer {
    private  float[] newbytes = new float[1024*4];
    private  float[][] arrayXYARGB = new float[3][100];
    private  float[] gravity = new float[100];
    private static float[] lastFft = new float[100];

    private  float[] freez = new float[100];
    private  float magAlpha = 0;
    private  Paint paint = new Paint();
    private static   Random random = new Random();
    private static Rect rect;
    public static float[][] stars;
    private  float[] x = new float[1024*4];


    public static void initStars(){
        if(stars == null){
            stars = new float[2][200];
            for(int i = 0 ; i < 200; i++){
                if (i < 100) {
                    int xRandom = random.nextInt(rect.width());
                    stars[0][i] = xRandom;
                    int xRandomW = random.nextInt(rect.height() / 2 - rect.height() / 3);
                    stars[1][i] = xRandomW;
                    lastFft[i] = 0;
                }else {
                    int xRandom = random.nextInt(rect.width());
                    stars[0][i] = xRandom;
                    int xRandomW = random.nextInt(rect.height() / 2 - rect.height() / 8);
                    stars[1][i] = xRandomW;
                }


            }
        }
    }

    @Override
    public void draw(Canvas canvas, float[] mFftBytes, float[] data) {
        rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());

        initStars();


        canvas.drawColor(Color.argb(255,0,0,0));
        float width = (rect.width() - 130f)/100f;


        int graund = (canvas.getHeight() / 2) + (canvas.getHeight() / 5);

        float xStart = 20f;

        Paint p = new Paint();
        Paint paintMirror = new Paint();
        Paint paintUp = new Paint();
        Paint paintLine = new Paint();

        paintMirror.setStrokeWidth(width);
        paintUp.setStrokeWidth(width);
        p.setStrokeWidth(width);

        paintUp.setColor(Color.WHITE);
        paintLine.setColor(Color.WHITE);

        int red = 250;
        int green = 0;
        int blue = 0;
        int k = 1;
        int lastIndex = 0;
        int index = 1;
        magAlpha = 0;


        p.setColor(Color.WHITE);
        // p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(2);

        for (int i = 0; i < 100; i++){
            float highSample = 0;
            for(int j = lastIndex; j <= index; j++){
                if(mFftBytes[j] > highSample){
                    highSample = mFftBytes[j];
                }
            }

            lastIndex = index;
            if(i > 87){
                if(i < 98){
                    highSample = highSample - highSample / 3;
                }
                index = index + i;
            }else{

                index = index + k + i/30;
            }
            if(i > 87){
                for (int f = i - 5; f < 100; f++){
                    if ((lastFft[f] > lastFft[f - 1])) {
                        lastFft[f-1] = lastFft[f] - lastFft[f]/5;
                    }
                }
            }
            lastFft[i] = highSample;
        }
        for (int f = 82; f < 95; f++){
            for (int i = 0; i < 5; i++){
                if (lastFft[f + i] > lastFft[f + i + 1]) {
                    lastFft[f + i+1] = lastFft[f + i] - lastFft[(f + i)]/5;
                }
            }
        }
        for(int i = 0; i < 100; i++) {

            if(i < 10 && (graund - x[i])/2-40 > magAlpha){
                magAlpha = (graund - x[i])/2-40;
            }
            newbytes[i] = graund - (lastFft[i] - lastFft[i]/5) ;

            if (newbytes[i] > graund - 6) {
                newbytes[i] = graund - 6;
            }
            gravity[i] = (float) (gravity[i] + freez[i]);
            x[i] = (x[i] + x[i] + x[i] + newbytes[i])/4;

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
            if (gravity[i] > x[i]) {
                gravity[i] = x[i];
                freez[i] = 0;
            }
            if (gravity[i] > graund - 7) {
                gravity[i] = graund - 7;
            }



            p.setColor(Color.argb(255, red, green, blue));
            p.setStrokeWidth(width);
            p.setStyle(Paint.Style.STROKE);

            arrayXYARGB[0][i] = xStart;
            arrayXYARGB[1][i] = x[i];
            freez[i] = freez[i] + 0.2f;
            gravity[i] = gravity[i] + 0.8f;
            canvas.drawCircle(stars[0][i], stars[1][i], 2, paintLine);


        }
        float circleSize = (canvas.getHeight() + canvas.getWidth())/30;
        canvas.drawCircle(canvas.getWidth()/2 + canvas .getWidth()/4, canvas.getHeight()/2 - canvas.getHeight() / 6,
                circleSize, paintLine);
        for(int i = 0; i < 10; i++) {

            paintLine.setStrokeWidth(magAlpha/2);

            circleSize = (canvas.getHeight() + canvas.getWidth())/30 + i*4;
            //     canvas.drawLine(10, graund,
            //            canvas.getWidth() - 10, graund , paintLine);
            paintLine.setAlpha((int) (magAlpha = (magAlpha) < 250 ? (int) magAlpha : 250)/2);
            paintLine.setStrokeWidth(i * 15);
            canvas.drawCircle(canvas.getWidth()/2 + canvas .getWidth()/4, canvas.getHeight()/2 - canvas.getHeight() / 6,
                    circleSize, paintLine);
            //  canvas.drawLine(10, graund - canvas.getHeight() / 7,
            //        canvas.getWidth() - 10, graund - canvas.getHeight() / 7, paintLine);


        }
        circleSize = (canvas.getHeight() + canvas.getWidth())/30;
        paintLine.setAlpha(255);
        canvas.drawCircle(canvas.getWidth()/2 + canvas .getWidth()/4, canvas.getHeight()/2 - canvas.getHeight() / 6,
                circleSize, paintLine);

        for(int i = 0; i < 100; i++) {
            canvas.drawCircle(stars[0][i+100], stars[1][i+100], 1 , paintUp);
            canvas.drawCircle(stars[0][i+100], stars[1][i+100], 1 , paintUp);

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
            canvas.drawLine(xStart, graund - 6,
                    xStart, arrayXYARGB[1][i] - 6, p);
            p.setColor(Color.argb(255, 255, 255, 255));

            canvas.drawLine(xStart, gravity[i] - 7,
                    xStart, gravity[i] - 10, p);
            paint.setColor(Color.argb(120, red, green, blue));
            paint.setStrokeWidth(width);

            canvas.drawLine(xStart, graund + 6,
                    xStart, graund + (graund - (arrayXYARGB[1][i]))/2 + 6, paint);

            xStart = xStart + width + 1;

        }


    }
}
