package com.oocl.johngao.smartcr.MyView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by johngao on 17/11/30.
 */

public class CameraGrid extends View {
    private int topBannerWidth = 0;
    private Paint mPaint;

    public CameraGrid(Context context){
        this(context,null);
    }

    public CameraGrid(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAlpha(120);
        mPaint.setStrokeWidth(1f);
    }

    private boolean showGird = true;

    public boolean isShowGird(){
        return showGird;
    }

    public void setShowGird(boolean showGird){
        this.showGird = showGird;
    }

    public int getTopWidth(){
        return topBannerWidth;
    }
}
