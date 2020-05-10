package com.ximen.plan.constants;

/**
 * 状态码
 */

public enum StatusCode {

    // 操作成功
    SUCCESS(true, 1001, "操作成功"),

    //操作失败
    ERROR_UNAUTHENTICATED(false, 2002, "未认证"),
    ERROR_UNAUTHORISE(false, 2003, "未授权"),

    //用户模块
    ERROR_USERNOTEXIST(false, 3001, "用户不存在"),
    ERROR_PASSWORDERROR(false, 3002, "密码错误"),

    //角色模块
    //菜单模块
    ERROR_EXISTCHIDRED(false, 5001, "存在子节点");

    private boolean flag;

    private Integer code;

    private String message;

    StatusCode() {
    }

    StatusCode(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
