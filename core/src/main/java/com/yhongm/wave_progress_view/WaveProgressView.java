package com.yhongm.wave_progress_view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class WaveProgressView extends View {
    private int mWidth = 900;
    private int mHeight = 900;
    private int mWaveLength = 800;//一个长度
    private int mWaveCount = 2;//数量
    private int mWaveOffsetX = 0;
    private int mWaveOffsetX2 = 0;
    private int mRadius;
    private int mCurrentHeight = 0;
    private int mDropOffsetY = 0;
    private int mDropOffsetY2 = 0;
    private float mPercent;
    private ValueAnimator valueAnimatorY;
    private ValueAnimator dropSplashAnimator;
    private int mWaveOffsetY;
    private int mWaveOffsetY2;
    private int mWaterColor = Color.rgb(84, 184, 227);//水颜色
    private int mCircleColor = mWaterColor;
    private HashMap<Integer, Float> mLeftHashMapPath = new HashMap<>();
    private HashMap<Integer, Float> mRightHashMapPath = new HashMap<>();
    private HashMap<Float, Integer> mRandomHashMap = new HashMap<>();
    private Paint mSplashPaint;//水滴飞溅画笔
    private Paint mDropPaint;//水滴画笔
    private Paint mWavePaint;//水波画笔
    private Paint mWavePaint2;//水波画笔
    private Paint mCirclePaint;//圆形画笔
    float defaultProgress;//默认的进度

    public WaveProgressView(Context context) {
        super(context);
    }

    public WaveProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveProgressView);
        mWaterColor = typedArray.getColor(R.styleable.WaveProgressView_waterColor, mWaterColor);
        mCircleColor = typedArray.getColor(R.styleable.WaveProgressView_circleColor, mWaterColor);

        defaultProgress = typedArray.getFloat(R.styleable.WaveProgressView_progress, 0);
        initPaint();
        initAnimator();

    }

    private void initPaint() {

        mSplashPaint = new Paint();
        mSplashPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mSplashPaint.setStyle(Paint.Style.FILL);
        mSplashPaint.setColor(mWaterColor);

        mDropPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDropPaint.setColor(mWaterColor);
        mDropPaint.setStyle(Paint.Style.FILL);


        mWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint.setColor(mWaterColor);
        mWavePaint.setStrokeWidth(10);

        mWavePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWavePaint2.setStyle(Paint.Style.FILL);
        mWavePaint2.setColor(mWaterColor);
        mWavePaint2.setStrokeWidth(10);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(10);
    }

    private void initAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mWaveLength);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWaveOffsetX = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(800);
        valueAnimator.start();
        ValueAnimator valueAnimator2 = ValueAnimator.ofInt(0, mWaveLength);

        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWaveOffsetX2 = (int) animation.getAnimatedValue();
            }
        });
        valueAnimator2.setInterpolator(new AccelerateInterpolator());
        valueAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator2.setDuration(1200);
        valueAnimator2.start();
        ValueAnimator valueAnimatorWaveY = ValueAnimator.ofInt(80, 120);
        valueAnimatorWaveY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWaveOffsetY = (int) animation.getAnimatedValue();
            }
        });
        valueAnimatorWaveY.setInterpolator(new AccelerateInterpolator());
        valueAnimatorWaveY.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorWaveY.setDuration(800);
        valueAnimatorWaveY.start();
        ValueAnimator valueAnimatorWaveY2 = ValueAnimator.ofInt(80, 120);
        valueAnimatorWaveY2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWaveOffsetY2 = (int) animation.getAnimatedValue();
            }
        });
        valueAnimatorWaveY2.setInterpolator(new DecelerateInterpolator());
        valueAnimatorWaveY2.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorWaveY2.setDuration(1200);
        valueAnimatorWaveY2.start();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(900, 900);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mCurrentHeight = mHeight;
        mRadius = Math.min(mWidth / 2, mHeight / 2) - 10;
        mWaveCount = (int) ((mWidth / mWaveLength) + 1.5);
        setProgress(defaultProgress / 100f);
    }

    public void setProgress(final float progress) {
        mCurrentHeight = (int) (mHeight * progress);
        mCurrentHeight = mHeight - mCurrentHeight;

        valueAnimatorY = ValueAnimator.ofInt(0, mCurrentHeight + 80);


        valueAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDropOffsetY = (int) animation.getAnimatedValue();
            }
        });
        valueAnimatorY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mHandler.hasMessages(1)) {
                    mHandler.removeMessages(1);
                }
                mDropPaint.setAlpha(255);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.sendEmptyMessage(1);
                mDropPaint.setAlpha(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimatorY.setDuration(1000);
        valueAnimatorY.setInterpolator(new AccelerateInterpolator());
        valueAnimatorY.setRepeatCount(0);
        if (!valueAnimatorY.isRunning()) {
            valueAnimatorY.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawWave(canvas);
        drawDropByLocation(canvas, mWidth / 2, mDropOffsetY);
        if (dropSplashAnimator != null && dropSplashAnimator.isRunning()) {
            drawDropSplash(canvas, mWidth / 2, mCurrentHeight);
        }
    }

    /**
     * 画两侧水滴飞溅的效果，并且随机生成水滴
     *
     * @param canvas
     * @param x
     * @param y      当前高度
     */
    private synchronized void drawDropSplash(Canvas canvas, int x, int y) {
        PathMeasure mLeftPathMeasure = getOnBothSidesOfPathMeasure(x, y, true);
        PathMeasure mRightPathMeasure = getOnBothSidesOfPathMeasure(x, y, false);

        float[] mLeftPos = new float[2];
        float[] mRightPos = new float[2];
        float[] mLeftTan = new float[2];
        float[] mRightTan = new float[2];

        for (int i = 0; i < 200; i++) {
            float percent = i / 200f;
            mLeftPathMeasure.getPosTan(mLeftPathMeasure.getLength() * percent, mLeftPos, mLeftTan);
            mRightPathMeasure.getPosTan(mRightPathMeasure.getLength() * percent, mRightPos, mRightTan);
            mLeftHashMapPath.put(Math.round(mLeftPos[1]), mLeftPos[0]);
            mRightHashMapPath.put(Math.round(mRightPos[1]), mRightPos[0]);
        }

        if (mRandomHashMap.isEmpty() && mRandomHashMap.size() == 0) {
            pushRandomDrag(y);
        }
        drawRandomDrag(canvas, x, y, mLeftHashMapPath, mRightHashMapPath);

        drawOnBothSidesOfWaterDrop(canvas, mLeftPathMeasure);

        drawOnBothSidesOfWaterDrop(canvas, mRightPathMeasure);

    }

    /**
     * 画两侧的水滴
     *
     * @param canvas
     * @param pathMeasure
     */
    private void drawOnBothSidesOfWaterDrop(Canvas canvas, PathMeasure pathMeasure) {
        float[] pos = new float[2];
        float[] tan = new float[2];
        pathMeasure.getPosTan(pathMeasure.getLength() * mPercent, pos, tan);
        canvas.save();
        canvas.rotate((float) Math.atan2(tan[1], tan[0]), pos[0], pos[1]);
        Path mLeftDropPath = waterDrop(pos[0], pos[1], 15);
        mSplashPaint.setColor(mWaterColor);
        canvas.drawPath(mLeftDropPath, mSplashPaint);
        canvas.restore();
    }

    /**
     * 计算两侧的PathMeasure
     *
     * @param x
     * @param y
     * @param isLeft
     * @return
     */
    private PathMeasure getOnBothSidesOfPathMeasure(int x, int y, boolean isLeft) {
        Path mSplashPath = new Path();
        mSplashPath.moveTo(x, y + 50);
        if (isLeft) {
            mSplashPath.cubicTo(x, y + 50, x - 170, y - 170, x - 200, y - 150);
            mSplashPath.lineTo(x - 200, y + 150);
        } else {
            mSplashPath.cubicTo(x, y + 50, x + 170, y - 170, x + 200, y - 150);
            mSplashPath.lineTo(x + 200, y + 150);
        }

        PathMeasure mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mSplashPath, false);
        return mPathMeasure;
    }

    /**
     * 画出随机生成水滴
     *
     * @param canvas
     * @param x
     * @param y                 当前的高度
     * @param mLeftHashMapPath
     * @param mRightHashMapPath
     */
    private void drawRandomDrag(Canvas canvas, int x, int y, HashMap<Integer, Float> mLeftHashMapPath, HashMap<Integer, Float> mRightHashMapPath) {
        Iterator<Map.Entry<Float, Integer>> iterator = mRandomHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Float, Integer> next = iterator.next();
            Float randomX = next.getKey();
            Integer randomY = next.getValue();
            drawSmartDropOnPath(canvas, x, y, 10, randomY, randomX);
        }
    }

    /**
     * 产生随机的水滴
     *
     * @param y
     */
    private void pushRandomDrag(int y) {
        Random r = new Random();
        for (int i = 0; i < 20; i++) {
            int randomY = r.nextInt(y);
            if (mLeftHashMapPath.containsKey(randomY)) {
                Float rightValue = mRightHashMapPath.get(randomY);
                Float leftValue = mLeftHashMapPath.get(randomY);
                int roundLeftValue = Math.round(leftValue);
                int roundRightValue = Math.round(rightValue);
                if (roundRightValue == roundLeftValue) {
                    roundRightValue++;
                }
                int roundMinus = Math.round(roundRightValue - roundLeftValue);//左右差值
                float randomX = r.nextInt(roundMinus) + mLeftHashMapPath.get(randomY);//左右差值加上最小值,保证随机值在两者之间
                mRandomHashMap.put(randomX, randomY);

            }
        }
    }

    /**
     * 画水滴溅起沿着路径
     *
     * @param canvas
     * @param x
     * @param y
     * @param size
     * @param randomY
     * @param randomX
     */
    private void drawSmartDropOnPath(Canvas canvas, int x, int y, int size, int randomY, float randomX) {
        Path smartDropPath = new Path();
        smartDropPath.moveTo(x, y + 50);
        if (x < randomX) {
            smartDropPath.cubicTo(x, y + 50, randomX + 30, randomY - 20, randomX, randomY);
        } else {
            smartDropPath.cubicTo(x, y + 50, randomX - 30, randomY - 20, randomX, randomY);
        }
        smartDropPath.lineTo(randomX, randomY + 150);
        PathMeasure pathMeasure = new PathMeasure();
        pathMeasure.setPath(smartDropPath, false);
        float[] pos = new float[2];
        float[] tan = new float[2];
        pathMeasure.getPosTan(pathMeasure.getLength() * mPercent, pos, tan);
        Path path = waterDrop(pos[0], pos[1], size);
        canvas.drawPath(path, mSplashPaint);
    }

    /**
     * 根据位置画水滴
     *
     * @param x
     * @param y
     * @param canvas
     */
    private void drawDropByLocation(Canvas canvas, int x, int y) {
        Path mDropPath = waterDrop(x, y, 30);
        if (y == (mCurrentHeight + 50)) {
            mDropPaint.setAlpha(0);
        }
        canvas.drawPath(mDropPath, mDropPaint);
    }

    /**
     * 水滴的Path
     *
     * @param x    水滴坐标x
     * @param y    水滴坐标y
     * @param size 水滴尺寸
     * @return
     */
    private Path waterDrop(float x, float y, int size) {
        Path mDropPath = new Path();
        mDropPath.moveTo(x - size, y);
        mDropPath.lineTo(x, (float) (y - size * 2.5));
        mDropPath.lineTo(x + size, y);
        mDropPath.addArc(x - size, y - size, x + size, y + size, 0, 180);
        return mDropPath;
    }

    /**
     * 画波浪
     *
     * @param canvas
     */
    private void drawWave(Canvas canvas) {
        Path mCirclePath = getCirclePath();
        Path mPath = getWavePath(-1f, mWaveLength, mWaveOffsetX, mWaveOffsetY);
        Path mPath2 = getWavePath(-0.75f, mWaveLength, mWaveOffsetX2, mWaveOffsetY2);
        canvas.clipPath(mCirclePath);
        canvas.drawPath(mPath, mWavePaint);
        canvas.clipPath(mCirclePath);
        canvas.drawPath(mPath2, mWavePaint2);
        canvas.drawPath(mCirclePath, mCirclePaint);
    }

    /**
     * 画圆
     *
     * @return
     */
    private Path getCirclePath() {
        Path mCirclePath = new Path();
        mCirclePath.addCircle(mWidth / 2, mHeight / 2, mRadius, Path.Direction.CW);
        return mCirclePath;
    }

    /**
     * 生成水波
     *
     * @param begin       水波形开始的位置
     * @param waveLength  水波的长度
     * @param waveOffsetX 水波水平的偏移
     * @param waveOffsetY 水波垂直方向的偏移
     * @return
     */
    private Path getWavePath(float begin, int waveLength, int waveOffsetX, int waveOffsetY) {
        Path mPath = new Path();
        mPath.reset();
        mPath.moveTo(waveLength * begin, mCurrentHeight);
        for (int i = 0; i < mWaveCount; i++) {
            mPath.quadTo(waveLength * (begin + 0.25f) + (i * waveLength) + waveOffsetX, mCurrentHeight + waveOffsetY, (waveLength * (begin + 0.5f) + (i * waveLength) + waveOffsetX), mCurrentHeight);
            mPath.quadTo(waveLength * (begin + 0.75f) + (i * waveLength) + waveOffsetX, mCurrentHeight - waveOffsetY, (waveLength * (begin + 1f) + (i * waveLength) + waveOffsetX), mCurrentHeight);
        }
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        return mPath;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    dropSplashAnimator = ValueAnimator.ofInt(0, mCurrentHeight);
                    dropSplashAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mDropOffsetY2 = (int) animation.getAnimatedValue();
                            mPercent = mDropOffsetY2 / (mCurrentHeight + 0f);
                        }
                    });
                    dropSplashAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLeftHashMapPath.clear();
                            mRightHashMapPath.clear();
                            mRandomHashMap.clear();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    dropSplashAnimator.setDuration(1200);
                    dropSplashAnimator.setRepeatCount(0);
                    dropSplashAnimator.start();
                    break;
            }
        }
    };

}