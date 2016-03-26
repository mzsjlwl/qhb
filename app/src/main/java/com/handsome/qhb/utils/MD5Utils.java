package com.handsome.qhb.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhang on 2016/3/24.
 */
public class MD5Utils {
        public static String digest(String value) {
            StringBuilder sb = null;
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                byte[] result = digest.digest(value.getBytes());
                sb = new StringBuilder();
                for (byte b : result) {
                    String hexString = Integer.toHexString(b & 0xFF);
                    if (hexString.length() == 1) {
                        sb.append("0" + hexString);// 0~F
                    } else {
                        sb.append(hexString);
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            return sb.toString();
        }
    }

