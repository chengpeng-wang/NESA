package com.baidu.mapapi.cloud;

import android.content.ContentValues;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map.Entry;

public class RegionSearchInfo {
    public String ak;
    public int cityID;
    public String cityName;
    public ContentValues filter = new ContentValues();
    public int pageIndex = 0;
    public int pageSize = 10;
    public String queryWords;
    public int scope = 1;
    public String sn;
    public int timeStamp = 0;

    /* access modifiers changed from: 0000 */
    public String a() {
        if (this.queryWords == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("?");
        if (this.queryWords != null) {
            try {
                stringBuilder.append("q=").append(URLEncoder.encode(this.queryWords, StringEncodings.UTF8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (this.cityID != 0) {
            stringBuilder.append("&region=").append(this.cityID);
        } else if (this.cityName != null) {
            try {
                stringBuilder.append("&region=").append(URLEncoder.encode(this.cityName, StringEncodings.UTF8));
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
        }
        if (this.filter.size() > 0) {
            stringBuilder.append("&filter=");
            Iterator it = this.filter.valueSet().iterator();
            if (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String str = (String) entry.getKey();
                stringBuilder.append(str).append(':').append(entry.getValue());
                while (it.hasNext()) {
                    entry = (Entry) it.next();
                    str = (String) entry.getKey();
                    stringBuilder.append('|').append(str).append(':').append(entry.getValue());
                }
            }
        }
        stringBuilder.append("&page_index=").append(this.pageIndex);
        stringBuilder.append("&scope=").append(this.scope);
        stringBuilder.append("&page_size=").append(this.pageSize);
        if (this.ak != null) {
            stringBuilder.append("&ak=").append(this.ak);
        }
        if (this.sn != null) {
            stringBuilder.append("&sn=").append(this.sn);
            stringBuilder.append("&timestamp=").append(this.timeStamp);
        }
        return stringBuilder.toString();
    }
}
