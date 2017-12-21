package com.oocl.johngao.smartcr.Adapter;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by johngao on 17/12/21.
 */

public abstract class NineGridImageViewAdapter<T> {
    public abstract void onDisplayImage(Context context, ImageView imageView, T t);

    public ImageView generateImageView(Context context){
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    //这里可以添加你所需要的事件之类的方法
}
