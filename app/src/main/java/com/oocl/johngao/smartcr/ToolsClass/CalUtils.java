package com.oocl.johngao.smartcr.ToolsClass;

import com.oocl.johngao.smartcr.Data.Container;

/**
 * Created by johngao on 17/12/19.
 */

public class CalUtils {

    public static String calConsWTCode(Container container){
        String s1;
        if (container.isW_Choose() == true){
            if (container.getWB_Count()>=0 && container.getWB_Count() <3){
                s1 = "W";
            }else if (container.getWA_Count()>=0&& container.getWA_Count() < 3){
                s1 = "WY";
            }else {
                s1 = " ";
            }
        }else {
            s1 = " ";
        }
        return s1;
    }
    public static String calConsRTCode(Container container){
        String s1;
        if (container.isR_Choose() == true){
            if (container.getRB_Count()>=0&&container.getRB_Count()<3){
                s1 = "D";
            }else if (container.getRA_Count()>=0&&container.getRA_Count()<3){
                s1 = "RY";
            }else {
                s1 = " ";
            }
        }else {
            s1 = " ";
        }
        return s1;
    }
}
