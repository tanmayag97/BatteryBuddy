package com.example.forest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView innerCircle = findViewById(R.id.innerCircle);
        ImageView outerCircle = findViewById(R.id.outerCircle);
        ImageView energyBolt = findViewById(R.id.energyBolt);

        Animation inner = new ScaleAnimation(1.5f,1.2f,1.5f,1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,  0.5f);
        inner.setDuration(2000);
        inner.setFillAfter(true);
        inner.setRepeatCount(Animation.INFINITE);
        inner.setRepeatMode(Animation.REVERSE);

        Animation outer = new ScaleAnimation(1.1f,0.8f,1.1f,0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,  0.5f);
        outer.setDuration(1700);
        outer.setFillAfter(true);
        outer.setRepeatCount(Animation.INFINITE);
        outer.setRepeatMode(Animation.REVERSE);

        outerCircle.startAnimation(outer);
        innerCircle.startAnimation(inner);
        energyBolt.animate().translationYBy(120f).setDuration(2000);

        int SPLASH_SCREEN_TIME_OUT = 5000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,
                        MainScreenActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

}
