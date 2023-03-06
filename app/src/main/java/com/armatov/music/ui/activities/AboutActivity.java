package com.armatov.music.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.armatov.music.BuildConfig;

import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.armatov.music.R;
import com.armatov.music.ui.activities.base.AbsBaseActivity;
import com.armatov.music.ui.activities.intro.AppIntroActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("FieldCanBeLocal")
public class AboutActivity extends AbsBaseActivity implements View.OnClickListener {

    private static String GITHUB = "https://github.com/MaxFour/Music-Player";

    private static String WebMoney="https://www.webmoney.ru/eng/";
    private static String YandexMoney="https://money.yandex.ru/to/410015372205898";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_version)
    TextView appVersion;
    @BindView(R.id.intro)
    LinearLayout intro;
    @BindView(R.id.write_an_email)
    LinearLayout writeAnEmail;
    @BindView(R.id.webmoney)
    LinearLayout webMoney;

    @BindView(R.id.dollar_button)
    Button dollarButton;
    @BindView(R.id.yandex_money)
    LinearLayout yandexMoney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setDrawUnderStatusbar();
        ButterKnife.bind(this);
        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();

        setUpViews();
    }

    private void setUpViews() {
        setUpToolbar();
        setUpAppVersion();
        setUpOnClickListeners();
    }

    private void setUpToolbar() {
        toolbar.setBackgroundColor(ThemeStore.primaryColor(this));
        toolbar.setTitleTextAppearance(this, R.style.ProductSansTextAppearace);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpAppVersion() {
        appVersion.setText(BuildConfig.VERSION_NAME);
    }

    private void setUpOnClickListeners() {
        intro.setOnClickListener(this);

        writeAnEmail.setOnClickListener(this);
        webMoney.setOnClickListener(this);
        dollarButton.setOnClickListener(this);
        yandexMoney.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == intro) {
            startActivity(new Intent(this, AppIntroActivity.class));
        } else if (v == writeAnEmail) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:armatov.jenishbek@gmail.com"));
            intent.putExtra(Intent.EXTRA_EMAIL, "armatov.jenishbek@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Music");
            startActivity(Intent.createChooser(intent, "E-Mail"));
        } else if (v == webMoney) {
            openUrl(WebMoney);
        } else if (v == dollarButton) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("WMZ", "Z841286949719");
            clipboard.setPrimaryClip(clipData);
            Toast.makeText(getApplicationContext(), R.string.clipboard_dollar_wallet_number_copied, Toast.LENGTH_LONG).show();
        } else if (v == yandexMoney) {
            showVideo();
        }
    }

    private void showVideo() {
            if (MainActivity.mRewardedAd != null) {
                MainActivity.mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {


                    }
                });
            }

    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


}
