package com.oocl.johngao.smartcr.ToolsClass;

import android.content.Context;
import android.util.Log;

import com.oocl.johngao.smartcr.Const.Const;
import com.oocl.johngao.smartcr.Data.Container;
import com.oocl.johngao.smartcr.Data.Pictures;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.oocl.johngao.smartcr.Const.Const;
/**
 * Created by johngao on 17/12/4.
 */

public class DataLab {
    private static DataLab sDataLab;
    private Context mContext;
    public static final String TAG = "DataLab";
    //照片
    private List<Pictures> mPicturesList;
    private int nullNum = 4;

    //货柜
    private List<Container> mContainerList;

    private DataLab(Context context){
        mContext = context.getApplicationContext();

        initContainerList();
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

    //向数据库添加照片
    public Pictures addPicsToDB(String ConNo,String TCode,String EndCode){
        try {
            Log.e(TAG, "addPicsToDB: ..... 前四个还剩 " + nullNum );
            if (nullNum == 0){
                int SeqNo = mPicturesList.size();
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode);
                pictures.save();
                mPicturesList.set(SeqNo-1,pictures);
                addNull();
                return pictures;
            }else if (nullNum ==1){
                int SeqNo = 4;
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode);
                pictures.save();
                mPicturesList.set(3,pictures);
                addNull();
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

    public List<Container> getContainerList() { return mContainerList; }

    public void initPicList(String ConNO,String sign){
        mPicturesList = new ArrayList<>();
        if (sign.equals("WashBefore")){

        }else if (sign.equals("WashAfter")){

        }else if (sign.equals("RepairBefore")){

        }else if (sign.equals("RepairAfter")){

        }

        mPicturesList = DataSupport.where("TCode = ?", "W").find(Pictures.class);
        if (mPicturesList.size() <= 4){
            initIcon(mPicturesList.size());
        }else {
            nullNum = 0;
            Log.e(TAG, "initPicList: 从数据库获取到的图片 :");
            int i = 0;
            for (Pictures pictures : mPicturesList){
                i++;
                Log.e(TAG, "initPicList: data " + i  + " "+ pictures.getName() );
            }
        }
    }
    public void initContainerList(){
        mContainerList = new ArrayList<>();
        mContainerList = DataSupport.findAll(Container.class);
        if (mContainerList.size() == 0){
            for (int i = 0; i<10; i++){
                Container container = new Container("OOLU0119900" + i,Const.NeedWash,Const.IgnoreRepair);
                container.save();
                mContainerList.add(container);
            }
            for (int i=10;i<20;i++){
                Container container = new Container("OOLU011990" + i,Const.IgnoreWash,Const.NeedRepair);
                container.save();
                mContainerList.add(container);
            }
            for (int i=20;i<25;i++){
                Container container = new Container("OOLU011990" + i, Const.NeedWash,Const.NeedRepair);
                container.save();
                mContainerList.add(container);
            }
            for (int i=25;i<30;i++){
                Container container = new Container("OOLU011990" + i, Const.NeedWash,Const.NeedRepair);
                container.setW_Progress(true);
                container.save();
                mContainerList.add(container);
            }
            for (int i=30;i<35;i++){
                Container container = new Container("OOLU011990" + i, Const.NeedWash,Const.NeedRepair);
                container.setR_Progress(true);
                container.save();
                mContainerList.add(container);
            }
            for (int i=35;i<40;i++){
                Container container = new Container("OOLU011990" + i, Const.NeedWash,Const.NeedRepair);
                container.setW_Progress(true);
                container.setR_Progress(true);
                container.save();
                mContainerList.add(container);
            }
            Log.e(TAG, "initContainerList: 模拟数据初始化完毕");
            for (Container container : mContainerList){
                Log.e(TAG, "initContainerList: 货柜号：" + container.getConNo() + "，是否需要洗：" + container.isW_Choose() + "，是否需要修：" + container.isR_Choose() +
                        " 是否洗完：" + container.isW_Progress() + " 是否修完：" + container.isR_Progress());
            }
        }else {
            Log.e(TAG,"initContainerList: 从数据库获取到的货柜：");
            for (Container container : mContainerList){
                Log.e(TAG, "initContainerList: 货柜号：" + container.getConNo() + "，是否需要洗：" + container.isW_Choose() + "，是否需要修：" + container.isR_Choose() +
                " 是否洗完：" + container.isW_Progress() + " 是否修完：" + container.isR_Progress());
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

    public void addNull(){
        Pictures pictures2 = new Pictures(Const.NullConNo,Const.NullTCode,mPicturesList.size()+1,".png");
        mPicturesList.add(pictures2);
    }
}
