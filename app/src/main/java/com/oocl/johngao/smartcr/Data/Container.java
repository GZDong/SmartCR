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
    private String Company;

    private boolean W_Choose = Const.IgnoreWash;

    private boolean R_Choose = Const.IgnoreRepair;

    private int WB_Count = 0;
    private int WA_Count = 0;
    private int RB_Count = 0;
    private int RA_Count = 0;

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


    public boolean isR_Choose() {
        return R_Choose;
    }


    public boolean isW_Choose() {
        return W_Choose;
    }


    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public int getWB_Count() {
        return WB_Count;
    }

    public void setWB_Count(int WB_Count) {
        this.WB_Count = WB_Count;
    }

    public int getWA_Count() {
        return WA_Count;
    }

    public void setWA_Count(int WA_Count) {
        this.WA_Count = WA_Count;
    }

    public int getRB_Count() {
        return RB_Count;
    }

    public void setRB_Count(int RB_Count) {
        this.RB_Count = RB_Count;
    }

    public int getRA_Count() {
        return RA_Count;
    }

    public void setRA_Count(int RA_Count) {
        this.RA_Count = RA_Count;
    }
}
