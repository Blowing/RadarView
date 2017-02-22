package com.wujie.radarview;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity {

    private ObjectAnimator animator1,animator2, animator3;
    private ImageView imageView1, imageView2, imageView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        animator1 = new ObjectAnimator();
        animator2 = new ObjectAnimator();
        animator3 = new ObjectAnimator();
        initAnimator(animator1, imageView1, 90.0f, 450.0f);

        initAnimator(animator3, imageView3, 0.0f, 360.0f);



        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Looper.prepare();

                        animator1.cancel();
                        animator2.cancel();
                        animator3.cancel();
                        timer.cancel();
                Looper.loop();


            }
        }, 6000);

    }
    private void initView() {
        imageView1 = (ImageView) findViewById(R.id.image1);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView3 = (ImageView) findViewById(R.id.image3);


     }

    private void initAnimator (ObjectAnimator animator, ImageView imageView , float f1, float f2) {
        animator = ObjectAnimator.ofFloat(imageView, "rotation", f1, f2);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());//不停顿
        animator.setRepeatCount(-1);//设置动画重复次数
        //animator.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
        animator.start();
    }
}
