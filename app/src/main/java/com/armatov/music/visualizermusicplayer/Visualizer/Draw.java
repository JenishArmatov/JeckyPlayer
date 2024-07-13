package com.armatov.music.visualizermusicplayer.Visualizer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.armatov.music.service.MultiPlayer;
import com.armatov.music.util.PreferenceUtil;

import com.armatov.music.util.Util;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.CircleWater;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.CirleRenderer;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.Classic;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.ClassicRenderer;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.ClassicRendererHead;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.Cubics;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.LineCircleRenderer;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.LineRenderer;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.LineRenderer2;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.Planet;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.interfaces.Renderer;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.SimpleLineRenderer;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.SpeakersRenderer;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.Subopod;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.Subopod2;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.Tesla;
import com.armatov.music.visualizermusicplayer.Visualizer.renderer.WeaveRenderer;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Draw {


    public float[][] fftHistory = new float[100][1024 * 4];
    public float[][] dataHistory = new float[100][1024 * 4];

    public static float[] data = new float[1024 * 4];

    public int stepForBluetoothLatency = 1;
    public static int latencyForBluetooth;
    private float[] fft = new float[1024 * 4];
    private Context context;
    private List<Renderer> renderers;


    private boolean chekBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean result = false;
        if (bluetoothAdapter != null) {
            result = BluetoothProfile.STATE_CONNECTED == bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);

        }
        return result;
    }

    public Draw(Context context) {
        renderers = new ArrayList<>();
        renderers.add(new SimpleLineRenderer());
        renderers.add(new Cubics());
        renderers.add(new ClassicRendererHead());
        renderers.add(new WeaveRenderer());
        renderers.add(new ClassicRenderer());
        renderers.add(new Tesla());
        renderers.add(new Planet());
        renderers.add(new LineCircleRenderer());
        renderers.add(new Subopod2());
        renderers.add(new LineRenderer());
        renderers.add(new CirleRenderer());
        renderers.add(new SpeakersRenderer());
        renderers.add(new Classic());
        renderers.add(new CircleWater());
        renderers.add(new LineRenderer2());
        renderers.add(new Subopod());


        this.context = context;
        if (chekBluetooth()) {
            latencyForBluetooth = 17;

        } else {
            latencyForBluetooth = 0;

        }
        setPlayerPos(context);
    }

    private void setPlayerPos(Context context) {

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        String timeText = dateText + timeFormat.format(currentDate);
        Util.checkedItem = PreferenceUtil.getInstance(context).getCheckedItem();
        long oldTime = PreferenceUtil.getInstance(context).getTimeOfWatchVideo(PreferenceUtil.getInstance(context).getCheckedItem()) + 1000000;
        long realTime = Long.parseLong(timeText);
        if (realTime > oldTime) {
            if (PreferenceUtil.getInstance(context).getCheckedItem() > 1) {
                Util.checkedItem = 0;

            } else {
                Util.checkedItem = PreferenceUtil.getInstance(context).getCheckedItem();

            }

        } else {
            Util.checkedItem = PreferenceUtil.getInstance(context).getCheckedItem();

        }
    }

    public void draw(Canvas canvas) {

        calculateFft();

        if (stepForBluetoothLatency != latencyForBluetooth) {
            renderers.get(Util.checkedItem).draw(canvas, fftHistory[stepForBluetoothLatency + 1],
                    dataHistory[stepForBluetoothLatency + 1]);
        } else {
            renderers.get(Util.checkedItem).draw(canvas, fftHistory[0], dataHistory[0]);
        }

        stepForBluetoothLatency++;

        if (stepForBluetoothLatency > latencyForBluetooth) {
            stepForBluetoothLatency = 0;
        }

    }

    private void calculateFft() {

        float[] lastFFt = new float[1024 * 4];

        System.arraycopy(MultiPlayer.data, 0, data, 0, MultiPlayer.data.length / 2);
        System.arraycopy(MultiPlayer.lastFFt, 0, lastFFt, 0, MultiPlayer.lastFFt.length / 2);

        int lenth = lastFFt.length / 2;

        for (int i = 0; i < lenth - 5; i++) {
            float div = (lenth / 2) - i * 2;
            div = div > 20 ? (int) (div) : 20;
            float w = lastFFt[i * 2] * lastFFt[i * 2];
            float q = ((lastFFt[i * 2 + 1] * lastFFt[i * 2 + 1]));
            float res = (w + q) / div;
            res = res > 0 ? (int) (60 * Math.log10(res * res)) : 0;
            for (int f = 0; f <= 5; f++) {
                if (i <= 5) {
                    fft[f] = 1;
                }
            }
            for (int f = 0; f < 8; f++) {
                if ((i > 7) && (fft[i - f] > fft[(i - f) - 1])) {
                    fft[(i - f) - 1] = fft[(i - f)] - fft[(i - f)] / 5;
                }
            }

            fft[i + 5] = res;

        }
        for (int f = 0; f < 1000; f++) {
            for (int i = 0; i < 8; i++) {
                if (fft[f] > fft[f + 1]) {
                    fft[f + 1] = fft[f] - fft[(f)] / 5;
                }
            }
        }
        fftHistory[stepForBluetoothLatency] = fft.clone();
        dataHistory[stepForBluetoothLatency] = data.clone();
    }
}
