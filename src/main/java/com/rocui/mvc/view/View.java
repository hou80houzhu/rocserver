package com.rocui.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class View {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    public abstract void out() throws Exception;
}
