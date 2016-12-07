package com.dingqiqi.hinttitlerecycleview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<Mode> mList;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    private SuspensionDecoration mSuspensionDecoration;
    /**
     * 提示
     */
    private TextView mTvHint;

    private Handler mHandler = new Handler();

    private int mCurIndex;

    private IndexView mIndexView;

    private String[] str = new String[]{"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};
    /**
     * 存索引
     */
    private Map<String, Integer> mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvHint = (TextView) findViewById(R.id.tv_hint);
        mIndexView = (IndexView) findViewById(R.id.indexView);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mList = new ArrayList<Mode>();
        mMap = new HashMap<>();

        for (int i = 0; i < str.length; i++) {
            Mode mode = new Mode();
            mode.setName("title" + i);
            mode.setTitle("title" + i);
            mode.setTag(str[i]);
            mList.add(mode);

            mode = new Mode();
            mode.setName("title" + i + 1);
            mode.setTitle("title" + i);
            mode.setTag(str[i]);
            mList.add(mode);

            if (!mMap.containsKey(str[i])) {
                mMap.put(str[i], i * 2);
            }
        }

        mAdapter = new RecyclerAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
        //加标题
        mSuspensionDecoration = new SuspensionDecoration(this, mList);
        mSuspensionDecoration.setOrien(SuspensionDecoration.Orien.VER);
        mRecyclerView.addItemDecoration(mSuspensionDecoration);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                Log.i("aaa", pos + "  " + mList.get(pos).getTag());

                if (!mList.get(pos).getTag().equals(mList.get(mCurIndex).getTag())) {
                    mHandler.removeCallbacks(mRunnable);

                    mTvHint.setVisibility(View.VISIBLE);
                    mTvHint.setText(mList.get(pos).getTag());

                    mIndexView.setIndexStr(mList.get(pos).getTag());

                    mHandler.postDelayed(mRunnable, 1000);
                }
                mCurIndex = pos;
            }
        });


        mIndexView.setListener(new IndexView.ScrollListener() {
            @Override
            public void backDownString(String str, int pos) {
                pos = mMap.get(str);
                Log.i("aaa", pos + "  " + str);
                //这个有bug，在屏幕内不会移动
                //mRecyclerView.smoothScrollToPosition(pos);
                LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                manager.scrollToPositionWithOffset(pos, 0);
            }
        });
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mTvHint.setVisibility(View.GONE);
        }
    };

}
