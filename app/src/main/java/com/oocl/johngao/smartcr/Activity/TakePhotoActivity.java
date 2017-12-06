package com.oocl.johngao.smartcr.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.oocl.johngao.smartcr.Adapter.PicListAdapter;
import com.oocl.johngao.smartcr.Data.Pictures;
import com.oocl.johngao.smartcr.MyView.SideBar;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.CameraPreview;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import org.w3c.dom.Text;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

public class TakePhotoActivity extends AppCompatActivity implements CameraPreview.OnCaptureListener{

    public static final String TAG = "TakePhotoActivity";
    private CameraPreview mPreview;
    private Button mTakePictureBtn;
    private FrameLayout mCameraLayout;
    private CameraPreview mCameraPreview;

    private RecyclerView mRecyclerView;
    private PicListAdapter mPicListAdapter;
    private List<Pictures> mPicList;
    private TextView mDialog;
    private SideBar mSideBar;
    private TextView mNameText;

    private DataLab mDataLab;
    private BroadcastReceiver mBroadcastReceiver;

    private ImageButton mImageButton;

    private int flag = 0;
    private boolean swtch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_photo);
        Log.e(TAG, "onCreate: ");
        mDataLab = DataLab.get(this);

        //检查相机是否可用
        if (!checkCameraHardware(this)) {
            Toast.makeText(this, "相机不支持", Toast.LENGTH_SHORT).show();
        }else {
            Log.e(TAG, "onCreate: Here, start to init Camera :");
            //初始化相机
            initCamera();
        }

        initList();
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.oocl.john.fresh");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "onReceive: 拍照完用于刷新照片列表和重启手电筒的接受器");
                mPicListAdapter.notifyDataSetChanged();

                if (swtch == true){
                    Intent intent1 = new Intent("com.oocl.john.switchlight");
                    intent1.putExtra("sign",false);
                    sendBroadcast(intent1);
                    Intent intent2 = new Intent("com.oocl.john.switchlight");
                    intent2.putExtra("sign",true);
                    sendBroadcast(intent2);
                    Log.e(TAG, "onReceive: 线程执行完，先发送关灯广播，再发送开灯广播");
                }else {
                    Intent intent1 = new Intent("com.oocl.john.switchlight");
                    intent1.putExtra("sign",false);
                    sendBroadcast(intent1);
                    Log.e(TAG, "onReceive: 线程执行完再发送关灯广播");
                }
            }
        };
        registerReceiver(mBroadcastReceiver,intentFilter);

    }


    private void initList() {
        mPicList = new ArrayList<>();
        mPicList = mDataLab.getPicturesList();

        /*for (int i = 1; i <= 10; i++) {
            String test = "test";
            mPicList.add(test);
        }*/

    }

    //判断相机是否支持
    private boolean checkCameraHardware(Context context) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    // 打开相机设备
    /*public static android.hardware.Camera getCameraInstance() {
        android.hardware.Camera camera = null;
        try {
            camera = android.hardware.Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getCameraInstance: 相机打不开");
        }
        return camera;
    }*/

    /*//释放相机设备
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }*/
    //初始化相机
    public void initCamera(){
        mPreview = new CameraPreview(TakePhotoActivity.this);
        mPreview.setOnCaptureListener(this);
        /*mPreview.setOnTouchListener(new View.OnTouchListener() {   //设置聚焦
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mCamera.autoFocus(null);
                return false;
            }
        });*/
        mCameraLayout = (FrameLayout) findViewById(R.id.camera_preview);
        mCameraLayout.addView(mPreview);
    }

    public void initView(){
        mPicListAdapter = new PicListAdapter(this, mPicList);
        mRecyclerView = (RecyclerView) findViewById(R.id.picture_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mPicListAdapter);
        mRecyclerView.scrollToPosition(mPicList.size()-1);

        //mCameraPreview = (CameraPreview) findViewById(R.id.camera_view);

        mTakePictureBtn = (Button) findViewById(R.id.btn_capture);
        mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mCamera.autoFocus(mAutoFocusCallback);*/
                mPreview.takePicture();
            }
        });

        mDialog = (TextView) findViewById(R.id.dialog);
        mSideBar = (SideBar) findViewById(R.id.sideBar);
        mSideBar.setTextView(mDialog);

        mNameText = (TextView) findViewById(R.id.picture_name);

        mImageButton = (ImageButton) findViewById(R.id.light_btn);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swtch == true){
                    Intent intent = new Intent("com.oocl.john.switchlight");
                    intent.putExtra("sign",false);
                    sendBroadcast(intent);
                    Log.e(TAG, "onClick: 在左下角点击后发送关灯广播");
                    swtch = false;
                    mImageButton.setImageResource(R.drawable.offs);
                }else {
                    Intent intent = new Intent("com.oocl.john.switchlight");
                    intent.putExtra("sign",true);
                    sendBroadcast(intent);
                    Log.e(TAG, "onClick: 在左下角点击后发送开灯广播");
                    swtch = true;
                    mImageButton.setImageResource(R.drawable.ons);
                }
            }
        });
    }
    //聚焦回调
    /*private android.hardware.Camera.AutoFocusCallback mAutoFocusCallback = new android.hardware.Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, android.hardware.Camera camera) {
            if (success){
                mCamera.takePicture(null,null,mPictureCallback);
            }
        }
    };*/

    @Override
    public void onCapture(String name) {
        mNameText.setText(name);
        mNameText.setVisibility(View.VISIBLE);
        mPicListAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mPicList.size()-1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        flag = 1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (flag ==1){
            mCameraLayout.removeAllViews();
            mDataLab = DataLab.get(this);

            //检查相机是否可用
            if (!checkCameraHardware(this)) {
                Toast.makeText(this, "相机不支持", Toast.LENGTH_SHORT).show();
            }else {
                //初始化相机
                initCamera();
            }

            initList();
            initView();
            flag = 0;
            if (swtch == true) {
                swtch = false;
                mImageButton.setImageResource(R.drawable.offs);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        unregisterReceiver(mBroadcastReceiver);
    }
}
