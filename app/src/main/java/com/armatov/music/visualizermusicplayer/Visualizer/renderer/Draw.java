package com.armatov.music.visualizermusicplayer.Visualizer.renderer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.armatov.music.util.PreferenceUtil;

import com.armatov.music.visualizermusicplayer.Visualizer.Player;
import com.armatov.music.visualizermusicplayer.Visualizer.VisualiserView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Draw {
    public static int stepForGalaxy = 1;
    public static float stepForSubopod;
    private int red = 250;
    private int green = 0;
    private int blue = 0;
    public  int i = 0;

    public float[][] fftHistory = new float[100][1024*4];
    public float[][] dataHistory = new float[100][1024*4];

    public static float REDRAW_TIME  = 20.0f; //частота обновления экрана - 10 мс

    public Paint paint;
    public static float[] data = new float[1024*4];

    private final CircleWater circleWater;
    private final Planet planet;
    private final ClassicRenderer classicRenderer;
    private final ClassicRendererHead classicRendererHead;
    private final LineCircleRenderer lineCircleRenderer;
    private final LineRenderer2 lineRenderer2;
    private final Cubics cubics;
    private final LineRenderer lineRenderer;


    private final CirleRenderer cirleRenderer;
    private final Subopod subopod;
    private final Subopod2 subopod2;
    private final SpeakersRenderer speakersRenderer;

    private final SimpleLineRenderer simpleLineRenderer;
    private final Tesla tesla;
    private final WeaveRenderer weaveRenderer = new WeaveRenderer();
    private final Classic classic;
    public static int column = 100;
    public int stepForBluetoothLatency = 1;
    private int latencyForBluetooth;
    private float[] fft = new float[1024*4];


    private boolean chekBluetooth(){
        REDRAW_TIME = 20;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean result = false;
        if(bluetoothAdapter != null){
            result = BluetoothProfile.STATE_CONNECTED == bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);

        }
        return result;
    }

    public Draw(Context context){
        classicRenderer = new ClassicRenderer();
        classicRendererHead = new ClassicRendererHead();
        lineCircleRenderer = new LineCircleRenderer();
        lineRenderer2 = new LineRenderer2();
        cubics = new Cubics();
        speakersRenderer = new SpeakersRenderer();
        cirleRenderer = new CirleRenderer();
        subopod = new Subopod();
        subopod2 = new Subopod2();
        simpleLineRenderer = new SimpleLineRenderer();
        tesla = new Tesla();
        planet = new Planet();
        circleWater = new CircleWater();
        classic = new Classic();
        lineRenderer = new LineRenderer();

        setPlayerPos(context);
    }
    private void setPlayerPos(Context context){

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        String timeText = dateText + timeFormat.format(currentDate);
        Player.checkedItem = PreferenceUtil.getInstance(context).getCheckedItem();
        long oldTime = PreferenceUtil.getInstance(context).getTimeOfWatchVideo(PreferenceUtil.getInstance(context).getCheckedItem()) + 1000000;
        long realTime = Long.parseLong(timeText);
        if (realTime > oldTime){
            if(PreferenceUtil.getInstance(context).getCheckedItem() > 1){
                Player.checkedItem = 0;

            }else {
                Player.checkedItem = PreferenceUtil.getInstance(context).getCheckedItem();

            }

        }else {
            Player.checkedItem = PreferenceUtil.getInstance(context).getCheckedItem();

        }
    }
    public void draw(Canvas canvas){
        if(chekBluetooth()){
            latencyForBluetooth = 11;

        } else {
            latencyForBluetooth = 0;

        }
        System.arraycopy(VisualiserView.data,0,data, 0, VisualiserView.data.length/2);
        calculateFft();
        fftHistory[stepForBluetoothLatency] = fft.clone();

        dataHistory[stepForBluetoothLatency] = data.clone();


        Rect rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());


        switch (Player.checkedItem){
            case 0 :


                if(stepForBluetoothLatency != latencyForBluetooth){
                    simpleLineRenderer.draw(fftHistory[stepForBluetoothLatency + 1],rect, canvas);

                }else {
                    simpleLineRenderer.draw(fftHistory[0], rect, canvas);
                }

                break;



            case 1 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    cubics.draw(canvas,fftHistory[stepForBluetoothLatency + 1],rect);
                }else {
                    cubics.draw(canvas,fftHistory[0],rect);
                }


                break;

            case 2 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    classicRendererHead.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect, paint);

                }else {
                    classicRendererHead.draw(canvas, fftHistory[0], rect, paint);

                }


                break;

            case 3 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    weaveRenderer.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect);

                }else {
                    weaveRenderer.draw(canvas, fftHistory[0], rect);
                }

                break;


            case 4 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    classicRenderer.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect);
                }else {
                    classicRenderer.draw(canvas, fftHistory[0],  rect);
                }

                break;
            case 5 :
                if(stepForBluetoothLatency != latencyForBluetooth){

                    if(rect.height() >= rect.width()){
                        planet.draw(canvas, fftHistory[stepForBluetoothLatency + 1],  rect, paint);
                    }
                    if(rect.height() < rect.width()){
                        planet.draw(canvas, fftHistory[stepForBluetoothLatency + 1],
                                new Rect(0,0,rect.height(),rect.width()),paint);
                    }

                }else {
                    if(rect.height() >= rect.width()){
                        planet.draw(canvas, fftHistory[0], rect, paint);
                    }
                    if(rect.height() < rect.width()){
                        planet.draw(canvas, fftHistory[0],
                                new Rect(0,0,rect.height(),rect.width()),paint);
                    }

                }


                break;
            case 6 :

                if(stepForBluetoothLatency != latencyForBluetooth){
                    if(rect.height() >= rect.width()){
                        lineCircleRenderer.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect);
                    }
                    if(rect.height() < rect.width()){
                        lineCircleRenderer.draw(canvas, fftHistory[stepForBluetoothLatency + 1],
                                new Rect(0,0,rect.height(),rect.width()));
                    }

                }else {
                    if(rect.height() >= rect.width()){
                        lineCircleRenderer.draw(canvas, fftHistory[0], rect);
                    }
                    if(rect.height() < rect.width()){
                        lineCircleRenderer.draw(canvas, fftHistory[0],
                                new Rect(0,0,rect.height(),rect.width()));
                    }

                }

                break;
            case 7 :

                if(stepForBluetoothLatency != latencyForBluetooth){

                    if(rect.height() >= rect.width()){
                        subopod2.draw(fftHistory[stepForBluetoothLatency + 1], rect, canvas, paint);
                    }
                    if(rect.height() < rect.width()){
                        subopod2.draw(fftHistory[stepForBluetoothLatency + 1],
                                new Rect(0,0,rect.width(),rect.height()), canvas,paint);
                    }


                }else {
                    if(rect.height() >= rect.width()){
                        subopod2.draw(fftHistory[0],rect, canvas, paint);
                    }
                    if(rect.height() < rect.width()){
                        subopod2.draw(fftHistory[0], new Rect(0,0,rect.width(),rect.height()), canvas, paint);

                    }

                }

                break;
            case 8 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    lineRenderer.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect,column);

                }else {
                    lineRenderer.draw(canvas, fftHistory[0], rect,column);

                }

                break;
            case 9 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    cirleRenderer.draw(dataHistory[stepForBluetoothLatency + 1], rect, paint, canvas);

                }else {
                    cirleRenderer.draw(dataHistory[0], rect, paint, canvas);

                }

                break;
            case 10 :
                REDRAW_TIME = 40;

                if(stepForBluetoothLatency != latencyForBluetooth){
                    subopod.draw(canvas, fftHistory[stepForBluetoothLatency + 1],rect,column);
                }else {
                    subopod.draw(canvas, fftHistory[0], rect,column);
                }

                break;
            case 11 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    classic.draw(canvas,fftHistory[stepForBluetoothLatency + 1], rect);
                }else {
                    classic.draw(canvas,fftHistory[0], rect);
                }

                break;
            case 12 :

                if(stepForBluetoothLatency != latencyForBluetooth){
                    if(rect.height() >= rect.width()){
                        circleWater.draw(canvas, fftHistory[stepForBluetoothLatency + 1], rect);
                    }
                    if(rect.height() < rect.width()){
                        circleWater.draw(canvas, fftHistory[stepForBluetoothLatency + 1],
                                new Rect(0,0,rect.height(),rect.width()));
                    }

                }else {
                    if(rect.height() >= rect.width()){
                        circleWater.draw(canvas, fftHistory[0],  rect);
                    }
                    if(rect.height() < rect.width()){
                        circleWater.draw(canvas, fftHistory[0],
                                new Rect(0,0,rect.height(),rect.width()));
                    }

                }

                break;

            case 13 :
                REDRAW_TIME = 40;
                if(stepForBluetoothLatency != latencyForBluetooth){
                    lineRenderer2.draw(canvas, fftHistory[stepForBluetoothLatency + 1],  rect,column);

                }else {
                    lineRenderer2.draw(canvas, fftHistory[0], rect,column);

                }

                break;
            case 14 :
                if(stepForBluetoothLatency != latencyForBluetooth){
                    tesla.draw(canvas, fftHistory[stepForBluetoothLatency + 1],   rect);

                }else {
                    tesla.draw(canvas, fftHistory[0],   rect);

                }

                break;

        }


        stepForGalaxy++;
        stepForSubopod++;
        stepForBluetoothLatency++;

        if(stepForGalaxy > 1300){
            stepForGalaxy = 0;
        }
        if(stepForSubopod > 100){
            stepForSubopod = 1;
        }
        if(stepForBluetoothLatency > latencyForBluetooth){
            stepForBluetoothLatency = 0;
        }

    }
    private void initColorChange() {
        paint = new Paint();
        paint.setARGB(255, red,green,blue);
        if(i < 26 && i > 0){
            red = 250;
            green = i * 10;
            blue = 0;
        }
        if(i > 25 && i < 51){
            red = 250 - ((i - 25)*10);
            blue = 0;
            green = 250;

        }
        if(i > 50 && i < 76){
            blue = (i - 50) * 10;
            green = 250 - ((i - 50)*10);
            red = 0;

        }
        if(i > 75 && i < 99){
            green = 0;
            red = (i - 75) * 10;
            blue = 250;
        }
        i = (int) (i + 1.5f);
        if(i > 100){i = 0;}

        paint.setARGB(255, red,green,blue);
    }
    private void calculateFft(){
        initColorChange();

        float[] lastFFt = new float[1024*4];
        System.arraycopy(VisualiserView.lastFFt,0,lastFFt, 0, VisualiserView.lastFFt.length/2);

        int lenth = lastFFt.length/2;

        for(int i = 0; i < lenth-5; i++) {
            float div = (lenth/2) - i*2;
            div = div > 20 ? (int) ( div) : 20;
            float w = lastFFt[i*2] * lastFFt[i*2];
            float q = ((lastFFt[i*2+1]*lastFFt[i*2+1]));
            float res = (w+q)/div;
            res = res > 0 ? (int) ( 60* Math.log10(res*res)) : 0;
            for (int f = 0; f <= 5; f++){
                if (i <= 5) {
                    fft[f] = 1;
                }
            }
            for (int f = 0; f < 5; f++){
                if ((i > 4) && (fft[i-f] > fft[(i-f) - 1])) {
                    fft[(i-f)-1] = fft[(i-f)]- fft[(i-f)]/4;
                }
            }


            fft[i+5] = res;


        }
        for (int f = 0; f < 1000; f++){
            for (int i = 0; i < 5; i++){
                if (fft[f] > fft[f + 1]) {
                    fft[f+1] = fft[f]- fft[(f)]/4;
                }
            }
        }
        //fft[0] = fft[1] - fft[1]/5;

    }
}
