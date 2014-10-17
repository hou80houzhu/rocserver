package com.rocui.jmail.data;

import com.rocui.jmail.page.JmailPage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JmailInfo {

    public static final String TEXTMAIL = "tm";
    public static final String HTMLMAIL = "hm";
    private String from = null;

    private List<String> cc = new ArrayList<String>();
    private List<String> bcc = new ArrayList<String>();
    private List<String> to = new ArrayList<String>();

    private String type = JmailInfo.HTMLMAIL;

    private String subject;
    List<String> filepath = new ArrayList<String>();

    String content = "";

    public JmailInfo attachFile(File file) {
        if (file != null) {
            this.filepath.add(file.getPath());
        }
        return this;
    }

    public static JmailInfo getInfo() {
        return new JmailInfo();
    }

    public JmailInfo attachFile(String filepath) {
        if (filepath != null && filepath.length() > 0) {
            this.filepath.add(filepath);
        }
        return this;
    }

    public JmailInfo addCc(String address) {
        this.cc.add(address);
        return this;
    }

    public JmailInfo addBcc(String address) {
        this.bcc.add(address);
        return this;
    }

    public JmailInfo addTo(String to) {
        this.to.add(to);
        return this;
    }

    public List<String> getAttach() {
        return this.filepath;
    }

    public String getFrom() {
        return from;
    }

    public JmailInfo setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public JmailInfo setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public JmailInfo setBcc(List<String> bcc) {
        this.bcc = bcc;
        return this;
    }

    public List<String> getTo() {
        return to;
    }

    public JmailInfo setTo(List<String> to) {
        this.to = to;
        return this;
    }

    public String getType() {
        return type;
    }

    public JmailInfo setType(String type) {
        this.type = type;
        return this;
    }

    public JmailInfo setContent(String content) {
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public JmailInfo setContent(String pageName, HashMap<String, Object> info) throws IOException {
        this.content = JmailPage.getContainer().getMailTemp(pageName).appendInfos(info).getString();
        return this;
    }
}
