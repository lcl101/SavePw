package com.aoki.savepw;

/**
 * Created by aoki on 15-12-24.
 */
public interface Encryption {
    void makeKey(String key);
    String en(String src);
    String de(String src);
}
