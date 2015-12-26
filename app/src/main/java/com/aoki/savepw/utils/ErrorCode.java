package com.aoki.savepw.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aoki on 15-12-24.
 */
public class ErrorCode {
    private static final Map<String,String> bizzCode = new HashMap<String, String>(10);
    private static final Map<String,String> funCode = new HashMap<String, String>(10);
    private static final Map<String,String> msg = new HashMap<String, String>(100);
    public static String getErrorCode(String bizzName,String funName){
        String bizz = bizzCode.get(bizzName);
        if (null == bizz) return "9999";
        String fun = funCode.get(funName);
        if (null == fun) return bizz+"99";
        return bizz+fun;
    }

    public static String getErrorMsg(String errorCode){
        return msg.get(errorCode);
    }

    private static void initCode(){
        bizzCode.put("util","00");
        bizzCode.put("data","01");

        funCode.put("find","01");
        funCode.put("write","02");
        funCode.put("read","03");
        funCode.put("add","04");
    }

    private static void initMsg(){
        msg.put("0001","未找到sd卡");
        msg.put("0002","写入文件错误");
        msg.put("0003","读文件错误");
        msg.put("0104","添加密码时，主题重叠");
    }

    static {
        initCode();
        initMsg();
    }
}
