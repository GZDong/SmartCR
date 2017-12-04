package com.oocl.johngao.smartcr.Data;

import org.litepal.crud.DataSupport;

/**
 * Created by johngao on 17/12/4.
 */

public class Pictures extends DataSupport{
    private String ConNo;
    private String TCode;
    private int SeqNo;
    private String EndCode;
    private String name;

    public Pictures(){}

    public Pictures(String ConNo,String TCode,int SeqNo,String EndCode){
        this.ConNo = ConNo;
        this.TCode = TCode;
        this.SeqNo = SeqNo;
        this.EndCode = EndCode;
        this.name = ConNo + TCode + SeqNo + EndCode;
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
}
