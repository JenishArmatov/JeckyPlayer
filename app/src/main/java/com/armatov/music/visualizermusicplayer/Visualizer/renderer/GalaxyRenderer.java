package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;


public class GalaxyRenderer {
    private float x = 0;
    private int numberOfBitmap = 1;
    private boolean isMinus = false;

    public void draw(Canvas canvas, float[] mFFTBytes, Rect rect, Context context) {

        int graund = canvas.getHeight()/2;
        float mag = 0;
        int hz = 0;
        for(int i = 2; i < 9; i++){
            if(mFFTBytes[i]/66 > mag){
                mag = mFFTBytes[i]/66;
                hz = i-2;
            }
        }
        switch (hz){
            case 0:
                if(isMinus){
                    numberOfBitmap=numberOfBitmap-1;
                }else {
                    numberOfBitmap=numberOfBitmap+1;
                }
                if(numberOfBitmap > mag){
                    isMinus = true;
                    numberOfBitmap = 5;
                }else if(numberOfBitmap < 1){
                    isMinus = false;
                    numberOfBitmap = 2;
                }
                break;
            case 1:
                if(isMinus){
                    numberOfBitmap=numberOfBitmap-2;
                }else {
                    numberOfBitmap=numberOfBitmap+2;
                }
                if(numberOfBitmap > mag){
                    isMinus = true;
                    numberOfBitmap = 5;
                }else if(numberOfBitmap < 1){
                    isMinus = false;
                    numberOfBitmap = 2;
                }
                break;
            case 2:
                if(isMinus){
                    numberOfBitmap=numberOfBitmap-3;
                }else {
                    numberOfBitmap=numberOfBitmap+3;
                }
                if(numberOfBitmap > mag){
                    isMinus = true;
                    numberOfBitmap = 5;
                }else if(numberOfBitmap < 1){
                    isMinus = false;
                    numberOfBitmap = 2;
                }

                break;
            case 3:
                if(isMinus){
                    numberOfBitmap=numberOfBitmap-4;
                }else {
                    numberOfBitmap=numberOfBitmap+4;
                }
                if(numberOfBitmap > mag){
                    isMinus = true;
                    numberOfBitmap = 5;
                }else if(numberOfBitmap < 1){
                    isMinus = false;
                    numberOfBitmap = 2;
                }

                break;
            case 4:
                if(isMinus){
                    numberOfBitmap=numberOfBitmap-5;
                }else {
                    numberOfBitmap=numberOfBitmap+5;
                }
                if(numberOfBitmap > mag){
                    isMinus = true;
                    numberOfBitmap = 5;
                }else if(numberOfBitmap < 1){
                    isMinus = false;
                    numberOfBitmap = 2;
                }

                break;
            case 5:
                if(isMinus){
                    numberOfBitmap=numberOfBitmap-4;
                }else {
                    numberOfBitmap=numberOfBitmap+4;
                }
                if(numberOfBitmap > mag){
                    isMinus = true;
                    numberOfBitmap = 4;
                }else if(numberOfBitmap < 1){
                    isMinus = false;
                    numberOfBitmap = 2;
                }

                break;

        }
        int resIDBitmap = context.getResources().getIdentifier("i" + numberOfBitmap,
                "drawable", context.getPackageName());
   //     BarGraphRenderer.bitmapGalaxy = BitmapFactory.decodeResource(context.getResources(), resIDBitmap);
  //      canvas.drawBitmap(BarGraphRenderer.bitmapGalaxy,100,100,new Paint());


    }

}