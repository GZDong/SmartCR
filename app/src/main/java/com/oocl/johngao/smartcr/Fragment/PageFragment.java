package com.oocl.johngao.smartcr.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oocl.johngao.smartcr.Activity.MainActivity;
import com.oocl.johngao.smartcr.Adapter.ConListAdapter;
import com.oocl.johngao.smartcr.Data.Container;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johngao on 17/12/14.
 */

public class PageFragment extends Fragment implements MainActivity.OnSearchListener{
    public static final String TAG = "PageFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;


    private RecyclerView mRecyclerView;
    private ConListAdapter mAdapter;

    private List<Container> mConList;
    private DataLab mDataLab;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        mDataLab = DataLab.get(getActivity());
        mConList = mDataLab.getContainerList(mPage);
        MainActivity mainActivity =(MainActivity) getActivity();
        if (mPage == 1){
            mainActivity.setOnSearchListener1(this);
        }else {
            mainActivity.setOnSearchListener2(this);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.container_list);
        mAdapter = new ConListAdapter(mConList,getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter!=null){
            mConList.clear();
            mConList = mDataLab.getContainerList(mPage);
            mAdapter.updateList(mConList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearch(String s) {
        Log.e(TAG, "onSearch:" + mPage );
        filterData(s);
    }

    private void filterData(String index){
        List<Container> filterList = new ArrayList<>();
        if (TextUtils.isEmpty(index)){
            filterList = mConList;
        }else{
            filterList.clear();
            for(Container container : mConList){
                int l = container.getConNo().length();
                if (container.getConNo().substring(l-4,l).indexOf(index)!=-1){
                    filterList.add(container);
                }
            }
        }
        mAdapter.updateList(filterList);
    }
}
