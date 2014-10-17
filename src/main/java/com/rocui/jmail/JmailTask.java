package com.rocui.jmail;

import com.rocui.jmail.data.JmailInfo;
import com.rocui.util.worker.Jtask;

public class JmailTask extends Jtask {

    private Jmail mail;
    private JmailInfo info;

    public JmailTask(Jmail mail, JmailInfo info) {
        this.mail = mail;
        this.info = info;
    }

    @Override
    public void excute() {
        this.getMail().sentMail(this.getInfo());
    }

    public Jmail getMail() {
        return mail;
    }

    public void setMail(Jmail mail) {
        this.mail = mail;
    }

    public JmailInfo getInfo() {
        return info;
    }

    public void setInfo(JmailInfo info) {
        this.info = info;
    }

}
