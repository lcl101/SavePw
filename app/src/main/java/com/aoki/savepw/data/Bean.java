package com.aoki.savepw.data;

/**
 * Created by aoki on 15-12-24.
 */
public abstract class Bean {

    public abstract String data();

    public void press(String str){

    }

    public String toString(){
        return data();
    }
}
