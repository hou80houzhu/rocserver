package com.rocui.jmsger.base;

public class SocketRequest {

    private String type;
    private String to;
    private String object;
    private String method;
    private String content;
    private String parameters;
    private String sessionId;
    private String groupId;
    private String callbackIndex;
    private String other;
    private SocketSession session;

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    private void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }

    public SocketSession getSession() {
        return session;
    }

    private void setSession(SocketSession session) {
        this.session = session;
    }

    public String getObject() {
        return object;
    }

    private void setObject(String object) {
        this.object = object;
    }

    public String getMethod() {
        return method;
    }

    private void setMethod(String method) {
        this.method = method;
    }

    public String getOther() {
        return other;
    }

    private void setOther(String other) {
        this.other = other;
    }

    public String getParameters() {
        return parameters;
    }

    private void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCallbackIndex() {
        return callbackIndex;
    }

    public void setCallbackIndex(String callbackIndex) {
        this.callbackIndex = callbackIndex;
    }

}
