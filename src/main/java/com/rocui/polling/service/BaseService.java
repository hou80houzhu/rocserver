package com.rocui.polling.service;

import com.rocui.polling.base.PollingRequest;
import com.rocui.polling.base.PollingResponse;
import javax.servlet.http.HttpServletRequest;

public abstract class BaseService {

    protected HttpServletRequest request;

    public abstract void excute(PollingRequest request, PollingResponse response);
}
