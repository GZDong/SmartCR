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

    public List<Container> getContainerList(int page) {
        List<Container> list1 = new ArrayList<>();
        List<Container> list2 = new ArrayList<>();

        for (Container container : mContainerList){
            if (container.isW_Choose() == true && container.isR_Choose() == false){
                if (container.getWA_Count() >= 3){
                    list2.add(container);
                }else {
                    list1.add(container);
                }
            }
            if (container.isW_Choose() == false && container.isR_Choose() == true){
                if (container.getRA_Count() >= 3){
                    list2.add(container);
                }else {
                    list1.add(container);
                }
            }
            if (container.isW_Choose() == true && container.isR_Choose() == true){
                if (container.getWA_Count() >= 3 && container.getRA_Count() >= 3){
                    list2.add(container);
                }else {
                    list1.add(container);
                }
            }
        }
        if (page == 1) {
            return list1;
        }else{
            return list2;
        }
    }

    public void initPicList(String ConNO,String sign){
        mPicturesList = new ArrayList<>();
        /*if (sign.equals("WashBefore")){

        }else if (sign.equals("WashAfter")){

        }else if (sign.equals("RepairBefore")){

        }else if (sign.equals("RepairAfter")){

        }*/

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

            Container container1 = new Container("OOLU0119900" + 1,Const.NeedWash,Const.IgnoreRepair);
            container1.save();
            mContainerList.add(container1);

            Container container2 = new Container("OOLU0119900" + 2,Const.IgnoreWash,Const.NeedRepair);
            container2.save();
            mContainerList.add(container2);

            Container container3 = new Container("OOLU0119900" + 3,Const.NeedWash,Const.NeedRepair);
            container3.save();
            mContainerList.add(container3);

            Container container4 = new Container("OOLU0119900" + 4,Const.NeedWash,Const.NeedRepair);
            container4.setWB_Count(2);
            container4.save();
            mContainerList.add(container4);

            Container container5 = new Container("OOLU0119900" + 5,Const.NeedWash,Const.NeedRepair);
            container5.setWB_Count(3);
            container5.save();
            mContainerList.add(container5);

            Container container6 = new Container("OOLU0119900" + 6,Const.NeedWash,Const.NeedRepair);
            container6.setWB_Count(3);
            container6.setWA_Count(1);
            container6.save();
            mContainerList.add(container6);

            Container container7 = new Container("OOLU0119900" + 7,Const.NeedWash,Const.NeedRepair);
            container7.setWB_Count(3);
            container7.setWA_Count(3);
            container7.save();
            mContainerList.add(container7);

            Container container8 = new Container("OOLU0119900" + 8,Const.NeedWash,Const.NeedRepair);
            container8.setWB_Count(2);
            container8.setRB_Count(2);
            container8.save();
            mContainerList.add(container8);

            Container container9 = new Container("OOLU0119900" + 9,Const.NeedWash,Const.NeedRepair);
            container9.setWB_Count(2);
            container9.setRB_Count(3);
            container9.save();
            mContainerList.add(container9);

            Container container10 = new Container("OOLU011990" + 10,Const.NeedWash,Const.NeedRepair);
            container10.setWB_Count(2);
            container10.setRB_Count(3);
            container10.setRA_Count(2);
            container10.save();
            mContainerList.add(container10);

            Container container11 = new Container("OOLU011990" + 11,Const.NeedWash,Const.NeedRepair);
            container11.setWB_Count(2);
            container11.setRB_Count(3);
            container11.setRA_Count(3);
            container11.save();
            mContainerList.add(container11);

            Container container12 = new Container("OOLU011990" + 12,Const.NeedWash,Const.NeedRepair);
            container12.setWB_Count(3);
            container12.setRB_Count(3);
            container12.setRA_Count(3);
            container12.save();
            mContainerList.add(container12);
            Container container13 = new Container("OOLU011990" + 13,Const.NeedWash,Const.NeedRepair);
            container13.setWB_Count(3);
            container13.setWA_Count(3);
            container13.setRB_Count(3);
            container13.setRA_Count(3);
            container13.save();
            mContainerList.add(container13);

            Container container14 = new Container("OOLU011990" + 14,Const.NeedWash,Const.IgnoreRepair);
            container14.setWB_Count(3);
            container14.setWA_Count(3);
            container14.save();
            mContainerList.add(container14);
            Container container15 = new Container("OOLU011990" + 15,Const.IgnoreWash,Const.NeedRepair);
            container15.setRB_Count(3);
            container15.setRA_Count(3);
            container15.save();
            mContainerList.add(container15);

            Log.e(TAG, "initContainerList: 模拟数据初始化完毕");
            for (Container container : mContainerList){
                Log.e(TAG, "initContainerList: 货柜号：" + container.getConNo() + "，是否需要洗：" + container.isW_Choose() + "，是否需要修：" + container.isR_Choose());
            }
        }else {
            Log.e(TAG,"initContainerList: 从数据库获取到的货柜：");
            for (Container container : mContainerList){
                Log.e(TAG, "initContainerList: 货柜号：" + container.getConNo() + "，是否需要洗：" + container.isW_Choose() + "，是否需要修：" + container.isR_Choose());
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
