package com.aoki.savepw.data;

/**
 * Created by aoki on 15-12-24.
 */
public class FileVersion extends Bean{
    public static char TYPE = '2';
    private int ver = 1;

    public void press(String str){
        ver = Integer.parseInt(str.substring(str.indexOf(':')+1));
    }

    public void incVersion(){
        ver++;
    }

    public String data(){
        return TYPE+":file_version:"+ver;
    }
}
