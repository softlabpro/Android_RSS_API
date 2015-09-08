package com.slp.rss_api.activity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.slp.rss_api.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Displays an AdSense interstitial before exiting the application.
 */
public class ExitActivity extends Activity {
    // How long in milliseconds to wait for the interstitial to load.
    private static final int WAIT_TIME = 10000;

    private InterstitialAd interstitial;
    private Timer waitTimer;
    private boolean interstitialCanceled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView image = new ImageView(this);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setImageResource(R.drawable.splash);
        setContentView(image);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // If the interstitial was canceled due to a timeout or an app being sent to the background,
                // don't show the interstitial.
                if (!interstitialCanceled) {
                    waitTimer.cancel();
                    interstitial.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The interstitial failed to load. Start the application.
                finish();
            }
        });
        interstitial.loadAd(new AdRequest.Builder().build());

        waitTimer = new Timer();
        waitTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                interstitialCanceled = true;
                ExitActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // The interstitial didn't load in a reasonable amount of time. Stop waiting for the
                        // interstitial, and start the application.
                        finish();
                    }
                });
            }
        }, WAIT_TIME);
    }

    @Override
    public void onPause() {
        // Flip the interstitialCanceled flag so that when the user comes back they aren't stuck inside
        // the splash screen activity.
        waitTimer.cancel();
        interstitialCanceled = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (interstitial.isLoaded()) {
            // The interstitial finished loading while the app was in the background. It's up to you what
            // the behavior should be when they return. In this example, we show the interstitial since
            // it's ready.
            interstitial.show();
        } else if (interstitialCanceled) {
            // There are two ways the user could get here:
            //
            // 1. After dismissing the interstitial
            // 2. Pressing home and returning after the interstitial finished loading.
            //
            // In either case, it's awkward to leave them in the splash screen activity, so just start the
            // application.
            finish();
        }
    }

}