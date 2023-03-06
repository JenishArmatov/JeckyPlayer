package com.armatov.music.visualizermusicplayer;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.armatov.music.App;
import com.armatov.music.ui.activities.base.AbsMusicServiceActivity;
import com.armatov.music.util.PreferenceUtil;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.armatov.music.R;
import com.armatov.music.adapter.VisualizerChangeAdapter;
import com.armatov.music.ui.activities.base.AbsBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChangeVisualization extends AbsMusicServiceActivity {
    private Unbinder unbinder;

    @BindView(R.id.visualizer_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.visualizer_toolbar)
    Toolbar toolbar;

    @BindView(R.id.background)
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_visualization);

        unbinder = ButterKnife.bind(this, this);
        constraintLayout.setBackgroundColor(ThemeStore.primaryColor(getApplicationContext()));

        toolbar.setBackgroundColor(ThemeStore.primaryColor(this));
        toolbar.setTitleTextAppearance(App.getInstance(), R.style.ProductSansTextAppearace);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.scrollToPositionWithOffset(PreferenceUtil.getInstance(getApplicationContext()).getCheckedItem(), 0);


        VisualizerChangeAdapter adapter = new VisualizerChangeAdapter(this, mRewardedAd);
        recyclerView.setAdapter(adapter);

    }

}