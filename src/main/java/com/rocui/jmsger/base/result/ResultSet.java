package com.rocui.jmsger.base.result;

public class ResultSet {

    public static int RESULT_SUCCESS = 1;
    public static int RUSUTL_ERROR = 0;
    private Result result;
    private String routName;
    private String routType;
    

    public ResultSet() {
        this.result=new Result();
    }

    public Result getResult(){
        return this.result;
    }
    public ResultSet setTo(String to) {
        this.result.setTo(to);
        return this;
    }

    public ResultSet setCode(int code) {
        this.result.setCode(code);
        return this;
    }

    public ResultSet setData(Object data) {
        this.result.setData(data);
        return this;
    }

    public String getTo() {
        return this.result.getTo();
    }

    public int getCode() {
        return this.result.getCode();
    }

    public Object getData() {
        return this.result.getData();
    }

    public String getRoutName() {
        return routName;
    }

    public ResultSet setRoutName(String routName) {
        this.routName = routName;
        return this;
    }

    public String getRoutType() {
        return routType;
    }

    public ResultSet setRoutType(String routType) {
        this.routType = routType;
        return this;
    }
    
    public ResultSet setService(String service){
        this.result.setService(service);
        return this;
    }
    public ResultSet setSessionId(String sessionId){
        this.result.setSessionId(sessionId);
        return this;
    }
    public ResultSet setGroupId(String groupId){
        this.result.setGroupId(groupId);
        return this;
    }
    public ResultSet setCallbackIndex(String callbackIndex){
        this.result.setCallbackIndex(callbackIndex);
        return this;
    }
    public ResultSet setOther(String other){
        this.result.setOther(other);
        return this;
    }
    public ResultSet setFrom(String from){
        this.result.setFrom(from);
        return this;
    }
}
