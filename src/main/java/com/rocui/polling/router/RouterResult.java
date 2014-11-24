package com.rocui.polling.router;

import com.rocui.polling.base.PollingRequest;
import com.rocui.polling.base.message.MessageWrapper;

public class RouterResult {

    private PollingRequest request;
    private MessageWrapper wrapper;

    public PollingRequest getRequest() {
        return request;
    }

    public void setRequest(PollingRequest request) {
        this.request = request;
    }

    public MessageWrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(MessageWrapper wrapper) {
        this.wrapper = wrapper;
    }

}
