package com.oocl.johngao.smartcr.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oocl.johngao.smartcr.R;

import java.util.List;

/**
 * Created by johngao on 17/12/22.
 */

public class SettingsListAdapter extends RecyclerView.Adapter<SettingsListAdapter.MyViewHolder> {

    List<String> mInsideList;
    Context mContext;

    public SettingsListAdapter(Context context, List<String> list){
        mContext= context;
        mInsideList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_settings,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == 3 || position == 5 || position == 7 || position ==9){
            holder.mLine.setVisibility(View.VISIBLE);
        }
        if (position == 9){

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.mOptionsTV.getLayoutParams();
            holder.mIntoIMG.setVisibility(View.GONE);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);


            holder.mOptionsTV.setLayoutParams(params);
            holder.mOptionsTV.setGravity(Gravity.CENTER);
        }
        holder.mOptionsTV.setText(mInsideList.get(position));
    }

    @Override
    public int getItemCount() {
        return mInsideList.size();
    }

    public void updateList(List<String> list){
        this.mInsideList = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mLine;
        private TextView mOptionsTV;
        private ImageView mIntoIMG;
        public MyViewHolder(View v){
            super(v);
            mLine = (TextView) v.findViewById(R.id.div_line);
            mOptionsTV = (TextView) v.findViewById(R.id.options_settings);
            mIntoIMG = (ImageView) v.findViewById(R.id.img_setttings);
        }
    }
}
