package com.aoki.savepw.excepton;

import com.aoki.savepw.utils.ErrorCode;

/**
 * Created by aoki on 15-12-24.
 */
public class BaseExceptoin extends RuntimeException {
    private String errorCode="9999";
    private String errorMsg="未知异常";

    public BaseExceptoin(String errorCode){
        super(errorCode);
        String msg = ErrorCode.getErrorMsg(errorCode);
        if (null == msg){
            this.errorMsg=errorCode;
        }
        else {
            this.errorCode=errorCode;
            this.errorMsg = msg;
        }
    }

    public BaseExceptoin(String errorCode,Throwable t){
        super(errorCode,t);
        String msg = ErrorCode.getErrorMsg(errorCode);
        if (null == msg){
            this.errorMsg=errorCode;
        }
        else {
            this.errorCode=errorCode;
            this.errorMsg = msg;
        }
    }

    public BaseExceptoin(String errorCode,String errorMsg){
        super("ec:"+errorCode+". em"+errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
