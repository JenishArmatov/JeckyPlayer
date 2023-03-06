package com.armatov.music.visualizermusicplayer.Visualizer.renderer;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class LineCircleRenderer {

    private float newbytes[] = new float[1024];
    private float magAlpha = 0;
    private float[] lastFft = new float[100];

    private  float[] x = new float[1024*4];

    public void draw(Canvas canvas, float[] mFftBytes,
                     Rect rect) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawColor(Color.argb(255,0,0,0));
        float width = 4;
        int graund = (canvas.getHeight() / 2);
        float xStart = 20f;

        Paint p = new Paint();
        p.setStrokeWidth(5);
        p.setStyle(Paint.Style.FILL);
        Paint pShadow = new Paint();
        pShadow.setStyle(Paint.Style.STROKE);
        pShadow.setColor(paint.getColor());

        int red = 250;
        int green = 0;
        int blue = 0;
        int k = 1;
        int lastIndex = 0;
        int index = 1;
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

            if(i < 10 && (graund - x[i])/4-40 > magAlpha){
                magAlpha = (graund - x[i])/4-40;
            }
            newbytes[i] = graund - (lastFft[i]) ;

            if (newbytes[i] > graund - 6) {
                newbytes[i] = graund - 6;
            }
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

            if (x[i] > graund - 6) {
                x[i] = graund - 6;
            }

            if (i > 2 && i < 98) {
                x[i - 1] = ((x[i] + x[i-3])/2 + (x[i]))/2;
                x[i - 2] = ((x[i] + x[i-3])/2 + (x[i - 3]))/2;
            }
            if (i < 90) {
                x[i + 1] = ((x[i] + x[i+3])/2 + (x[i]))/2;
                x[i + 2] = ((x[i] + x[i+3])/2 + (x[i + 3]))/2;
            }



            p.setColor(Color.argb(255, red, green, blue));
            p.setStrokeWidth(width);
      //      p.setStyle(Paint.Style.STROKE);



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

            p.setColor(Color.argb(255, red, green, blue));
            pShadow.setColor(paint.getColor());


            float[] cartPoint = {
                    (float) (i) / (196),
                    (rect.height() / 8 + magAlpha) + ((graund - x[i])/2) * (rect.height() / 2) / 128
            };

            float[] polarPoint = toPolar(cartPoint, new Rect((int) (0 - magAlpha), (int) (0 - magAlpha),
                    (int) (canvas.getWidth() - magAlpha), (int) (canvas.getHeight() - magAlpha)));


            //       canvas.drawCircle(polarPoint[0], polarPoint[1],8, paintCircle);
            float[] cartPoint1 = {
                    (float) (i) / (-196),
                    (rect.height() / 8 + magAlpha) + ((graund - x[i])/2) * (rect.height() / 2) / 128
            };

            float[] polarPoint1 = toPolar(cartPoint1, new Rect((int) (0 - magAlpha), (int) (0 - magAlpha),
                    (int) (canvas.getWidth() - magAlpha), (int) (canvas.getHeight() - magAlpha)));
            canvas.drawLine(canvas.getWidth()/2, canvas.getHeight()/2, polarPoint[0], polarPoint[1], p);
            canvas.drawLine(canvas.getWidth()/2, canvas.getHeight()/2, polarPoint1[0], polarPoint1[1], p);


        }
        magAlpha = 0;

        for(int i = 1; i < 8; i++) {
            if((mFftBytes[i])/2 > magAlpha){
                magAlpha = (mFftBytes[i])/2;
            }
        }
        p.setColor(Color.BLACK);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
                rect.height() / 9 - (rect.height() / 20) + magAlpha/2, p);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(6);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
                rect.height() / 9 - (rect.height() / 14) + magAlpha/2, p);


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
