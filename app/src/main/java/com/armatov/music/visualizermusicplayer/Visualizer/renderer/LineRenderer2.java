package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.armatov.music.visualizermusicplayer.JniBitmapHolder;

public class LineRenderer2 {
    private int[] pixels;
    private int red = 250;
    private int green = 0;
    private int blue = 0;
    private Bitmap b;
    private Matrix matrix;
    private Paint p;
    private  float[] x = new float[1024*4];
    private static float[] lastFft = new float[100];

    public void draw(Canvas canvas, float[] mFftBytes,  Rect rect, int column) {
        int graund = (int) (canvas.getHeight() / 2);
        canvas.drawColor(Color.argb(255,0,0,0));
        if (b == null){
            b = Bitmap.createBitmap(canvas.getWidth(),canvas.getHeight(), Bitmap.Config.ARGB_4444);
            matrix = new Matrix();
            p = new Paint();
            rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());

            //p.setShadowLayer(30, 0, 0, Color.RED);
          //  p.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL));

        }
        final int w=canvas.getWidth(),height=canvas.getHeight();
// store the bitmap in the JNI "world"
        final JniBitmapHolder bitmapHolder = new JniBitmapHolder(b);
// no need for the bitmap on the java "world", since the operations are done on the JNI "world"
        b.recycle();


// crop a center square from the bitmap, from (0.25,0.25) to (0.75,0.75) of the bitmap.
        bitmapHolder.cropBitmap(0, 3,w,height);
//rotate the bitmap:





//get the output java bitmap , and free the one on the JNI "world"
        b=bitmapHolder.getBitmapAndFree();

        int k = 1;
        int lastIndex = 0;
        int index = 1;
        int speedH = rect.height()/140;
        int speedW = 0;
        float[] newbytes = new float[column];
        float[][] arrayXYARGB = new float[3][column];
        float width = (canvas.getWidth())/(column);
        float xStart = rect.width()/2;
        p.setStrokeWidth(2);



        Canvas canvas1 = new Canvas(b);
        matrix.reset();
        matrix.setRotate(180);
        matrix.postTranslate(rect.width(),rect.height()/2);
        canvas1.setMatrix(matrix);
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

        for(int i = 0; i < column; i++) {

            newbytes[i] = graund -  mFftBytes[i] ;

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
            if (i > 2 && i < 98) {
                x[i - 1] = ((x[i] + x[i-3])/2 + (x[i]))/2;
                x[i - 2] = ((x[i] + x[i-3])/2 + (x[i - 3]))/2;
            }
            if (i < column - 3) {
                x[i + 1] = ((x[i] + x[i+3])/2 + (x[i]))/2;
                x[i + 2] = ((x[i] + x[i+3])/2 + (x[i + 3]))/2;
            }
            if (x[i] > graund) {
                x[i] = graund;
            }
            arrayXYARGB[0][i] = xStart;
        }
        canvas1.drawColor(Color.argb(15,0,0,0));

        for(int i = 0; i < column; i++) {
            calcolateColor(i);

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.argb(255, red, green, blue));
            p.setAlpha(255);

            if(i < column - 1){
                canvas1.drawLine(xStart,x[i],
                        xStart + width, x[i + 1],p);
             //   canvas1.drawLine(xStart,graund,
             //           xStart + width, graund,p);
            }
            if(i == column - 1){
                canvas1.drawLine(xStart,x[i],
                        xStart + width, graund,p);
             //   canvas1.drawLine(xStart,graund,
              //          xStart + width, graund,p);
            }

            if(i < column - 1){
                canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),x[i],
                        canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, x[i + 1],p);
               // canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),graund,
               //         canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, graund,p);
            }
            if(i == column - 1){
                canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),x[i],
                        canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, graund,p);
             //   canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),graund,
             //           canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, graund,p);
            }
            width = width - width/(60);
            xStart = xStart + width;
        }
        red = 250;
        green = 0;
        blue = 0;
        matrix.reset();
        matrix.setRotate(180);
        matrix.postTranslate(rect.width(),rect.height()/2);
        canvas.setMatrix(matrix);

        canvas.drawBitmap(b,0,0,p);

        matrix.reset();
        matrix.postTranslate(0,canvas.getHeight()/2);

        canvas.setMatrix(matrix);

        canvas.drawBitmap(b,0,0,p);
    }
    private void calcolateColor(int i){
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
    }
