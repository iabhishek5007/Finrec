package com.bangalore.bosankus.finrec.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bangalore.bosankus.finrec.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;


public class SplashActivity extends AppCompatActivity {

    private boolean SIGNIN_STATE;

    private FirebaseAuth mFirebaseAuth;

    private Trace splashScreenTrace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        setContentView(R.layout.activity_splash);

        final Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        splashScreenTrace = FirebasePerformance.getInstance().newTrace("splashScreenTrace");

        mFirebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        splashScreenTrace.start();

        checkForAuthentication();
    }

    private void checkForAuthentication() {

        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long l) {

                if (mFirebaseAuth.getCurrentUser() != null) {
                    SIGNIN_STATE = true;
                }
                else {
                    SIGNIN_STATE = false;
                }

            }

            @Override
            public void onFinish() {

                if (SIGNIN_STATE) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, SigninActivity.class));
                    finish();
                }

            }
        }.start();
    }
}
