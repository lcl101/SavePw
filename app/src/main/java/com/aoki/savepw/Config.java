package com.aoki.savepw;

import com.aoki.savepw.utils.EncryptionFactory;

import java.nio.charset.Charset;

/**
 * Created by aoki on 15-12-22.
 */
public class Config {
    public final static Charset CHARSET = Charset.forName("UTF8");
    public final static String DEFAULT_PWD = "AoKi_lEe";
    public final static int TOUCH_TIME = 1*60*1000;
    public final static String dataFile = "/aoki_sp.data";
    public final static DataBean dataBean = new DataBean();
    public final static Encryption encryBean = EncryptionFactory.makeEncryptionObject();
    public final static int PORT = 6213;
    public static String serverIp = "";
}
