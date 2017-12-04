package com.oocl.johngao.smartcr.Activity;

import android.content.Context;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.oocl.johngao.smartcr.Adapter.PicListAdapter;
import com.oocl.johngao.smartcr.Data.Pictures;
import com.oocl.johngao.smartcr.MyView.SideBar;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.CameraPreview;
import com.oocl.johngao.smartcr.ToolsClass.DataLab;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TakePhotoActivity extends AppCompatActivity implements CameraPreview.OnCaptureListener{

    public static final String TAG = "CameraSimple";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_photo);
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
    }

    private void initList() {
        mPicList = new ArrayList<>();
        mPicList = mDataLab.getPicturesList();
        if (mPicList.size() >0){
            for (Pictures pictures : mPicList){
                Log.e(TAG, "initList: "+ pictures.getName());
            }
        }else {
            Log.e(TAG, "initList: no pictureList" );
        }

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
    }
}
