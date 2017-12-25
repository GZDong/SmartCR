package com.oocl.johngao.smartcr.Data;

import org.litepal.crud.DataSupport;

/**
 * Created by johngao on 17/12/25.
 */

public class RuleSort extends DataSupport {

    private int id;
    private boolean flag;
    private String p1;
    private String p2;
    private String p3;
    private String p4;
    private String p5;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public void setP4(String p4) {
        this.p4 = p4;
    }

    public void setP5(String p5) {
        this.p5 = p5;
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public String getP3() {
        return p3;
    }

    public String getP4() {
        return p4;
    }

    public String getP5() {
        return p5;
    }
}
