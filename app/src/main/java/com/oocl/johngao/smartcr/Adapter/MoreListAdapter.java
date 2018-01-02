package com.oocl.johngao.smartcr.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oocl.johngao.smartcr.Interface.onAddItemListener;
import com.oocl.johngao.smartcr.Interface.onCancelAddListener;
import com.oocl.johngao.smartcr.Interface.onDeleteItemListener;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import java.util.List;

/**
 * Created by johngao on 17/12/26.
 */

public class MoreListAdapter extends RecyclerView.Adapter<MoreListAdapter.MyViewHolder> implements onDeleteItemListener,onCancelAddListener {

    private List<String> mInsideList;
    private Context mContext;
    private onAddItemListener mOnAddItemListener;
    private onAddItemListener mOnAddItemListener2;
    private DataLab mDataLab;

    public MoreListAdapter(Context context,List<String> list){
        mContext = context;
        mInsideList = list;
        mDataLab = DataLab.get(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_more_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (mInsideList.size()>0){
            switch (mInsideList.get(position)){
                case "C":
                    holder.mLinearLayout.setVisibility(View.VISIBLE);
                    holder.mImageView.setVisibility(View.VISIBLE);
                    holder.mTextView.setText("柜 号");
                    break;
                case "T":
                    holder.mLinearLayout.setVisibility(View.VISIBLE);
                    holder.mImageView.setVisibility(View.VISIBLE);
                    holder.mTextView.setText("操 作 码");
                    break;
                case "S":
                    holder.mLinearLayout.setVisibility(View.VISIBLE);
                    holder.mImageView.setVisibility(View.VISIBLE);
                    holder.mTextView.setText("序 号");
                    break;
                case "t":
                    holder.mLinearLayout.setVisibility(View.VISIBLE);
                    holder.mImageView.setVisibility(View.VISIBLE);
                    holder.mTextView.setText("时 间");
                    break;
                case "x":
                    holder.mLinearLayout.setVisibility(View.VISIBLE);
                    holder.mImageView.setVisibility(View.VISIBLE);
                    holder.mTextView.setText("公 司");
                    break;
                case "n":
                    holder.mLinearLayout.setVisibility(View.GONE);
                    holder.mImageView.setVisibility(View.GONE);
                    break;
            }
        }
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnAddItemListener.onAddItem(mInsideList.get(position));
                Log.e("dscvsd", "onClick: " + mInsideList.get(position) );
                mOnAddItemListener2.onAddItem(mInsideList.get(position));
                holder.mLinearLayout.setVisibility(View.GONE);
                holder.mImageView.setVisibility(View.GONE);
                mDataLab.deleteMore(mInsideList.get(position));
                updateList(mDataLab.getMoreList());
            }
        });
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
        private TextView mTextView;
        private LinearLayout mLinearLayout;
        private Button mButton;
        private ImageView mImageView;

        public MyViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.add_hint_text);
            mLinearLayout = (LinearLayout) view.findViewById(R.id.layout);
            mButton = (Button) view.findViewById(R.id.add_meta_btn);
            mImageView = (ImageView) view.findViewById(R.id.line_img);
        }
    }

    @Override
    public void onDeleteItem(String item) {
        String string;
        switch (item){
            case "柜 号":
                string = "C";
                break;
            case "操 作 码":
                string = "T";
                break;
            case "序 号":
                string = "S";
                break;
            case "时 间":
                string = "t";
                break;
            case "公 司":
                string = "x";
                break;
            default:
                string = "n";
        }
        mDataLab.addMore(string);
        updateList(mDataLab.getMoreList());
    }

    @Override
    public void cancelAdd(int pos, String s) {
        String str;
        int index = 0;
        for (int i = 0 ; i < mInsideList.size();i++){
            str = mInsideList.get(i);
            if (str.equals(s)){
                index = i;
                break;
            }
        }
        mDataLab.deleteMore(s);
        notifyDataSetChanged();
    }

    public void setOnAddItemListener(onAddItemListener onAddItemListener) {
        mOnAddItemListener = onAddItemListener;
    }
    public void setOnAddItemListener2(onAddItemListener onAddItemListener2){
        mOnAddItemListener2 = onAddItemListener2;
    }
}
