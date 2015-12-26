package com.aoki.savepw.data;

/**
 * Created by aoki on 15-12-24.
 */
public class AppVersion extends Bean {
    public static char TYPE = '1';
    private static int ver = 1;

    public String data(){
        return TYPE+":app_version:"+ver;
    }

}
