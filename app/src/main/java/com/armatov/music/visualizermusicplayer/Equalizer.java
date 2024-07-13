package com.armatov.music.visualizermusicplayer;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.armatov.music.util.PreferenceUtil;
import com.h6ah4i.android.media.IBasicMediaPlayer;
import com.h6ah4i.android.media.IMediaPlayerFactory;
import com.h6ah4i.android.media.audiofx.IBassBoost;
import com.h6ah4i.android.media.audiofx.IEqualizer;
import com.h6ah4i.android.media.audiofx.IPresetReverb;
import com.h6ah4i.android.media.audiofx.IVirtualizer;
import com.h6ah4i.android.media.opensl.OpenSLMediaPlayerFactory;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.armatov.music.R;
import com.armatov.music.service.MultiPlayer;


import me.tankery.lib.circularseekbar.CircularSeekBar;


public class Equalizer extends Activity {

    private IEqualizer equalizer;
    private PreferenceUtil activityPerference;
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private SeekBar seekBar4;
    private SeekBar seekBar5;
    private SeekBar seekBar6;
    private SeekBar seekBar7;
    private SeekBar seekBar8;
    private SeekBar seekBar9;
    private SeekBar seekBar10;



    private CircularSeekBar bassBoostCircular;
    private CircularSeekBar virtualizerCircular;

    private IBassBoost iBassBoost;
    private IVirtualizer iVirtualizer;
    private IPresetReverb mPressetReverb;


    private TextView db1;
    private TextView db2;
    private TextView db3;
    private TextView db4;
    private TextView db5;
    private TextView db6;
    private TextView db7;
    private TextView db8;
    private TextView db9;
    private TextView db10;

    private CoordinatorLayout coordinatorLayout;

