package com.dingqiqi.hinttitlerecycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */
public class IndexView extends View {

    private Context mContext;

    private String[] str = new String[]{"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};

    private Paint mPaint;

    private Paint mPaintCircle;

    private float mHeight;

    private Rect mTextBounds;

    private ScrollListener mListener;

    private int mIndex;

    private String mIndexStr;

    private float mRadius;

    public IndexView(Context context) {
        super(context);
        mContext = context;
        initView();
    }


    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(DisplayUtil.dp2px(mContext, 12));

        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.GREEN);
        mPaintCircle.setAntiAlias(true);

        mTextBounds = new Rect();
    }

    public void setListener(ScrollListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < str.length; i++) {
            mPaint.getTextBounds(str[i], 0, str[i].length(), mTextBounds);

            if (i == mIndex) {
                mPaint.setColor(Color.WHITE);
                canvas.drawCircle(getMeasuredWidth() / 2, mHeight * i + mHeight / 2, mRadius, mPaintCircle);
            } else {
                mPaint.setColor(Color.BLACK);
            }

            canvas.drawText(str[i], getMeasuredWidth() / 2 - mTextBounds.width() / 2, mHeight * i + mHeight / 2 + mTextBounds.height() / 2, mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (x < 0) {
            getParent().requestDisallowInterceptTouchEvent(false);

            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_MOVE) {

            if (y % mHeight == 0) {
                mIndex = y / (int) mHeight - 1;
            } else {
                mIndex = y / (int) mHeight;
            }

            if (mIndex >= str.length) {
                mIndex = str.length - 1;
            }

            if (mListener != null && (!str[mIndex].equals(mIndexStr))) {
                mListener.backDownString(str[mIndex], mIndex);
            }

            mIndexStr = str[mIndex];
            invalidate();
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h / (str.length * 1.0f);

        mRadius = Math.min(w / 2, mHeight / 2);
    }


    public interface ScrollListener {
        void backDownString(String str, int pos);
    }

}
