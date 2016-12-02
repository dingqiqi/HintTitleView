package com.dingqiqi.hinttitlerecycleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Mode> mList;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    private SuspensionDecoration mSuspensionDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mList = new ArrayList<Mode>();

        for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) {
                Mode mode = new Mode();
                mode.setName("title" + i);
                mode.setTitle("title" + i);
                mode.setTag("A");
                mList.add(mode);

                mode = new Mode();
                mode.setName("title" + i + 1);
                mode.setTitle("title" + i);
                mode.setTag("A");
                mList.add(mode);
            } else {
                Mode mode = new Mode();
                mode.setName("title" + i);
                mode.setTitle("title" + i);
                mode.setTag("B");
                mList.add(mode);

                mode = new Mode();
                mode.setName("title" + i + 1);
                mode.setTitle("title" + i);
                mode.setTag("B");
                mList.add(mode);
            }

        }

        mAdapter = new RecyclerAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        mSuspensionDecoration = new SuspensionDecoration(this, mList);
        mSuspensionDecoration.setOrien(SuspensionDecoration.Orien.VER);

        mRecyclerView.addItemDecoration(mSuspensionDecoration);
    }


}
