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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
public class IndexView extends View {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 右侧标签
     */
    private String[] str = new String[]{"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};
    /**
     * 标签画笔
     */
    private Paint mPaint;
    /**
     * 选中标签
     */
    private Paint mPaintCircle;
    /**
     * 单个标签范围高度
     */
    private float mHeight;
    /**
     * 文字信息
     */
    private Rect mTextBounds;
    /**
     * 滑动回调
     */
    private ScrollListener mListener;
    /**
     * 选中下标
     */
    private int mIndex;
    /**
     * 选中下标文字
     */
    private String mIndexStr;
    /**
     * 选中标签圆半径
     */
    private float mRadius;
    /**
     * 存储标签，提升效率，用于联动
     */
    private Map<String, Integer> mMap;


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

    /**
     * 初始化变量
     */
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
        //存储标签，当列表滑动时，用于联动
        mMap = new HashMap<>();
        for (int i = 0; i < str.length; i++) {
            mMap.put(str[i], i);
        }
    }

    /**
     * 设置回调
     * @param listener
     */
    public void setListener(ScrollListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < str.length; i++) {
            mPaint.getTextBounds(str[i], 0, str[i].length(), mTextBounds);
            //选中
            if (i == mIndex) {
                mPaint.setColor(Color.WHITE);
                canvas.drawCircle(getMeasuredWidth() / 2, mHeight * i + mHeight / 2, mRadius, mPaintCircle);
            } else {
                mPaint.setColor(Color.BLACK);
            }
            //画文字
            canvas.drawText(str[i], getMeasuredWidth() / 2 - mTextBounds.width() / 2, mHeight * i + mHeight / 2 + mTextBounds.height() / 2, mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        //超出控件范围不作处理
        if (x < 0) {
            getParent().requestDisallowInterceptTouchEvent(false);

            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_MOVE) {
            //获取选中下标
            if (y % mHeight == 0) {
                mIndex = y / (int) mHeight - 1;
            } else {
                mIndex = y / (int) mHeight;
            }
            //预防越界
            if (mIndex >= str.length) {
                mIndex = str.length - 1;
            }
            //相同的下标只返回一次
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
        //获取单个标签最大高度
        mHeight = h / (str.length * 1.0f);

        mRadius = Math.min(w / 2, mHeight / 2);
    }

    /**
     * 控件联动
     * @param str 当前选中的标签
     */
    public void setIndexStr(String str) {
        mIndex = mMap.get(str);
        invalidate();
    }

    /**
     * 选中回调
     */
    public interface ScrollListener {
        void backDownString(String str, int pos);
    }

}
