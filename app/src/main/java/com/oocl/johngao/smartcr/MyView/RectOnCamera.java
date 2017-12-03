package com.oocl.johngao.smartcr.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by zhidong on 2017/12/3.
 */

public class RectOnCamera extends View {
    private static final String TAG = "RectOnCamera";
    private int mScreenWidth;
    private int mScreenHeight;
    private Paint mPaint;
    private RectF mRectF;

    private Point centerPoint;
    private int radio;

    public RectOnCamera(Context context) {
        super(context);
    }

    public RectOnCamera(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getScreenMetrix(context);
        initView(context);
    }

    public RectOnCamera(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    private void initView(Context context){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        int marginLeft = (int) (mScreenWidth*0.15);
        int marginTop = (int) (mScreenHeight*0.25);
        mRectF = new RectF(marginLeft,marginTop,mScreenWidth - marginLeft,mScreenHeight - marginTop);
        centerPoint = new Point(mScreenWidth/2,mScreenHeight/2);
        radio = (int) (mScreenWidth*0.1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.RED);
        canvas.drawRect(mRectF,mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(centerPoint.x,centerPoint.y,radio,mPaint);
        canvas.drawCircle(centerPoint.x,centerPoint.y,radio - 20,mPaint);
    }
}
