package com.rocui.polling.base.message;

public class PollingMessage {

    private String code;
    private Object content;
    private String callback;
    private Long time;

    public PollingMessage() {
        this.time = System.currentTimeMillis();
    }

    public PollingMessage(String code, Object content, String callback) {
        this.code = code;
        this.content = content;
        this.callback = callback;
        this.time = System.currentTimeMillis();
    }

    public static PollingMessage getMessage(String code, Object content, String callback) {
        return new PollingMessage(code, content, callback);
    }

    public static PollingMessage getSuccessMessage(Object content, String callback) {
        return new PollingMessage("1", content, callback);
    }

    public static PollingMessage getErrorMessage(Object content, String callback) {
        return new PollingMessage("0", content, callback);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
