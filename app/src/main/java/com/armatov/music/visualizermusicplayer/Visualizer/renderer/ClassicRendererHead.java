package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class ClassicRendererHead {
    private float[] newbytes = new float[1024];
    private float[][] arrayXYARGB = new float[3][100];
    private  Paint paint = new Paint();
    private float[] x = new float[1024*4];

    public void draw(Canvas canvas, float[] mFftBytes,
                            Rect rect, Paint paintColor) {

        canvas.drawColor(Color.argb(255,0,0,0));
        float width = (rect.width() - 130f)/100f;


        int graund = (canvas.getHeight() / 2);

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
        float magAlpha = 0;

        Paint pShadow = new Paint();
        pShadow.setColor(paintColor.getColor());
        pShadow.setStrokeWidth(4);


        for(int i = 0; i < 100; i++) {
            float highSample = 0;
            if(i < 10 && (graund - x[i])/2-40 > magAlpha){
                magAlpha = (graund - x[i])/2-40;
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
            newbytes[i] = graund - (highSample - highSample/4) ;

            if (newbytes[i] > graund) {
                newbytes[i] = graund;
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


            if (i > 0 && x[i] < x[i - 1]) {
                x[i-1] = x[i];
            }
            if (x[i] < x[i + 1]) {
                x[i+1] = x[i];
            }
            p.setColor(Color.argb(255, red, green, blue));
            p.setStrokeWidth(width);
            p.setStyle(Paint.Style.STROKE);

            arrayXYARGB[0][i] = xStart;
            arrayXYARGB[1][i] = x[i];


        }
        for(int i = 0; i < 15; i++) {

            paintColor.setStrokeWidth(magAlpha/5);

            canvas.drawLine(10, graund,
                    canvas.getWidth() - 10, graund , paintColor);
            paintColor.setAlpha((int) (magAlpha = (magAlpha) < 250 ? (int) magAlpha : 250)/2);
            paintColor.setStrokeWidth(i * 10);

              canvas.drawLine(10, graund,
                    canvas.getWidth() - 10, graund, paintColor);


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
            p.setColor(Color.argb(255 , red, green, blue));
            canvas.drawLine(xStart, arrayXYARGB[1][i] - width - 6,
                    xStart, arrayXYARGB[1][i], p);
            paint.setColor(Color.argb(155, red, green, blue));
            paint.setStrokeWidth(width);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

            canvas.drawLine(xStart, graund + ((graund - (arrayXYARGB[1][i]))/2 + width/2 + 8),
                    xStart, graund + (graund - (arrayXYARGB[1][i]))/2, paint);

            xStart = xStart + width + 1;

        }


    }}
