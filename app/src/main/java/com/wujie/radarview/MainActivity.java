package com.wujie.radarview;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wujie.radarview.view.RaderWheelView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.check_layout_top_1)
    View top1Layout;
    @BindView(R.id.check_layout_top_2)
    View top2Layout;
    @BindView(R.id.check_radar_view)
    com.wujie.radarview.view.RadarView mRadarView;
    @BindView(R.id.check_radar_wheel_view)
    RaderWheelView mRaderWheelView;
    @BindView(R.id.wifi_acclerate_btn)
    Button acclerateBtn;
    @BindView(R.id.check_layout_animator)
    RelativeLayout checkLayoutAnimator;
    @BindView(R.id.wifi_acclerate_tv_tip)
    TextView wifiAcclerateTvTip;
    @BindView(R.id.wifi_acclerate_tv_check)
    TextView mCheckTips;
    @BindView(R.id.check_layout_acclerate)
    RelativeLayout checkLayoutAcclerate;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.wifi_acclerate_tv_optimze_core)
    TextView wifiAcclerateTvOptimzeCore;
    @BindView(R.id.checkd_layout_animator)
    RelativeLayout checkdLayoutAnimator;
    @BindView(R.id.wifi_optimize_tv_tip)
    TextView optimzeCore;
    @BindView(R.id.check_layout_optimize)
    RelativeLayout checkLayoutOptimize;
    @BindView(R.id.check_layout_top)
    RelativeLayout checkLayoutTop;
    @BindView(R.id.check_tv_score)
    TextView scoreTv;

    private ObjectAnimator animator1 = new ObjectAnimator(), animator2 = new ObjectAnimator(), animator3 = new ObjectAnimator();


    int progress = 100;
    private int core;
    private int position = 0;
    private int count = 0;
    private int position1 = 0;
    private int hasShowCheckImageViewCount = 0;

    private int during = 300;//检测时间间隔

    private int mChannelID;
    private String optimzeNames[];
    private String[] checkTips;

    private Subscription checkProcessSub, checkViewDoneSub;

    private Random mRandom = new Random();

    private String result = "";

    private boolean isToNext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRadarView.start();
        showCheckProcess(11);

    }


    public void showCheckProcess(int chan_2g_sta) {
        int time = 0;
        chan_2g_sta = 11;
        switch (chan_2g_sta) {
            case -1:
            case 0:
                core = 50;
                chan_2g_sta = 11;
                break;
            case 1:
                core = 75;
                chan_2g_sta = 15;
                break;
            case 2:
                core = 100;
                chan_2g_sta = 20;
                break;
            case 3:
                core = 100;
                chan_2g_sta = 20;
                break;
            case 11:
                core = 50;
                break;
            case 12:
                core = 60;
                break;
            case 13:
                core = 65;
                break;
            case 14:
                core = 70;
                break;
            case 15:
                core = 75;
                break;
            case 16:
                core = 80;
                break;
            case 17:
                core = 85;
                break;
            case 18:
                core = 90;
                break;
            case 19:
                core = 95;
                break;
            case 20:
                core = 100;
                break;
            default:
                core = 50;
                chan_2g_sta = 11;
                break;
        }
        mChannelID = chan_2g_sta;
        if (chan_2g_sta < 4) {
            time = 34 - (4 - chan_2g_sta);
        } else {
            time = 34 - (21 - chan_2g_sta);
        }
        final int finalCore = core;


        final int finalTime = time;
         Observable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(34)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        int id = aLong.intValue();

                        //if (mCheckTips != null) {
                           // mCheckTips.setText(checkTips[id]);
                            //mProgressbar.setProgress(id+1);
                            if (id >= finalTime) {
//                                if (finalCore == 100) {
//                                    toNextActivity(WifiAcclerateFullMarkActivity.class);
//                                }
                                if (progress > finalCore) {
                                    if (progress > 60) {
                                        progress = progress - (mRandom.nextInt(5) + 1);

                                        showBackground(progress);
                                        scoreTv.setText(String.valueOf(progress));
                                        progress = (progress + 5) / 5 * 5 - 5;
                                    } else {
                                        progress = progress - (mRandom.nextInt(10) + 1);

                                        showBackground(progress);
                                        scoreTv.setText(String.valueOf(progress));
                                        progress = progress / 10 * 10;
                                    }

                                } else {

                                    if (core <= 70) {
                                        showBackground(50);
                                    } else if (core > 70 && core <= 90) {
                                        showBackground(75);
                                    } else {
                                        showBackground(100);
                                    }
                                    mRadarView.stop();
                                    mRadarView.setStart(0);
                                    mRadarView.setStopRoate(true);
                                    mRaderWheelView.stop();
                                    scoreTv.setText(String.valueOf(finalCore));
                                    mRadarView.setCodeangel(finalCore * 360 / 100);


                                    // mProgressbar.setVisibility(View.INVISIBLE);
                                    mCheckTips.setVisibility(View.INVISIBLE);
                                    if (finalCore != 100) {
                                        mRadarView.start();
                                        acclerateBtn.setVisibility(View.VISIBLE);
                                        // tipTv.setVisibility(View.VISIBLE);
                                    } else {
                                        mRadarView.stop();
                                    }
                                }
                            }
                        //}


                    }
                }, throwable -> {
                    Log.e("raderview", throwable.getMessage()+"error");
                });

    }

    private void initAnimator(ObjectAnimator animator, ImageView imageView, float f1, float f2, int duration) {
        animator = ObjectAnimator.ofFloat(imageView, "rotation", f1, f2);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());//不停顿
        // animator.setRepeatMode(ValueAnimator.INFINITE);
        animator.setRepeatCount(-1);//设置动画重复次数
