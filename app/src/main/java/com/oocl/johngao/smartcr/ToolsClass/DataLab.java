package com.oocl.johngao.smartcr.ToolsClass;

import android.content.Context;
import android.util.Log;

import com.oocl.johngao.smartcr.Data.Pictures;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
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
            Log.e(TAG, "addPicsToDB: save to DB just now" );
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
            Log.e(TAG, "initPicList: It's null from DB!" );
        }else {
            Log.e(TAG, "initPicList: yeah!");
            for (Pictures pictures : mPicturesList){
                Log.e(TAG, "initPicList: "+ pictures.getName() );
            }
        }
    }
}
