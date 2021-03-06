package com.oocl.johngao.smartcr.Data;

import com.oocl.johngao.smartcr.ToolsClass.DateTools;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by johngao on 17/12/4.
 */

public class Pictures extends DataSupport implements Serializable{
    private String ConNo;
    private String TCode;
    private int SeqNo;
    private String EndCode;
    private String name;
    private String date;
    private String Company;
    private boolean isPush = false;

    public Pictures(){}

    public Pictures(String ConNo,String TCode,int SeqNo,String EndCode,String Company){
        this.ConNo = ConNo;
        this.TCode = TCode;
        this.SeqNo = SeqNo;
        this.EndCode = EndCode;
        this.Company = Company;
        date = DateTools.getNowDate();
    }

    public String getConNo() {
        return ConNo;
    }

    public void setConNo(String conNo) {
        ConNo = conNo;
    }

    public String getTCode() {
        return TCode;
    }

    public void setTCode(String TCode) {
        this.TCode = TCode;
    }

    public int getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(int seqNo) {
        SeqNo = seqNo;
    }

    public String getEndCode() {
        return EndCode;
    }

    public void setEndCode(String endCode) {
        EndCode = endCode;
    }

    public void setName() {
        name = ConNo + TCode + SeqNo + EndCode;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompany() {
        return Company;
    }

    public String getDate() {
        return date;
    }

    public boolean isPush() {
        return isPush;
    }

    public void setPush(boolean push) {
        isPush = push;
    }
}
