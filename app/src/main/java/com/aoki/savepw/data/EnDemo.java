package com.aoki.savepw.data;

import com.aoki.savepw.Config;

/**
 * Created by aoki on 15-12-24.
 */
public class EnDemo extends Bean {
    public static final char TYPE= 'E';
    private static final String DEMO = "AOKI_LEE";
    private String enStr;

    public void press(String str){
        enStr = str;
    }

    public boolean checkEnKey(){
        if (null == enStr || "".equals(enStr)){
            return true;
        }
        if (Config.encryBean.en(DEMO).equals(enStr)){
            return true;
        }
        return false;
    }

    public String data(){
        if (null == enStr||"".equals(enStr)){
            enStr = Config.encryBean.en(DEMO);
        }
        return TYPE+":"+enStr;
    }
}
