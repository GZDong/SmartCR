package com.oocl.johngao.smartcr.ToolsClass;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by johngao on 17/12/1.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera){
        super(context);
        mCamera = camera;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface() == null){
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
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degress = 0;
        switch (rotation){
            case Surface.ROTATION_0:
                degress = 0;
                break;
            case Surface.ROTATION_90:
                degress = 90;
                break;
            case Surface.ROTATION_180:
                degress = 180;
                break;
            case Surface.ROTATION_270:
                degress = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degress)%360;
            result = (360 - result) % 360;
        }else {
            result = (info.orientation - degress + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
