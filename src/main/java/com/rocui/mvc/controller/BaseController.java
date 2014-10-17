package com.rocui.mvc.controller;

import com.rocui.mvc.view.JsonView;
import com.rocui.mvc.view.JspxView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    protected JsonView getJsonView(Object obj) {
        return new JsonView(obj);
    }

    protected JsonView getJsonView(String str) {
        return new JsonView(str);
    }

    protected JspxView getJspxView(String path) {
        return new JspxView(path);
    }
}
