package com.oocl.johngao.smartcr.ToolsClass;

import android.content.Context;
import android.util.Log;

import com.oocl.johngao.smartcr.Data.Pictures;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by johngao on 17/12/4.
 */

public class DataLab {
    private static DataLab sDataLab;
    private Context mContext;
    public static final String TAG = "DataLab";

    private List<Pictures> mPicturesList;

    private DataLab(Context context){
        mContext = context.getApplicationContext();
        initPicList();
    }
    public static DataLab get(Context context){
        if (sDataLab == null){
            synchronized (DataLab.class){
                if (sDataLab == null){
                    sDataLab = new DataLab(context);
                }
            }
        }
        return sDataLab;
    }

    public Pictures addPicsToDB(String ConNo,String TCode,String EndCode){
        try {
            int SeqNo = mPicturesList.size() + 1;
            Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode);
            pictures.save();
            Log.e(TAG, "addPicsToDB: 点击拍照，将数据储存进数据库" );
            mPicturesList.add(pictures);
            return pictures;
        }catch (Exception e){
            e.printStackTrace();
            return new Pictures(null,null,00,null);
        }
    }

    public List<Pictures> getPicturesList(){
        return mPicturesList;
    }

    public void initPicList(){
        mPicturesList = new ArrayList<>();
        mPicturesList = DataSupport.where("TCode = ?", "W").find(Pictures.class);
        if (mPicturesList.size() == 0){
            Log.e(TAG, "initPicList: 当前，数据库中没有数据" );
        }else {
            Log.e(TAG, "initPicList: 从数据库获取数据 :");
            int i = 0;
            for (Pictures pictures : mPicturesList){
                i++;
                Log.e(TAG, "initPicList: data " + i  + " "+ pictures.getName() );
            }
        }
    }
}
