package com.unicom.common.api;

public enum  ResultCode implements  ResultEnum {
    SUCCESS(200,"操作成功"),
    FAILED(500,"操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    SERVER_ERROR(500,"服务器错误"),
    NULL_POINT(600,"空指针异常"),
    REPEAT_COMMIT(999,"重复提交");

    private  long code;
    private  String msg;
    private ResultCode(long code,String msg){
        this.code=code;
        this.msg=msg;
    }
    @Override
    public long getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
