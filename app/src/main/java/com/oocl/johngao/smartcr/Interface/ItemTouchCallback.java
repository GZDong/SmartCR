package com.oocl.johngao.smartcr.Interface;

import android.content.Context;
import android.graphics.Canvas;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oocl.johngao.smartcr.Adapter.MetaAdapter;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by johngao on 17/12/26.
 */

public class ItemTouchCallback extends ItemTouchHelper.Callback {

    private MetaAdapter mMetaAdapter;
    private Context mContext;
    private DataLab mDataLab;
    private onAddItemListener mOnAddItemListener;
    private onCancelAddListener mOnCancelAddListener;

    //限制ImageView长度所能增加的最大值
    private double ICON_MAX_SIZE = 50;
    //ImageView的初始长宽
    private int fixedWidth = 150;

    public ItemTouchCallback(MetaAdapter metaAdapter, Context context){
        super();
        this.mMetaAdapter = metaAdapter;
        this.mContext = context;
        mDataLab = DataLab.get(mContext);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags =  ItemTouchHelper.END;
            return  makeMovementFlags(dragFlags,swipeFlags);
        }

        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPostion = viewHolder.getAdapterPosition();
        int toPostion = target.getAdapterPosition();
        if (fromPostion < toPostion){
            for (int i = fromPostion; i<toPostion;i++){
                Collections.swap(mMetaAdapter.getInsideList(),i,i + 1);
            }
        }else {
            for (int i = fromPostion; i > toPostion;i--){
                Collections.swap(mMetaAdapter.getInsideList(),i,i - 1);
            }
        }
        mDataLab.resort(mMetaAdapter.getInsideList().get(fromPostion),mMetaAdapter.getInsideList().get(toPostion));
        mMetaAdapter.notifyItemMoved(fromPostion,toPostion);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        final int adapterPostion = viewHolder.getAdapterPosition();

        final String str = mDataLab.exchangeToP(mMetaAdapter.getInsideList().get(adapterPostion));

        Log.e("sdfsd", "onSwiped: 列表上被滑动的位置为  " + adapterPostion + "对应的中文为 "+  mMetaAdapter.getInsideList().get(adapterPostion) );
        mDataLab.deleteItem( mMetaAdapter.getInsideList().get(adapterPostion));
        Log.e("fdff", "onSwiped: " + mMetaAdapter.getInsideList().get(adapterPostion));
        mMetaAdapter.getOnDeleteItemListener().onDeleteItem(mMetaAdapter.getInsideList().get(adapterPostion));

        mMetaAdapter.notifyItemRemoved(adapterPostion);
        mMetaAdapter.getInsideList().remove(adapterPostion);

        mMetaAdapter.getOnMoveItem().onMove();



        Snackbar.make(viewHolder.itemView,"选项已删除",Snackbar.LENGTH_LONG).setAction("撤销删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMetaAdapter.CancelAdd(adapterPostion,str);
                mOnAddItemListener.onAddItem(" ");
                mOnCancelAddListener.cancelAdd(adapterPostion,str);
            }
        }).setActionTextColor(mContext.getResources().getColor(R.color.theme_blue)).show();
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_gray));
            TextView textView =  (TextView) viewHolder.itemView.findViewById(R.id.text_mata_name);
            textView.setTextColor(mContext.getResources().getColor(R.color.white) );
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_normal));
        TextView textView =  (TextView) viewHolder.itemView.findViewById(R.id.text_mata_name);
        textView.setTextColor(mContext.getResources().getColor(R.color.black) );
    }

    /*@Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        //仅对侧滑状态下的效果做出改变
        if (actionState ==ItemTouchHelper.ACTION_STATE_SWIPE){
            //如果dX小于等于删除方块的宽度，那么我们把该方块滑出来
            if (Math.abs(dX) <= getSlideLimitation(viewHolder)){
                viewHolder.itemView.scrollTo(-(int) dX,0);
            }
            //如果dX还未达到能删除的距离，此时慢慢增加“眼睛”的大小，增加的最大值为ICON_MAX_SIZE
            else if (Math.abs(dX) <= recyclerView.getWidth() / 2){
                double distance = (recyclerView.getWidth() / 2 -getSlideLimitation(viewHolder));
                double factor = ICON_MAX_SIZE / distance;
                double diff =  (Math.abs(dX) - getSlideLimitation(viewHolder)) * factor;
                if (diff >= ICON_MAX_SIZE)
                    diff = ICON_MAX_SIZE;
                ((MetaAdapter.MyViewHolder)viewHolder).mTV.setText("");   //把文字去掉
                ((MetaAdapter.MyViewHolder) viewHolder).mIV.setVisibility(View.VISIBLE);  //显示眼睛
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ((MetaAdapter.MyViewHolder) viewHolder).mIV.getLayoutParams();
                params.width = (int) (fixedWidth + diff);
                params.height = (int) (fixedWidth + diff);
                ((MetaAdapter.MyViewHolder) viewHolder).mIV.setLayoutParams(params);
            }
        }else {
            //拖拽状态下不做改变，需要调用父类的方法
            super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
        }

    }

    *//**
     * 获取删除方块的宽度
     *//*
    public int getSlideLimitation(RecyclerView.ViewHolder viewHolder){
        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
        return viewGroup.getChildAt(1).getLayoutParams().width;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //重置改变，防止由于复用而导致的显示问题
        viewHolder.itemView.setScrollX(0);
        ((MetaAdapter.MyViewHolder)viewHolder).mTV.setText("左滑删除");
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ((MetaAdapter.MyViewHolder) viewHolder).mIV.getLayoutParams();
        params.width = 150;
        params.height = 150;
        ((MetaAdapter.MyViewHolder) viewHolder).mIV.setLayoutParams(params);
        ((MetaAdapter.MyViewHolder) viewHolder).mIV.setVisibility(View.INVISIBLE);

    }*/

    public void setOnAddItemListener(onAddItemListener onAddItemListener) {
        mOnAddItemListener = onAddItemListener;
    }

    public void setOnCancelAddListener(onCancelAddListener onCancelAddListener) {
        mOnCancelAddListener = onCancelAddListener;
    }
}
