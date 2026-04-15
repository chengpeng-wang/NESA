package com.beita.contact;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {
    public static void sendByJavaMail(String str_from, String str_to, String str_title, String str_content) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "SMTP.126.COM");
            props.put("mail.smtp.auth", "true");
            Session s = Session.getInstance(props);
            s.setDebug(true);
            MimeMessage message = new MimeMessage(s);
            message.setFrom(new InternetAddress(str_from));
            message.setRecipient(RecipientType.TO, new InternetAddress(str_to));
            message.setSubject(str_title);
            message.setText(str_content, "utf-8");
            message.setSentDate(new Date());
            message.saveChanges();
            Transport transport = s.getTransport("smtp");
            transport.connect("SMTP.126.COM", "zhangdafeng2012", "a121994554");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