    private IBasicMediaPlayer mediaPlayer;
    private int selectedItemEqualizer;
    private String[] itemEqualizer = new String[11];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizer_layout);
        initMediaPlayer();
        initToolbarPlayer();
        itemEqualizer[0] = this.getResources().getString(R.string.equalizer_normal);
        itemEqualizer[1] = this.getResources().getString(R.string.equalizer_classical);
        itemEqualizer[2] = this.getResources().getString(R.string.equalizer_dance);
        itemEqualizer[3] = this.getResources().getString(R.string.equalizer_flat);
        itemEqualizer[4] = this.getResources().getString(R.string.equalizer_folk);
        itemEqualizer[5] = this.getResources().getString(R.string.equalizer_heavy_metal);
        itemEqualizer[6] = this.getResources().getString(R.string.equalizer_hip_hop);
        itemEqualizer[7] = this.getResources().getString(R.string.equalizer_jazz);
        itemEqualizer[8] = this.getResources().getString(R.string.equalizer_pop);
        itemEqualizer[9] = this.getResources().getString(R.string.equalizer_rock);
        itemEqualizer[10] = this.getResources().getString(R.string.equalizer_custom);




        activityPerference = PreferenceUtil.getInstance(getApplicationContext());
        selectedItemEqualizer = activityPerference.getSelectedItemOfSpinerEqualizer();
        final short lowerEqualiserBandLevel = equalizer.getBandLevelRange()[0];
        db1 = findViewById(R.id.tv_equalizer1);
        db2 = findViewById(R.id.tv_equalizer2);
        db3 = findViewById(R.id.tv_equalizer3);
        db4 = findViewById(R.id.tv_equalizer4);
        db5 = findViewById(R.id.tv_equalizer5);
        db6 = findViewById(R.id.tv_equalizer6);
        db7 = findViewById(R.id.tv_equalizer7);
        db8 = findViewById(R.id.tv_equalizer8);
        db9 = findViewById(R.id.tv_equalizer9);
        db10 = findViewById(R.id.tv_equalizer10);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int weight = displayMetrics.widthPixels;


        bassBoostCircular = findViewById(R.id.bass_boost);
        bassBoostCircular.setMax(1000);
        bassBoostCircular.setProgress(activityPerference.getBassBoostStrength());
        bassBoostCircular.setLayoutParams(new LinearLayout.LayoutParams(weight/2, ViewGroup.LayoutParams.MATCH_PARENT));


        bassBoostCircular.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                iBassBoost.setStrength((short) progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                activityPerference.setBassBoostStrength((int) seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        iBassBoost.setStrength((short) activityPerference.getBassBoostStrength());

        virtualizerCircular = findViewById(R.id.virtualizer);
        virtualizerCircular.setMax(1000);
        virtualizerCircular.setProgress(activityPerference.getVirtualizerStrength());
        virtualizerCircular.setLayoutParams(new LinearLayout.LayoutParams(weight/2, ViewGroup.LayoutParams.MATCH_PARENT));

        virtualizerCircular.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                iVirtualizer.setStrength((short) progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                activityPerference.setVirtualizerStrength((int) seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        iVirtualizer.setStrength((short) activityPerference.getVirtualizerStrength());


        seekBar1 = findViewById(R.id.equalizer1);
        seekBar1.setMax(30);
        seekBar1.setProgress((Integer.parseInt(activityPerference.getState()) - lowerEqualiserBandLevel) / 100);
        db1.setText(String.valueOf(seekBar1.getProgress() - 15));


        seekBar2 = findViewById(R.id.equalizer2);
        seekBar2.setMax(30);
        seekBar2.setProgress((Integer.parseInt(activityPerference.getState1()) - lowerEqualiserBandLevel) / 100);
        db2.setText(String.valueOf(seekBar2.getProgress() - 15));

        seekBar3 = findViewById(R.id.equalizer3);
        seekBar3.setMax(30);
        seekBar3.setProgress((Integer.parseInt(activityPerference.getState2()) - lowerEqualiserBandLevel) / 100);
        db3.setText(String.valueOf(seekBar3.getProgress() - 15));

        seekBar4 = findViewById(R.id.equalizer4);
        seekBar4.setMax(30);
        seekBar4.setProgress((Integer.parseInt(activityPerference.getState3()) - lowerEqualiserBandLevel) / 100);
        db4.setText(String.valueOf(seekBar4.getProgress() - 15));


        seekBar5 = findViewById(R.id.equalizer5);
        seekBar5.setMax(30);
        seekBar5.setProgress((Integer.parseInt(activityPerference.getState4()) - lowerEqualiserBandLevel) / 100);
        db5.setText(String.valueOf(seekBar5.getProgress() - 15));

        seekBar6 = findViewById(R.id.equalizer6);
        seekBar6.setMax(30);
        seekBar6.setProgress((Integer.parseInt(activityPerference.getState5()) - lowerEqualiserBandLevel) / 100);
        db6.setText(String.valueOf(seekBar6.getProgress() - 15));


        seekBar7 = findViewById(R.id.equalizer7);
        seekBar7.setMax(30);
        seekBar7.setProgress((Integer.parseInt(activityPerference.getState6()) - lowerEqualiserBandLevel) / 100);
        db7.setText(String.valueOf(seekBar7.getProgress() - 15));


        seekBar8 = findViewById(R.id.equalizer8);
        seekBar8.setMax(30);
        seekBar8.setProgress((Integer.parseInt(activityPerference.getState7()) - lowerEqualiserBandLevel) / 100);
        db8.setText(String.valueOf(seekBar8.getProgress() - 15));


        seekBar9 = findViewById(R.id.equalizer9);
        seekBar9.setMax(30);
        seekBar9.setProgress((Integer.parseInt(activityPerference.getState8()) - lowerEqualiserBandLevel) / 100);
        db9.setText(String.valueOf(seekBar9.getProgress() - 15));


        seekBar10 = findViewById(R.id.equalizer10);
        seekBar10.setMax(30);
        seekBar10.setProgress((Integer.parseInt(activityPerference.getState9()) - lowerEqualiserBandLevel) / 100);
        db10.setText(String.valueOf(seekBar10.getProgress() - 15));


        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initMediaPlayer();

                equalizer.setBandLevel((short) 0,
                        (short) (((seekBar.getProgress() * 100) + lowerEqualiserBandLevel)));
                db1.setText(String.valueOf(progress - 15));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                initMediaPlayer();

                saveSeekbarState();

            }
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initMediaPlayer();
                db2.setText(String.valueOf(progress - 15));

                equalizer.setBandLevel((short) 1,
                        (short) (((seekBar.getProgress() * 100) + lowerEqualiserBandLevel)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                initMediaPlayer();

                saveSeekbarState();

            }
        });
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initMediaPlayer();
                db3.setText(String.valueOf(progress - 15));

                equalizer.setBandLevel((short) 2,
                        (short) (((seekBar.getProgress() * 100) + lowerEqualiserBandLevel)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                initMediaPlayer();

                saveSeekbarState();

            }
        });
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initMediaPlayer();
                db4.setText(String.valueOf(progress - 15));

                equalizer.setBandLevel((short) 3,
                        (short) (((seekBar.getProgress() * 100) + lowerEqualiserBandLevel)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                initMediaPlayer();
                saveSeekbarState();

            }
        });
        seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initMediaPlayer();
                db5.setText(String.valueOf(progress - 15));

                equalizer.setBandLevel((short) 4,
                        (short) (((seekBar.getProgress() * 100) + lowerEqualiserBandLevel)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                initMediaPlayer();
                saveSeekbarState();

            }
        });

        seekBar6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initMediaPlayer();
                db6.setText(String.valueOf(progress - 15));

                equalizer.setBandLevel((short) 5,
                        (short) (((seekBar.getProgress() * 100) + lowerEqualiserBandLevel)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                initMediaPlayer();
                saveSeekbarState();

            }
        });
        seekBar7.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initMediaPlayer();
                db7.setText(String.valueOf(progress - 15));

                equalizer.setBandLevel((short) 6,
                        (short) (((seekBar.getProgress() * 100) + lowerEqualiserBandLevel)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                initMediaPlayer();
                saveSeekbarState();

            }
        });
        seekBar8.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initMediaPlayer();
                db8.setText(String.valueOf(progress - 15));

                equalizer.setBandLevel((short) 7,
                        (short) (((seekBar.getProgress() * 100) + lowerEqualiserBandLevel)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                initMediaPlayer();
                saveSeekbarState();

            }
        });
        seekBar9.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initMediaPlayer();
                db9.setText(String.valueOf(progress - 15));

                equalizer.setBandLevel((short) 8,
                        (short) (((seekBar.getProgress() * 100) + lowerEqualiserBandLevel)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                initMediaPlayer();
                saveSeekbarState();

            }
        });
        seekBar10.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initMediaPlayer();
                db10.setText(String.valueOf(progress - 15));

                equalizer.setBandLevel((short) 9,
                        (short) (((seekBar.getProgress() * 100) + lowerEqualiserBandLevel)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                initMediaPlayer();
                saveSeekbarState();

            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();

    }
    private void saveSeekbarState(){
        activityPerference.setCustomSeekBarState1(seekBar1.getProgress());
        activityPerference.setCustomSeekBarState2(seekBar2.getProgress());
        activityPerference.setCustomSeekBarState3(seekBar3.getProgress());
        activityPerference.setCustomSeekBarState4(seekBar4.getProgress());
        activityPerference.setCustomSeekBarState5(seekBar5.getProgress());
        activityPerference.setCustomSeekBarState6(seekBar6.getProgress());
        activityPerference.setCustomSeekBarState7(seekBar7.getProgress());
        activityPerference.setCustomSeekBarState8(seekBar8.getProgress());
        activityPerference.setCustomSeekBarState9(seekBar9.getProgress());
        activityPerference.setCustomSeekBarState10(seekBar10.getProgress());
        activityPerference.setState(String.valueOf((short) seekBar1.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState1(String.valueOf((short) seekBar2.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState2(String.valueOf((short) seekBar3.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState3(String.valueOf((short) seekBar4.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState4(String.valueOf((short) seekBar5.getProgress() * 100 + equalizer.getBandLevelRange()[0]));

        activityPerference.setState5(String.valueOf((short) seekBar6.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState6(String.valueOf((short) seekBar7.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState7(String.valueOf((short) seekBar8.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState8(String.valueOf((short) seekBar9.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState9(String.valueOf((short) seekBar10.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setSelectedItemOfSpinerEqualizer(itemEqualizer.length-1);
        selectedItemEqualizer = itemEqualizer.length-1;
    }

    private void initToolbarPlayer(){
        Toolbar toolbar = findViewById(R.id.equalizer_toolbar);
        toolbar.setBackgroundColor(ThemeStore.primaryColor(this));
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.mcab_nav_back);

        toolbar.inflateMenu(R.menu.menu_equalizer);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_equalizers) {
                    selectedItemEqualizer = activityPerference.getSelectedItemOfSpinerEqualizer();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Equalizer.this);
                    builder.setTitle(getResources().getString(R.string.equalizer));
                    builder.setSingleChoiceItems(itemEqualizer, activityPerference.getSelectedItemOfSpinerEqualizer(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (which == equalizer.getNumberOfPresets()) {
                                seekBar1.setProgress(activityPerference.getCustomSeekBarState1());
                                seekBar2.setProgress(activityPerference.getCustomSeekBarState2());
                                seekBar3.setProgress(activityPerference.getCustomSeekBarState3());
                                seekBar4.setProgress(activityPerference.getCustomSeekBarState4());
                                seekBar5.setProgress(activityPerference.getCustomSeekBarState5());
                                seekBar6.setProgress(activityPerference.getCustomSeekBarState6());
                                seekBar7.setProgress(activityPerference.getCustomSeekBarState7());
                                seekBar8.setProgress(activityPerference.getCustomSeekBarState8());
                                seekBar9.setProgress(activityPerference.getCustomSeekBarState9());
                                seekBar10.setProgress(activityPerference.getCustomSeekBarState10());

                                equalizer.setBandLevel((short) 0, (short) ((seekBar1.getProgress() - 15) * 100));
                                equalizer.setBandLevel((short) 1, (short) ((seekBar2.getProgress() - 15) * 100));
                                equalizer.setBandLevel((short) 2, (short) ((seekBar3.getProgress() - 15) * 100));
                                equalizer.setBandLevel((short) 3, (short) ((seekBar4.getProgress() - 15) * 100));
                                equalizer.setBandLevel((short) 4, (short) ((seekBar5.getProgress() - 15) * 100));
                                equalizer.setBandLevel((short) 5, (short) ((seekBar6.getProgress() - 15) * 100));
                                equalizer.setBandLevel((short) 6, (short) ((seekBar7.getProgress() - 15) * 100));
                                equalizer.setBandLevel((short) 7, (short) ((seekBar8.getProgress() - 15) * 100));
                                equalizer.setBandLevel((short) 8, (short) ((seekBar9.getProgress() - 15) * 100));
                                equalizer.setBandLevel((short) 9, (short) ((seekBar10.getProgress() - 15) * 100));

                            } else {
                                initMediaPlayer();
                                equalizer.usePreset((short) which);
                                final short lowerEqBandLvl = equalizer.getBandLevelRange()[0];
                                seekBar1.setProgress((equalizer.getBandLevel((short) 0) - lowerEqBandLvl) / 100);
                                seekBar2.setProgress((equalizer.getBandLevel((short) 1) - lowerEqBandLvl) / 100);
                                seekBar3.setProgress((equalizer.getBandLevel((short) 2) - lowerEqBandLvl) / 100);
                                seekBar4.setProgress((equalizer.getBandLevel((short) 3) - lowerEqBandLvl) / 100);
                                seekBar5.setProgress((equalizer.getBandLevel((short) 4) - lowerEqBandLvl) / 100);
                                seekBar6.setProgress((equalizer.getBandLevel((short) 5) - lowerEqBandLvl) / 100);
                                seekBar7.setProgress((equalizer.getBandLevel((short) 6) - lowerEqBandLvl) / 100);
                                seekBar8.setProgress((equalizer.getBandLevel((short) 7) - lowerEqBandLvl) / 100);
                                seekBar9.setProgress((equalizer.getBandLevel((short) 8) - lowerEqBandLvl) / 100);
                                seekBar10.setProgress((equalizer.getBandLevel((short) 9) - lowerEqBandLvl) / 100);

                            }

                            selectedItemEqualizer = which;
                            activityPerference.setSelectedItemOfSpinerEqualizer(selectedItemEqualizer);
                        }
                    });
                    builder.setPositiveButton("ok", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                return true;
            }
        });
    }
    private void initMediaPlayer() {
        if (MultiPlayer.equalizer != null) {
            equalizer = MultiPlayer.equalizer;
            iBassBoost = MultiPlayer.bassBoost;
            iVirtualizer = MultiPlayer.iVirtualizer;
       //     mPressetReverb = MultiPlayer.mPressetReverb;

        } else {
            if(mediaPlayer == null) {
                IMediaPlayerFactory factory = new OpenSLMediaPlayerFactory(getApplicationContext());
                mediaPlayer = factory.createMediaPlayer();
                equalizer = factory.createHQEqualizer();
                iBassBoost = factory.createBassBoost(mediaPlayer);
                iVirtualizer = factory.createVirtualizer(mediaPlayer);
            }

        }
    }

    @Override
    public void onBackPressed() {
        initMediaPlayer();
        activityPerference.setState(String.valueOf(
                (short) seekBar1.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState1(String.valueOf((short) seekBar2.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState2(String.valueOf((short) seekBar3.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState3(String.valueOf((short) seekBar4.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState4(String.valueOf((short) seekBar5.getProgress() * 100 + equalizer.getBandLevelRange()[0]));

        activityPerference.setState5(String.valueOf(
                (short) seekBar6.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState6(String.valueOf((short) seekBar7.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState7(String.valueOf((short) seekBar8.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState8(String.valueOf((short) seekBar9.getProgress() * 100 + equalizer.getBandLevelRange()[0]));
        activityPerference.setState9(String.valueOf((short) seekBar10.getProgress() * 100 + equalizer.getBandLevelRange()[0]));

        if(mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onBackPressed();
    }


}
