package com.oocl.johngao.smartcr.ToolsClass;

import android.content.Context;
import android.util.Log;

import com.oocl.johngao.smartcr.Const.Const;
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
    private int nullNum = 4;

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
            if (nullNum == 0){
                int SeqNo = mPicturesList.size() + 1;
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode);
                pictures.save();
                Log.e(TAG, "addPicsToDB: 点击拍照，将数据储存进数据库" );
                mPicturesList.add(pictures);
                return pictures;
            }else if (nullNum ==1){
                int SeqNo = 4;
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode);
                pictures.save();
                mPicturesList.set(3,pictures);
                nullNum --;
                return pictures;
            }else if (nullNum ==2){
                int SeqNo = 3;
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode);
                pictures.save();
                mPicturesList.set(2,pictures);
                nullNum --;
                return pictures;
            }else if (nullNum == 3){
                int SeqNo = 2;
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode);
                pictures.save();
                mPicturesList.set(1,pictures);
                nullNum --;
                return pictures;
            }else if (nullNum == 4){
                int SeqNo = 1;
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode);
                pictures.save();
                mPicturesList.set(0,pictures);
                nullNum --;
                return pictures;
            }else {
                return null;
            }

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
        if (mPicturesList.size() <= 4){
            initIcon(mPicturesList.size());
        }else {
            Log.e(TAG, "initPicList: 从数据库获取数据 :");
            int i = 0;
            for (Pictures pictures : mPicturesList){
                i++;
                Log.e(TAG, "initPicList: data " + i  + " "+ pictures.getName() );
            }
        }
    }

    private void initIcon(int size){
        if (size == 3){
            Pictures pictures = new Pictures(Const.NullConNo,Const.NullTCode,4,".png");
            mPicturesList.add(pictures);
            nullNum = nullNum - 3;
        }
        if (size == 2){
            for (int i = 1; i <= 2;i++){
                Pictures pictures = new Pictures(Const.NullConNo,Const.NullTCode,i+2,".png");
                mPicturesList.add(pictures);
            }
            nullNum = nullNum - 2;
        }
        if (size == 1){
            for (int i = 1; i <= 3;i++){
                Pictures pictures = new Pictures(Const.NullConNo,Const.NullTCode,i+1,".png");
                mPicturesList.add(pictures);
            }
            nullNum = nullNum - 1;
        }
        if (size == 0){
            for (int i = 1; i <= 4; i++){
                Pictures pictures = new Pictures(Const.NullConNo,Const.NullTCode,i,".png");
                mPicturesList.add(pictures);
            }
        }
        if (size == 4){
            nullNum = nullNum - 4;
        }
    }
}
