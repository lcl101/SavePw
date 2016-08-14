package com.aoki.savepw.data;

import com.aoki.savepw.Config;

/**
 * Created by aoki on 15-12-24.
 */
public class Password extends Bean {
    public static char TYPE = '3';
    private String enDesc;
    private String enContext;

    public void press(String str){
        String[] ss = str.split(":");
        enDesc = ss[0];
        enContext = ss[1];
    }

    public String getContext() {
        return Config.encryBean.de(enContext);
    }

    public void setContext(String context) {
        Data.modify = true;
        this.enContext = Config.encryBean.en(context);
    }

    public String getDesc() {
        return Config.encryBean.de(enDesc);
    }

    public void setDesc(String desc) {
        Data.modify = true;
        this.enDesc = Config.encryBean.en(desc);
    }

    public String data(){
        return TYPE+":"+enDesc+":"+enContext;
    }
}
