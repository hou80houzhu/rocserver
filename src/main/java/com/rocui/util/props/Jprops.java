package com.rocui.util.props;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

public class Jprops {

    private Properties props = new Properties();

    private Jprops(File file) throws Exception {
        InputStream x = new FileInputStream(file);
        this.props.load(x);
    }

    private Jprops(String filepath) throws Exception {
        InputStream x = new FileInputStream(new File(filepath));
        this.props.load(x);
    }

    private Jprops() {
    }

    public static Jprops with(String filepath) throws Exception {
        return new Jprops(filepath);
    }

    public static Jprops with(File file) throws Exception {
        return new Jprops(file);
    }

    public static Jprops create() {
        return new Jprops();
    }

    public String get(String key) {
        return this.props.getProperty(key);
    }

    public String get(String key, String defaultString) {
        return this.props.getProperty(key, defaultString);
    }

    public Jprops set(String key, String value) {
        this.props.setProperty(key, value);
        return this;
    }

    public Jprops set(HashMap<String, String> map) {
        for (Entry<String, String> x : map.entrySet()) {
            this.props.setProperty(x.getKey(), x.getValue());
        }
        return this;
    }

    public Jprops each(JpropsEach each) {
        Enumeration er = props.propertyNames();
        while (er.hasMoreElements()) {
            String paramName = (String) er.nextElement();
            boolean isbreak = each.each(paramName, props.getProperty(paramName));
            if (isbreak) {
                break;
            }
        }
        return this;
    }

    public Jprops save(Writer writer, String comment) throws IOException {
        this.props.store(writer, comment);
        return this;
    }

    public Jprops save(OutputStream out, String comment) throws IOException {
        this.props.store(out, comment);
        return this;
    }

    public Jprops save(File file, String comment) throws IOException {
        OutputStream out = new FileOutputStream(file);
        this.props.store(out, comment);
        return this;
    }

    public Jprops save(String fileName, String comment) throws IOException {
        OutputStream out = new FileOutputStream(new File(fileName));
        this.props.store(out, comment);
        return this;
    }

    public Jprops saveXML(OutputStream out, String comment) throws IOException {
        this.props.storeToXML(out, comment);
        return this;
    }

    public Jprops saveXML(File file, String comment) throws IOException {
        OutputStream out = new FileOutputStream(file);
        this.props.storeToXML(out, comment);
        return this;
    }

    public Jprops saveXML(String fileName, String comment) throws IOException {
        OutputStream out = new FileOutputStream(new File(fileName));
        this.props.storeToXML(out, comment);
        return this;
    }
}
