package com.rocui.mvc.controller;

import com.rocui.mvc.view.FileView;
import com.rocui.mvc.view.JsonView;
import com.rocui.mvc.view.JspxView;
import com.rocui.mvc.view.StringView;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    protected String basePath() {
        return request.getSession().getServletContext().getRealPath("/");
    }

    protected JsonView getJsonView(Object obj) {
        return new JsonView(obj);
    }

    protected JsonView getJsonView(String str) {
        return new JsonView(str);
    }

    protected JspxView getJspxView(String path) {
        return new JspxView(path);
    }

    protected StringView getStringView(String str) {
        return new StringView(str);
    }

    protected FileView getFileView(String path) {
        return new FileView(path);
    }

    protected FileView getFileView(File file) {
        return new FileView(file);
    }
}
