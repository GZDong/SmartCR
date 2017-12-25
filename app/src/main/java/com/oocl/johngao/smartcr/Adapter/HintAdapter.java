package com.oocl.johngao.smartcr.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oocl.johngao.smartcr.R;

import java.util.List;

/**
 * Created by johngao on 17/12/25.
 */

public class HintAdapter extends RecyclerView.Adapter<HintAdapter.MyViewHolder> {

    private Context mContext;
    public static final String TAG = "onHintAdapter";
    private List<String> mInsideList;

    public HintAdapter(Context context, List<String> list){
        mContext = context;
        mInsideList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hint_list,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (!mInsideList.get(position).equals("death")){
            holder.mHintText.setText(mInsideList.get(position));
            if (position+1 < mInsideList.size()){
                holder.mPlusImg.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mInsideList.size();
    }

    public void updateList(List<String> list){
        mInsideList = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mHintText;
        private ImageView mPlusImg;
        public MyViewHolder(View view){
            super(view);
            mHintText = (TextView) view.findViewById(R.id.text_hint);
            mPlusImg = (ImageView) view.findViewById(R.id.img_plus);
        }
    }
}
