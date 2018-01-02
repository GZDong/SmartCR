package com.oocl.johngao.smartcr.Fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oocl.johngao.smartcr.Adapter.HintAdapter;
import com.oocl.johngao.smartcr.Adapter.MetaAdapter;
import com.oocl.johngao.smartcr.Adapter.MoreListAdapter;
import com.oocl.johngao.smartcr.Interface.ItemTouchCallback;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import java.util.List;


public class RuleFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = "onRuleFragment";

    private DataLab mDataLab;
    private RecyclerView mHintRY;
    private RecyclerView mMetaRY;
    private List<String> mHintList;
    private List<String> mMetaList;
    private HintAdapter mHintAdapter;
    private MetaAdapter mMetaAdapter;

    private MoreListAdapter mMoreListAdapter;
    private RecyclerView mMoreRY;

    private FrameLayout mContainerLayout;
    private LinearLayout mFrontLayout;
    private FrameLayout mBackLayout;
    private AnimatorSet mRightOutAnimSet,mLeftInAnimSet;
    private boolean mIsShowBack = false;

    private ImageView mEditImg;
    private Button mFinishBtn;

    private Button mMakeSureBtn;

    private List<String> mMoreList;
    public RuleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        mContainerLayout = (FrameLayout) view.findViewById(R.id.side_container);
        mFrontLayout = (LinearLayout) view.findViewById(R.id.front_side);
        mBackLayout = (FrameLayout) view.findViewById(R.id.back_side);
        setAnimators();
        setCameraDistance();

        mHintRY = view.findViewById(R.id.list_hint);
        mMetaRY = view.findViewById(R.id.list_meta);
        mMoreRY = view.findViewById(R.id.more_list);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        mHintAdapter = new HintAdapter(getActivity(),mHintList);
        mHintRY.setAdapter(mHintAdapter);
        mHintRY.setLayoutManager(lm);

        LinearLayoutManager lm2 = new LinearLayoutManager(getActivity());
        mMoreListAdapter = new MoreListAdapter(getActivity(),mMoreList);
        mMoreRY.setAdapter(mMoreListAdapter);
        mMoreRY.setLayoutManager(lm2);

        LinearLayoutManager lm1 = new LinearLayoutManager(getActivity());
        mMetaAdapter = new MetaAdapter(getActivity(),mMetaList,mHintAdapter,mMoreListAdapter);
        mMetaRY.setAdapter(mMetaAdapter);
        mMetaRY.setLayoutManager(lm1);

        ItemTouchCallback itemTouchCallback = new ItemTouchCallback(mMetaAdapter,getActivity());
        itemTouchCallback.setOnAddItemListener(mHintAdapter);
        itemTouchCallback.setOnCancelAddListener(mMoreListAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mMetaRY);


        mMoreListAdapter.setOnAddItemListener(mMetaAdapter);
        mMoreListAdapter.setOnAddItemListener2(mHintAdapter);

        mEditImg = (ImageView) view.findViewById(R.id.edit_img);
        mFinishBtn = (Button) view.findViewById(R.id.finish_btn);
        mEditImg.setOnClickListener(this);
        mFinishBtn.setOnClickListener(this);

    }

    private void initList(){
        //这里的3是暂时的，需要到数据里查看目前是记录了几个

        mDataLab.initMetaList();
        mMetaList = mDataLab.getMetaList();

        mDataLab.initHintList(mDataLab.getMetaSum());
        mHintList = mDataLab.getHintList();

        mMoreList = mDataLab.getMoreList();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAnimators(){
        mRightOutAnimSet = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),R.animator.anim_right_out);
        mLeftInAnimSet = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),R.animator.anim_left_in);

        mRightOutAnimSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mContainerLayout.setClickable(false);
            }
        });

        mLeftInAnimSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mContainerLayout.setClickable(true);
            }
        });
    }

    private void setCameraDistance(){
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mBackLayout.setCameraDistance(scale);
        mFrontLayout.setCameraDistance(scale);
    }


    private void flipCard(){
        if (!mIsShowBack){
            mRightOutAnimSet.setTarget(mFrontLayout);
            mLeftInAnimSet.setTarget(mBackLayout);
            mRightOutAnimSet.start();
            mLeftInAnimSet.start();
            mIsShowBack = true;
        }else {
            mRightOutAnimSet.setTarget(mBackLayout);
            mLeftInAnimSet.setTarget(mFrontLayout);
            mRightOutAnimSet.start();
            mLeftInAnimSet.start();
            mIsShowBack = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_img:
            case R.id.finish_btn:
                flipCard();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRightOutAnimSet.cancel();
        mLeftInAnimSet.cancel();
    }
}
