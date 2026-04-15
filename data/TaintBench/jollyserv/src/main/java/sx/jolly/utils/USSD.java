package sx.jolly.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class USSD {
    private static String endmsg = "MMI code has finished running";
    private static String startmsg = "displayMMIComplete";
    private static String trimmsg = "- using text from MMI message: '";
    private long after = 3000;
    private long before = 3000;
    private boolean found = false;
    private String msg = "";
    private long t = -1;

    public USSD(long before_creation, long after_creation) {
        this.before = before_creation;
        this.after = after_creation;
        long timestamp = System.currentTimeMillis();
        Utils.slog(USSD.class, "Class creation - timestamp: " + String.valueOf(timestamp));
        try {
            BufferedReader mReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("logcat -v time -b main PhoneUtils:D").getInputStream()), 1024);
            String line = "";
            boolean tostop = false;
            long stop = timestamp + this.after;
            while (true) {
                line = mReader.readLine();
                if (line != null && System.currentTimeMillis() < stop && !tostop) {
                    if (line.length() > 19) {
                        if (line.contains(startmsg)) {
                            this.t = extracttimestamp(line);
                            Utils.slog(USSD.class, "Found line at timestamp : " + String.valueOf(this.t));
                            if (this.t >= timestamp - this.before) {
                                this.found = true;
                            }
                        } else if (this.found) {
                            if (line.contains(endmsg)) {
                                tostop = true;
                            } else {
                                Utils.slog(USSD.class, "Line content : " + line);
                                String[] v = line.split("\\): ");
                                if (v.length > 1) {
                                    this.msg += v[1].replace(trimmsg, "").trim() + "\n";
                                }
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            Utils.slog(USSD.class, "Exception:" + e.toString());
        }
    }

    public boolean IsFound() {
        return this.found;
    }

    public String getMsg() {
        return this.msg;
    }

    private long extracttimestamp(String line) {
        String[] v = line.split(" ");
        if (v.length <= 1) {
            return -1;
        }
        try {
            long timestamp = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse(v[0] + "-" + Calendar.getInstance().get(1) + " " + v[1]).getTime();
            String[] ms = v[1].split(".");
            if (ms.length > 1) {
                return timestamp + ((long) Integer.getInteger(ms[1]).intValue());
            }
            return timestamp;
        } catch (ParseException e) {
            Utils.slog(USSD.class, "USDD.extractimestamp exception:" + e.toString());
            return -1;
        }
    }
}
