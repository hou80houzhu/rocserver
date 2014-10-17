package com.rocui.jmail.data;

import com.rocui.util.jsonx.Jsonx;
import java.io.File;
import java.util.Properties;

public class JmailConfig {

    private static JmailConfig config = null;
    private String username;
    private String password;
    private String host;
    private String from;
    private boolean auth = true;
    private int port = 25;
    private int connectiontimeout = 60000;
    private int timeout = 60000;
    private boolean quitwait = true;
    private String socketFactoryClass = "";
    private boolean socketFactoryFallback = false;
    private boolean debug = true;
    private boolean issl = false;

    private Properties props = null;

    private JmailConfig() {
    }

    private JmailConfig(String configpath) {
        try {
            this._set(Jsonx.create(new File(configpath)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JmailConfig(Jsonx json) {
        this._set(json);
    }

    private void _set(Jsonx json) {
        try {
            this.username = json.get("username").toString();
            this.password = json.get("password").toString();
            this.from = json.get("from").toString();
            this.host = json.get("host").toString();
            if (!json.get("auth").isNull()) {
                this.auth = json.get("auth").toBoolean();
            }
            if (!json.get("connectiontimeout").isNull()) {
                this.connectiontimeout = json.get("connectiontimeout").toInt();
            }
            if (!json.get("quitwait").isNull()) {
                this.quitwait = json.get("quitwait").toBoolean();
            }
            if (!json.get("timeout").isNull()) {
                this.timeout = json.get("timeout").toInt();
            }
            if (!json.get("debug").isNull()) {
                this.debug = json.get("debug").toBoolean();
            }
            if (!json.get("socketFactoryFallback").isNull()) {
                this.socketFactoryFallback = json.get("socketFactoryFallback").toBoolean();
            }
            if (!json.get("socketFactoryClass").isNull()) {
                this.socketFactoryClass = json.get("socketFactoryClass").toString();
            }
            if (!json.get("ssl").isNull()) {
                this.issl = json.get("ssl").toBoolean();
                if (this.issl) {
                    this.socketFactoryClass = "javax.net.ssl.SSLSocketFactory";
                }
            }
            if (!json.get("auth").isNull()) {
                this.auth = json.get("auth").toBoolean();
            }
            if (!json.get("port").isNull()) {
                this.port = json.get("port").toInt();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized JmailConfig init(String configpath) {
        if (JmailConfig.config == null) {
            JmailConfig.config = new JmailConfig(configpath);
        }
        return JmailConfig.config;
    }

    public static synchronized JmailConfig init(Jsonx json) {
        if (JmailConfig.config == null) {
            JmailConfig.config = new JmailConfig(json);
        }
        return JmailConfig.config;
    }

    public static JmailConfig getConfig() {
        if (JmailConfig.config != null) {
            return JmailConfig.config.clone();
        } else {
            return new JmailConfig();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public boolean isAuth() {
        return auth;
    }

    public int getPort() {
        return port;
    }

    public String getFrom() {
        return from;
    }

    public int getConnectiontimeout() {
        return connectiontimeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean isQuitwait() {
        return quitwait;
    }

    public String getSocketFactoryClass() {
        return socketFactoryClass;
    }

    public boolean isSocketFactoryFallback() {
        return socketFactoryFallback;
    }

    public JmailConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public JmailConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public JmailConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public JmailConfig setAuth(boolean auth) {
        this.auth = auth;
        return this;
    }

    public JmailConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public JmailConfig setFrom(String from) {
        this.from = from;
        return this;
    }

    public JmailConfig setConnectiontimeout(int connectiontimeout) {
        this.connectiontimeout = connectiontimeout;
        return this;
    }

    public JmailConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public JmailConfig setQuitwait(boolean quitwait) {
        this.quitwait = quitwait;
        return this;
    }

    public JmailConfig setSocketFactoryClass(String socketFactoryClass) {
        this.socketFactoryClass = socketFactoryClass;
        return this;
    }

    public JmailConfig setSocketFactoryFallback(boolean socketFactoryFallback) {
        this.socketFactoryFallback = socketFactoryFallback;
        return this;
    }

    public Properties getProperties() {
        if (this.props == null) {
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", host);
            props.setProperty("mail.smtp.auth", Boolean.toString(this.auth));
            props.setProperty("mail.smtp.port", Integer.toString(this.port));
            if (this.issl) {
                props.setProperty("mail.smtp.socketFactory.port", Integer.toString(this.port));
                props.setProperty("mail.smtp.starttls.enable", "true");
            }
            props.setProperty("mail.smtp.socketFactory.class", this.socketFactoryClass);
            props.setProperty("mail.smtp.socketFactory.fallback", Boolean.toString(this.socketFactoryFallback));
            props.setProperty("mail.smtp.connectiontimeout", Integer.toString(this.connectiontimeout));
            props.setProperty("mail.smtp.timeout", Integer.toString(this.timeout));
            props.setProperty("mail.smtp.quitwait", Boolean.toString(this.quitwait));
            this.props = props;
        }
        return this.props;
    }

    public boolean isDebug() {
        return debug;
    }

    public JmailConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public JmailConfig setSSL(boolean issl) {
        this.issl = issl;
        return this;
    }

    public JmailConfig clone() {
        JmailConfig config = new JmailConfig();
        config.auth = this.auth;
        config.connectiontimeout = this.connectiontimeout;
        config.debug = this.debug;
        config.from = this.from;
        config.host = this.host;
        config.password = this.password;
        config.port = this.port;
        config.props = this.props;
        config.quitwait = this.quitwait;
        config.socketFactoryClass = this.socketFactoryClass;
        config.socketFactoryFallback = this.socketFactoryFallback;
        config.timeout = this.timeout;
        config.username = this.username;
        config.issl = this.issl;
        return config;
    }
}
