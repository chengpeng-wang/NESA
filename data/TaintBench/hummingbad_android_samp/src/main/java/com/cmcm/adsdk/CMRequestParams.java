package com.cmcm.adsdk;

import com.cmcm.adsdk.requestconfig.log.a;
import java.util.HashMap;
import java.util.Map;

public class CMRequestParams {
    public static final String KEY_GDT_REQUESTNUM = "key_gdt_requestnum";
    public static final String KEY_PICKS_LOAD_BY_PAGE = "picks_load_by_page";
    public static final String KEY_PICK_LOOP = "picks_loop";
    public static final String KEY_REPORT_SHOW_IGNORE_VIEW = "report_show_ignore_view";
    Map<String, Object> mParams;

    public CMRequestParams() {
        this.mParams = null;
        this.mParams = new HashMap();
    }

    public void setPicksLoop(boolean isLoop) {
        if (this.mParams != null) {
            this.mParams.put(KEY_PICK_LOOP, Boolean.valueOf(isLoop));
        }
    }

    public boolean getPicksLoop() {
        if (this.mParams != null) {
            Object obj = this.mParams.get(KEY_PICK_LOOP);
            if (obj != null) {
                return Boolean.valueOf(obj.toString()).booleanValue();
            }
        }
        return true;
    }

    public void setReportShowIgnoreView(boolean ignoreView) {
        if (this.mParams != null) {
            this.mParams.put(KEY_REPORT_SHOW_IGNORE_VIEW, Boolean.valueOf(ignoreView));
        }
    }

    public boolean getReportShowIgnoreView() {
        if (this.mParams != null) {
            Object obj = this.mParams.get(KEY_REPORT_SHOW_IGNORE_VIEW);
            if (obj != null) {
                return Boolean.valueOf(obj.toString()).booleanValue();
            }
        }
        return false;
    }

    public void setPicksLoadByPage() {
        if (this.mParams != null) {
            this.mParams.put(KEY_PICKS_LOAD_BY_PAGE, Boolean.valueOf(true));
        }
    }

    public boolean getIsPicksLoadByPage() {
        if (this.mParams != null) {
            Object obj = this.mParams.get(KEY_PICKS_LOAD_BY_PAGE);
            if (obj != null) {
                return Boolean.valueOf(obj.toString()).booleanValue();
            }
        }
        return false;
    }

    public void setGDTRequestNum(int count) {
        if (this.mParams != null) {
            this.mParams.put(KEY_GDT_REQUESTNUM, Integer.valueOf(count));
            a.a(Const.TAG, count + "set *******************");
        }
    }

    public int getGDTRequestNum() {
        if (this.mParams == null) {
            return 0;
        }
        int intValue = ((Integer) this.mParams.get(KEY_GDT_REQUESTNUM)).intValue();
        a.a(Const.TAG, intValue + " get*******************");
        return intValue;
    }
}
