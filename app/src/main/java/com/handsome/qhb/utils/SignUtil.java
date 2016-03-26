package com.handsome.qhb.utils;

import com.handsome.qhb.config.Config;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by zhang on 2016/3/24.
 */
public class SignUtil {

    public static String getSign(String method,String url,Map<String,String> map) throws MalformedURLException, NoSuchAlgorithmException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(method.toUpperCase());
        URL parse_url = new URL(url);
        if(parse_url.getHost()!=null&&parse_url.getPath()!=null){
            stringBuffer.append(parse_url.getHost()).append(parse_url.getPath());
        }
        if(map!=null) {

            for (Map.Entry<String, String> entry : map.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        stringBuffer.append(Config.SECRETKEY);
        return MD5Utils.digest(stringBuffer.toString());
    }
}
