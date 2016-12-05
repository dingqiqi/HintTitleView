package com.dingqiqi.hinttitlerecycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by dingqiqi on 2016/12/1.
 */
public class SuspensionDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private List<Mode> mList;

    private Paint mPaint;
    private Paint mPaintRect;
    private Paint mPaintOver;

    private int mTitleHeight;

    private Orien mOrien = Orien.VER;
    private Rect mTextBound;

    public enum Orien {
        HOR, VER;
    }

    /**
     * 初始化
     *
     * @param mContext
     * @param mList
     */
    public SuspensionDecoration(Context mContext, List<Mode> mList) {
        this.mContext = mContext;
        this.mList = mList;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(DisplayUtil.dp2px(mContext, 14));

        mPaintRect = new Paint(mPaint);
        mPaintRect.setColor(Color.RED);

        mPaintOver = new Paint(mPaint);
        mPaintOver.setColor(Color.BLUE);

        mTitleHeight = DisplayUtil.dp2px(mContext, 26);

        mTextBound = new Rect();
    }

    /**
     * 设置方向
     *
     * @param orien
     */
    public void setOrien(Orien orien) {
        this.mOrien = orien;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (mOrien == Orien.HOR) {
            int top = parent.getPaddingTop();
            int bottom = parent.getHeight() - parent.getPaddingBottom();

            for (int i = 0; i < parent.getChildCount() - 1; i++) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) parent.getChildAt(i)
                        .getLayoutParams();

                int left = parent.getChildAt(i).getRight() + params.rightMargin;
                int right = left + mTitleHeight;

                c.drawRect(left, top, right, bottom, mPaintRect);
            }
        } else if (mOrien == Orien.VER) {
            int left = parent.getPaddingLeft();
            //recyclerView宽度减去右边间距
            int right = parent.getWidth() - parent.getPaddingRight();

            for (int i = 0; i < parent.getChildCount(); i++) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) parent.getChildAt(i)
                        .getLayoutParams();
                //获取当前View下标
                int pos = params.getViewLayoutPosition();

                boolean flag = false;
                //第一行肯定有标题
                if (pos == 0) {
                    flag = true;
                } else {
                    //上下不同有标题
                    if (!mList.get(pos).getTag().equals(mList.get(pos - 1).getTag())) {
                        flag = true;
                    }
                }

                if (flag) {
                    int top = parent.getChildAt(i).getTop() + params.topMargin - mTitleHeight;
                    int bottom = top + mTitleHeight;
                    //画标题矩形
                    c.drawRect(left, top, right, bottom, mPaintRect);
                    //画文字
                    String text = mList.get(pos).getTag();
                    mPaint.getTextBounds(text, 0, text.length(), mTextBound);
                    c.drawText(text, (right - left - mTextBound.width()) / 2, bottom - (mTitleHeight - mTextBound.height()) / 2, mPaint);
                }
            }
        }

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //显示的第一个View，因为会滑动，这个View一直是最新的那个
        int pos = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();

        if (pos == -1) {
            return;
        }

        //获取当前View
        View child = parent.findViewHolderForAdapterPosition(pos).itemView;

        //悬浮菜单
        boolean flag = false;
        if (pos + 1 < mList.size()) {
            if (!mList.get(pos).getTag().equals(mList.get(pos + 1).getTag())) {
                if (child.getTop() + child.getHeight() < mTitleHeight) {
                    //保存布局
                    c.save();
                    //上一个标题推出去(child.getTop() + child.getHeight() - mTitleHeight 滑动的时候慢慢减小)
                    c.translate(0, child.getTop() + child.getHeight() - mTitleHeight);
                    //覆盖上一个标题(滑动的时候显示区域慢慢向上，表现出覆盖现象)
                    //c.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getWidth() - parent.getPaddingRight(), parent.getPaddingTop() + child.getTop() + child.getHeight());
                }
            }
        }
        //画标题区域
        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getWidth() - parent.getPaddingRight(), parent.getPaddingTop() + mTitleHeight, mPaintRect);
        //画文字
        String text = mList.get(pos).getTag();
        mPaint.getTextBounds(text, 0, text.length(), mTextBound);
        c.drawText(text, (parent.getWidth() - parent.getPaddingRight() - parent.getPaddingLeft() - mTextBound.width()) / 2, parent.getPaddingTop() + mTitleHeight - (mTitleHeight - mTextBound.height()) / 2, mPaint);
        //还原布局
        if (flag) {
            c.restore();
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //获取当前View的position
        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        if (pos > -1) {
            if (mOrien == Orien.HOR) {
                outRect.set(0, 0, mTitleHeight, 0);
            } else if (mOrien == Orien.VER) {
                //title高度到0（第一个肯定有标题）
                if (pos == 0) {
                    outRect.set(0, mTitleHeight, 0, 0);
                } else {
                    //标题不同的话就给标题间隔高度
                    if (!mList.get(pos).getTag().equals(mList.get(pos - 1).getTag())) {
                        outRect.set(0, mTitleHeight, 0, 0);
                    } else {
                        //标题相同的话就不给标题间隔高度
                        outRect.set(0, 0, 0, 0);
                    }
                }
            }
        }
    }

}
