package com.armatov.music.visualizermusicplayer.Visualizer.renderer;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Electro {
    private static float[] mPoints;
    private static Paint mPaint;
    public static void draw(float[] fft, float[] data, Rect rect, Paint mFlashPaint, Canvas canvas) {


        mPaint = new Paint();
        mPaint.setColor(mFlashPaint.getColor());
        mPaint.setStrokeWidth(3);
        mFlashPaint.setStrokeWidth(3);
        mPoints = new float[data.length];

        for (int i = 0; i < data.length/4 -1; i++) {
            mPoints[i * 4] = (rect.width() * i) / (data.length / 4);
            mPoints[i * 4 + 1] = rect.height() / 2
                    + ((data[i + data.length/4])) / 16;
            mPoints[i * 4 + 2] = rect.width() * (i + 1) / (data.length/4);
            mPoints[i * 4 + 3] = rect.height() / 2
                    + ( (data[i + data.length/4 + 1])) / 16;
        }
        canvas.drawLines(mPoints,mPaint);


    }


}
