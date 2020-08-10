package me.zhouzhuo810.zznote;

public class NoteMessage {

    public static final String ACTION_OPEN_NOTE = "open_note";
    public static final String ACTION_CLOSE_NOTE = "close_note";
    public static final String ACTION_SELECTION_CHANGE = "action_selection_change";
    public static final String ACTION_TEXT_CHANGE = "action_text_change";

    private String action;
    private long id;
    private int selectionStart;
    private int selectionEnd;
    private String content;

    public int getSelectionStart() {
        return selectionStart;
    }

    public void setSelectionStart(int selectionStart) {
        this.selectionStart = selectionStart;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = selectionEnd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
