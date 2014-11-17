package com.rocui.mvc.view;

public class StringView  extends View{

    private String str;
    
    public StringView(String str) {
        this.str = str;
    }

    public void setResult(String a) {
        this.str = a;
    }
    public String getResult(){
        return this.str;
    }
    
    @Override
    public void out() throws Exception {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.getWriter().write(str);
        response.getWriter().flush();
        response.getWriter().close();
    }
    
}
