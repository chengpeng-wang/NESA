package cn.smsmanager.internet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class SocketHttpRequester {
    public static byte[] postXml(String path, String xml, String encoding) throws Exception {
        byte[] data = xml.getBytes(encoding);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml; charset=" + encoding);
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        conn.setConnectTimeout(5000);
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            return readStream(conn.getInputStream());
        }
        return null;
    }

    public static boolean post(String path, Map<String, String> params, FormFile[] files) throws Exception {
        String BOUNDARY = "---------------------------7da2137580612";
        String endline = "-----------------------------7da2137580612--\r\n";
        int fileDataLength = 0;
        for (FormFile uploadFile : files) {
            StringBuilder fileExplain = new StringBuilder();
            fileExplain.append("--");
            fileExplain.append("---------------------------7da2137580612");
            fileExplain.append("\r\n");
            fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
            fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
            fileExplain.append("\r\n");
            fileDataLength += fileExplain.length();
            if (uploadFile.getInStream() != null) {
                fileDataLength = (int) (((long) fileDataLength) + uploadFile.getFile().length());
            } else {
                fileDataLength += uploadFile.getData().length;
            }
        }
        StringBuilder textEntity = new StringBuilder();
        for (Entry<String, String> entry : params.entrySet()) {
            textEntity.append("--");
            textEntity.append("---------------------------7da2137580612");
            textEntity.append("\r\n");
            textEntity.append("Content-Disposition: form-data; name=\"" + ((String) entry.getKey()) + "\"\r\n\r\n");
            textEntity.append((String) entry.getValue());
            textEntity.append("\r\n");
        }
        int dataLength = (textEntity.toString().getBytes().length + fileDataLength) + "-----------------------------7da2137580612--\r\n".getBytes().length;
        URL url = new URL(path);
        int port = url.getPort() == -1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        outStream.write(("POST " + url.getPath() + " HTTP/1.1\r\n").getBytes());
        outStream.write("Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n".getBytes());
        outStream.write("Accept-Language: zh-CN\r\n".getBytes());
        outStream.write("Content-Type: multipart/form-data; boundary=---------------------------7da2137580612\r\n".getBytes());
        outStream.write(("Content-Length: " + dataLength + "\r\n").getBytes());
        outStream.write("Connection: Keep-Alive\r\n".getBytes());
        outStream.write(("Host: " + url.getHost() + ":" + port + "\r\n").getBytes());
        outStream.write("\r\n".getBytes());
        outStream.write(textEntity.toString().getBytes());
        for (FormFile uploadFile2 : files) {
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append("---------------------------7da2137580612");
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile2.getParameterName() + "\";filename=\"" + uploadFile2.getFilname() + "\"\r\n");
            fileEntity.append("Content-Type: " + uploadFile2.getContentType() + "\r\n\r\n");
            outStream.write(fileEntity.toString().getBytes());
            if (uploadFile2.getInStream() != null) {
                byte[] buffer = new byte[1024];
                while (true) {
                    int len = uploadFile2.getInStream().read(buffer, 0, 1024);
                    if (len == -1) {
                        break;
                    }
                    outStream.write(buffer, 0, len);
                }
                uploadFile2.getInStream().close();
            } else {
                outStream.write(uploadFile2.getData(), 0, uploadFile2.getData().length);
            }
            outStream.write("\r\n".getBytes());
        }
        outStream.write("-----------------------------7da2137580612--\r\n".getBytes());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if (bufferedReader.readLine().indexOf("200") == -1) {
            return false;
        }
        outStream.flush();
        outStream.close();
        bufferedReader.close();
        socket.close();
        return true;
    }

    public static boolean post(String path, Map<String, String> params, FormFile file) throws Exception {
        return post(path, (Map) params, new FormFile[]{file});
    }

    public static byte[] postFromHttpClient(String path, Map<String, String> params, String encode) throws Exception {
        List<NameValuePair> formparams = new ArrayList();
        for (Entry<String, String> entry : params.entrySet()) {
            formparams.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, encode);
        HttpPost httppost = new HttpPost(path);
        httppost.setEntity(entity);
        return readStream(new DefaultHttpClient().execute(httppost).getEntity().getContent());
    }

    public static byte[] post(String path, Map<String, String> params, String encode) throws Exception {
        StringBuilder parambuilder = new StringBuilder("");
        if (!(params == null || params.isEmpty())) {
            for (Entry<String, String> entry : params.entrySet()) {
                parambuilder.append((String) entry.getKey()).append("=").append(URLEncoder.encode((String) entry.getValue(), encode)).append("&");
            }
            parambuilder.deleteCharAt(parambuilder.length() - 1);
        }
        byte[] data = parambuilder.toString().getBytes();
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        conn.setRequestProperty("Connection", "Keep-Alive");
        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(data);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            return readStream(conn.getInputStream());
        }
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = inStream.read(buffer);
            if (len == -1) {
                outSteam.close();
                inStream.close();
                return outSteam.toByteArray();
            }
            outSteam.write(buffer, 0, len);
        }
    }

    public static String sockPost(String path, Map<String, String> params, String encode) throws Exception {
        String data = "";
        for (Entry<String, String> entry : params.entrySet()) {
            data = new StringBuilder(String.valueOf(data)).append(URLEncoder.encode((String) entry.getKey(), encode)).append("=").append(URLEncoder.encode((String) entry.getValue(), encode)).append("&").toString();
        }
        data = data.substring(0, data.length() - 1);
        URL url = new URL(path);
        int port = url.getPort() == -1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
        outStream.write(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(requestmethod)).append("Host: " + url.getHost() + ":" + port + "\r\n").append("Content-Length: " + data.getBytes().length + "\r\n").append("Connection: close\r\n").append("Content-Type: application/x-www-form-urlencoded\r\n").toString())).append("\r\n").append(data).toString().getBytes());
        outStream.flush();
        String response = new String(readStream(socket.getInputStream()), encode);
        return response.substring(response.indexOf("\r\n\r\n") + 4);
    }

    public static void sockPostNoResponse(String path, Map<String, String> params, String encode) throws Exception {
        String data = "";
        for (Entry<String, String> entry : params.entrySet()) {
            data = new StringBuilder(String.valueOf(data)).append(URLEncoder.encode((String) entry.getKey(), encode)).append("=").append(URLEncoder.encode((String) entry.getValue(), encode)).append("&").toString();
        }
        data = data.substring(0, data.length() - 1);
        URL url = new URL(path);
        int port = url.getPort() == -1 ? 80 : url.getPort();
        OutputStream outStream = new Socket(InetAddress.getByName(url.getHost()), port).getOutputStream();
        String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
        outStream.write(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(requestmethod)).append("Host: " + url.getHost() + ":" + port + "\r\n").append("Content-Length: " + data.getBytes().length + "\r\n").append("Content-Type: application/x-www-form-urlencoded\r\n").toString())).append("\r\n").append(data).toString().getBytes());
        outStream.flush();
    }
}
