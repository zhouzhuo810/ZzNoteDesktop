package me.zhouzhuo810.zznote.api.entity;

public class HeartBeatEntity {
    private int code;
    private String msg;
    private long editingNoteId;

    public void setEditingNoteId(long editingNoteId) {
        this.editingNoteId = editingNoteId;
    }

    public long getEditingNoteId() {
        return editingNoteId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
