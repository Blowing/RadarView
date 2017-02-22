package com.wujie.radarview.view;

/**
 * Created by wujie on 2017/2/22.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wujie.radarview.Utils;


/**
 * Created by wujie on 2016/12/16.
 */
public class RaderWheelView extends FrameLayout {

    private Context mContext;
    private int mWidth;
    private int mHeight;
    private Paint mPaintLine;
    private int bigR = 100;
    private int smallR = 60;
    private int stoke = 7;
    private int lineWidth = 1;
    private boolean stopRoate = false;
    RectF rectF;

    public RaderWheelView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        initPaint();
    }

    public RaderWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context.getApplicationContext();
        initPaint();
    }


    private void initRadius() {
        bigR = Utils.dip2px(mContext, bigR);
        smallR = Utils.dip2px(mContext, smallR);
        stoke = Utils.dip2px(mContext, stoke);
        lineWidth = Utils.px2dip(mContext, lineWidth);

    }

    private void initPaint() {
        // TODO Auto-generated method stub
        setBackgroundColor(Color.TRANSPARENT);
        initRadius();
        //宽度=1，抗锯齿，描边效果的白色画笔
        mPaintLine = new Paint();
        mPaintLine.setStrokeWidth(lineWidth);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(0x60FFFFFF);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        rectF = new RectF(mWidth / 2 - bigR + 20, mHeight / 2 - bigR + 20, mWidth / 2 + bigR - 20, mHeight / 2 + bigR - 20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub

        if (!stopRoate) {//停止转圈的时候，隐藏掉中间的小圈
            canvas.drawCircle(mWidth / 2, mHeight / 2, smallR, mPaintLine);
            canvas.drawLine(mWidth / 2 - smallR, mHeight / 2, mWidth /2 + smallR, mHeight / 2 , mPaintLine);
            canvas.drawLine(mWidth / 2 , mHeight / 2 - smallR, mWidth /2 , mHeight / 2+ smallR , mPaintLine);
        }
        canvas.drawCircle(mWidth / 2, mHeight / 2, bigR, mPaintLine);
        super.onDraw(canvas);
    }

    public void stop() {
        stopRoate = true;
        invalidate();
    }
}

