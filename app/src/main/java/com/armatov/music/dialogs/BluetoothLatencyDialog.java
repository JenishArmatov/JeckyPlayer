package com.armatov.music.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.armatov.music.R;
import com.armatov.music.visualizermusicplayer.Visualizer.Draw;
import com.triggertrap.seekarc.SeekArc;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BluetoothLatencyDialog extends DialogFragment {
    @BindView(R.id.seek_arc)
    SeekArc seekArc;
    @BindView(R.id.timer_display)
    TextView timerDisplay;
    @BindView(R.id.should_finish_last_song)
    CheckBox shouldFinishLastSong;

    private int seekArcProgress;
    private MaterialDialog materialDialog;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(getActivity())
                .title(getActivity().getResources().getString(R.string.action_bluetooth_latency))
                .positiveText(R.string.action_set)
                .onPositive((dialog, which) -> {
                })

                .showListener(dialog -> {
                })
                .customView(R.layout.dialog_sleep_timer, false)
                .build();

        if (getActivity() == null || materialDialog.getCustomView() == null) {
            return materialDialog;
        }
        ButterKnife.bind(this, materialDialog.getCustomView());
        shouldFinishLastSong.setVisibility(View.GONE);
        seekArc.setMax(50);
        seekArc.setProgressColor(ThemeSingleton.get().positiveColor.getDefaultColor());
        seekArc.setThumbColor(ThemeSingleton.get().positiveColor.getDefaultColor());
        seekArc.post(() -> {
            int width = seekArc.getWidth();
            int height = seekArc.getHeight();
            int small = Math.min(width, height);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(seekArc.getLayoutParams());
            layoutParams.height = small;
            seekArc.setLayoutParams(layoutParams);
        });
        seekArc.setProgress(Draw.latencyForBluetooth);
        seekArcProgress = Draw.latencyForBluetooth;
        updateTimeDisplayTime();

        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(@NonNull SeekArc seekArc, int i, boolean b) {
                if (i < 1) {
                    seekArc.setProgress(1);
                    return;
                }
                seekArcProgress = i;
                updateTimeDisplayTime();
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
                updateTimeDisplayTime();
            }
        });

        return materialDialog;
    }

    private void updateTimeDisplayTime() {
        Draw.latencyForBluetooth = seekArcProgress;
        timerDisplay.setText(seekArcProgress + "");
    }



}
