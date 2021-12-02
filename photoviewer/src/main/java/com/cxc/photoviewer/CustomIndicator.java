package com.cxc.photoviewer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caixingcun
 * @date 2021/5/26
 * Description : 自定义指示器
 */
public class CustomIndicator extends View {
    /**
     * 画板
     */
    private Paint mPaint;
    /**
     * 指示器高度
     */
    private int mHeight;
    /**
     * 指示器宽度
     */
    private int mWidth;
    /**
     * 矩形范围
     */
    private RectF mRectF;
    /**
     * 指示器 点列表
     */
    private ArrayList<RectF> mPointList;
    /**
     * 原点集合
     */
    private List<PointF> mCirclePoints;

    /**
     * 指示器数量
     */
    private int mIndicatorSize;
    /**
     * 当前指示器位置
     */
    private int mCurrent = 0;
    /**
     * 指示器 每个宽度
     */
    private int mPerWidth;
    /**
     * 指示器 每个高度
     */
    private int mPerHeight;
    /**
     * 指示器 起点
     */
    private PointF mPointStart;
    /**
     * 指示器终点
     */
    private PointF mPointEnd;
    /**
     * 一般颜色
     */
    private int mNormalColor;
    /**
     * 选中颜色
     */
    private int mSelectorColor;

    public CustomIndicator(Context context) {
        this(context, null);
    }

    public CustomIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomIndicator);

        mIndicatorSize = a.getInt(R.styleable.CustomIndicator_size, 1);
        mPerHeight = (int) a.getDimension(R.styleable.CustomIndicator_perHeight, 4);

        mPerWidth = (int) a.getDimension(R.styleable.CustomIndicator_perWidth, 25);

        mNormalColor = a.getColor(R.styleable.CustomIndicator_normalColor, getResources().getColor(android.R.color.darker_gray));

        mSelectorColor = a.getColor(R.styleable.CustomIndicator_selectorColor, getResources().getColor(android.R.color.holo_red_light));

        a.recycle();

        initPaint();
    }

    private void initPaint() {
        //获取xml中的属性
        //初始化paint，初始化一些其他内容
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        calculate();

    }

    /**
     * 计算点位
     */
    private void calculate() {
        int widthAll = mIndicatorSize * mPerWidth;
        int paddingLeft = (mWidth - widthAll) / 2;
        int paddingTop = (mHeight - mPerHeight) / 2;
        mPointList = new ArrayList<>();
        mCirclePoints = new ArrayList<>();
        for (int i = 0; i < mIndicatorSize; i++) {
            mPointList.add(new RectF(paddingLeft + i * mPerWidth, paddingTop, paddingLeft + (i + 1) * mPerWidth, paddingTop + mPerHeight));
            mCirclePoints.add(new PointF(paddingLeft + i * mPerWidth, paddingTop + mPerHeight / 2));
        }
        mCirclePoints.add(new PointF(paddingLeft + mIndicatorSize * mPerWidth, paddingTop + mPerHeight / 2));

        mRectF = new RectF(paddingLeft, paddingTop, paddingLeft + mPerWidth * mIndicatorSize, paddingTop + mPerHeight);

        mPointStart = new PointF(paddingLeft, paddingTop + mPerHeight / 2);
        mPointEnd = new PointF(mWidth - paddingLeft, paddingTop + mPerHeight / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircleIndicator(canvas);
    }


    private void drawCircleIndicator(Canvas canvas) {
        mPaint.setColor(mNormalColor);
        for (int i = 0; i < mPointList.size(); i++) {
            RectF rect = mPointList.get(i);
            float width = rect.right - rect.left;
            float height = rect.bottom - rect.top;
            float radius = height < width ? height / 2 : width / 2;
            if (i == mCurrent) {
                mPaint.setColor(mSelectorColor);
            } else {
                mPaint.setColor(mNormalColor);
            }
            canvas.drawCircle(rect.centerX(), rect.centerY(), radius, mPaint);
        }
    }

    /**
     * 更新指示器位置
     *
     * @param current 更新后的位置
     */
    public void setCurrent(int current) {
        if (current > mIndicatorSize - 1) {
            return;
        }
        this.mCurrent = current;
        postInvalidate();
    }

    /**
     * 设置指示器 数量
     *
     * @param indicatorSize
     */
    public void setIndicatorSize(int indicatorSize) {
        if (indicatorSize == 0) {
            return;
        }
        this.mIndicatorSize = indicatorSize;
        mCurrent = 0;
        calculate();
        postInvalidate();
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
