package com.armatov.music.service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.h6ah4i.android.media.IBasicMediaPlayer;
import com.h6ah4i.android.media.IMediaPlayerFactory;
import com.h6ah4i.android.media.audiofx.IBassBoost;
import com.h6ah4i.android.media.audiofx.IEqualizer;
import com.h6ah4i.android.media.audiofx.IVirtualizer;
import com.armatov.music.R;
import com.armatov.music.service.playback.Playback;
import com.armatov.music.util.PreferenceUtil;
import com.h6ah4i.android.media.opensl.OpenSLMediaPlayer;

public class MultiPlayer implements Playback, IBasicMediaPlayer.OnErrorListener, IBasicMediaPlayer.OnCompletionListener {
    public static final String TAG = MultiPlayer.class.getSimpleName();


    public IMediaPlayerFactory factory;

    private OpenSLMediaPlayer mCurrentMediaPlayer;
    private OpenSLMediaPlayer mNextMediaPlayer;
    public static IEqualizer equalizer;
    public static IBassBoost bassBoost;
    public static IVirtualizer iVirtualizer;

    private final Context context;
    @Nullable
    private Playback.PlaybackCallbacks callbacks;

    private boolean mIsInitialized = false;

    public MultiPlayer(final Context context, IMediaPlayerFactory f) {
        this.context = context;
        this.factory = f;
        PreferenceUtil activityPerference = PreferenceUtil.getInstance(context);


  //      visualiserView.link(factory);
        mCurrentMediaPlayer = (OpenSLMediaPlayer) factory.createMediaPlayer();

        equalizer = factory.createHQEqualizer();
        equalizer.setEnabled(true);
        if(activityPerference.getSelectedItemOfSpinerEqualizer() < 10){
            equalizer.usePreset((short) activityPerference.getSelectedItemOfSpinerEqualizer());

        }else {
            equalizer.setBandLevel((short)0, Short.parseShort(activityPerference.getState()));
            equalizer.setBandLevel((short)1, Short.parseShort(activityPerference.getState1()));
            equalizer.setBandLevel((short)2, Short.parseShort(activityPerference.getState2()));
            equalizer.setBandLevel((short)3, Short.parseShort(activityPerference.getState3()));
            equalizer.setBandLevel((short)4, Short.parseShort(activityPerference.getState4()));
            equalizer.setBandLevel((short)5, Short.parseShort(activityPerference.getState5()));
            equalizer.setBandLevel((short)6, Short.parseShort(activityPerference.getState6()));
            equalizer.setBandLevel((short)7, Short.parseShort(activityPerference.getState7()));
            equalizer.setBandLevel((short)8, Short.parseShort(activityPerference.getState8()));
            equalizer.setBandLevel((short)9, Short.parseShort(activityPerference.getState9()));
        }

        if((short) activityPerference.getSelectedItemOfSpinerEqualizer() < 10){
            equalizer.usePreset((short) activityPerference.getSelectedItemOfSpinerEqualizer());
        }


        bassBoost = factory.createBassBoost(mCurrentMediaPlayer);
        bassBoost.setEnabled(false);
        bassBoost.setStrength((short) activityPerference.getBassBoostStrength());

        iVirtualizer = factory.createVirtualizer(mCurrentMediaPlayer);
        iVirtualizer.setEnabled(true);
        iVirtualizer.setStrength((short) activityPerference.getVirtualizerStrength());


        mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
    }
    @Override
    public boolean setDataSource(@NonNull final String path) {
        mIsInitialized = false;
        mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
        if (mIsInitialized) {
            setNextDataSource(null);
        }
        return mIsInitialized;
    }

