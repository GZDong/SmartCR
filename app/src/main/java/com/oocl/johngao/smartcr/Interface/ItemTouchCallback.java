package com.oocl.johngao.smartcr.Interface;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.oocl.johngao.smartcr.Adapter.MetaAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by johngao on 17/12/26.
 */

public class ItemTouchCallback extends ItemTouchHelper.Callback {

    private MetaAdapter mMetaAdapter;

    public ItemTouchCallback(MetaAdapter metaAdapter){
        super();
        this.mMetaAdapter = metaAdapter;
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
        mMetaAdapter.notifyItemMoved(fromPostion,toPostion);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int adapterPostion = viewHolder.getAdapterPosition();
        mMetaAdapter.getOnDeleteItemListener().onDeleteItem(mMetaAdapter.getInsideList().get(adapterPostion));
        mMetaAdapter.notifyItemRemoved(adapterPostion);
        mMetaAdapter.getInsideList().remove(adapterPostion);
        mMetaAdapter.getOnMoveItem().onMove();
    }

}
