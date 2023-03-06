package com.armatov.music.adapter;


import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.armatov.music.util.PreferenceUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.armatov.music.R;

import com.armatov.music.ui.activities.MainActivity;
import com.armatov.music.visualizermusicplayer.Visualizer.Player;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class VisualizerChangeAdapter extends RecyclerView.Adapter<VisualizerChangeAdapter.ViewHolder> {


    protected final AppCompatActivity activity;
    private int pos;


    public VisualizerChangeAdapter(AppCompatActivity activity, RewardedAd rewardedAd) {
        super();
        this.activity = activity;



    }


    private void showVideo(){
        if (MainActivity.mRewardedAd != null) {
            MainActivity.mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {


                    long time = getTime();
                    PreferenceUtil.getInstance(activity.getApplicationContext()).setTimeOfWatchVideo(time, pos);
                    PreferenceUtil.getInstance(activity.getApplicationContext()).setCheckedItem(pos);
                    Player.checkedItem = pos;
                    activity.onBackPressed();
                }
            });
        } else {

            showDialogWindow();
        }
    }

    private void showDialogWindow() {
        loadAd();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.check_the_internet_connection);  // заголовок

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setCancelable(true);
        final AlertDialog dlg = builder.create();
        dlg.show();

    }

    public void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(activity.getApplicationContext(), "ca-app-pub-3760122179679469/5883570196",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.getMessage());
                        Toast toast = Toast.makeText(activity.getApplicationContext(),
                                "Check connection! Reward video not ready!", Toast.LENGTH_SHORT);
                        MainActivity.mRewardedAd = null;
                        toast.show();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        MainActivity.mRewardedAd = rewardedAd;
                        Toast toast = Toast.makeText(activity.getApplicationContext(),
                                "Ad was loaded", Toast.LENGTH_SHORT);
                        toast.show();
                        Log.d("TAG", "Ad was loaded.");
                    }
                });
        if(MainActivity.mRewardedAd != null){
            MainActivity.mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d("", "Ad was shown.");
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.d("TAG", "Ad failed to show.");
                    MainActivity.mRewardedAd = null;
                    loadAd();

                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d("TAG", "Ad was dismissed.");
                    MainActivity.mRewardedAd = null;

                    loadAd();

                }
            });
        }
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.visualizer_card, parent, false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int weight = displayMetrics.widthPixels;


        view.setLayoutParams(new ViewGroup.LayoutParams(weight,height / 3));

        return new ViewHolder(view);
    }
    private long getTime(){
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        String timeText = dateText + timeFormat.format(currentDate);
        return Long.parseLong(timeText);
    }


    //   214316 2206214316
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        int resIDBitmap = activity.getResources().getIdentifier("image" + holder.getAdapterPosition(),
                "drawable", activity.getPackageName());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int weight = displayMetrics.widthPixels;
        Picasso.get()
                .load(resIDBitmap)
                .resize(weight,height/3)
                .into(holder.imageView);
        long oldTime = PreferenceUtil.getInstance(activity.getApplicationContext()).getTimeOfWatchVideo(holder.getAdapterPosition()) + 1000000;
        long realTime = getTime();
        holder.rewardedVideo.setVisibility(View.GONE);
        if (realTime > oldTime){
            if(position > 1){
                Picasso.get()
                        .load(R.drawable.watch_video_icon)
                        .resize(weight/5,height/8)
                        .into(holder.rewardedVideo);
                holder.rewardedVideo.setVisibility(View.VISIBLE);

                Log.d("///d","" + position);
            }

        }

        //      canvas.drawBitmap(BarGraphRenderer.bitmapGalaxy,100,100,new Paint());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long oldTime = PreferenceUtil.getInstance(activity.getApplicationContext()).getTimeOfWatchVideo(holder.getAdapterPosition()) + 1000000;
                long realTime = getTime();
                pos = holder.getAdapterPosition();

                if(holder.getAdapterPosition() == 0 || holder.getAdapterPosition() == 1){
                    Player.checkedItem = holder.getAdapterPosition();
                    PreferenceUtil.getInstance(activity.getApplicationContext()).setCheckedItem(holder.getAdapterPosition());
                    activity.onBackPressed();

                }
                if(holder.getAdapterPosition() > 1){
                    if (realTime > oldTime){
                        showVideo();
                    }else {
                        Player.checkedItem = holder.getAdapterPosition();
                        activity.onBackPressed();
                        PreferenceUtil.getInstance(activity.getApplicationContext()).setCheckedItem(holder.getAdapterPosition());
                    }
                }



                // Log.d("D///","onClick" +  holder.getAdapterPosition());
            }

        });


    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public ImageView rewardedVideo;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.image_view);
            rewardedVideo =  itemView.findViewById(R.id.video);

        }
    }



    @Override
    public int getItemCount() {
        return 15;
    }

}