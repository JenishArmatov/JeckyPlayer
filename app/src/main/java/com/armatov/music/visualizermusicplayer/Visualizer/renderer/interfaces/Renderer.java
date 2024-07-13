package com.armatov.music.visualizermusicplayer.Visualizer.renderer.interfaces;

import android.graphics.Canvas;

public interface Renderer {
    void draw(Canvas canvas, float[] mFftBytes, float[] data);
}