    private boolean setDataSourceImpl(@NonNull final IBasicMediaPlayer player, @NonNull final String path) {
        if (context == null) {
            return false;
        }
        try {
            player.reset();
            player.setOnPreparedListener(null);
            if (path.startsWith("content://")) {
                player.setDataSource(context, Uri.parse(path));
            } else {
                player.setDataSource(path);
            }
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();
        } catch (Exception e) {
            return false;
        }
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        final Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.getPackageName());
        intent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);

        context.sendBroadcast(intent);
        return true;
    }

    @Override
    public void setNextDataSource(@Nullable final String path) {
        if (context == null) {
            return;
        }
        try {
            mCurrentMediaPlayer.setNextMediaPlayer(null);
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "Next media player is current one, continuing");
        } catch (IllegalStateException e) {
            Log.e(TAG, "Media player not initialized!");
            return;
        }
        if (mNextMediaPlayer != null) {
            mNextMediaPlayer.release();
            mNextMediaPlayer = null;
        }
        if (path == null) {
            return;
        }
        if (PreferenceUtil.getInstance(context).gaplessPlayback()) {
            mNextMediaPlayer = (OpenSLMediaPlayer) factory.createMediaPlayer();
            mNextMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
            mNextMediaPlayer.setAudioSessionId(getAudioSessionId());
            if (setDataSourceImpl(mNextMediaPlayer, path)) {
                try {
                    mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer);
                } catch (@NonNull IllegalArgumentException | IllegalStateException e) {
                    Log.e(TAG, "setNextDataSource: setNextMediaPlayer()", e);
                    if (mNextMediaPlayer != null) {
                        mNextMediaPlayer.release();
                        mNextMediaPlayer = null;
                    }
                }
            } else {
                if (mNextMediaPlayer != null) {
                    mNextMediaPlayer.release();
                    mNextMediaPlayer = null;
                }
            }
        }
    }

    @Override
    public void setCallbacks(@Nullable Playback.PlaybackCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public boolean isInitialized() {
        return mIsInitialized;
    }

    @Override
    public boolean start() {
        try {
            mCurrentMediaPlayer.start();

            return true;
        } catch (IllegalStateException e) {
            return false;
        }

    }

    @Override
    public void stop() {
        mCurrentMediaPlayer.reset();
        mIsInitialized = false;
    }

    /**
     * Releases resources associated with this MediaPlayer object.
     */
    @Override
    public void release() {
        stop();
        mCurrentMediaPlayer.release();
        if (mNextMediaPlayer != null) {
            mNextMediaPlayer.release();
        }
    }

    /**
     * Pauses playback. Call start() to resume.
     */
    @Override
    public boolean pause() {
        try {
            mCurrentMediaPlayer.pause();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Checks whether the MultiPlayer is playing.
     */
    @Override
    public boolean isPlaying() {
        return mIsInitialized && mCurrentMediaPlayer.isPlaying();
    }


    @Override
    public int duration() {
        if (!mIsInitialized) {
            return -1;
        }
        try {
            return mCurrentMediaPlayer.getDuration();
        } catch (IllegalStateException e) {
            return -1;
        }
    }


    @Override
    public int position() {
        if (!mIsInitialized) {
            return -1;
        }
        try {
            return mCurrentMediaPlayer.getCurrentPosition();

        } catch (IllegalStateException e) {
            return -1;
        }

    }


    @Override
    public int seek(final int whereto) {
        try {
            mCurrentMediaPlayer.seekTo(whereto);
            return whereto;
        } catch (IllegalStateException e) {
            return -1;
        }
    }

    @Override
    public boolean setVolume(final float vol) {
        try {
            mCurrentMediaPlayer.setVolume(vol, vol);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }


    @Override
    public boolean setAudioSessionId(final int sessionId) {
        try {
            mCurrentMediaPlayer.setAudioSessionId(sessionId);
            return true;
        } catch (@NonNull IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

    /**
     * Returns the audio session ID.
     *
     * @return The current audio session ID.
     */
    @Override
    public int getAudioSessionId() {
        return mCurrentMediaPlayer.getAudioSessionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onError(final IBasicMediaPlayer mp, final int what, final int extra) {
        mIsInitialized = false;
        mCurrentMediaPlayer.release();
        mCurrentMediaPlayer = (OpenSLMediaPlayer) factory.createMediaPlayer();
        mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        if (context != null) {
            Toast.makeText(context, context.getResources().getString(R.string.unplayable_file), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onCompletion(final IBasicMediaPlayer mp) {
        if (mp == mCurrentMediaPlayer && mNextMediaPlayer != null) {
            mIsInitialized = false;
            mCurrentMediaPlayer.release();
            mCurrentMediaPlayer = mNextMediaPlayer;
            mIsInitialized = true;
            mNextMediaPlayer = null;
            if (callbacks != null)
                callbacks.onSongEnded();
        } else {
            if (callbacks != null)
                callbacks.onSongEnded();
        }
    }
}
