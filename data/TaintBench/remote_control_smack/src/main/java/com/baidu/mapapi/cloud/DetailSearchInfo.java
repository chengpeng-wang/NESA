package com.baidu.mapapi.cloud;

public class DetailSearchInfo {
    public String ak;
    public int id;
    public int scope = 1;
    public String sn;
    public int timeStamp = 0;

    /* access modifiers changed from: 0000 */
    public String a() {
        if (this.id == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("/");
        stringBuilder.append(this.id).append('?');
        stringBuilder.append("scope=").append(this.scope);
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
