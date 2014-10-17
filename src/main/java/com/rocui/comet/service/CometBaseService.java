package com.rocui.comet.service;

import com.rocui.comet.base.CometConnectCenter.CometConnect;
import com.rocui.comet.base.CometRequest;
import com.rocui.comet.base.CometResponse;
import javax.servlet.http.HttpServletRequest;

public abstract class CometBaseService {

    public static final String SERVICE_OFFLINE = "offline";
    public static final String SERVICE_NODATA = "nodata";
    public static final String SERVICE_MESSAGE = "message";
    public static final String SERVICE_SELF = "self";

    private CometRequest request;
    private HttpServletRequest req;
    private CometConnect connect;

    public abstract CometResponse excute();

    public CometRequest getCometRequest() {
        return request;
    }

    public void setRequest(CometRequest request) {
        this.request = request;
    }

    public HttpServletRequest getHttpRequest() {
        return req;
    }

    public void setReq(HttpServletRequest req) {
        this.req = req;
    }

    public CometConnect getConnect() {
        return connect;
    }

    public void setConnect(CometConnect connect) {
        this.connect = connect;
    }

}
