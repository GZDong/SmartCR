package com.oocl.johngao.smartcr.ToolsClass;

import com.oocl.johngao.smartcr.Data.Container;
import com.oocl.johngao.smartcr.R;

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

    public static String calConsTCodeFromTag(String tag){
        String TCode;
        switch (tag){
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
                //布局：根据sign标志显示；图片：TCode默认为RY，后面会根据具体的码进行改变
                TCode = "RY";
                break;
            default:
                TCode = null;
                break;
        }

        return TCode;
    }

    public static String calWTagFromCons(Container container){
        String tag;
        if (container.isW_Choose()){
            if (container.getWB_Count() == 0 ){
                //do noting
                tag = "WashBeforeWithZero";
            }else if (container.getWB_Count() < 3){

                tag = "WashBeforeProgress";
            }else {

                if (container.getWA_Count() == 0){

                    tag = "WashBeforeFinishWashAfterWithZero";
                }else if (container.getWA_Count() < 3){

                    tag = "WashBeforeFinishWashAfterProgress";
                }else {
                    tag = null;
                }
            }
        }else {
            tag= "null";
        }

        return tag;
    }

    public static String calRTagFromCons(Container container){
        String tag;
        if (container.isR_Choose()){
            if (container.getRB_Count() == 0 ){
                //do noting
                tag = "RepairBeforeWithZero";
            }else if (container.getRB_Count() < 3){

                tag = "RepairBeforeProgress";
            }else {

                if (container.getRA_Count() == 0){

                    tag = "RepairBeforeFinishRepairAfterWithZero";
                }else if (container.getRA_Count() < 3){

                    tag = "RepairBeforeFinishRepairAfterProgress";
                }else {
                    tag = null;
                }
            }
        }else {
            tag = null;
        }

        return tag;
    }

    public static String[] calTagSort(String tag){
        if(tag.equals("WashBeforeWithZero") || tag.equals("WashBeforeProgress")){
            String a[] = {"WashBeforeWithZero","WashBeforeProgress"};
            return a;
        }
        if (tag.equals("WashBeforeFinishWashAfterWithZero")||tag.equals("WashBeforeFinishWashAfterProgress")){
            String b[] = {"WashBeforeFinishWashAfterWithZero","WashBeforeFinishWashAfterProgress"};
            return b;
        }
        if(tag.equals("RepairBeforeWithZero") || tag.equals("RepairBeforeProgress")){
            String a[] = {"RepairBeforeWithZero","RepairBeforeProgress"};
            return a;
        }
        if (tag.equals("RepairBeforeFinishRepairAfterWithZero")||tag.equals("RepairBeforeFinishRepairAfterProgress")){
            String b[] = {"RepairBeforeFinishRepairAfterWithZero","RepairBeforeFinishRepairAfterProgress"};
            return b;
        }
        return null;
    }
}
