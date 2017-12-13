package com.oocl.johngao.smartcr.ToolsClass;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by johngao on 17/12/8.
 */

public class DateTools {
    public static String getNowDate(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(date);
        return dateStr;
    }
}
