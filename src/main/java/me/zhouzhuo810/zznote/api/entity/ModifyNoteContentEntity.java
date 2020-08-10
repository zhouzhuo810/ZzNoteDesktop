package me.zhouzhuo810.zznote.api.entity;

import java.util.List;
/**
 * 修改便签内容
 */
public class ModifyNoteContentEntity {
    private int code; 
    private String msg; 

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}