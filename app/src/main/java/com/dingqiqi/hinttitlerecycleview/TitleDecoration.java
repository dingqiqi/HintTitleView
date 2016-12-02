package com.dingqiqi.hinttitlerecycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Created by dingqiqi on 2016/12/1.
 */
public class TitleDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private List<String> mList;

    private Paint mPaint;
    private Paint mPaintRect;
    private Paint mPaintOver;

    private int mTitleHight;

    private Orien mOrien = Orien.VER;
    private Rect mTextBound;

    public enum Orien {
        HOR, VER;
    }

    public TitleDecoration(Context mContext, List<String> mList) {
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

        mTitleHight = DisplayUtil.dp2px(mContext, 26);

        mTextBound = new Rect();
    }

    public void setOrien(Orien orien) {
        this.mOrien = orien;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int pos = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();

        if (mOrien == Orien.HOR) {
            int top = parent.getPaddingTop();
            int bottom = parent.getHeight() - parent.getPaddingBottom();

            for (int i = 0; i < parent.getChildCount() - 1; i++) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) parent.getChildAt(i)
                        .getLayoutParams();

                int left = parent.getChildAt(i).getRight() + params.rightMargin;
                int right = left + mTitleHight;

                c.drawRect(left, top, right, bottom, mPaintRect);
            }
        } else if (mOrien == Orien.VER) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            for (int i = 0; i < parent.getChildCount(); i++) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) parent.getChildAt(i)
                        .getLayoutParams();

                if (pos > -1) {
                    int top = parent.getChildAt(i).getTop() + params.topMargin - mTitleHight;
                    int bottom = top + mTitleHight;

                    c.drawRect(left, top, right, bottom, mPaintRect);

                    String text = "Title" + pos;
                    mPaint.getTextBounds(text, 0, text.length(), mTextBound);
                    c.drawText(text, (right - left - mTextBound.width()) / 2, bottom - (mTitleHight - mTextBound.height()) / 2, mPaint);
                }
            }

        }

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
//        int pos = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
//        View child = parent.findViewHolderForAdapterPosition(pos).itemView;
//
//        //悬浮菜单
//        boolean flag = false;
//        if (child.getTop() + child.getHeight() < mTitleHight) {
//            flag = true;
//            c.save();
//
//            Log.i("aaa", "  " + (child.getTop() + child.getHeight() - mTitleHight));
//            //上一个标题推出去
//            //c.translate(0, child.getTop() + child.getHeight() - mTitleHight);
//
//            //覆盖上一个标题
//            c.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getWidth() - parent.getPaddingRight(), parent.getPaddingTop() + child.getTop() + child.getHeight());
//        }
//
//        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getWidth() - parent.getPaddingRight(), parent.getPaddingTop() + mTitleHight, mPaintRect);
//
//        String text = "Title" + pos;
//        mPaint.getTextBounds(text, 0, text.length(), mTextBound);
//        c.drawText(text, (parent.getWidth() - parent.getPaddingRight() - parent.getPaddingLeft() - mTextBound.width()) / 2, parent.getPaddingTop() + mTitleHight - (mTitleHight - mTextBound.height()) / 2, mPaint);
//
//        if (flag) {
//            c.restore();
//        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        if (pos > -1) {
            if (mOrien == Orien.HOR) {
                outRect.set(0, 0, mTitleHight, 0);
            } else if (mOrien == Orien.VER) {
                //title高度到0
                outRect.set(0, mTitleHight, 0, 0);
            }
        }
    }

}
