package com.oocl.johngao.smartcr.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oocl.johngao.smartcr.R;

/**
 * Created by zhidong on 2017/12/3.
 */

public class SideBar extends View {
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public static String[] b = { "水洗","化学洗","除纸","除钉" };
    private int choose = 0;  //以对应的位置的值表示被选中
    private Paint paint = new Paint();

    private TextView mTextDialog;
    private ImageView mImageView;

    /**
     * 为SideBar设置显示字母的TextView
     * @param textDialog
     */
    public void setTextView(TextView textDialog,ImageView imageView) {
        this.mTextDialog = textDialog;
        this.mImageView = imageView;
    }


    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight(); //默认时match_parent
        int width = getWidth();
        int singleHeight = height / b.length;// 获取每一个字母的高度

        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.rgb(33, 65, 98));
            paint.setTypeface(Typeface.DEFAULT_BOLD);  //字体加粗
            paint.setAntiAlias(true);  //设置抗锯齿
            paint.setTextSize(30);
            if (i == choose) {// 选中的状态,再点击时改变choose的值
                paint.setColor(Color.parseColor("#00f5ff"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半,最后对应画的起点
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight; //跟写字母的顺序一样，从底部画起
            //指定画的内容、起点、画笔
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;  //一个事件序列创建一次，用于对比改变的
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数

        switch (action) {
            //UP事件产生：1.清除背景颜色 2.隐藏textView
            case MotionEvent.ACTION_UP:
                setBackground(new ColorDrawable(0x00000000));
                //choose = -1;//清除被选中的值
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                    mImageView.setVisibility(INVISIBLE);
                }
                break;
            //DOWN、MOVE事件产生：1.设置背景颜色 2.列表滚动 3.显示字母
            default:
                setBackgroundResource(R.drawable.sidebar_background);
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            //让RecyclerView一起滚动
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            //设置显示字母
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                            mImageView.setVisibility(VISIBLE);
                        }
                        choose = c;
                        invalidate(); //重新绘画，保证点击字母颜色改变
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 触摸事件
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}
