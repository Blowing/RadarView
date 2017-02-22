package com.wujie.radarview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wujie.radarview.Utils;


/**
 * Created by wujie on 2016/11/1.
 */
public class RadarView extends FrameLayout {

    private Context mContext;
    //    private int viewSize = 800;
    private int mWidth;
    private int mHeight;
    private Paint mPaintLine;
    private Paint mPaintSector;
    private Paint mPaintArc;
    private Paint mPaintArcLine;
    public boolean isstart = false;
    private ScanThread mThread;
    private Paint mPaintPoint;
    //旋转效果起始角度
    private int start = 0;
    private int Istart = 0;
    private int bigR = 100;
    private int smallR = 60;
    private int stoke = 7;
    private int lineWidth = 1;

    private int[] point_x;

    public void stopThread() {
        stop();
        if(mThread != null) {
            mThread = null;
        }
    }

    public void setStart(int start) {
        this.Istart = start;
    }

    private int[] point_y;

    private Matrix matrix = new Matrix();
    private boolean stopRoate = false;

    private int codeangel = 0;

    public final static int CLOCK_WISE = 1;
    public final static int ANTI_CLOCK_WISE = -1;
    RectF rectF;


    public void setStopRoate(boolean stopRoate) {
        this.stopRoate = stopRoate;
    }

    public void setCodeangel(int codeangel) {
        this.codeangel = codeangel;
    }


    @IntDef({CLOCK_WISE, ANTI_CLOCK_WISE})
    public @interface RADAR_DIRECTION {

    }

    //默认为顺时针呢
    private final static int DEFAULT_DIERCTION = CLOCK_WISE;

    //设定雷达扫描方向
    private int direction = DEFAULT_DIERCTION;

    private boolean threadRunning = true;

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context.getApplicationContext();
        initPaint();
    }

    public RadarView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
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
        mPaintLine.setColor(0x40FFFFFF);

        //画扇形的画笔
        mPaintSector = new Paint();

        mPaintSector.setAntiAlias(true);
        RadialGradient radialGradient = new RadialGradient(getWidth() / 2, getHeight() / 2, bigR,
                new int[]{Color.TRANSPARENT, 0x1eFFFFFF}, new float[]{0.0f, 1f}
                , Shader.TileMode.CLAMP);
        mPaintSector.setShader(radialGradient);


        //画白色打分宽圆弧
        mPaintArc = new Paint();
        mPaintArc.setAntiAlias(true);
        mPaintArc.setStrokeWidth(stoke);
        mPaintArc.setStyle(Paint.Style.STROKE);

        //绘制旋转圆弧上的亮线
        mPaintArcLine = new Paint();
        mPaintArcLine.setAntiAlias(true);
        mPaintArcLine.setColor(0x7affffff);
        mPaintArcLine.setStyle(Paint.Style.STROKE);
        mPaintArcLine.setStrokeWidth(lineWidth);

        //随机生成的点，模拟雷达扫描结果

        //白色实心画笔
       /* mPaintPoint = new Paint();
        mPaintPoint.setColor(Color.WHITE);
        mPaintPoint.setStyle(Paint.Style.FILL);*/
//        point_x = UtilTools.Getrandomarray(15, 300);
//        point_y = UtilTools.Getrandomarray(15, 300);

    }

//    public void setViewSize(int size) {
//        this.viewSize = size;
//        setMeasuredDimension(viewSize, viewSize);
//    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        // TODO Auto-generated method stub
//        setMeasuredDimension(viewSize, viewSize);
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        rectF = new RectF(mWidth / 2 - bigR + 20, mHeight / 2 - bigR + 20, mWidth / 2 + bigR - 20, mHeight / 2 + bigR - 20);

    }

    public void start() {
        mThread = new ScanThread();
        mThread.setName("radar");
        mThread.start();
        threadRunning = true;
        isstart = true;
    }

    public void stop() {
        if (isstart) {
            threadRunning = false;
            isstart = false;
            mContext = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub




        if (stopRoate) { //停止转圈的时候，开始绘制打分进度先绘制一个浅色的圆，然后再绘制一个进度的圆弧
            if (Istart <= 360) {
                mPaintArc.setColor(0x7aFFFFFF);
                canvas.drawArc(rectF, -90, Istart, false, mPaintArc);
            } else if (Istart > 360 && Istart <= 360 + codeangel) {
                mPaintArc.setColor(0x7aFFFFFF);
                canvas.drawArc(rectF, -90, 360, false, mPaintArc);
                mPaintArc.setColor(0xc1FFFFFF);
                canvas.drawArc(rectF, -90, Istart - 360, false, mPaintArc);
            } else {
                mPaintArc.setColor(0x7aFFFFFF);
                canvas.drawArc(rectF, -90, 360, false, mPaintArc);
                mPaintArc.setColor(0xc1FFFFFF);
                canvas.drawArc(rectF, -90, codeangel, false, mPaintArc);
            }
        }


        if (!stopRoate) {
            //根据matrix中设定角度，不断绘制shader,呈现出一种扇形扫描效果
            canvas.save();
            int sweep = 1;
            canvas.rotate(start, mWidth / 2, mHeight / 2);
            RectF rect = new RectF(mWidth / 2 - bigR, mHeight / 2 - bigR, mWidth / 2 + bigR, mHeight / 2 + bigR);
            canvas.drawArc(rect, 0, 90, true, mPaintSector);
            canvas.drawArc(rect, 0, 90, false, mPaintArcLine);
            canvas.restore();
            canvas.setMatrix(matrix);
        }
        super.onDraw(canvas);
    }

    public void setDirection(@RADAR_DIRECTION int direction) {
        if (direction != CLOCK_WISE && direction != ANTI_CLOCK_WISE) {
            throw new IllegalArgumentException("Use @RADAR_DIRECTION constants only!");
        }
        this.direction = direction;
    }

    protected class ScanThread extends Thread {

//        private RadarView view;
//
//        public ScanThread(RadarView view) {
//            // TODO Auto-generated constructor stub
//            this.view = view;
//        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (threadRunning) {
                if (isstart) {
//                    view.post(new Runnable() {
//                        public void run() {
                    start = start + 5;
                    Istart= Istart + 5;
                    matrix = new Matrix();
                    //设定旋转角度,制定进行转转操作的圆心
//                            matrix.postRotate(start, viewSize / 2, viewSize / 2);
//                            matrix.setRotate(start,viewSize/2,viewSize/2);
                    matrix.preRotate(direction * start, mWidth / 2, mHeight / 2);
                    //invalidate();
                    postInvalidate();

//                        }
//                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
