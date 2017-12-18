package com.oocl.johngao.smartcr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
            if (container.getConNo().equals("OOLU01199001")){
                holder.mDateTV.setVisibility(View.VISIBLE);
            }else {
                holder.mDateTV.setVisibility(View.GONE);
            }

            if (container.isW_Choose() == Const.NeedWash){
                holder.mWashLayout.setVisibility(View.VISIBLE);
                if (container.getWB_Count() == 0 ){
                    //do noting
                    holder.mWashStateTX.setText("洗前");
                    holder.mWashStateIMG.setImageResource(R.drawable.tagbegin);
                    holder.mWashStateIMG.setTag("WashBeforeWithZero");
                }else if (container.getWB_Count() < 3){
                    holder.mWashStateTX.setText("洗前");
                    holder.mWashStateIMG.setImageResource(R.drawable.tagprogress);
                    holder.mWashStateIMG.setTag("WashBeforeProgress");
                }else {
                    holder.mWashStateTX.setText("洗后");
                    if (container.getWA_Count() == 0){
                        holder.mWashStateIMG.setImageResource(R.drawable.tagbegin);
                        holder.mWashStateIMG.setTag("WashBeforeFinishWashAfterWithZero");
                    }else if (container.getWA_Count() < 3){
                        holder.mWashStateIMG.setImageResource(R.drawable.tagprogress);
                        holder.mWashStateIMG.setTag("WashBeforeFinishWashAfterProgress");
                    }else {
                        holder.mWashStateTX.setText("清洗");
                        holder.mWashStateIMG.setImageResource(R.drawable.tagfinish);
                        holder.mWashStateIMG.setTag("Finish");
                        holder.mWashLayout.setOnClickListener(null);
                    }
                }

                if (!holder.mWashStateIMG.getTag().equals("Finish")){
                    holder.mWashLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, TakePhotoActivity.class);
                            intent.putExtra("ConNo",container.getConNo());
                            intent.putExtra("Message",holder.mWashStateIMG.getTag().toString());
                            mContext.startActivity(intent);
                        }
                    });
                }

            }else {
               holder.mWashLayout.setVisibility(View.GONE);
                Log.e(TAG, "onBindViewHolder: 判断是否需要洗" + container.getConNo() + "的" + container.isW_Choose());
            }
            if (container.isR_Choose() == Const.NeedRepair){
                holder.mRepairLayout.setVisibility(View.VISIBLE);
                if (container.getRB_Count() == 0 ){
                    //do noting
                    holder.mRepairTX.setText("修前");
                    holder.mRepairIMG.setImageResource(R.drawable.tagbegin);
                    holder.mRepairIMG.setTag("RepairBeforeWithZero");
                }else if (container.getRB_Count() < 3){
                    holder.mRepairTX.setText("修前");
                    holder.mRepairIMG.setImageResource(R.drawable.tagprogress);
                    holder.mRepairIMG.setTag("RepairBeforeProgress");
                }else {
                    holder.mRepairTX.setText("修后");
                    if (container.getRA_Count() == 0){
                        holder.mRepairIMG.setImageResource(R.drawable.tagbegin);
                        holder.mRepairIMG.setTag("RepairBeforeFinishRepairAfterWithZero");
                    }else if (container.getRA_Count() < 3){
                        holder.mRepairIMG.setImageResource(R.drawable.tagprogress);
                        holder.mRepairIMG.setTag("RepairBeforeFinishRepairAfterProgress");
                    }else {
                        holder.mRepairTX.setText("维修");
                        holder.mRepairIMG.setImageResource(R.drawable.tagfinish);
                        holder.mRepairIMG.setTag("Finish");
                        holder.mRepairLayout.setOnClickListener(null);
                    }
                }

                if (!holder.mRepairIMG.getTag().equals("Finish")){
                    holder.mRepairLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, TakePhotoActivity.class);
                            intent.putExtra("ConNo",container.getConNo());
                            intent.putExtra("Message",holder.mRepairIMG.getTag().toString());
                            mContext.startActivity(intent);
                        }
                    });
                }
            }else {
                holder.mRepairLayout.setVisibility(View.GONE);
                Log.e(TAG, "onBindViewHolder: 判断是否需要修" + container.getConNo() + "的" + container.isR_Choose());
            }
        }
    }

    public void updateList(List<Container> list){
        this.mInsideList = list;
        for (Container container : mInsideList){
            Log.e(TAG, "updateList: " + container.getConNo() + container.isW_Choose() + container.isR_Choose());
        }
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

        private FrameLayout mWashLayout;
        private FrameLayout mRepairLayout;
        private TextView mWashStateTX;
        private ImageView mWashStateIMG;
        private TextView mRepairTX;
        private ImageView mRepairIMG;

        public MyViewHolder(View view){
            super(view);
            mTextView =(TextView) view.findViewById(R.id.container_name);

            mCompaTV = (TextView) view.findViewById(R.id.company_text);
            mPicsIV = (ImageView) view.findViewById(R.id.pics_iv);
            mDateTV = (TextView) view.findViewById(R.id.date_text);

            mWashLayout = (FrameLayout) view.findViewById(R.id.wash_layout);
            mRepairLayout = (FrameLayout) view.findViewById(R.id.repair_layout);
            mWashStateTX = (TextView) view.findViewById(R.id.wash_state_text);
            mWashStateIMG = (ImageView) view.findViewById(R.id.wash_state_img);
            mRepairTX = (TextView) view.findViewById(R.id.repair_state_text);
            mRepairIMG = (ImageView) view.findViewById(R.id.repair_state_img);
        }
    }
}
