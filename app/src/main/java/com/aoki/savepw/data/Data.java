package com.aoki.savepw.data;

import com.aoki.savepw.excepton.BizzException;
import com.aoki.savepw.utils.ErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aoki on 15-12-24.
 */
public class Data {
    public static boolean modify = false;
    private static AppVersion av = new AppVersion();
    private static FileVersion fv = new FileVersion();
    private static EnDemo ed = new EnDemo();
    private static List<Password> pwds = new ArrayList<Password>(10);
    private static List<Password> oldPwds = null;

    public static List<Password> getPwds(){
        return pwds;
    }

    public static void add(Password p){
        check(p);
        pwds.add(p);
        modify = true;
    }

    public static void remove(int index){
        pwds.remove(index);
        modify = true;
    }

    public static Password get(int index){
        return pwds.get(index);
    }

    private static void check(Password p){
        for (Password ep : pwds){
            if (p.getDesc().equals(ep.getDesc())){
                throw new BizzException(ErrorCode.getErrorCode("data","add"));
            }
        }
    }

    public static boolean checkEnKey(){
        return ed.checkEnKey();
    }

    public static void remove(Password p){
        pwds.remove(p);
        modify = true;
    }

    public static List<String> getData(){
//        if (!modify) return null;
        List<String> d = new ArrayList<String>(15);
        d.add(av.toString()+"\n");
        fv.incVersion();
        d.add(fv.toString()+"\n");
        d.add(ed.toString()+"\n");
        for (Password p : pwds){
            d.add(p.toString()+"\n");
        }
        return d;
    }

    public static void clean() {
        if (pwds.size()<1) return;
        modify = true;
        oldPwds = pwds;
        pwds = new ArrayList<Password>(10);
    }

    public static void reClean() {
        if (oldPwds != null) {
            pwds = oldPwds;
        }
    }

    public static void press(String str){
        if (null == str || str.trim().length()<1){
            return;
        }
        char t = str.charAt(0);
        if (Password.TYPE == t){
            Password p = new Password();
            p.press(str.substring(2));
            pwds.add(p);
        }
        else if (AppVersion.TYPE == t){
            av = new AppVersion();
        }
        else if (FileVersion.TYPE == t){
            fv = new FileVersion();
            fv.press(str.substring(2));
        }
        else if(EnDemo.TYPE == t){
            ed = new EnDemo();
            ed.press(str.substring(2));
        }
        else {
            //丢弃
        }
    }
}
