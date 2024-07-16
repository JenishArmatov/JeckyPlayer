package com.armatov.music.visualizermusicplayer.Visualizer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.graphics.Canvas;

import com.armatov.music.App;
import com.armatov.music.service.MultiPlayer;
import com.armatov.music.util.CalendarUtil;
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


import java.util.ArrayList;
import java.util.List;

public class Draw {

    private float[][] fftHistory;
    private float[][] dataHistory;

    private float[] data = new float[1024 * 4];
    private float[] fft = new float[1024 * 4];

    private int stepForBluetoothLatency = 1;
    public static int latencyForBluetooth;
    private List<Renderer> renderers;


    private boolean chekBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean result = false;
        if (bluetoothAdapter != null) {
            result = BluetoothProfile.STATE_CONNECTED == bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);

        }
        return result;
    }

    public Draw() {
        fftHistory = new float[100][1024 * 4];
        dataHistory = new float[100][1024 * 4];

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

        if (chekBluetooth()) {
            latencyForBluetooth = 17;
        } else {
            latencyForBluetooth = 0;
        }
        setPlayerPos();
    }

    private void setPlayerPos() {
        Context context = App.getInstance();
        String timeText = new CalendarUtil().getDate();
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

        List<float[]> fftList = Util.calculateFft(data,fft);

        data = fftList.get(0);
        fft  = fftList.get(1);

        fftHistory[stepForBluetoothLatency] = fft.clone();
        dataHistory[stepForBluetoothLatency] = data.clone();

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


}
