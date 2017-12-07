package com.oocl.johngao.smartcr.ToolsClass;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.oocl.johngao.smartcr.Data.Pictures;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by johngao on 17/12/1.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Camera.AutoFocusCallback{

    private static final String TAG = "CameraPreview";
    private static final double Rate = 0.77;
    private int mCameraId = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters parameters;
    private Context mContext;

    private int mScreenWidth;
    private int mScreenHeight;

    private OnCaptureListener mOnCaptureListener;
    private DataLab mDataLab;
    private BroadcastReceiver mBroadcastReceiver;

    private boolean mSwitch = false;

    public CameraPreview(Context context) {
        super(context);
        mContext = context;
        Log.e(TAG, "CameraPreview: 1. 调用 SurfaceView的构造方法，创建surfaceView实例" );
        initView();
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        Log.e(TAG, "CameraPreview: 2" );
        initView();
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        Log.e(TAG, "CameraPreview: 3" );
        initView();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, " 3.调用 surfaceCreated 方法");
        mCamera = getCameraInstance();     //创建Camera实例，一般也在这个方法进行子线程操作
        //mCamera = Camera.open();
        setCameraDisplayOrientation((Activity) mContext,mCameraId,mCamera);
        try {
            mCamera.setPreviewDisplay(holder); //holder与Camera实例进行绑定，摄像头显示在Surface上
        }catch (IOException e){
            e.printStackTrace();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.oocl.john.switchlight");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra("sign",false)==true){
                    Log.e(TAG, "onReceive: 获得广播去打开手电筒");
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(parameters);
                    mSwitch = true;

                }else if (intent.getBooleanExtra("sign",false)==false){
                    Log.e(TAG, "onReceive: 获得广播去关闭手电筒");
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(parameters);
                    mSwitch = false;
                }
            }
        };
        Log.e(TAG, "surfaceCreated: 注册广播接受器");
        mContext.registerReceiver(mBroadcastReceiver,intentFilter);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "4 : 调用 surfaceChanged 方法,在这里");
        /*if (mSurfaceHolder.getSurface() == null){
            return;
        }
        try {
            mCamera.stopPreview();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        }catch (Exception e){
            e.printStackTrace();
        }*/
        setCameraParams(mCamera,mScreenWidth,(int) (mScreenHeight*Rate));
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed方法被调用");
        releaseCamera();    //释放资源
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success){
            Log.i(TAG, "onAutoFocus success" + success);
        }
    }

    public void initView(){
        Log.e(TAG, "initView: 2. 创建和初始化 SurfaceHodler");
        mDataLab = DataLab.get(mContext);
        getScreenMetrix(mContext);   //获取屏幕长宽的像素
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation){
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degree)%360;
            result = (360 - result) % 360;
        }else {
            result = (info.orientation - degree + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private void getScreenMetrix(Context context){
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    private android.hardware.Camera getCameraInstance() {
        try {
            if (mCamera == null){
                mCamera = Camera.open();
                Log.e(TAG, "getCameraInstance : 3.1 .在Created方法里用open获得Camera实例");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getCameraInstance: 相机打不开");
        }
        return mCamera;
    }
    //释放相机设备
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mSurfaceHolder = null;
        }
        mOnCaptureListener = null;
        mContext.unregisterReceiver(mBroadcastReceiver);
        Log.e(TAG, "releaseCamera: 释放资源，Camera holder listener receiver 被释放" );
    }

    /**
     * 这个方法用于根据屏幕像素设置指定Camera的相关参数
     * 主要参数：Preview大小、pictureview大小、FrameLayout大小
     * @param camera
     * @param width
     * @param height
     */
    private void setCameraParams(Camera camera, int width, int height){
        Log.e(TAG, "setCameraParams: 配置相机系数，开始配置。。。" );
        Log.i(TAG, "setCameraParams:  width = " + width + "height" + height);
        parameters = camera.getParameters();  //获取指定Camera的参数对象
        //通过参数对象获取Camera所能支持的PictureSize列表
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        for (Camera.Size size : pictureSizeList){
            Log.i(TAG, "pictureSizeList size.width = " + size.width + "size height = " + size.height);
        }

        //从能使用的size列表中选取合适的大小作为picture大小
        Camera.Size picSize = getProperSize(pictureSizeList,((float) height / width));

        if (picSize == null){      //没有就直接使用摄像头的当前所设置的picture大小
            Log.i(TAG, "picSize == null");
            picSize = parameters.getPictureSize();
        }

        Log.i(TAG, "picSize.width=" + picSize.width + "  picSize.height=" + picSize.height);
        //根据选出来的Size重新设置pictureSize大小
        float w = picSize.width;
        float h = picSize.height;
        parameters.setPictureSize(picSize.width,picSize.height); //重新设置pictureview的大小

        //设置SurfaceView的父布局打下
        this.setLayoutParams(new FrameLayout.LayoutParams((int) (height*(h/w)),height)); //高度不变，宽度按比例缩放

        //通过参数对象获取Camera所能支持的PreviewSize列表
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();

        for (Camera.Size size : previewSizeList){
            Log.i(TAG, "previewSizeList size.width=" +size.width +"size.height" + size.height);
        }

        //从能使用的size列表中选取合适的大小作为preview大小
        Camera.Size preSize = getProperSize(previewSizeList,((float) height) /width);

        if (preSize != null){
            Log.i(TAG,"preSize.width=" + preSize.width + "  preSize.height=" + preSize.height);
            parameters.setPreviewSize(preSize.width,preSize.height);
        }

        //检测是否摄像头具有连续对焦
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }

        //设置自动对焦
        mCamera.cancelAutoFocus();
        mCamera.setParameters(parameters);
        Log.e(TAG, "setCameraParams: 结束配置。。。");
    }
    /**
     * 从列表中选取合适的分辨率
     * 默认width ＝ 4:3
     * <p>tip: w对应屏幕的height,h对应屏幕的width，因为Camera的长宽和屏幕是相反的，默认是横向</p>
     */
     private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio){
         Log.i(TAG, "screenRatio=" + screenRatio);
         Camera.Size result = null;
         for (Camera.Size size : pictureSizeList){
             float currentRatio = ((float) size.width) / size.height;
             if (currentRatio - screenRatio == 0) {
                 result = size;
                 break;
             }
         }
         if (result == null){
             for (Camera.Size size : pictureSizeList){
                 float curRatio = ((float)size.width)/ size.height;
                 if (curRatio == 4f / 3){
                     result = size;
                     break;
                 }
             }
         }
         return result;
     }


    public void takePicture(){
        Log.e(TAG, "takePicture: 在takePicture方法里" );
        setCameraParams(mCamera,mScreenWidth,(int) (mScreenHeight*Rate));
        mCamera.takePicture(null,null,mPictureCallback);
        Log.e(TAG, "takePicture: 执行完之后。。。" );
    }

    //拍照回调
    private android.hardware.Camera.PictureCallback mPictureCallback = new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, android.hardware.Camera camera) {
            Log.e(TAG, "onPictureTaken: 点击了拍照按钮，触发回调接口");
            final Pictures pictures = mDataLab.addPicsToDB("CAIU3438311","W",".png");
            String pictureName = pictures.getName();

            File pictureDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File inDir = new File(pictureDir,pictures.getTCode());
            inDir.mkdirs();
            final String picturePath = inDir + File.separator + pictureName;
            File DeFile = new File(picturePath);
            Log.e(TAG, "onPictureTaken: 点击拍照后，图片储存在 :" + picturePath );

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: 开启线程进行储存。。。");
                    File file = new File(picturePath);
                    try {
                        //获取当前旋转角度，并旋转照片
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,data.length);
                        bitmap = rotateBitmapByDegree(bitmap,90);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
                        bos.flush();
                        bos.close();
                        bitmap.recycle();
                        Intent intent = new Intent("com.oocl.john.fresh");
                        intent.putExtra("index",pictures.getSeqNo());
                        mContext.sendBroadcast(intent);
                        Log.e(TAG, "run: 线程执行完毕。。。");
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();
            mCamera.stopPreview();
            mCamera.startPreview();
            Log.e(TAG, "onPictureTaken: 拍照的此时，手电筒开关： " + mSwitch );
            Log.e(TAG, "onPictureTaken: 拍照回调接口执行完之后。。。");
            /*if (mSwitch == true){
                Intent intent = new Intent("com.oocl.john.switchlight");
                intent.putExtra("sign",true);
                mContext.sendBroadcast(intent);
                Log.e(TAG, "onPictureTaken: 在回调接口里发送开灯的广播");
            }else {
                Intent intent = new Intent("com.oocl.john.switchlight");
                intent.putExtra("sign",false);
                mContext.sendBroadcast(intent);
                Log.e(TAG, "onPictureTaken: 在回调接口里发送关灯的广播");
            }*/
            mOnCaptureListener.onCapture(pictureName,pictures.getSeqNo());
        }
    };
    //旋转图片进行储存
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree){
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

    public interface OnCaptureListener {
        void onCapture(String name, int seq);
    }
    public void setOnCaptureListener(OnCaptureListener onCaptureListener){
        mOnCaptureListener = onCaptureListener;
    }

}
