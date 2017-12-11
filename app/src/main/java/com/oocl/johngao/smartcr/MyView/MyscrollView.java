package com.oocl.johngao.smartcr.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by johngao on 17/12/11.
 */

public class MyscrollView extends ScrollView {
    public MyscrollView(Context context) {
        super(context);
    }

    public MyscrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyscrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    View view;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed){
            LinearLayout v = (LinearLayout) getChildAt(0);
            if (v != null){
                for (int i = 0; i<getChildCount();i++){
                    if (v.getChildAt(i).getTag() != null && ((String)v.getChildAt(i).getTag()).equals("insidell")){
                        view = v.getChildAt(i);
                        break;
                    }
                }
            }
            view = v;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (getScrollY() >= view.getTop()){
            fixHead();
            canvas.save();
            canvas.translate(0,getScrollY());
            canvas.clipRect(0,0,view.getWidth(),view.getHeight());
            view.draw(canvas);
            canvas.restore();
        }else {
            resetHead();
        }
    }

    private OnFixHeadListener listener;

    private void fixHead(){
        if (listener != null){
            listener.onFix();
        }
    }

    private void resetHead(){
        if (listener != null){
            listener.onReset();
        }
    }

    public interface OnFixHeadListener {
        void onFix();
        void onReset();
    }

    public void setListener(OnFixHeadListener listener){
        this.listener = listener;
    }
}
