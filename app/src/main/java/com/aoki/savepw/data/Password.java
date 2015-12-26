package com.aoki.savepw.data;

import com.aoki.savepw.Config;

/**
 * Created by aoki on 15-12-24.
 */
public class Password extends Bean {
    public static char TYPE = '3';
    private String desc;
    private String context;
    private String deDesc;
//    private String id = ""+System.currentTimeMillis();

    public void press(String str){
        String[] ss = str.split(":");
        desc = ss[0];
        context = ss[1];
//        id=ss[0];
        deDesc=Config.encryBean.de(desc);
    }

    public String data(){
        return TYPE+":"+desc+":"+context;//+":"+id
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        Data.modify = true;
        this.context = context;
    }

    public String getDeDesc() {
        return deDesc;
    }

    public void setDeDesc(String deDesc) {
        Data.modify = true;
        this.desc = Config.encryBean.en(deDesc);
        this.deDesc = deDesc;
    }
}
