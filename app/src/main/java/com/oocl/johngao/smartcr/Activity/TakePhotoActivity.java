package com.oocl.johngao.smartcr.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.oocl.johngao.smartcr.Adapter.PicListApater;
import com.oocl.johngao.smartcr.R;
import com.oocl.johngao.smartcr.ToolsClass.CameraPreview;

import java.io.BufferedOutputStream;
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
    private int mCameraId = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
    private CameraPreview mPreview;
    private Button mTakePictureBtn;
    private FrameLayout mCameraLayout;

    private RecyclerView mRecyclerView;
    private PicListApater mPicListApater;
    private List<String> mPicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_photo);

        if (ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (!checkCameraHardware(this)) {
            Toast.makeText(this, "相机不支持", Toast.LENGTH_SHORT).show();
        }else {
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(TakePhotoActivity.this, mCamera);
            CameraPreview.setCameraDisplayOrientation(this, mCameraId, mCamera);
            mCameraLayout = (FrameLayout) findViewById(R.id.camera_preview);
            mCameraLayout.addView(mPreview);
        }

        initList();
        mPicListApater = new PicListApater(this, mPicList);
        mRecyclerView = (RecyclerView) findViewById(R.id.picture_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mPicListApater);

        mTakePictureBtn = (Button) findViewById(R.id.btn_capture);
        mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera == null){
                    Log.e(TAG, "1.onCreate: this is null");
                }
                mCamera.takePicture(null, null, mPictureCallback);
            }
        });
    }

    private void initList() {
        mPicList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String test = "test";
            mPicList.add(test);
        }
    }

    //判断相机是否支持
    private boolean checkCameraHardware(Context context) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    // 获取相机
    public static android.hardware.Camera getCameraInstance() {
        android.hardware.Camera camera = null;
        try {
            camera = android.hardware.Camera.open();  //打开相机
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getCameraInstance: 相机打不开");
        }
        return camera;
    }

    //释放相机
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    //拍照回调
    private android.hardware.Camera.PictureCallback mPictureCallback = new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, android.hardware.Camera camera) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            String pictureName = simpleDateFormat.format(date) + ".png";
            File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            final String picturePath = pictureDir + File.separator + pictureName;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(picturePath);
                    try {
                        //获取当前旋转角度，并旋转照片
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,data.length);
                        bitmap = rotateBitmapByDegress(bitmap,90);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
                        bos.flush();
                        bos.close();
                        bitmap.recycle();
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();

            mCamera.startPreview();
        }
    };
    //旋转图片
    public static Bitmap rotateBitmapByDegress(Bitmap bm, int degree){
        Bitmap returnBm = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try{
            returnBm = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(),matrix,true);
        }catch (OutOfMemoryError e){

        }
        if (returnBm == null){
            returnBm = bm;
        }
        if (bm != returnBm){
            bm.recycle();
        }
        return returnBm;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (!checkCameraHardware(this)) {
                        Toast.makeText(TakePhotoActivity.this, "Can not take photo!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mCamera == null){
                            mCamera = getCameraInstance();
                            mPreview = new CameraPreview(TakePhotoActivity.this, mCamera);
                            CameraPreview.setCameraDisplayOrientation(this, mCameraId, mCamera);
                            mCameraLayout = (FrameLayout) findViewById(R.id.camera_preview);
                            mCameraLayout.addView(mPreview);
                        }
                    }
                } else {
                    Toast.makeText(this, "no permission!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }
}
