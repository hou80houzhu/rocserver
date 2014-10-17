package com.rocui.jmail;

import com.rocui.jmail.data.JmailConfig;
import com.rocui.jmail.data.JmailInfo;
import com.rocui.util.worker.JworkerContainer;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Jmail {

    private JmailConfig config;

    public Jmail() {
        this.config = JmailConfig.getConfig();
    }

    public Jmail(JmailConfig config) {
        this.config = config;
    }

    public static void sentToService(JmailInfo info) {
        Jmail mail = new Jmail();
        JmailTask task = new JmailTask(mail, info);
        JworkerContainer.getContainer().getSinglePool("xxmailonly").addTask(task);
    }

    public static void sentToService(JmailConfig config, JmailInfo info) {
        Jmail mail = new Jmail(config);
        JmailTask task = new JmailTask(mail, info);
        JworkerContainer.getContainer().getSinglePool("xxmailonly").addTask(task);
    }

    public static void sent(JmailInfo info) {
        new Jmail().sentMail(info);
    }

    public static void sent(JmailConfig config, JmailInfo info) {
        new Jmail(config).sentMail(info);
    }

    public static Jmail getMail() {
        return new Jmail();
    }

    public static Jmail getMail(JmailConfig config) {
        return new Jmail(config);
    }

    public void sentMail(JmailInfo info) {
        String from = info.getFrom();
        if (from == null) {
            from = config.getFrom();
        }
        List<String> to = info.getTo();
        List<String> cc = info.getCc();
        List<String> bcc = info.getBcc();
        String subject = info.getSubject();
        String content = info.getContent();
        List<String> file = info.getAttach();
        Session session = Session.getInstance(config.getProperties(), new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });
        if (config.isDebug()) {
            session.setDebug(true);
            System.out.println("[mail config]:" + config.getProperties());
        }
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            for (String xto : to) {
                InternetAddress[] address = {new InternetAddress(xto)};
                msg.setRecipients(Message.RecipientType.TO, address);
            }
            for (String xto : cc) {
                InternetAddress[] address = {new InternetAddress(xto)};
                msg.setRecipients(Message.RecipientType.CC, address);
            }
            for (String xto : bcc) {
                InternetAddress[] address = {new InternetAddress(xto)};
                msg.setRecipients(Message.RecipientType.BCC, address);
            }
            msg.setSubject(subject);
            Multipart mp = new MimeMultipart();
            MimeBodyPart mbpContent = new MimeBodyPart();
            if (info.getType().equals(JmailInfo.TEXTMAIL)) {
                mbpContent.setText(content, "utf-8");
            } else {
                mbpContent.setContent(content, "text/html;charset=utf-8");
            }
            mp.addBodyPart(mbpContent);
            for (String path : file) {
                MimeBodyPart mbpFile = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(path);
                mbpFile.setDataHandler(new DataHandler(fds));
                mbpFile.setFileName(fds.getName());
                mp.addBodyPart(mbpFile);
            }

            msg.setContent(mp);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        HashMap<String, Object> info = new HashMap<String, Object>();
        info.put("aa", "xxxxxxxx");
        Jmail.sent(
                JmailConfig.getConfig()
                .setFrom("market@siliconspread.com")
                .setPort(465)
                .setHost("smtp.exmail.qq.com")
                .setPassword("guizhan123")
                .setUsername("market@siliconspread.com")
                .setSocketFactoryClass("javax.net.ssl.SSLSocketFactory")
                .setAuth(true),
                JmailInfo.getInfo()
                .addTo("398414000@qq.com")
                .setSubject("just test")
                //.setContent("just test...")
                .setContent("test", info)
                .setType(JmailInfo.HTMLMAIL)
        );
    }
}
