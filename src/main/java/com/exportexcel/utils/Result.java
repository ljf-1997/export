package com.exportexcel.utils;

/**
 * @author nimn
 * API 接口返回工具类
 */
public class Result {
    private String errCode;
    private String errMsg;
    private Object data;
    private Object page;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
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

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

    public Result() {
        this.errMsg = "OK";
        this.errCode = "S";
    }

    public Result declareFailure() {
        this.errMsg = "error";
        this.errCode = "E";
        return this;
    }

    public Result declareFailure(String errMsg) {
        this.errMsg = errMsg;
        this.errCode = "E";
        return this;
    }
}
