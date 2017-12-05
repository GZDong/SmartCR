package com.oocl.johngao.smartcr.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.oocl.johngao.smartcr.Adapter.MyFragmentStateAdapter;
import com.oocl.johngao.smartcr.R;

public class PicShowActivity extends AppCompatActivity {

    private MyFragmentStateAdapter mAdapter;

    private ViewPager mPager;

    public static Intent newInstance(Context context, int position){
        Intent intent = new Intent(context, PicShowActivity.class);
        intent.putExtra("position",position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pic_show);
        initViews();
    }

    private void initViews(){
        mPager = (ViewPager) findViewById(R.id.ivew_pager);
        mAdapter = new MyFragmentStateAdapter(getSupportFragmentManager(),this);
        mPager.setAdapter(mAdapter);
        int position = getIntent().getIntExtra("position",1);
        mPager.setCurrentItem(position);
    }
}
