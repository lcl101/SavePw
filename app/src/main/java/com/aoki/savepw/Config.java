package com.aoki.savepw;

import com.aoki.savepw.utils.EncryptionFactory;

import java.nio.charset.Charset;

/**
 * Created by aoki on 15-12-22.
 */
public class Config {
    public final static Charset CHARSET = Charset.forName("UTF8");
    public final static String DEFAULT_PWD = "AoKi_lEe";
    public final static String dataFile = "/aoki_sp.data";
    public final static DataBean dataBean = new DataBean();
    public final static Encryption encryBean = EncryptionFactory.makeEncryptionObject();
}
