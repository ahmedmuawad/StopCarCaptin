package com.stopgroup.stopcar.captain.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ErrorResponse {

    @SerializedName("errors_str")
    @Expose
    private String msg = "";

    @SerializedName("message")
    @Expose
    private String message = "";

    public String getMessage() {
        return message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}