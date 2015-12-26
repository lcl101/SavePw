package com.aoki.savepw.excepton;

/**
 * Created by aoki on 15-12-24.
 */
public class AppException extends BaseExceptoin {

    public AppException(String errorCode){
        super(errorCode);
    }

    public AppException(String errorCode,Throwable t){
        super(errorCode,t);
    }

    public AppException(String errorCode,String errorMsg){
        super(errorCode,errorMsg);
    }


}
