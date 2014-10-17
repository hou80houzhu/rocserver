package com.rocui.jpager.base;

import java.util.HashMap;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;

public class URLQueryHandler {

    private String enterURL;
    private HashMap<String, String> queries;
    private String baseURL;
    private String currentURL;
    private HashMap<String, String> baseQueries;
    private HttpServletRequest request;
    private String settingURL;

    public URLQueryHandler(HttpServletRequest request) {
        this.request = request;
        this.enterURL = request.getRequestURL() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        this.currentURL = request.getRequestURL().toString();
        this.baseURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        this.baseQueries = this.parseQuery(request.getQueryString());
        this.queries = this.parseQuery(request.getQueryString());
        this.settingURL = this.currentURL;
    }

    public URLQueryHandler reset() {
        this.queries = this.parseQuery(request.getQueryString());
        this.settingURL = this.currentURL;
        return this;
    }

    public URLQueryHandler setPath(String path) {
        this.settingURL = this.baseURL + path;
        return this;
    }

    public URLQueryHandler remove(String key) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (Entry<String, String> c : this.queries.entrySet()) {
            if (!c.getKey().equals(key)) {
                map.put(c.getKey(), c.getValue());
            }
        }
        this.queries = map;
        return this;
    }

    public URLQueryHandler set(String key, String value) {
        this.queries.put(key, value);
        return this;
    }

    public URLQueryHandler empty() {
        this.queries = new HashMap<String, String>();
        return this;
    }

    public String get(String key) {
        return this.baseQueries.get(key);
    }

    public String getKey(String key) {
        return this.request.getParameter(key);
    }

    public String getURL(HashMap<String, String> queries) {
        return this.setURLQueries(enterURL, queries);
    }

    public String getURL(String key, String value) {
        HashMap<String, String> t = new HashMap<String, String>();
        t.put(key, value);
        return this.setURLQueries(enterURL, t);
    }

    public String getURL() {
        String t = this.appendURL(this.settingURL, queries);
        return t;
    }

    private String appendURL(String url, HashMap<String, String> map) {
        StringBuffer n = new StringBuffer();
        for (Entry<String, String> t : map.entrySet()) {
            n.append("&" + t.getKey() + "=" + t.getValue());
        }
        if (n.length() > 0) {
            url += "?" + n.deleteCharAt(0).toString();
        }
        return url;
    }

    private String setURLQueries(String url, HashMap<String, String> queries) {
        String reg = null;
        StringBuffer ps = new StringBuffer();
        StringBuffer qs = new StringBuffer();
        ps.append("(");
        for (Entry<String, String> t : queries.entrySet()) {
            ps.append(t.getKey()).append("|");
            qs.append("&" + t.getKey() + "=");
            qs.append(t.getValue());
        }
        ps.deleteCharAt(ps.length() - 1);
        ps.append(")");
        reg = "(?<=[\\?&])" + ps.toString() + "=[^&]*&?";
        url = url.replaceAll(reg, "");
        url = url.replaceAll("(\\?|&+)$", "");
        if (url.indexOf("?") >= 0) {
            url = url + qs;
        } else {
            url = url + "?" + qs.deleteCharAt(0);
        }
        return url;
    }

    private HashMap<String, String> parseQuery(String query) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (query != null && query != "") {
            String[] t = query.split("[&]");
            for (String a : t) {
                String[] b = a.split("[=]");
                if (b.length > 1) {
                    map.put(b[0], b[1]);
                } else {
                    map.put(b[0], "");
                }
            }
        }
        return map;
    }
}
