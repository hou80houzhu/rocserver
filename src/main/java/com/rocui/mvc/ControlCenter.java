package com.rocui.mvc;

import com.rocui.mvc.ConnectionManager.BaseConnection;
import com.rocui.mvc.mapping.ControllerContainer;
import com.rocui.mvc.mapping.ControllerContainer.ControllerInfo;
import com.rocui.mvc.filter.ActionFilter;
import com.rocui.mvc.mapping.FilterContainer;
import com.rocui.mvc.mapping.ServiceContainer;
import com.rocui.mvc.view.View;
import com.rocui.util.base.reflect.ObjectSnooper;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ControlCenter implements Filter {

    public static ThreadLocal sessionState = new ThreadLocal();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[filter init]");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SessionState state = new SessionState();
        sessionState.set(state);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HashMap<String, Object> main = new HashMap<>();
        main.put("request", httpRequest);
        main.put("response", httpResponse);
        try {
            String url = httpRequest.getRequestURI();
            ControllerInfo info = ControllerContainer.getContainer().getInfo(url.substring(request.getServletContext().getContextPath().length()));
            if (null != info) {
                View isgoon = null;
                ActionFilter chainx = FilterContainer.getContainer().getFilterChain(info.getFilters());
                if (null != chainx) {
                    ObjectSnooper.snoop(chainx).sett(main);
                    isgoon = chainx.filter();
                }
                if (null == isgoon) {
                    ParameterInfo pas = this.getParameters(httpRequest);
                    HashMap<String, String> sers = info.getServices();
                    for (Entry<String, String> xpt : sers.entrySet()) {
                        pas.parameters.put(xpt.getKey(), ServiceContainer.getContainer().getService(xpt.getValue()));
                    }
                    try {
                        ObjectSnooper snooper = ObjectSnooper.snoop(info.getControllerName()).sett(main).set(pas.parameters);
                        Object result = null;
                        if (info.isTransation()) {
                            state.push();
                            BaseConnection connection = state.getConnectionManager().getConnection();
                            try {
                                connection.setAutoCommit(false);
                                if (info.isHasParas()) {
                                    result = snooper.trigger(info.getActionName(), info.getURLpars());
                                } else {
                                    result = snooper.trigger(info.getActionName());
                                }
                                connection.commit();
                            } catch (Exception e) {
                                connection.rollback();
                                throw e;
                            } finally {
                                connection.close();
                                state.pop();
                            }
                        } else {
                            if (info.isHasParas()) {
                                result = snooper.trigger(info.getActionName(), info.getURLpars());
                            } else {
                                result = snooper.trigger(info.getActionName());
                            }
                        }
                        if (null != result && result instanceof View) {
                            View view = (View) result;
                            ObjectSnooper.snoop(view).sett(main);
                            view.out();
                        }
                    } finally {
                        for (File file : pas.files) {
                            file.delete();
                        }
                    }
                } else {
                    //chain.doFilter(request, response);
                    ObjectSnooper.snoop(isgoon).sett(main);
                    isgoon.out();
                }
            } else {
                System.out.println(url + "-->[default]");
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (this.isAjaxRequest(httpRequest)) {
                httpResponse.getWriter().write("{\"code\":\"0\"}");
                httpResponse.getWriter().flush();
                httpResponse.getWriter().close();
            } else {
                throw new ServletException(e.fillInStackTrace());
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("[filter destroy]");
    }

    private ParameterInfo getParameters(HttpServletRequest request) throws UnsupportedEncodingException {
        ParameterInfo result = new ParameterInfo();
        request.setCharacterEncoding("UTF-8");
        String type = request.getHeader("Content-Type");
        HashMap<String, Object> hasmap = new HashMap<>();
        List<File> files = new ArrayList<>();
        Map xt = request.getParameterMap();
        Iterator i = xt.keySet().iterator();
        while (i.hasNext()) {
            String key = i.next().toString();
            hasmap.put(key, request.getParameter(key));
        }
        if (null != type && type.contains("multipart/form-data")) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            try {
                List items = upload.parseRequest(request);
                Iterator itr = items.iterator();
                while (itr.hasNext()) {
                    FileItem item = (FileItem) itr.next();
                    if (item.isFormField()) {
                        hasmap.put(item.getFieldName(), item.getString("UTF-8"));
                    } else {
                        if (item.getName() != null && !item.getName().equals("")) {
                            File tempFile = new File(item.getName());
                            File file = new File("D:\\temp", tempFile.getName());
                            item.write(file);
                            hasmap.put(item.getFieldName(), file);
                            files.add(file);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.parameters = hasmap;
        result.files = files;
        return result;
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header);
        return isAjax;
    }

    class ParameterInfo {

        private HashMap<String, Object> parameters;
        private List<File> files;
    }
}
