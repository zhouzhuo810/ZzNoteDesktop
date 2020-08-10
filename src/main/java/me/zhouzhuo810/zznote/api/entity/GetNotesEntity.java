package me.zhouzhuo810.zznote.api.entity;

/**
 * 获取便签内容
 */
public class GetNotesEntity {
    private int code; 
    private String msg; 
    private DataEntity data;

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

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private long noteId;
        private String content; 
        private long createTime;
        private String wordCount; 

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public long getNoteId() {
            return noteId;
        }

        public void setNoteId(long noteId) {
            this.noteId = noteId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setWordCount(String wordCount) {
            this.wordCount = wordCount;
        }

        public String getWordCount() {
            return wordCount;
        }
    }
}