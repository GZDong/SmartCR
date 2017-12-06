package com.oocl.johngao.smartcr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.Snackbar;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by johngao on 17/11/30.
 */

public class PicListAdapter extends RecyclerView.Adapter<PicListAdapter.MyViewHodler> {
    public static final String TAG = "PicListAdapter";
    public boolean flag = false;

    private Context mContext;
    private List<Pictures> mInsideList;

    public PicListAdapter(Context context, List<Pictures> list) {
        mContext = context;
        mInsideList = list;
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
            if (!pic.getConNo().equals(Const.NullConNo)){
                File file = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                String path = file + File.separator+ pic.getTCode()+ File.separator + pic.getName();
                Log.e(TAG, "onBindViewHolder: to show pic from : " +path );

                /*BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
                Bitmap bitmap = BitmapFactory.decodeStream(in);*/
                Glide.with(mContext).load(path).asBitmap().centerCrop().into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PicShowActivity.class);
                        intent.putExtra("position",position);
                        mContext.startActivity(intent);
                    }
                });
            }else{
                if (pic.getSeqNo() == 1){
                    Glide.with(mContext).load(R.drawable.noicon).centerCrop().into(holder.imageView);
                    holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.bound_bg));
                    flag = true;
                }
                if (pic.getSeqNo() == 2){
                    Glide.with(mContext).load(R.drawable.flooricon).centerCrop().into(holder.imageView);
                    if (flag = false){
                        holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.bound_bg));
                        flag = true;
                    }
                }
                if (pic.getSeqNo() == 3){
                    Glide.with(mContext).load(R.drawable.sideicon).centerCrop().into(holder.imageView);
                    if (flag = false){
                        holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.bound_bg));
                        flag = true;
                    }
                }
                if (pic.getSeqNo() == 4){
                    Glide.with(mContext).load(R.drawable.plus).centerCrop().into(holder.imageView);
                    if (flag = false){
                        holder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.bound_bg));
                        flag = true;
                    }
                }
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
}
