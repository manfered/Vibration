package com.gilasak.larzeafzar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity implements Animation.AnimationListener {

    ImageView splashScreenImageLogo;
    Animation animationSplash1;
    Animation animationSplash2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //getWindow().setFormat(PixelFormat.RGB_565);

        splashScreenImageLogo = (ImageView) findViewById(R.id.splashScreenImageLogo);
        animationSplash2 = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.splash_fade_out);
        animationSplash1 = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.splash_fade_in);
        animationSplash1.setAnimationListener(this);
        splashScreenImageLogo.startAnimation(animationSplash1);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intent = new Intent(SplashScreen.this , MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.main_activity_in_anim , R.anim.splash_fade_out);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
