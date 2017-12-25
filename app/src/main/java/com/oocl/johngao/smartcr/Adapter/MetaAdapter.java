package com.oocl.johngao.smartcr.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oocl.johngao.smartcr.R;

import java.util.List;

/**
 * Created by johngao on 17/12/25.
 */

public class MetaAdapter extends RecyclerView.Adapter<MetaAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> mInsideList;

    public MetaAdapter(Context context, List<String> list){
        mContext = context;
        mInsideList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(mContext).inflate(R.layout.item_meta,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mInsideList.size() == 0){
            Log.e("test", "onBindViewHolder:  没有数据，发送错误" );
        }
        holder.mMetaTV.setText(mInsideList.get(position));
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
        private TextView mMetaTV;
        public MyViewHolder(View view){
            super(view);
            mMetaTV = (TextView) view.findViewById(R.id.text_mata_name);
        }
    }
}
