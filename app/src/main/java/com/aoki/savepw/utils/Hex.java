package com.aoki.savepw.utils;

/**
 * Created by aoki on 15-12-23.
 */
public class Hex {
    public static String byte2hex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if(src==null||src.length<=0){
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hex2byte(String src) {
        if (src == null || src.equals("")) {
            return null;
        }
        String tmp = src.toUpperCase();
        int length = tmp.length() / 2;
        char[] hexChars = tmp.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
