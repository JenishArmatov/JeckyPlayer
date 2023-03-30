package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


class LineRenderer {
    private Bitmap bitmapLine;
    public float stepForLineRenderer = 1;
    public boolean flag = false;
    private  float[] x = new float[1024*4];

    public void draw(Canvas canvas, float[] mFftBytes, Rect rect, int column) {
        int speedH = rect.height()/120;
        int speedW = rect.width()/90;
        float[] newbytes = new float[column];
        float[][] arrayXYARGB = new float[3][column];
        float width = (canvas.getWidth())/(column*2);
        int graund = (canvas.getHeight() / 4);
        canvas.drawColor(Color.argb(255,0,0,0));

        float xStart = rect.width()/4;

        Paint p = new Paint();
        p.setStrokeWidth(3);


        int red = 250;
        int green = 0;
        int blue = 0;
        int k = 1;
        int lastIndex = 0;
        int index = 1;
        if(bitmapLine == null){
            bitmapLine = Bitmap.createBitmap(50,50, Bitmap.Config.ARGB_8888);

        }
        bitmapLine = Bitmap.createScaledBitmap(bitmapLine, (int) (rect.width()/2+speedW
                        - stepForLineRenderer),
                rect.height()/2 + speedH, true);

        Canvas canvas1 = new Canvas(bitmapLine);
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
            newbytes[i] = graund - highSample/3 ;

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

        }
        for(int i = 0; i < column; i++) {


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
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.argb(255, red, green, blue));
            p.setAlpha(255);
            p.setStrokeWidth(2);

            if(i < column - 1){
                canvas1.drawLine(xStart,arrayXYARGB[1][i],
                        xStart + width, arrayXYARGB[1][i + 1],p);
            }
            if(i == column - 1){
                canvas1.drawLine(xStart,arrayXYARGB[1][i],
                        xStart + width, graund,p);
            }
            if(i < column - 1){
                canvas1.drawLine(rect.centerX() - xStart,arrayXYARGB[1][i],
                        rect.centerX() - xStart - width, arrayXYARGB[1][i + 1],p);
            }
            if(i == column - 1){
                canvas1.drawLine(rect.centerX() - xStart,arrayXYARGB[1][i],
                        rect.centerX() - xStart - width, graund,p);
            }

//////////////////            ////////////////////////////////*
/*            p.setStrokeWidth(10);
            p.setAlpha(60);
            if(i < column - 1){
                canvas1.drawLine(xStart,arrayXYARGB[1][i],
                        xStart + width, arrayXYARGB[1][i + 1],p);
            }
            if(i == column - 1){
                canvas1.drawLine(xStart,arrayXYARGB[1][i],
                        xStart + width, graund,p);
            }
            if(i < column - 1){
                canvas1.drawLine(rect.centerX() - xStart,arrayXYARGB[1][i],
                        rect.centerX() - xStart - width, arrayXYARGB[1][i + 1],p);
            }
            if(i == column - 1){
                canvas1.drawLine(rect.centerX() - xStart,arrayXYARGB[1][i],
                        rect.centerX() - xStart - width, graund,p);
            }
*/
            width = width - width/(60);
            xStart = xStart + width;
        }
        p.setAlpha(255);
        canvas.drawBitmap(bitmapLine,new Rect(0,0, (int) (rect.width()/2
                         ),
                rect.height()/2),new Rect(rect),p);
        try {
            bitmapLine = Bitmap.createBitmap(bitmapLine,speedW - (int)stepForLineRenderer,0,
                    (int) (rect.width()/2-speedW  - stepForLineRenderer), rect.height()/2-speedH);
        }catch (Exception e){}

        if(stepForLineRenderer <= 10 && !flag){
            stepForLineRenderer = stepForLineRenderer + 0.1f;
        }
        if(stepForLineRenderer >= 10){
            flag = true;

        }
        if(flag){
            stepForLineRenderer = stepForLineRenderer - 0.1f;

        }
        if(stepForLineRenderer < 0 - (speedW-1)){
            flag = false;

        }

    }


}

