package com.oocl.johngao.smartcr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.oocl.johngao.smartcr.Activity.PicShowActivity;
import com.oocl.johngao.smartcr.Const.Const;
import com.oocl.johngao.smartcr.Data.Pictures;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import java.io.File;
import java.util.List;

/**
 * Created by johngao on 17/11/30.
 */

public class PicListAdapter extends RecyclerView.Adapter<PicListAdapter.MyViewHodler> {
    public static final String TAG = "PicListAdapter";
    public int s = 0;

    private Context mContext;
    private List<Pictures> mInsideList;

    private DataLab mDataLab;
    private boolean flag = false;
    public String mTag;

    public PicListAdapter(Context context, List<Pictures> list,String tag) {
        mContext = context;
        mInsideList = list;
        calSum();
        mTag = tag;
    }

    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_view_picture_list,parent,false);
        return new MyViewHodler(view);
    }

    @Override
    public void onBindViewHolder(MyViewHodler holder, final int position) {
        if(mInsideList.size() > 0){
            Pictures pic = mInsideList.get(position);
            Log.e(TAG, "onBindViewHolder: position is " + position );
            if (!pic.getConNo().equals(Const.NullConNo)){   //如果不是空图片，那么根据货柜的ConNo、TCode去文件里加载图片到指定位置
                File file = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                String path = file + File.separator+ pic.getConNo()+ File.separator + pic.getName();
                Log.e(TAG, "onBindViewHolder: to show pic from : " +path );

                Glide.with(mContext).load(path).asBitmap().centerCrop().into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PicShowActivity.class);
                        intent.putExtra("position",position);
                        mContext.startActivity(intent);
                    }
                });
                holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.img_bg));
                Log.e(TAG, "onBindViewHolder: 清除原本被加框的布局" );
            }else{                                          //如果是空图片，那么根据空图片所在位置进行加框处理
                //这句很关键，用于清除之前可能被加载到框的子选项的状态
                holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.img_bg));

                if (pic.getSeqNo() == 1){
                    holder.imageView.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(R.drawable.noicon).centerCrop().into(holder.imageView);
                    if (s ==1){
                        holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.bound_bg));
                        Log.e(TAG, "onBindViewHolder: 附上框，s = " + s);
                    }
                }
                if (pic.getSeqNo() == 2){
                    if (mTag.equals("W")||mTag.equals("WY")){
                        holder.imageView.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(R.drawable.flooricon).centerCrop().into(holder.imageView);
                    }else if (mTag.equals("D")||mTag.equals("RY")){
                        holder.imageView.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(R.drawable.iconbig).centerCrop().into(holder.imageView);
                    }

                    if (s==2){
                        holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.bound_bg));
                        Log.e(TAG, "onBindViewHolder: 附上框，s = " + s);
                    }
                }
                if (pic.getSeqNo() == 3){
                    if (mTag.equals("W")||mTag.equals("WY")){
                        holder.imageView.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(R.drawable.sideicon).centerCrop().into(holder.imageView);
                    }else if (mTag.equals("D")||mTag.equals("RY")){
                        holder.imageView.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(R.drawable.iconsmall).centerCrop().into(holder.imageView);
                    }

                    if (s==3){
                        holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.bound_bg));
                        Log.e(TAG, "onBindViewHolder: 附上框，s = " + s);
                    }
                }
                if (pic.getSeqNo() == 4){
                    if (mTag.equals("W")||mTag.equals("RY")){
                        Glide.with(mContext).load(R.drawable.plus).centerCrop().into(holder.imageView);
                    }else if (mTag.equals("D")||mTag.equals("WY")){
                        holder.imageView.setBackgroundColor(mContext.getResources().getColor(R.color.alpha));
                        holder.imageView.setImageResource(0);
                    }

                    if (s==4){
                        holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.bound_bg));
                        Log.e(TAG, "onBindViewHolder: 附上框，s = " + s);
                        flag = true;
                    }

                }
                //这里的flag用于判断临界值
                if (pic.getSeqNo() > 4 && flag == false){
                    holder.imageView.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(R.drawable.plus).centerCrop().into(holder.imageView);
                    holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.bound_bg));
                }
                flag = false;

            }

            //holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }

    }

    @Override
    public int getItemCount() {
        return mInsideList.size();
    }

    public class MyViewHodler extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public MyViewHodler(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.picture_took);
        }
    }

    //这个方法用于根据空白图片的数量，计算出此时应该要加载边框的序号、位置
    private void calSum(){
        int i = 0;
        for (Pictures pictures : mInsideList){
            if (pictures.getConNo().equals(Const.NullConNo)){
                i++;
            }
        }
        s = 5 - i;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }
}
