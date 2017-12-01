package com.oocl.johngao.smartcr.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.oocl.johngao.smartcr.Adapter.PicListApater;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TakePhotoActivity extends AppCompatActivity {

    public static final String TAG = "CameraSimple";
    private android.hardware.Camera mCamera;
    private CameraPreview mPreview;
    private Button mTakePictureBtn;
    private FrameLayout mCameraLayout;

    private RecyclerView mRecyclerView;
    private PicListApater mPicListApater;
    private List<String> mPicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        if (ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(TakePhotoActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TakePhotoActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        initList();
        mPicListApater = new PicListApater(this,mPicList);
        mRecyclerView = (RecyclerView) findViewById(R.id.picture_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mPicListApater);

        mTakePictureBtn = (Button) findViewById(R.id.btn_capture);
        mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null,null,mPictureCallback);
            }
        });


    }

    private void initList(){
        mPicList = new ArrayList<>();
        for (int i = 1;i <= 10;i ++){
            String test = "test";
            mPicList.add(test);
        }
    }

    //判断相机是否支持
    private boolean checkCameraHardware(Context context){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        }else{
            return false;
        }
    }

    // 获取相机
    public static android.hardware.Camera getCameraInstance(){
        android.hardware.Camera camera = null;
        try{
            camera = android.hardware.Camera.open();  //打开相机
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "getCameraInstance: 相机打不开");
        }
        return camera;
    }

    //拍照回调
    private android.hardware.Camera.PictureCallback mPictureCallback = new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (pictureDir == null){
                Log.d(TAG,"Error creating media file, check storage permissions!");
                return;
            }
            try {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
                String pictureName = simpleDateFormat.format(date) + ".png";
                FileOutputStream fos = new FileOutputStream(pictureDir + File.separator + pictureName);
                fos.write(data);
                fos.close();
            }catch (FileNotFoundException e){
                Log.d(TAG, "File not found: " + e.getMessage());
            }catch (IOException e){
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    if (!checkCameraHardware(this)){
                        Toast.makeText(TakePhotoActivity.this, "Can not take photo!", Toast.LENGTH_SHORT).show();
                    }else {
                        mCamera = getCameraInstance();
                        mPreview = new CameraPreview(TakePhotoActivity.this,mCamera);
                        mCameraLayout = (FrameLayout) findViewById(R.id.camera_preview);
                        mCameraLayout.addView(mPreview);
                    }
                }else {
                    Toast.makeText(this, "no permission!", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
