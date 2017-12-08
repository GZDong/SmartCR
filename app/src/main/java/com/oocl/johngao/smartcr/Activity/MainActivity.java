package com.oocl.johngao.smartcr.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.oocl.johngao.smartcr.Adapter.ConListAdapter;
import com.oocl.johngao.smartcr.Data.Container;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private ConListAdapter mAdapter;
    private FloatingActionButton mFAB;
    private List<Container> mConList;
    private DataLab mDataLab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();
        initView();
    }

    public void initView(){
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mRecyclerView = (RecyclerView) findViewById(R.id.container_list);
        mAdapter = new ConListAdapter(mConList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mFAB = (FloatingActionButton) findViewById(R.id.capture_flt_btn);
    }
    public void initList(){
        mDataLab = DataLab.get(this);
        mConList = mDataLab.getContainerList();
        if (mConList.size() == 0){
            Log.e(TAG, "initList: *****");
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

}
