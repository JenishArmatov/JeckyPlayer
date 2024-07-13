package com.armatov.music.visualizermusicplayer;

import java.nio.ByteBuffer;
import android.graphics.Bitmap;
import android.util.Log;

public class JniBitmapHolder
{
    ByteBuffer _handler =null;
    static
    {
        System.loadLibrary("myapplication");
    }

    private native ByteBuffer jniStoreBitmapData(Bitmap bitmap);

    private native Bitmap jniGetBitmapFromStoredBitmapData(ByteBuffer handler);

    private native void jniFreeBitmapData(ByteBuffer handler);

    private native void jniRotateBitmapCcw90(ByteBuffer handler);
    private native void jniFlipBitmapVertical(ByteBuffer handler);
    private native void jniScaleNNBitmap(ByteBuffer handler, int width, int height);
   // private native Bitmap jnicropAndResizeBitmap(Bitmap bitmap);

    public native void cropBitmap(Bitmap inputBitmap, Bitmap outputBitmap);
    private native void jniCropBitmap(ByteBuffer handler,final int left,final int top,final int right,final int bottom);

    public JniBitmapHolder()
    {}

    public JniBitmapHolder(final Bitmap bitmap)
    {
        storeBitmap(bitmap);
    }

    public void storeBitmap(final Bitmap bitmap)
    {

        _handler=jniStoreBitmapData(bitmap);
    }

    public void flipBitmapVertical()
    {
        if(_handler==null){

            return;
        }
        jniFlipBitmapVertical(_handler);
    }
    public void rotateBitmapCcw90()
    {
        if(_handler==null){

            return;
        }
        jniRotateBitmapCcw90(_handler);
    }
    public void cropBitmap(final int left,final int top,final int right,final int bottom)
    {
        if(_handler==null){
            return;
        }

        jniCropBitmap(_handler,left,top,right,bottom);
    }

    public Bitmap getBitmap()
    {

        return jniGetBitmapFromStoredBitmapData(_handler);
    }

    public Bitmap getBitmapAndFree()
    {
        final Bitmap bitmap=getBitmap();
        freeBitmap();
        return bitmap;
    }

    public void freeBitmap()
    {
        if(_handler==null){

            return;
        }
        jniFreeBitmapData(_handler);
        _handler=null;
    }
    public void scaleNNBitmap(int width, int height)
    {
        if(_handler==null){

            return;
        }
        jniScaleNNBitmap(_handler, width, height);
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        if(_handler==null)
            return;
        freeBitmap();
    }


}