//        //animator.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
        animator.start();
    }


    public void showBackground(int core) {
        if (core > 90 && core <= 100) {
            top1Layout.setAlpha(1);
            top2Layout.setAlpha(1);
            top2Layout.setBackgroundColor(getResources().getColor(R.color.wifi_acclerate_green));
            top1Layout.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else if (core >= 75 && core <= 90) {
            float topAlpha = (float) ((core - 75) * 0.04);
            top2Layout.setAlpha(topAlpha);
            top1Layout.setAlpha(1 - topAlpha);
            top1Layout.setBackgroundColor(getResources().getColor(R.color.wifi_acclerate_orange));

        } else {
            float topAlpha = (float) ((core - 50) * 0.04);
            top2Layout.setAlpha(topAlpha);
            top1Layout.setAlpha(1 - topAlpha);
            top2Layout.setBackgroundColor(getResources().getColor(R.color.wifi_acclerate_orange));
            top1Layout.setBackgroundColor(getResources().getColor(R.color.wifi_acclerate_red));

        }
    }


    public void showOptimzeAnimator(int channelID) {

        /**
         * 初始化优化的动画
         */
        animator1 = new ObjectAnimator();
        animator2 = new ObjectAnimator();
        animator3 = new ObjectAnimator();
        initAnimator(animator1, image1, 90.0f, 450.0f, 500);
        initAnimator(animator3, image3, 360.0f, 0.0f, 1000);
        optimzeCore.setText(String.valueOf(core));


        /**
         * 这里是优化以后的自动加分
         */
        progress = core;
        Observable.interval(500, 500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(20 - channelID + 1)
                .subscribe(aLong -> {
                    if (progress < 100) {
                        if (progress == 50) {
                            int score = progress + mRandom.nextInt(10) + 1;
                            showBackground(score);
                            wifiAcclerateTvOptimzeCore.setText(String.valueOf(score));
                            progress = progress + 10;
                        } else if (progress >= 60 && progress < 100) {
                            int score = progress + mRandom.nextInt(5) + 1;
                            showBackground(score);
                            wifiAcclerateTvOptimzeCore.setText(String.valueOf(score));
                            progress = progress + 5;
                        } else {
                            showBackground(99);
                            wifiAcclerateTvOptimzeCore.setText(String.valueOf(99));
                            progress = progress + 5;
                        }
                    } else {
                        showBackground(100);
                        wifiAcclerateTvOptimzeCore.setText(String.valueOf(100));
                        animator1.cancel();
                        animator2.cancel();
                        animator3.cancel();
                        image1.setVisibility(View.GONE);
                        image3.setVisibility(View.GONE);

                        //timer.cancel();

                    }
                }, throwable -> {
                    Log.e("throwablecore", throwable.getMessage());
                    // LogUtil.e("throwablecore", throwable.getMessage());
                });

    }


    @OnClick(R.id.wifi_acclerate_btn)
    public void onClick() {
        checkLayoutAcclerate.setVisibility(View.GONE);
        checkLayoutOptimize.setVisibility(View.VISIBLE);
        showOptimzeAnimator(mChannelID);
        showOptimzeAnimator(11);
    }
}
