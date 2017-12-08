package com.oocl.johngao.smartcr.Data;

import android.util.Log;

import com.oocl.johngao.smartcr.Const.Const;
import com.oocl.johngao.smartcr.ToolsClass.DateTools;

import org.litepal.crud.DataSupport;

/**
 * Created by johngao on 17/12/6.
 */

public class Container  extends DataSupport{
    public static final String TAG = "Container";
    private String ConNo;

    /**
     * 假如是需要洗/修，那么需要置true，后面的必须是洗/修前，不可能是洗/修后
     * 假如是不需要洗/修，那么默认就是置false，或者手动置false，后面可以是洗/修前，也可以是洗/修后，代表不同情况
     * 也就是，w : 0,1 或者 0，0 前者代表操作完了，后者代表不需要
     *        w : 1,0 代表需要操作，必定是操作前
     * 也就是说，2种操作各自有3种情况
     */
    private boolean W_Choose = Const.IgnoreWash;
    private boolean W_Progress = Const.WaitWash;
    private boolean R_Choose = Const.IgnoreRepair;
    private boolean R_Progress = Const.WaitRepair;
    private String WashDate = null;
    private String RepairDate = null;

    public Container(){}

    //在创建Container对象时，指明编号、是否需要洗、是否需要修，用于每天添加新的container列表使用的构造方法
    public Container(String conNo,boolean WChoose,boolean RChoose){
        ConNo = conNo;
        W_Choose = WChoose;
        R_Choose = RChoose;
        if (W_Choose == Const.NeedWash){
            WashDate = DateTools.getNowDate();
        }
        if (R_Choose == Const.NeedRepair){
            RepairDate = DateTools.getNowDate();
        }
    }

    public String getConNo() {
        return ConNo;
    }

    public void setConNo(String conNo) {
        ConNo = conNo;
    }


    public String getWashDate() {
        return WashDate;
    }

    public void setWashDate(String washDate) {
        WashDate = washDate;
    }

    public String getRepairDate() {
        return RepairDate;
    }

    public void setRepairDate(String repairDate) {
        RepairDate = repairDate;
    }

    public void setW_Choose(boolean w_Choose) {
        W_Choose = w_Choose;
    }

    public void setR_Choose(boolean r_Choose) {
        R_Choose = r_Choose;
    }

    public void setR_Progress(boolean r_Progress) {
        R_Progress = r_Progress;
    }

    public void setW_Progress(boolean w_Progress) {
        W_Progress = w_Progress;
    }

    public boolean isR_Choose() {
        return R_Choose;
    }

    public boolean isR_Progress() {
        return R_Progress;
    }

    public boolean isW_Choose() {
        return W_Choose;
    }

    public boolean isW_Progress() {
        return W_Progress;
    }
}
