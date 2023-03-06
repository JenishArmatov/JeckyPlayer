package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class CirleRenderer {

        private float mFFTPoints[];
        private float x[] = new float[1024*4];



        public void draw(float[] data,Rect rect, Paint mPaint,Canvas canvas) {
            canvas.drawColor(Color.argb(250,0,0,0));
            Paint paint = new Paint();
            paint.setColor(Color.argb(255,0,0,0));

            mFFTPoints = new float[data.length];
            mPaint.setStrokeWidth(2);
            for (int i = 0; i < data.length/4-1; i++){

                x[i] = (x[i] + x[i] + x[i] + data[i])/4;

            }


            for (int i = 0; i < data.length/4-1; i++) {
                // Calculate dbValue

                float[] cartPoint = {
                        (float) (i) / ((data.length/4) - 1),
                        (rect.width()+rect.height())/10
                };

                float[] polarPoint = toPolar(cartPoint, rect);
                mFFTPoints[i * 4] = polarPoint[0];
                mFFTPoints[i * 4 + 1] = polarPoint[1];

                float[] cartPoint2 = {
                        (float) (i ) / ((data.length/4) - 1),
                        (rect.width()+rect.height())/10 + ((x[i + 1])*2) * ((rect.width()+rect.height())/10)
                };

                float[] polarPoint2 = toPolar(cartPoint2, rect);
                mFFTPoints[i * 4 + 2] = polarPoint2[0];
                mFFTPoints[i * 4 + 3] = polarPoint2[1];
            }

            canvas.drawCircle(rect.width()/2,rect.height()/2,(rect.width()*2+rect.height()*2)/22, paint);
            canvas.drawLines(mFFTPoints, mPaint);

            // Controls the pulsing rate
            modulation += 0.13;
            angleModulation += 0.28;
        }

        float modulation = 0;
        float modulationStrength = 0; // 0-1
        float angleModulation = 0;
        float aggresive = 0.78f;
        private float[] toPolar(float[] cartesian, Rect rect)
        {
            double cX = rect.width()/2;
            double cY = rect.height()/2;
            double angle = (cartesian[0]) * 2 * Math.PI;
            double radius = ((rect.width()+rect.height())/5 * (1 - aggresive)
                    + aggresive * cartesian[1]/2);
            float[] out =  {
                    (float)(cX + radius * Math.sin(angle)),
                    (float)(cY + radius * Math.cos(angle ))
            };
            return out;
        }




}
