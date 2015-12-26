package com.aoki.savepw.excepton;

/**
 * Created by aoki on 15-12-24.
 */
public class BizzException extends BaseExceptoin {

    public BizzException(String errorCode){
        super(errorCode);
    }

    public BizzException(String errorCode,Throwable t){
        super(errorCode,t);
    }

    public BizzException(String errorCode, String errorMsg){
        super(errorCode,errorMsg);
    }

}
