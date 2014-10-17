package com.rocui.jmsger.base.result;

import com.rocui.jmsger.util.JmsgerUtils;


public class Result {
    private int code=1;
    private String to;
    private String service;
    private Object data;
    private String other;
    private String time;
    private String groupId;
    private String sessionId;
    private String callbackIndex;
    private String from;
 
    public Result(){
        this.time=JmsgerUtils.getCurrentTime();
    }
    public void setCode(int code) {
        this.code = code;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @return the other
     */
    public String getOther() {
        return other;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the callbackIndex
     */
    public String getCallbackIndex() {
        return callbackIndex;
    }

    /**
     * @param callbackIndex the callbackIndex to set
     */
    public void setCallbackIndex(String callbackIndex) {
        this.callbackIndex = callbackIndex;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }
    
}
