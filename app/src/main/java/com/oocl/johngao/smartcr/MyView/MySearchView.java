package com.oocl.johngao.smartcr.MyView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.oocl.johngao.smartcr.R;

/**
 * Created by johngao on 17/12/11.
 */

public class MySearchView extends AppCompatEditText implements View.OnFocusChangeListener,TextWatcher{

    private Drawable mClearDrawable;

    public MySearchView(Context context) {
        super(context);
        init();
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null){
            mClearDrawable = getResources().getDrawable(R.drawable.clearicon);
        }

        mClearDrawable.setBounds(0,0,90, 90);
        setClearIconVisible(false);
        addTextChangedListener(this);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null){
            if (event.getAction() == MotionEvent.ACTION_UP){
                boolean touchable = event.getX() > (getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth()) && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable){
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            setClearIconVisible(getText().length() > 0);
        }else {
            setClearIconVisible(false);
        }
    }

    protected void setClearIconVisible(boolean visible){
        Drawable right = visible ? mClearDrawable : null;
        Drawable drawable = getResources().getDrawable(R.drawable.searchicon);
        drawable.setBounds(0,0,40,40);
        setCompoundDrawables(drawable,getCompoundDrawables()[1],right,getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setClearIconVisible(s.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
