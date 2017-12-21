package com.oocl.johngao.smartcr.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oocl.johngao.smartcr.Data.Pictures;
import com.oocl.johngao.smartcr.Fragment.PicturesFragment;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import java.util.List;

/**
 * Created by johngao on 17/12/5.
 */

public class MyFragmentStateAdapter extends FragmentStatePagerAdapter {

    List<Pictures> mList;
    private Context mContext;
    private DataLab mDataLab;

    public MyFragmentStateAdapter(FragmentManager fm,Context context){
        super(fm);
        mContext = context;
        this.mList = DataLab.get(mContext).getPicturesList();
    }

    public MyFragmentStateAdapter(FragmentManager fm,Context context,String ConNo){
        super(fm);
        mContext = context;
        mDataLab = DataLab.get(mContext);
        this.mList = mDataLab.getPics(ConNo);
    }

    //返回需要展示的fragment


    @Override
    public Fragment getItem(int position) {
        return PicturesFragment.newInstance(mList.get(position));
    }

    //返回fragment总量

    @Override
    public int getCount() {
        return mList.size();
    }
}
