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

public class TCodeView2 extends RelativeLayout {
    private TCodeView2.OnTabClickListener mOnTabClickListener;

    private TextView view1;
    private TextView view2;
    private TextView view3;
    private TextView view4;
    private TextView defaultView;
    private int width;
    private int height;
    private boolean isShowing = false;
    private boolean isAnimating = false;

    public TCodeView2(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutParams params = new LayoutParams(150,90); //指定子view的大小
        width = 100;
        height = 90;
        params.addRule(ALIGN_PARENT_BOTTOM); //
        view1 = new TextView(context);
        view1.setText("IICL1");
        view1.setGravity(Gravity.CENTER);
        view1.setBackground(context.getResources().getDrawable(R.drawable.bg_textview_style));
        view1.setTextColor(context.getResources().getColor(R.color.white));
        view1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultView.setText("IICL1");
                defaultView.setTextColor(context.getResources().getColor(R.color.white));
                mOnTabClickListener.onTabClick(view1.getText().toString());
                hideMenu();
            }
        });
        this.addView(view1,params);

        view2 = new TextView(context);
        view2.setText("IICL2");
        view2.setGravity(Gravity.CENTER);
        view2.setBackground(context.getResources().getDrawable(R.drawable.bg_textview_style));
        view2.setTextColor(context.getResources().getColor(R.color.white));
        view2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultView.setText("IICL2");
                defaultView.setTextColor(context.getResources().getColor(R.color.white));
                mOnTabClickListener.onTabClick(view2.getText().toString());
                hideMenu();
            }
        });
        this.addView(view2,params);

        view3 = new TextView(context);
        view3.setText("IICL3");
        view3.setGravity(Gravity.CENTER);
        view3.setBackground(context.getResources().getDrawable(R.drawable.bg_textview_style));
        view3.setTextColor(context.getResources().getColor(R.color.white));
        view3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultView.setText("IICL3");
                defaultView.setTextColor(context.getResources().getColor(R.color.white));
                mOnTabClickListener.onTabClick(view3.getText().toString());
                hideMenu();
            }
        });
        this.addView(view3, params);

        view4 = new TextView(context);
        view4.setText("IICL4");
        view4.setGravity(Gravity.CENTER);
        view4.setBackground(context.getResources().getDrawable(R.drawable.bg_textview_style));
        view4.setTextColor(context.getResources().getColor(R.color.white));
        view4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultView.setText("IICL4");
                defaultView.setTextColor(context.getResources().getColor(R.color.white));
                mOnTabClickListener.onTabClick(view4.getText().toString());
                hideMenu();
            }
        });
        this.addView(view4,params);

        defaultView = new TextView(context);
        defaultView.setText("IICL1");
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
        ObjectAnimator firstAnimator = ObjectAnimator.ofFloat(view1,"translationY",new float[]{0.0F,(float)(-(this.height + 1)*4)});
        ObjectAnimator secondAnimator = ObjectAnimator.ofFloat(view2,"translationY",new float[]{0.0F,(float)(-(this.height + 1) *3)});
        ObjectAnimator thirdAnimator = ObjectAnimator.ofFloat(view3,"translationY",new float[]{0.0F,(float)(-(this.height + 1)*2)});
        ObjectAnimator forthAnimator = ObjectAnimator.ofFloat(view4,"translationY",new float[]{0.0F,(float)(-(this.height + 1)*1)});
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
        ObjectAnimator firstAnimator = ObjectAnimator.ofFloat(this.view1, "translationY", new float[]{this.view1.getTranslationY(), 0.0F});
        ObjectAnimator secondAnimator = ObjectAnimator.ofFloat(this.view2, "translationY", new float[]{this.view2.getTranslationY(), 0.0F});
        ObjectAnimator thirdAnimator = ObjectAnimator.ofFloat(this.view3, "translationY", new float[]{this.view3.getTranslationY(), 0.0F});
        ObjectAnimator forthAnimator = ObjectAnimator.ofFloat(this.view4, "translationY", new float[]{this.view4.getTranslationY(), 0.0F});
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
