package com.oocl.johngao.smartcr.Interface;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.oocl.johngao.smartcr.Adapter.MetaAdapter;
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

    public ItemTouchCallback(MetaAdapter metaAdapter, Context context){
        super();
        this.mMetaAdapter = metaAdapter;
        this.mContext = context;
        mDataLab = DataLab.get(mContext);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
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
        int adapterPostion = viewHolder.getAdapterPosition();
        Log.e("sdfsd", "onSwiped: 列表上被滑动的位置为  " + adapterPostion + "对应的中文为 "+  mMetaAdapter.getInsideList().get(adapterPostion) );
        mDataLab.deleteItem( mMetaAdapter.getInsideList().get(adapterPostion));
        Log.e("fdff", "onSwiped: " + mMetaAdapter.getInsideList().get(adapterPostion));
        mMetaAdapter.getOnDeleteItemListener().onDeleteItem(mMetaAdapter.getInsideList().get(adapterPostion));

        mMetaAdapter.notifyItemRemoved(adapterPostion);
        mMetaAdapter.getInsideList().remove(adapterPostion);

        mMetaAdapter.getOnMoveItem().onMove();

    }

}
