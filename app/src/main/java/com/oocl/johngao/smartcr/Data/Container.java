package com.oocl.johngao.smartcr.Data;

import org.litepal.crud.DataSupport;

/**
 * Created by johngao on 17/12/6.
 */

public class Container  extends DataSupport{
    private String ConNo;

    public Container(){}

    public Container(String conNo){
        ConNo = conNo;
    }

    public String getConNo() {
        return ConNo;
    }

    public void setConNo(String conNo) {
        ConNo = conNo;
    }
}
