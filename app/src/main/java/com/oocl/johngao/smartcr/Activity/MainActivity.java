package com.oocl.johngao.smartcr.Activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oocl.johngao.smartcr.Adapter.ConListAdapter;
import com.oocl.johngao.smartcr.Data.Container;
import com.oocl.johngao.smartcr.MyView.MySearchView;
import com.oocl.johngao.smartcr.MyView.MyscrollView;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private ConListAdapter mAdapter;

    private List<Container> mConList;
    private DataLab mDataLab;
    private boolean fixedFlag = false, resetFlag = false;
    private int selectPos = 0;
    private LinearLayout headBarLayout;
    private TextView mFilterText;

    private TextView tv1;
    private TextView tv2;

    private boolean isMenuOpen = false;
    private List<TextView> tvViews = new ArrayList<>();
    private ImageView imgTake;

    private ImageView mListBtn;
    private ImageView mSetBtn;

    private LinearLayout mLeftLY;
    private LinearLayout mRightLY;
    private TextView mlt;
    private TextView mrt;

    private MySearchView mSearchView;
    private LinearLayout mReplaceLY;
    private ImageView mFilterIcon;

    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        initList();
        initView();
    }

    public void initView(){
        /*BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
*/

        mRecyclerView = (RecyclerView) findViewById(R.id.container_list);
        mAdapter = new ConListAdapter(mConList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);

        mFilterText = (TextView) findViewById(R.id.filter_text);
        mFilterText.setOnClickListener(this);

        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);

        mFilterIcon = (ImageView) findViewById(R.id.filter_icon);

        tvViews.add(tv1);
        tvViews.add(tv2);
        imgTake = (ImageView) findViewById(R.id.img_take);
        imgTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMenuOpen){
                    mListBtn.setImageResource(R.drawable.listbtnbefore);
                    mSetBtn.setImageResource(R.drawable.setbefore);
                    showOpenAnim(80);
                    imgTake.setImageResource(R.drawable.takingb);
                    tv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class);
                            intent.putExtra("ConNo",mConList.get(0).getConNo());
                            intent.putExtra("Message","Wash");
                            startActivity(intent);
                        }
                    });
                    tv2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class);
                            intent.putExtra("ConNo",mConList.get(0).getConNo());
                            intent.putExtra("Message","Repair");
                            startActivity(intent);
                        }
                    });
                }else {
                    showCloseAnim(80);
                    imgTake.setImageResource(R.drawable.takinga);
                    if (flag == true){
                        mListBtn.setImageResource(R.drawable.listbtnafter);
                        mSetBtn.setImageResource(R.drawable.setbefore);
                    }else {
                        mListBtn.setImageResource(R.drawable.listbtnbefore);
                        mSetBtn.setImageResource(R.drawable.setafter2);
                    }
                }
            }
        });

        mListBtn = (ImageView) findViewById(R.id.left_btn);
        mSetBtn = (ImageView) findViewById(R.id.right_btn);

        mLeftLY = (LinearLayout) findViewById(R.id.left_layout);
        mRightLY = (LinearLayout) findViewById(R.id.right_layout);

        mlt = (TextView) findViewById(R.id.l_t);
        mrt = (TextView) findViewById(R.id.r_t);

        mListBtn.setOnClickListener(this);
        mSetBtn.setOnClickListener(this);
        mlt.setOnClickListener(this);
        mrt.setOnClickListener(this);

        mSearchView = (MySearchView) findViewById(R.id.search_view);
        mSearchView.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mReplaceLY = (LinearLayout) findViewById(R.id.replace_layout);
        mReplaceLY.setOnClickListener(this);
    }
    public void initList(){
        mDataLab = DataLab.get(this);
        mConList = mDataLab.getContainerList();
        if (mConList.size() == 0){
            Log.e(TAG, "initList: *****");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_btn || v.getId() == R.id.l_t){
            mListBtn.setImageResource(R.drawable.listbtnafter);
            mSetBtn.setImageResource(R.drawable.setbefore);
            flag = true;
        }else if (v.getId() == R.id.right_btn || v.getId() == R.id.r_t){
            mListBtn.setImageResource(R.drawable.listbtnbefore);
            mSetBtn.setImageResource(R.drawable.setafter2);
            flag = false;
        }
        if (v.getId() == R.id.replace_layout){
            mSearchView.setVisibility(View.VISIBLE);
            //手动获得聚焦，如果只有这个，因为没有touch，软键盘不会弹出来，需要再点击一下
            mSearchView.requestFocus();

            //这个强制弹出软键盘是在获得聚焦的前提下
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(mSearchView,0);
            mReplaceLY.setVisibility(View.GONE);
            mFilterText.setText(R.string.cancel);
            mFilterText.setTextColor(getResources().getColor(R.color.theme_blue));
            mFilterIcon.setVisibility(View.GONE);
        }

        if (v.getId() == R.id.filter_text){
            if (mFilterIcon.getVisibility() == View.GONE){
                mReplaceLY.setVisibility(View.VISIBLE);
                mSearchView.setText("");
                mSearchView.setVisibility(View.GONE);
                mFilterText.setText(R.string.filter_hint);
                mFilterText.setTextColor(getResources().getColor(R.color.black));
                mFilterIcon.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }else {
                Intent intent = new Intent(MainActivity.this,FilterActivity.class);
                startActivity(intent);
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_list:

                    return true;
                case R.id.navigation_settings:

                    return true;

                /*case R.id.navigation_take:
                    Intent intent = new Intent(MainActivity.this,TakePhotoActivity.class);
                    startActivity(intent);
                    return true;*/
            }
            return false;
        }
    };

    //打开扇形菜单的属性动画，店铺为半径长度
    private void showOpenAnim(int dp){
        tv1.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.VISIBLE);

        //for循环来开始小图标的出现动画
        for (int i = 0; i < tvViews.size(); i++){
            AnimatorSet set = new AnimatorSet();
            //标题1与x轴负方向角度为20，标题2位100，转换为弧度
            double a ;
            double b ;
            double x ;
            double y ;
            if (i == 0){
                a = -1;
                b = 0;
                x = a * dip2px(dp);
                y = b * dip2px(dp);
            }else {
                a = 1;
                b = 0;
                x = a * dip2px(dp);
                y = b * dip2px(dp);
            }

            set.playTogether(ObjectAnimator.ofFloat(tvViews.get(i),"translationX",(float) (x * 0.10),(float) x),
                    ObjectAnimator.ofFloat(tvViews.get(i),"alpha",0,1).setDuration(2000));
            set.setInterpolator(new BounceInterpolator());
            set.setDuration(1000);
            set.start();

            //转动相机图标
            ObjectAnimator rotate = ObjectAnimator.ofFloat(imgTake,"rotation", 180, 0).setDuration(1000);
            rotate.setInterpolator(new BounceInterpolator());
            rotate.start();


            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isMenuOpen = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }
    //关闭扇形菜单的属性动画，参数与打开时相反
    private void showCloseAnim(int dp){
        for (int i = 0; i < tvViews.size(); i++){
            AnimatorSet set = new AnimatorSet();
            double a ;
            double b ;
            double x ;
            double y ;
            if (i == 0){
                a = -1;
                b = 0;
                x = a * dip2px(dp);
                y = b * dip2px(dp);
            }else {
                a = 1;
                b = 0;
                x = a * dip2px(dp);
                y = b * dip2px(dp);
            }

            set.playTogether(ObjectAnimator.ofFloat(tvViews.get(i),"translationX",(float) x ,(float) (x * 0.10)),
                    ObjectAnimator.ofFloat(tvViews.get(i),"alpha",1,0).setDuration(2000));
        //    set.setInterpolator(new BounceInterpolator());
            set.setDuration(1000);
            set.start();

            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    tv1.setVisibility(View.GONE);
                    tv2.setVisibility(View.GONE);

                    isMenuOpen = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        ObjectAnimator rotate = ObjectAnimator.ofFloat(imgTake, "rotation", 0, 180).setDuration(1500);
        rotate.setInterpolator(new BounceInterpolator());
        rotate.start();
    }

    private int dip2px(int value) {
        float density = getResources()
                .getDisplayMetrics().density;
        return (int) (density * value + 0.5f);
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
