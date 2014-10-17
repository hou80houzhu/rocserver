package com.rocui.mvc.view;

import com.rocui.util.jsonx.Jsonx;
import java.io.IOException;

public class JsonView extends View {

    private String str;

    public JsonView(String str) {
        this.str = str;
    }

    public JsonView(Object obj) {
        this.str = Jsonx.create(obj).toString();
    }

    public void setResult(String a) {
        this.str = a;
    }
    public String getResult(){
        return this.str;
    }

    @Override
    public void out() throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.getWriter().write(str);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
