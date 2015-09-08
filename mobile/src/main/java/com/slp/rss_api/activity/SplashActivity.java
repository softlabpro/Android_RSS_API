package com.slp.rss_api.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.slp.rss_api.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Displays a splash screen image before starting the application.
 */
public class SplashActivity extends Activity {
    // How long in milliseconds to wait for the interstitial to load.
    private static final int WAIT_TIME = 1000;

    private Timer waitTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView image = new ImageView(this);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setImageResource(R.drawable.splash);
        setContentView(image);

        waitTimer = new Timer();
        waitTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                SplashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // The interstitial didn't load in a reasonable amount of time. Stop waiting for the
                        // interstitial, and start the application.
                        startMainActivity();
                    }
                });
            }
        }, WAIT_TIME);
    }

    /**
     * Starts the application's {@link HomeActivity}.
     */
    private void startMainActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}