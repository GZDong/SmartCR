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
            if (container.isW_Choose()==false){
                holder.mWashImg.setVisibility(View.GONE);
            }else {
                holder.mWashImg.setVisibility(View.VISIBLE);
            }
            if (container.isR_Choose()==false){
                holder.mRepairImg.setVisibility(View.GONE);
            }else {
                holder.mRepairImg.setVisibility(View.VISIBLE);
            }
            if (container.isW_Progress()==true){
                holder.mWashImg.setImageResource(R.drawable.washafter);
            }else{
                holder.mWashImg.setImageResource(R.drawable.washbefore);
            }
            if (container.isR_Progress()==true){
                holder.mRepairImg.setImageResource(R.drawable.repairafter);
            }else {
                holder.mRepairImg.setImageResource(R.drawable.reqairbefore);
            }

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
            holder.mWashImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (container.isW_Choose() == true && container.isW_Progress() == false){ //洗前拍照
                        Intent intent = new Intent(mContext, TakePhotoActivity.class);
                        intent.putExtra("ConNo",container.getConNo());
                        intent.putExtra("Message","WashBefore");
                        mContext.startActivity(intent);
                    }
                    if (container.isW_Choose() == true && container.isW_Progress() == true){  //洗后拍照
                        Intent intent = new Intent(mContext, TakePhotoActivity.class);
                        intent.putExtra("ConNo",container.getConNo());
                        intent.putExtra("Message","WashAfter");
                        mContext.startActivity(intent);
                    }
                }
            });

            holder.mRepairImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (container.isR_Choose() == true && container.isR_Progress() == false){ //修前拍照
                        Intent intent = new Intent(mContext, TakePhotoActivity.class);
                        intent.putExtra("Message","RepairBefore");
                        intent.putExtra("ConNo",container.getConNo());
                        mContext.startActivity(intent);
                    }
                    if (container.isR_Choose() == true && container.isR_Progress() == true){  //修后拍照
                        Intent intent = new Intent(mContext, TakePhotoActivity.class);
                        intent.putExtra("Message","RepairAfter");
                        intent.putExtra("ConNo",container.getConNo());
                        mContext.startActivity(intent);
                    }
                }
            });
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
        private ImageView mWashImg;
        private ImageView mRepairImg;
        private TextView mCompaTV;
        private ImageView mPicsIV;
        private TextView mDateTV;

        public MyViewHolder(View view){
            super(view);
            mTextView =(TextView) view.findViewById(R.id.container_name);
            mWashImg = (ImageView) view.findViewById(R.id.wash_icon);
            mRepairImg = (ImageView) view.findViewById(R.id.repair_icon);
            mCompaTV = (TextView) view.findViewById(R.id.company_text);
            mPicsIV = (ImageView) view.findViewById(R.id.pics_iv);
            mDateTV = (TextView) view.findViewById(R.id.date_text);
        }
    }
}
