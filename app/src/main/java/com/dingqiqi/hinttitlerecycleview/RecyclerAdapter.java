package com.dingqiqi.hinttitlerecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dingqiqi on 2016/12/1.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    private Context mContext;
    private List<Mode> mList;

    public RecyclerAdapter(Context mContext, List<Mode> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //绑定parent，因为item宽度没绑定parent，match_parent没效果，item跟布局高度必须设置为wrap_content
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mTextView.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public Holder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv);
        }
    }

}
