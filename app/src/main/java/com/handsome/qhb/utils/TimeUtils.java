package com.handsome.qhb.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhang on 2016/4/20.
 */
public class TimeUtils  {

    public static String getInterval(String createtime){
        String interval = null;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date d1 = (Date)sd.parse(createtime,pos);
        Date now = new Date();
        long time = now.getTime()-d1.getTime();
        if(time/3600000<24){
            SimpleDateFormat sd1 = new SimpleDateFormat("HH");
            int now_h = Integer.valueOf(sd1.format(now));
            int create_h = Integer.valueOf(sd1.format(d1));
            SimpleDateFormat sd2 = new SimpleDateFormat("HH:mm");
            interval = sd2.format(d1);
            if(now_h-create_h<0){
                interval = "昨天 "+interval;
            }
        }else{
            SimpleDateFormat sd3 = new SimpleDateFormat("yy/MM/dd");
            interval = sd3.format(d1);
        }
        return interval;
    }

    public static String compareLast(String createtime,String lasttime) {
        LogUtils.e("createtime",createtime);
        LogUtils.e("lasttime",lasttime);
        String interval = null;
        try{
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date  d1 = sd.parse(createtime);
            Date d2 = sd.parse(lasttime);
            Date now = new Date();
            long time = now.getTime()-d1.getTime();
            if((d1.getTime()-d2.getTime())/60000<3){
                LogUtils.e("3分钟","===>");
                interval = "";
            }else if(time/3600000<24&&time/3600000>=1){
                SimpleDateFormat sd1 = new SimpleDateFormat("HH");
                int now_h = Integer.valueOf(sd1.format(now));
                int create_h = Integer.valueOf(sd1.format(d1));
                SimpleDateFormat sd2 = new SimpleDateFormat("HH:mm");
                interval = sd2.format(d1);
                if(now_h-create_h<0){
                    interval = "昨天 "+interval;
                }
            }else{
                SimpleDateFormat sd3 = new SimpleDateFormat("yy/MM/dd");
                interval = sd3.format(d1);
            }
        }catch(ParseException e){
            e.printStackTrace();
        }

        return interval;
    }
}
