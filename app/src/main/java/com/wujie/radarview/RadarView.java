package com.wujie.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by wujie on 2016/10/31.
 */
public class RadarView extends FrameLayout {

    private Context mContext;
    //    private int viewSize = 800;
    private int mWidth;
    private int mHeight;
    private Paint mPaintLine;
    private Paint mPaintCircle;
    private Paint mPaintSector;
    private Paint mPaintArc;
    public boolean isstart = false;
    private ScanThread mThread;
    private Paint mPaintPoint;
    //旋转效果起始角度
    private int start = 0;
    private int Istart = 0;
    private int bigR = 200;
    private int smallR = 120;

    private int[] point_x;

    public void setStart(int start) {
        this.Istart = start;
    }

    private int[] point_y;

    private Shader mShader;

    private Matrix matrix;
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
        mContext = context;
        initPaint();
    }

    public RadarView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        initPaint();

    }

    private void initPaint() {
        // TODO Auto-generated method stub
        setBackgroundColor(Color.TRANSPARENT);

        //宽度=5，抗锯齿，描边效果的白色画笔
        mPaintLine = new Paint();
        mPaintLine.setStrokeWidth(1);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(0x40FFFFFF);

        //宽度=5，抗锯齿，描边效果的浅绿色画笔
        mPaintCircle = new Paint();
        mPaintCircle.setStrokeWidth(5);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.FILL);
        mPaintCircle.setColor(Color.GREEN);

        //暗绿色的画笔
        mPaintSector = new Paint();
        mPaintSector.setColor(0x9D00ff00);
        mPaintSector.setAntiAlias(true);
        mShader = new SweepGradient(mWidth / 2, mWidth / 2, Color.TRANSPARENT, Color.RED);
        mPaintSector.setShader(mShader);

        //白色实心画笔
        mPaintPoint = new Paint();
        mPaintPoint.setColor(Color.WHITE);
        mPaintPoint.setStyle(Style.FILL);

        mPaintArc = new Paint();
        mPaintArc.setAntiAlias(true);
        mPaintArc.setStrokeWidth(12);
        mPaintArc.setStyle(Paint.Style.STROKE);

        //随机生成的点，模拟雷达扫描结果
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
        rectF = new RectF(mWidth / 2 - bigR+20, mHeight / 2 - bigR+20, mWidth / 2 + bigR-20, mHeight / 2 + bigR-20);

    }

    public void start() {
        mThread = new ScanThread(this);
        mThread.setName("radar");
        mThread.start();
        threadRunning = true;
        isstart = true;
    }

    public void stop() {
        if (isstart) {
            threadRunning = false;
            isstart = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        //canvas.drawCircle(mWidth / 2, mHeight / 2, 250, mPaintCircle);
        if(!stopRoate)
        canvas.drawCircle(mWidth / 2, mHeight / 2, smallR, mPaintLine);
//        canvas.drawCircle(mWidth / 2, mHeight / 2, 100, mPaintLine);
        canvas.drawCircle(mWidth / 2, mHeight / 2, bigR, mPaintLine);
        //绘制两条十字线

//        canvas.drawLine(mWidth / 2, mHeight/2 -250, mWidth / 2, mHeight/2+250, mPaintLine);
//        canvas.drawLine(mWidth / 2 -250, mHeight / 2, mWidth / 2 + 250, mHeight / 2, mPaintLine);


//        //这里在雷达扫描过制定圆周度数后，将随机绘制一些白点，模拟搜索结果
//        if (start > 100) {
//            for (int i = 0; i < 2; i++) {
//                canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
//            }
//        }
//        if (start > 200) {
//            for (int i = 2; i < 5; i++) {
//                canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
//            }
//        }
//        if (start > 300) {
//            for (int i = 5; i < 9; i++) {
//                canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
//            }
//        }
//        if (start > 500) {
//            for (int i = 9; i < 11; i++) {
//                canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
//            }
//        }
//        if (start > 800) {
//            for (int i = 11; i < point_x.length; i++) {
//                canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
//            }
//        }

        //根据matrix中设定角度，不断绘制shader,呈现出一种扇形扫描效果




        if (stopRoate) {
            if(Istart <= 360) {
                mPaintArc.setColor(0x7aFFFFFF);
                canvas.drawArc(rectF, -90, Istart, false, mPaintArc);
            } else if(Istart >360 && Istart  <= 360 + codeangel) {
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
            canvas.save();
            int sweep = 1;

            canvas.rotate(start, mWidth / 2, mHeight / 2);
            RectF rect = new RectF(mWidth / 2 - bigR, mHeight / 2 - bigR, mWidth / 2 + bigR, mHeight / 2 + bigR);
            RadialGradient radialGradient = new RadialGradient(getWidth() / 2, getHeight() / 2, bigR,
                    new int[] {Color.TRANSPARENT, 0x1eFFFFFF}, new float[]{0.0f,1f}
                    , Shader.TileMode.CLAMP);
            mPaintSector.setShader(radialGradient);
            canvas.drawArc(rect, 0, 90, true, mPaintSector);
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(0x7affffff);
            mPaint.setStyle(Style.STROKE);
            mPaint.setStrokeWidth(1);
            canvas.drawArc(rect, 0, 90, false, mPaint);
            canvas.restore();
            canvas.concat(matrix);
        }


        //canvas.drawArc(mWidth / 2, mHeight / 2, 350, mPaintSector);
        super.onDraw(canvas);
    }

    public void setDirection(@RADAR_DIRECTION int direction) {
        if (direction != CLOCK_WISE && direction != ANTI_CLOCK_WISE) {
            throw new IllegalArgumentException("Use @RADAR_DIRECTION constants only!");
        }
        this.direction = direction;
    }

    protected class ScanThread extends Thread {

        private RadarView view;

        public ScanThread(RadarView view) {
            // TODO Auto-generated constructor stub
            this.view = view;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (threadRunning) {
                if (isstart) {
                    view.post(new Runnable() {
                        public void run() {
                            start = start + 1;
                            Istart ++;
                            matrix = new Matrix();
                            //设定旋转角度,制定进行转转操作的圆心
//                            matrix.postRotate(start, viewSize / 2, viewSize / 2);
//                            matrix.setRotate(start,viewSize/2,viewSize/2);
                            matrix.preRotate(direction * start, mWidth / 2, mHeight / 2);
                            view.invalidate();

                        }
                    });
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
