package com.oocl.johngao.smartcr.ToolsClass;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.oocl.johngao.smartcr.Adapter.ConListAdapter;
import com.oocl.johngao.smartcr.Const.Const;
import com.oocl.johngao.smartcr.Data.Container;
import com.oocl.johngao.smartcr.Data.Pictures;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.oocl.johngao.smartcr.Const.Const;
import com.oocl.johngao.smartcr.Data.RuleSort;

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

    private ConListAdapter mConListAdapter;

    public boolean nextbt = false;
    private List<String> mSettingsList;

    private List<String> mHintList;
    private List<String> mMetaList;

    private List<RuleSort>  mRuleSortList;
    private RuleSort mRuleSort;

    private List<String> mMoreList;

    private DataLab(Context context){
        mContext = context.getApplicationContext();

        initContainerList();
        initSettingsList();
        initRuleSort();
        initMorelist();
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

    //向数据库添加照片，同时根据所剩的空白图片位置数量nullNum，决定在哪里加载照片
    public Pictures addPicsToDB(String ConNo,String TCode,String EndCode,String company){
        try {
            Log.e("Next", "addPicsToDB: ..... 前四个还剩 " + nullNum );
            if (nullNum == 0){
                int SeqNo = mPicturesList.size();
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode,company);
                CreatePicName(pictures);
                pictures.save();
                mPicturesList.set(SeqNo-1,pictures);
                addNull();   //此时空白数量为0，需要添加一个空白图片以用于添加加号标志
                return pictures;
            }else if (nullNum ==1){
                int SeqNo = 4;
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode,company);
                CreatePicName(pictures);
                pictures.save();
                mPicturesList.set(3,pictures);
                addNull();  //此时空白数量为1，替换掉第四号位置后，需要在后面添加一个空白位置，以便于后面的加号显示
                nullNum --;
                return pictures;
            }else if (nullNum ==2){
                int SeqNo = 3;
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode,company);
                CreatePicName(pictures);
                pictures.save();
                mPicturesList.set(2,pictures);
                nullNum --;
                return pictures;
            }else if (nullNum == 3){
                int SeqNo = 2;
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode,company);
                CreatePicName(pictures);
                pictures.save();
                mPicturesList.set(1,pictures);
                nullNum --;
                return pictures;
            }else if (nullNum == 4){
                int SeqNo = 1;
                Log.e("Next", "addPicsToDB: " + ConNo+TCode+SeqNo+EndCode );
                Pictures pictures = new Pictures(ConNo,TCode,SeqNo,EndCode,company);
                CreatePicName(pictures);
                Log.e("Next", "addPicsToDB: 1111" );
                pictures.save();
                Log.e("Next", "addPicsToDB: 2222" );
                if (mPicturesList != null){
                    for (Pictures pictures1 : mPicturesList){
                        Log.e("Next", "addPicsToDB: " + pictures1.getName() );
                    }
                }
                Log.e("Next", "addPicsToDB: " + pictures.getName() );
                mPicturesList.set(0,pictures);
                Log.e("Next", "addPicsToDB: 3333" );
                nullNum --;
                return pictures;
            }else {
                Log.e("Next", "addPicsToDB: 在这里，nullNum不为前四");
                return null;
            }

        }catch (Exception e){
            Log.e("Next", "addPicsToDB: 在这里发生异常");
            e.printStackTrace();
            return new Pictures(null,null,00,null,null);
        }
    }

    public List<Pictures> getPicturesList(){
        return mPicturesList;
    }

    //初始化Container列表，根据完成状态进行分类初始化
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
        String TCode;
        //根据在初始化时点击的container的ConNo和拍摄状态，决定如何读取数据库
        switch (sign){
            case "WashBeforeWithZero":
            case "WashBeforeProgress":
                //布局：根据sign标志选择如何显示；图片：TCode定义为空或者W
                TCode = "W";
                break;

            case "WashBeforeFinishWashAfterWithZero":
                //布局：根据sign标志选择如何显示；图片：TCode默认定义为WY，可以根据选择变成C、P、NIL
            case "WashBeforeFinishWashAfterProgress":
                TCode = "WY";
                break;

            case "RepairBeforeWithZero":
            case "RepairBeforeProgress":
                //布局：根据sign标志选择如何显示；图片：TCode默认为D
                TCode = "D";
                break;

            case "RepairBeforeFinishRepairAfterWithZero":
            case "RepairBeforeFinishRepairAfterProgress":
                //布局：根据sign标志显示；图片：TCode默认为IICL，后面会根据具体的码进行改变
                TCode = "RY";
                break;
            default:
                TCode = null;
                break;
        }
        if (TCode.equals("WY")){
            List<Pictures>  l1 = DataSupport.where("TCode = ? and ConNo = ?", "WY",ConNO).find(Pictures.class);
            List<Pictures>  l2 = DataSupport.where("TCode = ? and ConNo = ?", "C",ConNO).find(Pictures.class);
            List<Pictures>  l3 = DataSupport.where("TCode = ? and ConNo = ?", "P",ConNO).find(Pictures.class);
            List<Pictures>  l4 = DataSupport.where("TCode = ? and ConNo = ?", "NIL",ConNO).find(Pictures.class);
            mPicturesList.addAll(l1);
            mPicturesList.addAll(l2);
            mPicturesList.addAll(l3);
            mPicturesList.addAll(l4);
        }else if (TCode.equals("RY")){
            List<Pictures>  l1 = DataSupport.where("TCode = ? and ConNo = ?", "IICL1",ConNO).find(Pictures.class);
            List<Pictures>  l2 = DataSupport.where("TCode = ? and ConNo = ?", "IICL2",ConNO).find(Pictures.class);
            List<Pictures>  l3 = DataSupport.where("TCode = ? and ConNo = ?", "IICL3",ConNO).find(Pictures.class);
            List<Pictures>  l4 = DataSupport.where("TCode = ? and ConNo = ?", "IICL4",ConNO).find(Pictures.class);
            mPicturesList.addAll(l1);
            mPicturesList.addAll(l2);
            mPicturesList.addAll(l3);
            mPicturesList.addAll(l4);
        }else {
            mPicturesList = DataSupport.where("TCode = ? and ConNo = ?", TCode,ConNO).find(Pictures.class);
        }
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
            container1.setCompany("OOCL");
            container1.save();
            mContainerList.add(container1);

            Container container2 = new Container("OOLU0119900" + 2,Const.IgnoreWash,Const.NeedRepair);
            container2.setCompany("ITA");
            container2.save();
            mContainerList.add(container2);

            Container container3 = new Container("OOLU0119900" + 3,Const.NeedWash,Const.NeedRepair);
            container3.setCompany("CCTV");
            container3.save();
            mContainerList.add(container3);

            /*Container container4 = new Container("OOLU0119900" + 4,Const.NeedWash,Const.NeedRepair);
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
            mContainerList.add(container15);*/

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

    //这个方法用于计算在初始化照片列表时，需要加载哪些导航图片
    private void initIcon(int size){
        if (size == 3){
            Pictures pictures = new Pictures(Const.NullConNo,Const.NullTCode,4,".png",null);
            mPicturesList.add(pictures);
            nullNum = nullNum - 3;
        }
        if (size == 2){
            for (int i = 1; i <= 2;i++){
                Pictures pictures = new Pictures(Const.NullConNo,Const.NullTCode,i+2,".png",null);
                mPicturesList.add(pictures);
            }
            nullNum = nullNum - 2;
        }
        if (size == 1){
            for (int i = 1; i <= 3;i++){
                Pictures pictures = new Pictures(Const.NullConNo,Const.NullTCode,i+1,".png",null);
                mPicturesList.add(pictures);
            }
            nullNum = nullNum - 1;
        }
        if (size == 0){
            for (int i = 1; i <= 4; i++){
                Pictures pictures = new Pictures(Const.NullConNo,Const.NullTCode,i,".png",null);
                mPicturesList.add(pictures);
            }
        }
        if (size == 4){
            nullNum = nullNum - 4;
        }
    }

    //这个函数的作用是，当需要拍摄的照片无上限时，用于在尾部添加一个加号空白图片的
    public void addNull(){
        Pictures pictures2 = new Pictures(Const.NullConNo,Const.NullTCode,mPicturesList.size()+1,".png",null);
        mPicturesList.add(pictures2);
    }

    public void resetPicturesListNull(){
        mPicturesList.clear();
        nullNum = 4;
    }
    public void ConsAddOne(String conNo,String tCode){
        for (Container container : mContainerList){
            if (container.getConNo().equals(conNo)){
                switch (tCode){
                    case "W":
                        container.addWB_Count();
                        container.save();
                        break;

                    case "WY":
                    case "C":
                    case "P":
                    case "NIL":
                        container.addWA_Count();
                        container.save();
                        break;


                    case "D":
                        container.addRB_Count();
                        container.save();
                        break;
                    case Const.IICL1:
                    case Const.IICL2:
                    case Const.IICL3:
                    case Const.IICL4:
                        container.addRA_Count();
                        container.save();
                        break;
                }
            }
        }

    }

    public void setConListAdapter(ConListAdapter conListAdapter) {
        mConListAdapter = conListAdapter;
    }

    public ConListAdapter getConListAdapter() {
        return mConListAdapter;
    }

    public void setNextbt(boolean nextbt) {
        this.nextbt = nextbt;
    }

    public boolean isNextbt() {
        return nextbt;
    }

    public List<String> getPathList(String ConNo){
        if (ConNo.equals(Const.NullConNo)){
            return null;
        }
        List<String> pathList = new ArrayList<>();
        List<Pictures> pictures = getPics(ConNo);
        if (pictures==null){
            return null;
        }
        for (Pictures p: pictures){
            File file = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String path = file + File.separator+ p.getConNo()+ File.separator + p.getName();
            Log.e(TAG, "getPathList: " + path);
            pathList.add(path);
        }
        return pathList;
    }

    public List<Pictures> getPics(String conNo){
        List<Pictures> pictures = DataSupport.where("ConNo = ?",conNo).find(Pictures.class);
        if (pictures.size() ==0){
            return null;
        }
        return pictures;
    }


    /**
     * 设置界面
     *
     */

    public List<String> getSettingsList() {
        return mSettingsList;
    }

    private void initSettingsList(){
        List<String> s = new ArrayList<>();
        s.add("账户与安全");
        s.add("消息通知");
        s.add("设置规则");
        s.add("图片管理");
        s.add("地址管理");
        s.add("意见反馈");
        s.add("清理缓存");
        s.add("关于");
        s.add("检查更新");
        s.add("退出登录");
        mSettingsList = s;
    }

    public void initHintList(int sum){
        Log.e(TAG, "initHintList: sum 为：" + sum );
        if (mHintList != null){
            mHintList.clear();
        }
        mHintList = new ArrayList<>();
        for (int i= 1 ; i <= sum; i++){
            String hint = "Part " + i;
            mHintList.add(hint);
        }
    }

    public List<String> getHintList() {
        return mHintList;
    }

    public void initMetaList(){
        if (mMetaList!= null){
            mMetaList.clear();
        }
        mMetaList = new ArrayList<>();

        if (exchangData(mRuleSort.getP1()) != null){
            mMetaList.add(exchangData(mRuleSort.getP1()));
        }
        if (exchangData(mRuleSort.getP2()) != null){
            mMetaList.add(exchangData(mRuleSort.getP2()));
        }
        if (exchangData(mRuleSort.getP3()) != null){
            mMetaList.add(exchangData(mRuleSort.getP3()));
        }
        if (exchangData(mRuleSort.getP4()) != null){
            mMetaList.add(exchangData(mRuleSort.getP4()));
        }
        if (exchangData(mRuleSort.getP5()) != null){
            mMetaList.add(exchangData(mRuleSort.getP5()));
        }

        if (mMetaList.size()!=0){
            for (String s : mMetaList){
                Log.e(TAG, "initMetaList: " + s);
            }
        }else {
            Log.e(TAG, "initMetaList: 错误" );
        }
    }

    public List<String> getMetaList(){
        return mMetaList;
    }

    private void initRuleSort(){
        mRuleSortList = new ArrayList<>();
        mRuleSortList = DataSupport.where("id = ?", "1").find(RuleSort.class);
        if (mRuleSortList.size() == 0){
            mRuleSort = new RuleSort();
            mRuleSort.setId(1);
            mRuleSort.setP1("C");
            mRuleSort.setP2("T");
            mRuleSort.setP3("S");
            mRuleSort.setP4("n");
            mRuleSort.setP5("n");
            mRuleSort.setFlag(true);
            mRuleSort.save();
            mRuleSortList.add(mRuleSort);
        }else {
            mRuleSort = mRuleSortList.get(0);

            Log.e(TAG, "initRuleSort: 数据库数据 " + mRuleSort.getId() + mRuleSort.getP1() + mRuleSort.getP2()+ mRuleSort.getP3()+ mRuleSort.getP4()+ mRuleSort.getP5() + mRuleSort.isFlag() );

        }
    }

    public RuleSort getRuleSort() {
        return mRuleSort;
    }

    public int getMetaSum(){

        return mMetaList.size();
    }

    private String exchangData(String p){
        switch (p){
            case "C":
                return "柜 号";
            case "T":
                return "操 作 码";
            case "S":
                return "序 号";
            case "n":
                return null;
            case "t":
                return "时 间";
            case "x":
                return "公 司";

        }
        return null;
    }

    public void initMorelist(){
        mMoreList = new ArrayList<>();

        mMoreList.add("C");
        mMoreList.add("T");
        mMoreList.add("S");
        mMoreList.add("t");
        mMoreList.add("x");

        for (int i = 0; i<5 ;i ++){
            mMoreList.get(i);
            if (mMoreList.get(i).equals(mRuleSort.getP1()) || mMoreList.get(i).equals(mRuleSort.getP2()) || mMoreList.get(i).equals(mRuleSort.getP3())|| mMoreList.get(i).equals(mRuleSort.getP4())|| mMoreList.get(i).equals(mRuleSort.getP5())){
                mMoreList.set(i,"n");
            }
        }
    }
    public List<String> getMoreList(){
        return mMoreList;
    }

    public void addMore(String T){
        String s;
        for (int i = 0; i< mMoreList.size();i++){
            s = mMoreList.get(i);
            if (s.equals("n")){
                mMoreList.set(i,T);
                return;
            }
        }
    }
    public void deleteMore(String T){
        for (int i = 0; i < 5; i++){
            if (mMoreList.get(i).equals(T)){
                mMoreList.set(i,"n");
            }
        }
    }
    public void addMeta(String T){
        /*switch (T){
            case "C":
                 mMetaList.add("柜 号") ;
                 break;
            case "T":
                mMetaList.add("操 作 码") ;
                break;
            case "S":
                mMetaList.add("序 号") ;
                break;
            case "n":
                break;
            case "t":
                mMetaList.add("时 间") ;
                break;
            case "x":
                mMetaList.add("未 知") ;
                break;
        }*/
        mMetaList.clear();
        initMetaList();
    }

    public void addHint(){
        int i = mHintList.size() + 1;
        String part = "Part" + i;
        mHintList.add(part);
    }
    public void resort(String s1,String s2){
        int index1 = 0,index2 = 0;
        if (mRuleSort.getP1().equals(exchangeToP(s1)) ){
            index1 = 1;
        }
        if (mRuleSort.getP2().equals(exchangeToP(s1)) ){
            index1 = 2;
        }
        if (mRuleSort.getP3().equals(exchangeToP(s1)) ){
            index1 = 3;
        }
        if (mRuleSort.getP4().equals(exchangeToP(s1)) ){
            index1 = 4;
        }
        if (mRuleSort.getP5().equals(exchangeToP(s1)) ){
            index1 = 5;
        }

        if (mRuleSort.getP1().equals(exchangeToP(s2)) ){
            index2 = 1;
        }
        if (mRuleSort.getP2().equals(exchangeToP(s2)) ){
            index2 = 2;
        }
        if (mRuleSort.getP3().equals(exchangeToP(s2)) ){
            index2 = 3;
        }
        if (mRuleSort.getP4().equals(exchangeToP(s2)) ){
            index2 = 4;
        }
        if (mRuleSort.getP5().equals(exchangeToP(s2)) ){
            index2 = 5;
        }

        if ((index1 == 1 && index2 == 2) || (index1 == 2 && index2 == 1)){
            String t = mRuleSort.getP2();
            mRuleSort.setP2(mRuleSort.getP1());
            mRuleSort.setP1(t);
        }if ((index1 == 1 && index2 ==3) || (index1 == 3 && index2 ==1) ){
            String t = mRuleSort.getP3();
            mRuleSort.setP3(mRuleSort.getP1());
            mRuleSort.setP1(t);
        }if ((index1 == 1 && index2 == 4)||(index1 == 4 && index2 == 1)) {
            String t = mRuleSort.getP4();
            mRuleSort.setP4(mRuleSort.getP1());
            mRuleSort.setP1(t);
        }if ((index1 == 1 && index2 ==5) || (index1 == 5 && index2 ==1)){
            String t = mRuleSort.getP5();
            mRuleSort.setP5(mRuleSort.getP1());
            mRuleSort.setP1(t);
        }

        if ((index1 == 2 && index2 == 3) || (index1 == 3 && index2 == 2) ){
            String t = mRuleSort.getP3();
            mRuleSort.setP3(mRuleSort.getP2());
            mRuleSort.setP2(t);
        }if ((index1 == 2 && index2 ==4) || (index1 == 4 && index2 ==2)){
            String t = mRuleSort.getP4();
            mRuleSort.setP4(mRuleSort.getP2());
            mRuleSort.setP2(t);
        }if ((index1 == 2 && index2 == 5) ||(index1 == 5 && index2 == 2) ) {
            String t = mRuleSort.getP5();
            mRuleSort.setP5(mRuleSort.getP2());
            mRuleSort.setP2(t);
        }

        if ( (index1 == 3 && index2 == 4) || (index1 == 4 && index2 == 3)) {
            String t = mRuleSort.getP4();
            mRuleSort.setP4(mRuleSort.getP3());
            mRuleSort.setP3(t);
        }if ((index1 == 3 && index2 ==5)|| (index1 == 5 && index2 == 3) ){
            String t = mRuleSort.getP5();
            mRuleSort.setP5(mRuleSort.getP3());
            mRuleSort.setP3(t);
        }

        if ((index1 == 4 && index2 == 5)|| (index1 == 5 && index2 == 4) ){
            String t = mRuleSort.getP5();
            mRuleSort.setP5(mRuleSort.getP4());
            mRuleSort.setP4(t);
        }

        mRuleSort.save();
        Log.e(TAG, "resort: " + mRuleSort.getP1() + mRuleSort.getP2() +mRuleSort.getP3() + mRuleSort.getP4() + mRuleSort.getP5() );
    }

    public String exchangeToP(String s){
        switch (s){
            case "柜 号":
                return "C";

            case "操 作 码":
                return "T";

            case "序 号":
                return "S";

            case "时 间":
                return "t";

            case "公 司":
                return "x";

            default:
                return "n";
        }
    }

    public void deleteItem(String s){
        int index = 0;

        Log.e(TAG, "deleteItem: 在接受端接收到的中文为" + s );
        if (mRuleSort.getP1().equals(exchangeToP(s))){
            index = 1;
        }if (mRuleSort.getP2().equals(exchangeToP(s))){
            index = 2;
        }if (mRuleSort.getP3().equals(exchangeToP(s))){
            Log.e(TAG, "deleteItem:   happen" + mRuleSort.getP3());
            index = 3;
        }if (mRuleSort.getP4().equals(exchangeToP(s))){
            index = 4;
        }if (mRuleSort.getP5().equals(exchangeToP(s))){
            index = 5;
        }
        Log.e(TAG, "deleteItem: 在mRuleSort中对应的下标为 ： " + index );

        if (index == 1){
            mRuleSort.setP1("n");
        }if (index == 2){
            mRuleSort.setP2("n");
        }if (index == 3){
            mRuleSort.setP3("n");
        }if (index == 4){
            mRuleSort.setP4("n");
        }if (index == 5){
            mRuleSort.setP5("n");
        }
        Log.e(TAG, "deleteItem: 替换里面的元素为空后，mRuleSort的5个参数为：" + mRuleSort.getP1() + mRuleSort.getP2() + mRuleSort.getP3() + mRuleSort.getP4() + mRuleSort.getP5()  );
        //排除空白
        List<String> list = new ArrayList<>();

        if (!mRuleSort.getP1().equals("n")){
            Log.e(TAG, "deleteItem: 添加进容器的值为：" +mRuleSort.getP1()  );
            list.add(mRuleSort.getP1());
        }
        if (!mRuleSort.getP2().equals("n")){
            Log.e(TAG, "deleteItem: 添加进容器的值为：" +mRuleSort.getP2()  );
            list.add(mRuleSort.getP2());
        }
        if (!mRuleSort.getP3().equals("n")){
            Log.e(TAG, "deleteItem: 添加进容器的值为：" +mRuleSort.getP3()  );
            list.add(mRuleSort.getP3());
        }
        if (!mRuleSort.getP4().equals("n")){
            Log.e(TAG, "deleteItem: 添加进容器的值为：" +mRuleSort.getP4()  );
            list.add(mRuleSort.getP4());
        }
        if (!mRuleSort.getP5().equals("n")){
            Log.e(TAG, "deleteItem: 添加进容器的值为：" +mRuleSort.getP5()  );
            list.add(mRuleSort.getP5());
        }
        Log.e(TAG, "deleteItem: 筛选有值的位置后容器的大小为：" + list.size() );
        if (list.size()<5){
            int e= 5-list.size();
            Log.e(TAG, "deleteItem: " + e);
            for (int i = 1; i <=e ;i++){
                list.add("n");
                Log.e(TAG, "deleteItem: " + "循环了" + i + "次" );
            }
            Log.e(TAG, "deleteItem: 补充空白后容器的大小为"+ list.size() );
            mRuleSort.setP1(list.get(0));
            mRuleSort.setP2(list.get(1));
            mRuleSort.setP3(list.get(2));
            mRuleSort.setP4(list.get(3));
            mRuleSort.setP5(list.get(4));
            mRuleSort.save();
            Log.e(TAG, "deleteItem: 排空后，list为：" + mRuleSort.getP1() + mRuleSort.getP2() + mRuleSort.getP3() + mRuleSort.getP4() + mRuleSort.getP5() );
        }
    }

    public void addItem(String s){
        if (mRuleSort.getP1().equals("n")){
            mRuleSort.setP1(s);
            mRuleSort.save();
            return;
        }
        if (mRuleSort.getP2().equals("n")){
            mRuleSort.setP2(s);
            mRuleSort.save();
            return;
        }
        if (mRuleSort.getP3().equals("n")){
            mRuleSort.setP3(s);
            mRuleSort.save();
            return;
        }
        if (mRuleSort.getP4().equals("n")){
            mRuleSort.setP4(s);
            mRuleSort.save();
            return;
        }
        if (mRuleSort.getP5().equals("n")){
            mRuleSort.setP5(s);
            mRuleSort.save();
            return;
        }
    }

    public void addItemAndMetaByPos(int pos, String s){
        switch (pos){
            case 0:
                mRuleSort.setP5(mRuleSort.getP4());
                mRuleSort.setP4(mRuleSort.getP3());
                mRuleSort.setP3(mRuleSort.getP2());
                mRuleSort.setP2(mRuleSort.getP1());
                mRuleSort.setP1(s);
                break;
            case 1:
                mRuleSort.setP5(mRuleSort.getP4());
                mRuleSort.setP4(mRuleSort.getP3());
                mRuleSort.setP3(mRuleSort.getP2());
                mRuleSort.setP2(s);
                break;
            case 2:
                mRuleSort.setP5(mRuleSort.getP4());
                mRuleSort.setP4(mRuleSort.getP3());
                mRuleSort.setP3(s);
                break;
            case 3:
                mRuleSort.setP5(mRuleSort.getP4());
                mRuleSort.setP4(s);
                break;
            case 4:
                mRuleSort.setP5(s);
                break;
            default:
                break;
        }
        mRuleSort.save();
        addMeta(s);
    }

    public String getCompany(String Con){
        for (Container container:mContainerList){
            if (container.getConNo().equals(Con)){
                return container.getCompany();
            }
        }
        return null;
    }

    public void CreatePicName(Pictures pictures){
        String C = pictures.getConNo();
        String T = pictures.getTCode();
        int S = pictures.getSeqNo();
        String t = pictures.getDate();
        String x = pictures.getCompany();

        String p1 = null,p2 = null ,p3 = null ,p4 = null,p5 = null;

        switch (mRuleSort.getP1()){
            case "C":
                p1 = C;
                break;
            case "T":
                p1 = T;
                break;
            case "S":
                p1 = String.valueOf(S);
                break;
            case "t":
                p1 = t;
                break;
            case "x":
                p1 = x;
                break;
            case "n":
                p1 = "";
                break;
        }
        switch (mRuleSort.getP2()){
            case "C":
                p2 = C;
                break;
            case "T":
                p2 = T;
                break;
            case "S":
                p2 = String.valueOf(S);
                break;
            case "t":
                p2 = t;
                break;
            case "x":
                p2 = x;
                break;
            case "n":
                p2 = "";
                break;
        }
        switch (mRuleSort.getP3()){
            case "C":
                p3 = C;
                break;
            case "T":
                p3 = T;
                break;
            case "S":
                p3 = String.valueOf(S);
                break;
            case "t":
                p3 = t;
                break;
            case "x":
                p3 = x;
                break;
            case "n":
                p3 = "";
                break;
        }
        switch (mRuleSort.getP4()){
            case "C":
                p4 = C;
                break;
            case "T":
                p4 = T;
                break;
            case "S":
                p4 = String.valueOf(S);
                break;
            case "t":
                p4 = t;
                break;
            case "x":
                p4 = x;
                break;
            case "n":
                p4 = "";
                break;
        }
        switch (mRuleSort.getP5()){
            case "C":
                p5 = C;
                break;
            case "T":
                p5 = T;
                break;
            case "S":
                p5 = String.valueOf(S);
                break;
            case "t":
                p5 = t;
                break;
            case "x":
                p5 = x;
                break;
            case "n":
                p5 = "";
                break;
        }

        pictures.setName(p1 + p2 + p3 + p4 + p5);
    }
}
