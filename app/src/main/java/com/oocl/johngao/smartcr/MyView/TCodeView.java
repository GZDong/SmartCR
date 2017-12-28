package com.oocl.johngao.smartcr.MyView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oocl.johngao.smartcr.Const.Const;
import com.oocl.johngao.smartcr.R;

/**
 * Created by johngao on 17/12/28.
 */

public class TCodeView extends RelativeLayout {

    private OnTabClickListener mOnTabClickListener;

    private TextView firstView;
    private TextView secondView;
    private TextView thirdView;
    private TextView forthView;
    private TextView defaultView;
    private int width;
    private int height;
    private boolean isShowing = false;
    private boolean isAnimating = false;

    public TCodeView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutParams params = new LayoutParams(150,90); //指定子view的大小
        width = 100;
        height = 90;
        params.addRule(ALIGN_PARENT_BOTTOM); //
        firstView = new TextView(context);
        firstView.setText(Const.CWash);
        firstView.setGravity(Gravity.CENTER);
        firstView.setBackground(context.getResources().getDrawable(R.drawable.bg_textview_style));
        firstView.setTextColor(context.getResources().getColor(R.color.white));
        firstView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultView.setText(Const.CWash);
                defaultView.setTextColor(context.getResources().getColor(R.color.white));
                mOnTabClickListener.onTabClick(firstView.getText().toString());
                hideMenu();
            }
        });
        this.addView(firstView,params);

        secondView = new TextView(context);
        secondView.setText(Const.PWash);
        secondView.setGravity(Gravity.CENTER);
        secondView.setBackground(context.getResources().getDrawable(R.drawable.bg_textview_style));
        secondView.setTextColor(context.getResources().getColor(R.color.white));
        secondView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultView.setText(Const.PWash);
                defaultView.setTextColor(context.getResources().getColor(R.color.white));
                mOnTabClickListener.onTabClick(secondView.getText().toString());
                hideMenu();
            }
        });
        this.addView(secondView,params);

        thirdView = new TextView(context);
        thirdView.setText(Const.NWash);
        thirdView.setGravity(Gravity.CENTER);
        thirdView.setBackground(context.getResources().getDrawable(R.drawable.bg_textview_style));
        thirdView.setTextColor(context.getResources().getColor(R.color.white));
        thirdView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultView.setText(Const.NWash);
                defaultView.setTextColor(context.getResources().getColor(R.color.white));
                mOnTabClickListener.onTabClick(thirdView.getText().toString());
                hideMenu();
            }
        });
        this.addView(thirdView, params);

        forthView = new TextView(context);
        forthView.setText(Const.Wash);
        forthView.setGravity(Gravity.CENTER);
        forthView.setBackground(context.getResources().getDrawable(R.drawable.bg_textview_style));
        forthView.setTextColor(context.getResources().getColor(R.color.white));
        forthView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultView.setText(Const.Wash);
                defaultView.setTextColor(context.getResources().getColor(R.color.white));
                mOnTabClickListener.onTabClick(forthView.getText().toString());
                hideMenu();
            }
        });
        this.addView(forthView,params);

        defaultView = new TextView(context);
        defaultView.setText(Const.Wash);
        defaultView.setGravity(Gravity.CENTER);
        defaultView.setBackground(context.getResources().getDrawable(R.drawable.bg_textview_style));
        defaultView.setTextColor(context.getResources().getColor(R.color.white));
        defaultView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnimating){
                    if (!isShowing){
                        isShowing = true;
                        showMenu();
                        defaultView.setTextColor(context.getResources().getColor(R.color.theme_light_blue));
                    }else {
                        isShowing = false;
                        hideMenu();
                        defaultView.setTextColor(context.getResources().getColor(R.color.white));
                    }
                }
            }
        });
        this.addView(defaultView,params);
    }

    protected void showMenu(){
        ObjectAnimator firstAnimator = ObjectAnimator.ofFloat(firstView,"translationY",new float[]{0.0F,(float)(-(this.height + 1)*4)});
        ObjectAnimator secondAnimator = ObjectAnimator.ofFloat(secondView,"translationY",new float[]{0.0F,(float)(-(this.height + 1) *3)});
        ObjectAnimator thirdAnimator = ObjectAnimator.ofFloat(thirdView,"translationY",new float[]{0.0F,(float)(-(this.height + 1)*2)});
        ObjectAnimator forthAnimator = ObjectAnimator.ofFloat(forthView,"translationY",new float[]{0.0F,(float)(-(this.height + 1)*1)});
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500L);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.playTogether(new Animator[]{firstAnimator,secondAnimator,thirdAnimator,forthAnimator});
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
            }
        });
        animatorSet.start();
    }

    protected void hideMenu(){
        ObjectAnimator firstAnimator = ObjectAnimator.ofFloat(this.firstView, "translationY", new float[]{this.firstView.getTranslationY(), 0.0F});
        ObjectAnimator secondAnimator = ObjectAnimator.ofFloat(this.secondView, "translationY", new float[]{this.secondView.getTranslationY(), 0.0F});
        ObjectAnimator thirdAnimator = ObjectAnimator.ofFloat(this.thirdView, "translationY", new float[]{this.thirdView.getTranslationY(), 0.0F});
        ObjectAnimator forthAnimator = ObjectAnimator.ofFloat(this.forthView, "translationY", new float[]{this.forthView.getTranslationY(), 0.0F});
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500L);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(new Animator[]{firstAnimator,secondAnimator,thirdAnimator,forthAnimator});
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
            }
        });
        animatorSet.start();
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener){
        this.mOnTabClickListener = onTabClickListener;
    }
    public interface OnTabClickListener{
        void onTabClick(String var1);
    }
}
