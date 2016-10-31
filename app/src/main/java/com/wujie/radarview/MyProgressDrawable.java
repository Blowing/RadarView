package com.wujie.radarview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.LinearInterpolator;

/**
 * Created by wujie on 2016/10/31.
 */
public class MyProgressDrawable extends Drawable implements Animatable {
    private static final int SCAN_DURATION = 800; /* 扫描频率，旋转一周时间 */

    public static final int SCANNING = 0;
    public static final int SCAN_SUCCESS = 1;
    public static final int SCAN_ERROR = 2;
    private int mState = SCANNING;

    private static final String COLOR_SUCCESS = "#2CBE1B"; /* 绿色 */
    private static final String COLOR_ERROR = "#FF9400"; /* 橙色 */

    private Context context;

    private boolean mRunning;

    private ObjectAnimator mObjectAnimatorAngle; /* 旋转动画 */
    private float mCurrAngle; /* 旋转偏移角度 */

    private Paint paint;
    private int width, height;

    public MyProgressDrawable(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true); /* 消除锯齿 */
        setupAnimations();
    }

    @Override
    public void draw(Canvas canvas) {
        switch (mState) {
            case SCANNING:
                paint.setColor(Color.parseColor(COLOR_SUCCESS));
                paint.setStyle(Paint.Style.FILL); /* 设置实心 */
			/* 扫描框 */
                Bitmap pb_scan = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.pb_scan);
                pb_scan = Bitmap.createScaledBitmap(pb_scan, width, height, true);
                Matrix matrix = new Matrix();
                matrix.postRotate(mCurrAngle % 360f, pb_scan.getWidth() / 2,
                        pb_scan.getHeight() / 2);
                canvas.drawBitmap(pb_scan, matrix, paint);
                break;
            case SCAN_SUCCESS:
                paint.setColor(Color.parseColor(COLOR_SUCCESS));
                paint.setStyle(Paint.Style.FILL); /* 设置实心 */
                break;
            case SCAN_ERROR:
                paint.setColor(Color.parseColor(COLOR_ERROR));
                paint.setStyle(Paint.Style.FILL); /* 设置实心 */
                break;
            default:
                break;
        }

		/* 中心圆 */
        int centre = width / 2; /* 获取圆心的x坐标 */
        int radius = (int) (centre * 4 / 5); /* 圆环的半径 */
        canvas.drawCircle(centre, centre, radius, paint); /* 画出圆 */

		/* 中心图标 */
        Rect rect = new Rect(centre - radius, centre - radius, centre + radius,
                centre + radius);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.pb_icon1);
        canvas.drawBitmap(bitmap, null, rect, paint);

    }

    private Property<MyProgressDrawable, Float> mAngleProperty = new Property<MyProgressDrawable, Float>(
            Float.class, "angle") {
        @Override
        public Float get(MyProgressDrawable object) {
            return object.getCurrentGlobalAngle();
        }

        @Override
        public void set(MyProgressDrawable object, Float value) {
            object.setCurrentGlobalAngle(value);
        }
    };

    private void setupAnimations() {
        mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty,
                360f);
        mObjectAnimatorAngle.setInterpolator(new LinearInterpolator());
        mObjectAnimatorAngle.setDuration(SCAN_DURATION);
        mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrAngle = currentGlobalAngle;
        invalidateSelf();
    }

    public float getCurrentGlobalAngle() {
        return mCurrAngle;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        width = bounds.right - bounds.left;
        height = bounds.bottom - bounds.top;
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        mRunning = true;
        mObjectAnimatorAngle.start();
        invalidateSelf();
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        mRunning = false;
        mObjectAnimatorAngle.cancel();
        invalidateSelf();
    }

    public void testStart() {
        mRunning = true;
        invalidateSelf();
    }

    public int getmState() {
        return mState;
    }

    public void setmState(int mState) {
        this.mState = mState;
        switch (mState) {
            case SCANNING:
                start();
                break;
            case SCAN_SUCCESS:
                stop();
                break;
            default:
                break;
        }
        invalidateSelf();
    }
}