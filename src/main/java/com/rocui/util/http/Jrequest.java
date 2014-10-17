package com.rocui.util.http;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.SSLContext;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Jrequest {

    public final static String CHARSET_UTF8 = "UTF-8";
    public final static String CHARSET_GBK = "gbk";

    private String url;
    private String charset = "utf-8";
    private String pagecharset = "utf-8";
    private HashMap<String, String> header = new HashMap<String, String>();
    private HashMap<String, String> parameters = new HashMap<String, String>();
    private List<Cookie> cookies = new ArrayList<Cookie>();

    protected HashMap<String, xfile> files = new HashMap<String, xfile>();
    protected HashMap<String, xtext> texts = new HashMap<String, xtext>();
    protected String text = null;

    protected CookieStore cookieStore = new BasicCookieStore();

    public Jrequest appendText(String text) {
        this.text = text;
        return this;
    }

    public Jrequest addText(String key, String text, ContentType contentType) {
        this.texts.put(key, new xtext(text, contentType));
        return this;
    }

    public Jrequest addFile(String key, String path, ContentType contentType) {
        this.files.put(key, new xfile(new File(path), contentType));
        return this;
    }

    public HttpEntity geEntity() throws Exception {
        HttpEntity entity;
        if (text != null) {
            entity = new StringEntity(text, charset);
        } else if (files.size() > 0) {
            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            for (Entry<String, xfile> x : this.files.entrySet()) {
                xfile file = x.getValue();
                reqEntity.addPart(x.getKey(), new FileBody(file.file, file.type));
            }
            for (Entry<String, String> xx : this.parameters.entrySet()) {
                reqEntity.addPart(xx.getKey(), new StringBody(xx.getValue(), ContentType.DEFAULT_TEXT));
            }
            for (Entry<String, xtext> xxx : this.texts.entrySet()) {
                reqEntity.addPart(xxx.getKey(), new StringBody(xxx.getValue().text, xxx.getValue().type));
            }
            entity = reqEntity.build();
        } else {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> xx : this.parameters.entrySet()) {
                nvps.add(new BasicNameValuePair(xx.getKey(), xx.getValue()));
            }
            entity = new UrlEncodedFormEntity(nvps, charset);
        }
        return entity;
    }

    public static Jrequest getRequest() {
        Jrequest request = new Jrequest();
        request.header.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        request.header.put("accept-language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,fr;q=0.2");
        request.header.put("user-agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; .NET4.0C; .NET4.0E)");
        request.header.put("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        return request;
    }

    public Jrequest setURL(String url) {
        this.url = url;
        return this;
    }

    public Jrequest setPageCharset(String charset) {
        this.pagecharset = charset;
        return this;
    }

    public Jrequest setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public Jrequest addCookie(Jcookie jcookie) {
        this.cookies.add(jcookie.cookie);
        return this;
    }

    public Jrequest addCookie(Cookie cookie) {
        this.cookies.add(cookie);
        return this;
    }

    public Jrequest addCookies(CookieStore store) {
        List<Cookie> list = store.getCookies();
        for (Cookie cookie : list) {
            this.cookies.add(cookie);
        }
        return this;
    }

    public Jrequest addHeader(String key, String value) {
        this.header.put(key, value);
        return this;
    }

    public Jrequest addParameter(String key, String value) {
        this.parameters.put(key, value);
        return this;
    }

    public void doGetSSL(Jresponse responsex) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = this.getSSLClient();
            HttpGet httpost = this.getMethodGet();
            HttpClientContext context = HttpClientContext.create();
            context.setCookieStore(cookieStore);
            response = httpClient.execute(httpost, context);
            this.setResponse(context, response, responsex);
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

    public void doPostSSL(Jresponse responsex) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = this.getSSLClient();
            HttpPost httpost = new HttpPost(url);
            HttpClientContext context = HttpClientContext.create();
            context.setCookieStore(cookieStore);
            response = httpClient.execute(httpost, context);
            this.setResponse(context, response, responsex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

    public void doPost(Jresponse responsex) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = this.getClient();
            HttpPost httpost = this.getMethPost();
            HttpClientContext context = HttpClientContext.create();
            context.setCookieStore(cookieStore);
            response = httpClient.execute(httpost, context);
            this.setResponse(context, response, responsex);
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

    private CloseableHttpClient getClient() {
        for (Cookie cox : this.cookies) {
            cookieStore.addCookie(cox);
        }
        return HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    private CloseableHttpClient getSSLClient() throws Exception {
        for (Cookie cox : this.cookies) {
            cookieStore.addCookie(cox);
        }
        SSLContext ctx = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);
        return HttpClients.custom().setDefaultCookieStore(cookieStore).setSSLSocketFactory(socketFactory).build();
    }

    private HttpGet getMethodGet() throws Exception {
        StringBuilder querystring = new StringBuilder();
        String querystringx = "";
        for (Entry<String, String> xx : this.parameters.entrySet()) {
            querystring.append(URLEncoder.encode(xx.getKey(), charset));
            querystring.append("=");
            querystring.append(URLEncoder.encode(xx.getValue(), charset));
            querystring.append("&");
        }
        if (querystring.length() > 0) {
            querystringx = querystring.substring(0, querystring.length() - 1);
        }
        if (url.contains("?")) {
            url = url + querystringx;
        } else {
            if (querystringx.length() > 0) {
                url = url + "?" + querystringx;
            }
        }
        HttpGet get = new HttpGet(url);
        for (Entry<String, String> x : this.header.entrySet()) {
            get.addHeader(x.getKey(), x.getValue());
        }
        return get;
    }

    private HttpPost getMethPost() throws Exception {
        HttpPost httpost = new HttpPost(this.url);
        for (Entry<String, String> x : this.header.entrySet()) {
            httpost.addHeader(x.getKey(), x.getValue());
        }
        httpost.setEntity(this.geEntity());
        for (Entry<String, String> x : this.header.entrySet()) {
            httpost.addHeader(x.getKey(), x.getValue());
        }
        httpost.setEntity(this.geEntity());
        return httpost;
    }

    private void setResponse(HttpClientContext context, CloseableHttpResponse response, Jresponse jresponsex) throws Exception {
        jresponsex.hreader = this.getHeader(response);
        jresponsex.status = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        jresponsex.contentLength = entity.getContentLength();
        try {
            jresponsex.body = EntityUtils.toString(entity, this.charset);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        jresponsex.cookieStore = context.getCookieStore();
        jresponsex.cookieOrigin = context.getCookieOrigin();
        int statuscode = jresponsex.status;
        if (statuscode == HttpStatus.SC_OK) {
            jresponsex.success(jresponsex.body);
        } else if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER) || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
            Header redirectLocation = response.getFirstHeader("Location");
            String newuri = redirectLocation.getValue();
            if (newuri != null && !newuri.equals("")) {
                this.cloneJrequest().doGet(jresponsex);
            } else {
                jresponsex.error();
            }
        } else {
            jresponsex.error();
        }
    }

    public void doGet(Jresponse responsex) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = this.getClient();
            HttpGet httpost = this.getMethodGet();
            HttpClientContext context = HttpClientContext.create();
            context.setCookieStore(cookieStore);
            response = httpClient.execute(httpost, context);
            this.setResponse(context, response, responsex);
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

    private Jrequest cloneJrequest() {
        Jrequest request = new Jrequest();
        request.charset = this.charset;
        request.header = this.header;
        request.parameters = this.parameters;
        request.url = this.url;
        return request;
    }

    private Jresponse getJresponse(CloseableHttpResponse response, Jresponse jresponse) {
        jresponse.hreader = this.getHeader(response);
        jresponse.status = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        jresponse.contentLength = entity.getContentLength();
        try {
            jresponse.body = EntityUtils.toString(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jresponse;
    }

    private HashMap<String, String> getHeader(CloseableHttpResponse response) {
        HashMap<String, String> map = new HashMap<String, String>();
        Header[] headers = response.getAllHeaders();
        for (Header headerx : headers) {
            map.put(headerx.getName(), headerx.getValue());
        }
        return map;
    }

    public class xfile {

        private ContentType type;
        private File file;

        public xfile(File file, ContentType type) {
            this.type = type;
            this.file = file;
        }
    }

    public class xtext {

        private ContentType type;
        private String text;

        public xtext(String text, ContentType type) {
            this.type = type;
            this.text = text;
        }
    }

    public static void main(String[] args) throws Exception {
//        Jxml.create("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//                + "<soapenv:Envelope "
//                + "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
//                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
//                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
//                + "<soapenv:Body>"
//                + "<ns1:queryUser soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://services.zhang.com\">"
//                + "<in0 xsi:type=\"ns2:UserInfo\" xsi:nil=\"true\" xmlns:ns2=\"http://tdo.zhang.com\"/>"
//                + "</ns1:queryUser>"
//                + "</soapenv:Body>"
//                + "</soapenv:Envelope>").print();
//        Jquery.createTag("soapenv:Envelope")
//                .addNameSpace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/")
//                .addNameSpace("xsd", "http://www.w3.org/2001/XMLSchema/")
//                .addNameSpace("xsi", "http://www.w3.org/2001/XMLSchema-instance/")
//                .appendWith("soapenv:Body")
//                .appendWith("ns1:queryUser")
//                .attribute("soapenv:encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/")
//                .addNameSpace("ns1", "http://services.zhang.com")
//                .appendSingleWith("in0")
//                .attribute("xsi:type", "ns2:UserInfo")
//                .addNameSpace("ns2", "http://tdo.zhang.com")
//                .attribute("xsi:nil", "true").parent()
//                .appendWith("test")
//                .root().print();
//        Jquery c = Jxml.create("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body></soap:Body></soap:Envelope>");
//                c.children().eq(0)
//                .append("<mt xmlns=\"http://tempuri.org/\"></mt>").children().eq(0)
//                .append("<sn>SDK-111-010-01962</sn>")
//                .append("<pwd>886107</pwd>")
//                .append("<mobile>13478699805</mobile>")
//                .append("<content></content>")
//                .append("<stime></stime>")
//                .append("<rrid></rrid>");
//        System.out.println(c.xml());
        String sn = "SDK-111-010-01962";
        String pwd = "886107";
        Jrequest.getRequest()
                .setURL("https://www.google.com.hk")
                .doGetSSL(new Jresponse() {
                    @Override
                    public void success(String result) {
                        System.out.println(result);
                    }

                    @Override
                    public void error() {
                        System.out.println(this.body);
                    }
                });
    }
}
