package com.rocui.mvc.filter;

import com.rocui.mvc.view.View;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ActionFilter {

    private ActionFilter next;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public abstract View filter() throws Exception;

    public void setFilter(ActionFilter filter) {
        this.next = filter;
    }

    public View next() throws Exception {
        if (null != this.next) {
            this.next.request = this.request;
            return this.next.filter();
        } else {
            return null;
        }
    }
}
