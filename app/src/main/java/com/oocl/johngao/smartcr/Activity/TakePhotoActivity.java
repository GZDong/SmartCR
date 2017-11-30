package com.oocl.johngao.smartcr.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.oocl.johngao.smartcr.Adapter.PicListApater;
import com.oocl.johngao.smartcr.R;

import java.util.ArrayList;
import java.util.List;

public class TakePhotoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PicListApater mPicListApater;
    private List<String> mPicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        initList();
        mPicListApater = new PicListApater(this,mPicList);
        mRecyclerView = (RecyclerView) findViewById(R.id.picture_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mPicListApater);
    }

    private void initList(){
        mPicList = new ArrayList<>();
        for (int i = 1;i <= 10;i ++){
            String test = "test";
            mPicList.add(test);
        }
    }
}
