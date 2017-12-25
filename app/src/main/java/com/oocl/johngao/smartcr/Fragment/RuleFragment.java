package com.oocl.johngao.smartcr.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oocl.johngao.smartcr.Adapter.HintAdapter;
import com.oocl.johngao.smartcr.Adapter.MetaAdapter;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import java.util.List;


public class RuleFragment extends Fragment {

    public static final String TAG = "onRuleFragment";

    private DataLab mDataLab;
    private RecyclerView mHintRY;
    private RecyclerView mMetaRY;
    private List<String> mHintList;
    private List<String> mMetaList;
    private HintAdapter mHintAdapter;
    private MetaAdapter mMetaAdapter;

    private Button mMakeSureBtn;

    public RuleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataLab = DataLab.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rule, container, false);
        initList();
        initView(view);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView(View view){
        mHintRY = view.findViewById(R.id.list_hint);
        mMetaRY = view.findViewById(R.id.list_meta);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        mHintAdapter = new HintAdapter(getActivity(),mHintList);
        mHintRY.setAdapter(mHintAdapter);
        mHintRY.setLayoutManager(lm);


        LinearLayoutManager lm1 = new LinearLayoutManager(getActivity());
        mMetaAdapter = new MetaAdapter(getActivity(),mMetaList);
        mMetaRY.setAdapter(mMetaAdapter);
        mMetaRY.setLayoutManager(lm1);

    }

    private void initList(){
        //这里的3是暂时的，需要到数据里查看目前是记录了几个

        mDataLab.initMetaList();
        mMetaList = mDataLab.getMetaList();

        mDataLab.initHintList(mDataLab.getMetaSum());
        mHintList = mDataLab.getHintList();

    }
}