/*        int graund = (int) (canvas.getHeight() / 2);
        canvas.drawColor(Color.argb(255,0,0,0));
        if (b == null){
            b = Bitmap.createBitmap(canvas.getWidth(),canvas.getHeight(), Bitmap.Config.ARGB_4444);
            matrix = new Matrix();
            p = new Paint();
            rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());

            //p.setShadowLayer(30, 0, 0, Color.RED);
          //  p.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL));

        }
        final int w=canvas.getWidth(),height=canvas.getHeight();
// store the bitmap in the JNI "world"
        final JniBitmapHolder bitmapHolder = new JniBitmapHolder(b);
// no need for the bitmap on the java "world", since the operations are done on the JNI "world"
        b.recycle();


// crop a center square from the bitmap, from (0.25,0.25) to (0.75,0.75) of the bitmap.
        bitmapHolder.cropBitmap(0,5,w,height);
//rotate the bitmap:
//get the output java bitmap , and free the one on the JNI "world"
        b=bitmapHolder.getBitmapAndFree();

        int speedH = rect.height()/140;
        int speedW = 0;
        float[] newbytes = new float[column];
        float[][] arrayXYARGB = new float[3][column];
        float width = (canvas.getWidth())/(column);
        float xStart = rect.width()/2;
        p.setStrokeWidth(2);



        Canvas canvas1 = new Canvas(b);
        matrix.reset();
        matrix.setRotate(180);
        matrix.postTranslate(rect.width(),rect.height()/2);
        canvas1.setMatrix(matrix);

        for(int i = 0; i < column; i++) {

            newbytes[i] = graund -  mFftBytes[i] ;

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
            if (i > 2 && i < 98) {
                x[i - 1] = ((x[i] + x[i-3])/2 + (x[i]))/2;
                x[i - 2] = ((x[i] + x[i-3])/2 + (x[i - 3]))/2;
            }
            if (i < column - 3) {
                x[i + 1] = ((x[i] + x[i+3])/2 + (x[i]))/2;
                x[i + 2] = ((x[i] + x[i+3])/2 + (x[i + 3]))/2;
            }
            if (x[i] > graund) {
                x[i] = graund;
            }
            arrayXYARGB[0][i] = xStart;
        }
        canvas1.drawColor(Color.argb(15,0,0,0));

        for(int i = 0; i < column; i++) {
            calcolateColor(i);

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.argb(255, red, green, blue));
            p.setAlpha(255);

            if(i < column - 1){
                canvas1.drawLine(xStart,x[i],
                        xStart + width, x[i + 1],p);
                canvas1.drawLine(xStart,graund,
                        xStart + width, graund,p);
            }
            if(i == column - 1){
                canvas1.drawLine(xStart,x[i],
                        xStart + width, graund,p);
                canvas1.drawLine(xStart,graund,
                        xStart + width, graund,p);
            }

            if(i < column - 1){
                canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),x[i],
                        canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, x[i + 1],p);
                canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),graund,
                        canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, graund,p);
            }
            if(i == column - 1){
                canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),x[i],
                        canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, graund,p);
                canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),graund,
                        canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, graund,p);
            }
            width = width - width/(70);
            xStart = xStart + width;
        }
        red = 250;
        green = 0;
        blue = 0;
        matrix.reset();
        matrix.setRotate(180);
        matrix.postTranslate(rect.width(),rect.height()/2);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(b,0,0,p);

        matrix.reset();
        matrix.postTranslate(0,canvas.getHeight()/2);

        canvas.setMatrix(matrix);

        canvas.drawBitmap(b,0,0,p);
    }
    private void calcolateColor(int i){
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
    }

 */
}
