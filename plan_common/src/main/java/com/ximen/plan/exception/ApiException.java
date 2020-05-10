package com.ximen.plan.exception;

import com.ximen.plan.constants.StatusCode;

public class ApiException extends RuntimeException {
    private static final long serialVersionUID = -5400704137609742487L;
    private Integer code;
    private String errMsg;
    private Object data;

    public ApiException(Integer code, String errMsg) {
        super(errMsg);
        this.code = code;
        this.errMsg = errMsg;
    }

    public ApiException(Integer code, String errMsg, Object data) {
        super(errMsg);
        this.code = code;
        this.errMsg = errMsg;
        this.data = data;
    }

    public ApiException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.code = statusCode.getCode();
        this.errMsg = statusCode.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
