package com.mountreachsolution.foodizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {
    ImageView ivLogo;
    TextView tvTitle;

    Animation animFadeIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivLogo =findViewById(R.id.ivSplashLogo);
        tvTitle=findViewById(R.id.tvSplashTitle);

        animFadeIn = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.fadein);
        ivLogo.setAnimation(animFadeIn);
       //class_name Object_name=new ConstructorName();
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);
            }
        },3000);
    }
}
//ObjectName=method_name(R.id.IdName);