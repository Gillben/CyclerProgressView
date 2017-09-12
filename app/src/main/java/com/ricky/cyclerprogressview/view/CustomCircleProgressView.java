package com.ricky.cyclerprogressview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ricky.cyclerprogressview.R;

/**
 * 自定义圆形进度条
 * Created by postech on 2017/9/12.
 */

public class CustomCircleProgressView extends View {

    //进度条的当前位置
    private int mCurrentLocation;
    private Paint mPaintDefault;
    private Paint mPaintCurrent;
    private Paint mPaintText;
    private float mPaintWidth;//画笔宽度
    private int mPaintColor = Color.RED;//画笔颜色

    private float mTextSize;//字体大小
    private int mTextColor = Color.BLACK;//字体颜色

    private int sweepLocation;//进度条扫描的位置
    private float startAngle;//开始角度

    public LoadingCompletedListener completeListener;


    public CustomCircleProgressView(Context context) {
        this(context, null);
    }

    public CustomCircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
    }

    private void initParams(Context context, AttributeSet attribute) {
        TypedArray mTypeArray = context.obtainStyledAttributes(attribute, R.styleable.CustomCircleProgressView);
        sweepLocation = mTypeArray.getInt(R.styleable.CustomCircleProgressView_sweep_location, 1);
        mPaintWidth = mTypeArray.getDimension(R.styleable.CustomCircleProgressView_custom_paint_with, dp2px(context, 3));
        mPaintColor = mTypeArray.getColor(R.styleable.CustomCircleProgressView_custom_paint_color, mPaintColor);
        mTextSize = mTypeArray.getDimension(R.styleable.CustomCircleProgressView_custom_text_size, dp2px(context, 16));
        mTextColor = mTypeArray.getColor(R.styleable.CustomCircleProgressView_custom_text_color, mTextColor);
        mTypeArray.recycle();

        //进度条默认背景
        mPaintDefault = new Paint();
        mPaintDefault.setAntiAlias(true);
        mPaintDefault.setStrokeWidth(mPaintWidth);
        mPaintDefault.setStyle(Paint.Style.STROKE);
        mPaintDefault.setColor(Color.GRAY);
        mPaintDefault.setStrokeCap(Paint.Cap.ROUND);

        //当前进度条的模式
        mPaintCurrent = new Paint();
        mPaintCurrent.setAntiAlias(true);
        mPaintCurrent.setStrokeWidth(mPaintWidth);
        mPaintCurrent.setStyle(Paint.Style.STROKE);
        mPaintCurrent.setColor(mPaintColor);
        mPaintCurrent.setStrokeCap(Paint.Cap.ROUND);

        //字体的画笔
        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setStrokeWidth(mPaintWidth);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setTextSize(mTextSize);
        mPaintText.setColor(mTextColor);

        if (sweepLocation == 1) {
            startAngle = -90;
        } else if (sweepLocation == 2) {
            startAngle = 0;
        } else if (sweepLocation == 3) {
            startAngle = 90;
        } else if (sweepLocation == 4) {
            startAngle = -180;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int with = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = with > height ? height : with;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制进度条的背景
        RectF mRectF = new RectF(mPaintWidth / 2, mPaintWidth / 2, getWidth() - mPaintWidth / 2, getHeight() - mPaintWidth / 2);
        canvas.drawArc(mRectF, 0, 360, false, mPaintDefault);

        //绘制当前的进度
        float sweepAngle = 360*mCurrentLocation/100;
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, mPaintCurrent);

        //绘制进度值
        String text = mCurrentLocation + "%";
        float textWith = mPaintText.measureText(text,0,text.length());   //获取text 所占用的宽度
        float dx = getWidth()/2 - textWith/2;
        Paint.FontMetricsInt fonMetricsInt = mPaintText.getFontMetricsInt();
        float dy = (fonMetricsInt.bottom - fonMetricsInt.top)/2 - fonMetricsInt.bottom;
        float baseLine = getHeight()/2 + dy;
        canvas.drawText(text,dx,baseLine,mPaintText);

        if (completeListener != null && mCurrentLocation == 100){
            completeListener.complete();
        }
    }

    public int getCurrentProgress() {
        return mCurrentLocation;
    }

    public void updateCurrentProgress(float value){
        mCurrentLocation = (int) value;
        invalidate();
    }

    private int dp2px(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }


    public void setLoadingCompletedListener(LoadingCompletedListener listener){
        this.completeListener = listener;
    }


     public interface LoadingCompletedListener{
        void complete();
    }

}
