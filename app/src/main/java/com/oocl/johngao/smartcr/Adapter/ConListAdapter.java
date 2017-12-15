package com.oocl.johngao.smartcr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oocl.johngao.smartcr.Activity.TakePhotoActivity;
import com.oocl.johngao.smartcr.Activity.TestActivity;
import com.oocl.johngao.smartcr.Const.Const;
import com.oocl.johngao.smartcr.Data.Container;
import com.oocl.johngao.smartcr.R;

import java.util.List;

/**
 * Created by johngao on 17/12/8.
 */

public class ConListAdapter extends RecyclerView.Adapter<ConListAdapter.MyViewHolder> {

    public static final String TAG = "ConListAdapter";
    private List<Container> mInsideList;
    private Context mContext;

    public ConListAdapter(List<Container> list, Context context){
        mInsideList = list;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_container_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (mInsideList != null){
            holder.mTextView.setText("货柜编号：" + mInsideList.get(position).getConNo());
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TestActivity.class);
                    mContext.startActivity(intent);
                }
            });

            final Container container = mInsideList.get(position);


            if (container.getCompany()== null){
                container.setCompany("未知");
            }
            holder.mCompaTV.setText("公司：" + container.getCompany());

            //设置那个布局来显示时间
            if (container.getConNo().equals("OOLU01199000")){
                holder.mDateTV.setVisibility(View.VISIBLE);
            }else {
                holder.mDateTV.setVisibility(View.GONE);
            }

            if (container.isW_Choose() == Const.NeedWash){
                if (container.getWB_Count() == 0 ){
                    //do noting
                    holder.mV11.setTextColor(mContext.getResources().getColor(R.color.before));
                    holder.mV12.setImageResource(R.drawable.dont);
                    holder.mV13.setTextColor(mContext.getResources().getColor(R.color.before));
                    holder.mV14.setImageResource(R.drawable.goon);
                    holder.mV14.setTag("No");
                }else if (container.getWB_Count() < 3){
                    holder.mV11.setTextColor(mContext.getResources().getColor( R.color.progress));
                    holder.mV12.setImageResource(R.drawable.dont);
                    holder.mV13.setTextColor(mContext.getResources().getColor(R.color.before));
                    holder.mV14.setImageResource(R.drawable.goon);
                    holder.mV14.setTag("No");
                }else {
                    holder.mV11.setTextColor(mContext.getResources().getColor(R.color.finish));
                    if (container.getWA_Count() == 0){
                        holder.mV12.setImageResource(R.drawable.doing);
                        holder.mV13.setTextColor(mContext.getResources().getColor(R.color.before));
                        holder.mV14.setImageResource(R.drawable.goon);
                        holder.mV14.setTag("No");
                    }else if (container.getWA_Count() < 3){
                        holder.mV12.setImageResource(R.drawable.finish);
                        holder.mV13.setTextColor(mContext.getResources().getColor( R.color.progress));
                        holder.mV14.setImageResource(R.drawable.goon);
                        holder.mV14.setTag("No");
                    }else {
                        holder.mV12.setImageResource(R.drawable.finish);
                        holder.mV13.setTextColor(mContext.getResources().getColor( R.color.finish));
                        holder.mV14.setImageResource(R.drawable.goonfinish);
                        holder.mV14.setTag("Finish");
                        holder.mV14.setOnClickListener(null);
                    }
                }

                if (holder.mV14.getTag().equals("No")){
                    holder.mV14.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(mContext, TakePhotoActivity.class);
                                intent.putExtra("Meassge","WashBefore");
                                intent.putExtra("ConNO",container.getConNo());
                                mContext.startActivity(intent);
                        }
                    });
                }

            }else {
                holder.mV11.setTextColor(mContext.getResources().getColor(R.color.before));
                holder.mV12.setImageResource(R.drawable.dont);
                holder.mV13.setTextColor(mContext.getResources().getColor(R.color.before));
                holder.mV14.setImageResource(R.drawable.dontneed);
                holder.mV14.setOnClickListener(null);
            }
            if (container.isR_Choose() == Const.NeedRepair){
                if (container.getRB_Count() == 0 ){
                    //do noting
                    holder.mV21.setTextColor(mContext.getResources().getColor(R.color.before));
                    holder.mV22.setImageResource(R.drawable.dont);
                    holder.mV23.setTextColor(mContext.getResources().getColor(R.color.before));
                    holder.mV24.setImageResource(R.drawable.goon);
                    holder.mV24.setTag("No");
                }else if (container.getRB_Count() < 3){
                    holder.mV21.setTextColor(mContext.getResources().getColor( R.color.progress));
                    holder.mV22.setImageResource(R.drawable.dont);
                    holder.mV23.setTextColor(mContext.getResources().getColor(R.color.before));
                    holder.mV24.setImageResource(R.drawable.goon);
                    holder.mV24.setTag("No");
                }else {
                    holder.mV21.setTextColor(mContext.getResources().getColor(R.color.finish));
                    if (container.getRA_Count() == 0){
                        holder.mV22.setImageResource(R.drawable.doing);
                        holder.mV23.setTextColor(mContext.getResources().getColor(R.color.before));
                        holder.mV24.setImageResource(R.drawable.goon);
                        holder.mV24.setTag("No");
                    }else if (container.getRA_Count() < 3){
                        holder.mV22.setImageResource(R.drawable.finish);
                        holder.mV23.setTextColor(mContext.getResources().getColor( R.color.progress));
                        holder.mV24.setImageResource(R.drawable.goon);
                        holder.mV24.setTag("No");
                    }else {
                        holder.mV22.setImageResource(R.drawable.finish);
                        holder.mV23.setTextColor(mContext.getResources().getColor( R.color.finish));
                        holder.mV24.setImageResource(R.drawable.goonfinish);
                        holder.mV24.setTag("Finish");
                        holder.mV24.setOnClickListener(null);
                    }
                }

                if (holder.mV24.getTag().equals("No")){
                    holder.mV24.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, TakePhotoActivity.class);
                            intent.putExtra("Meassge","WashBefore");
                            intent.putExtra("ConNO",container.getConNo());
                            mContext.startActivity(intent);
                        }
                    });
                }
            }else {
                holder.mV21.setTextColor(mContext.getResources().getColor(R.color.before));
                holder.mV22.setImageResource(R.drawable.dont);
                holder.mV23.setTextColor(mContext.getResources().getColor(R.color.before));
                holder.mV24.setImageResource(R.drawable.dontneed);
                holder.mV24.setOnClickListener(null);
            }
        }
    }

    public void updateList(List<Container> list){
        this.mInsideList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mInsideList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;

        private TextView mCompaTV;
        private ImageView mPicsIV;
        private TextView mDateTV;

        private TextView mV11;
        private ImageView mV12;
        private TextView mV13;
        private ImageView mV14;

        private TextView mV21;
        private ImageView mV22;
        private TextView mV23;
        private ImageView mV24;

        public MyViewHolder(View view){
            super(view);
            mTextView =(TextView) view.findViewById(R.id.container_name);

            mCompaTV = (TextView) view.findViewById(R.id.company_text);
            mPicsIV = (ImageView) view.findViewById(R.id.pics_iv);
            mDateTV = (TextView) view.findViewById(R.id.date_text);

            mV11 = (TextView) view.findViewById(R.id.wash_b_text);
            mV12 = (ImageView) view.findViewById(R.id.wash_arrow);
            mV13 = (TextView) view.findViewById(R.id.wash_a_text);
            mV14 = (ImageView) view.findViewById(R.id.wash_goon);

            mV21 = (TextView) view.findViewById(R.id.repair_b_text);
            mV22 = (ImageView) view.findViewById(R.id.repair_arrow);
            mV23 = (TextView) view.findViewById(R.id.repair_a_text);
            mV24 = (ImageView) view.findViewById(R.id.repire_goon);
        }
    }
}
