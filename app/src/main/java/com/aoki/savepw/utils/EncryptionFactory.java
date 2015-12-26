package com.aoki.savepw.utils;

import com.aoki.savepw.Encryption;

/**
 * Created by aoki on 15-12-24.
 */
public class EncryptionFactory {
    public static Encryption makeEncryptionObject(){
        return new MyDes();
    }
}
