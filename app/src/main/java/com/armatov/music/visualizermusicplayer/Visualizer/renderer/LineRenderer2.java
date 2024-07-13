package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.armatov.music.visualizermusicplayer.JniBitmapHolder;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.interfaces.Renderer;

public class LineRenderer2 implements Renderer {
    private int red = 250;
    private int green = 0;
    private int blue = 0;
    private Bitmap b;
    private Paint p;
    private final float[] x = new float[1024*4];
    private Matrix matrix;


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

    @Override
    public void draw(Canvas canvas, float[] mFftBytes, float[] data){
        int column = 100;
        Rect rect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (matrix == null){
            b = Bitmap.createBitmap(canvas.getWidth(),canvas.getHeight(), Bitmap.Config.ARGB_8888);
            matrix = new Matrix();
            p = new Paint();
        }
        int graund = (canvas.getHeight() / 2);
        canvas.drawColor(Color.argb(255,0,0,0));
        final int w=canvas.getWidth(),height=canvas.getHeight();
        final JniBitmapHolder bitmapHolder = new JniBitmapHolder(b);
        b.recycle();

        bitmapHolder.cropBitmap((int) mFftBytes[3]/2,6,w/2,height);

        b=bitmapHolder.getBitmapAndFree();

        float[] newbytes = new float[column];
        float width = (canvas.getWidth())/(column);
        float xStart = rect.width()/2;
        p.setStrokeWidth(3);



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
        }
        canvas1.drawColor(Color.argb(25,0,0,0));

        for(int i = 0; i < column; i++) {
            calcolateColor(i);

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.argb(255, red, green, blue));

            if(i < column - 1){
                canvas1.drawLine(xStart,x[i],
                        xStart + width, x[i + 1],p);
                //      canvas1.drawLine(xStart,graund,
                //               xStart + width, graund,p);
            }
            if(i == column - 1){
                canvas1.drawLine(xStart,x[i],
                        xStart + width, graund,p);
                //     canvas1.drawLine(xStart,graund,
                //             xStart + width, graund,p);
            }

            if(i < column - 1){
                canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),x[i],
                        canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, x[i + 1],p);
                //    canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),graund,
                //            canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, graund,p);
            }
            if(i == column - 1){
                canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),x[i],
                        canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2) - width, graund,p);
                // canvas1.drawLine(canvas.getWidth() / 2 - (xStart - canvas.getWidth()/2),graund,
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
        matrix.setScale(1.0f, -1.0f);
        matrix.postTranslate(0,rect.height()/2);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(b,0,0,p);

        matrix.reset();
        matrix.setScale(-1.0f, 1.0f);
        matrix.postTranslate(w,rect.height()/2);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(b,0,0,p);
        matrix.setScale(1.0f, 1.0f);
        matrix.postTranslate(0,rect.height()/2);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(b,0,0,p);

    }
}
