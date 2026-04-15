package com.mb.bdapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public class AdvActionDao {
    public static String COL_ACT_ADV_DB = "col_act_4";
    public static String COL_ACT_CLICK = "col_act_3";
    public static String COL_ACT_DOWN_START = "col_act_8";
    public static String COL_ACT_DOWN_SUCCESS = "col_act_9";
    public static String COL_ACT_GET_REF = "col_act_7";
    public static String COL_ACT_GP_DB = "col_act_5";
    public static String COL_ACT_INSTALL = "col_act_10";
    public static String COL_ACT_INSTALL_SUCC = "col_act_11";
    public static String COL_ACT_REQ = "col_act_1";
    public static String COL_ACT_SEND_REF = "col_act_12";
    public static String COL_ACT_SHOW = "col_act_2";
    public static String COL_ACT_START = "col_act_13";
    public static String COL_GP = "col_act_6";
    public static String COL_PKG = "col_pkg";
    public static String TB_NAME = "tb_name";
    private static AdvActionDao instance;
    private Db db;
    private SQLiteDatabase mReadDb;
    private SQLiteDatabase mWriteDb;

    private AdvActionDao(Context context) {
        this.db = new Db(context);
        init();
    }

    public static AdvActionDao getInstance(Context context) {
        AdvActionDao advActionDao;
        synchronized (AdvActionDao.class) {
            if (instance == null) {
                instance = new AdvActionDao(context);
            }
            advActionDao = instance;
        }
        return advActionDao;
    }

    private void init() {
        this.mReadDb = this.db.getReadableDatabase();
        this.mWriteDb = this.db.getWritableDatabase();
    }

    public void markGp(String pkgName, boolean isGp) {
        if (!TextUtils.isEmpty(pkgName)) {
            int i;
            ContentValues kvs = new ContentValues();
            String str = COL_GP;
            if (isGp) {
                i = 1;
            } else {
                i = 0;
            }
            kvs.put(str, Integer.valueOf(i));
            this.mWriteDb.update(TB_NAME, kvs, COL_PKG + "=?", new String[]{pkgName});
        }
    }

    public void markAdvAction(String pkgName, int action) {
        if (!TextUtils.isEmpty(pkgName)) {
            AdvAction dbact = getByPkgName(pkgName);
            ContentValues kvs = new ContentValues();
            if (dbact == null) {
                kvs.put("col_act_" + action, Integer.valueOf(1));
                kvs.put(COL_PKG, pkgName);
                this.mWriteDb.insert(TB_NAME, null, kvs);
                return;
            }
            kvs.put("col_act_" + action, Integer.valueOf(dbact.getShow() + 1));
            this.mWriteDb.update(TB_NAME, kvs, COL_PKG + "=?", new String[]{pkgName});
        }
    }

    public List<AdvAction> listAll() {
        Cursor c = this.mReadDb.query(TB_NAME, null, null, null, null, null, null);
        if (c == null) {
            return null;
        }
        ArrayList<AdvAction> list = new ArrayList();
        while (c.moveToNext()) {
            list.add(parse(c));
        }
        c.close();
        list.trimToSize();
        return list;
    }

    public void cleanDatas() {
        this.mWriteDb.delete(TB_NAME, null, null);
    }

    public AdvAction getByPkgName(String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return null;
        }
        Cursor c = this.mReadDb.query(TB_NAME, null, COL_PKG + "=?", new String[]{pkgName}, null, null, null);
        if (!c.moveToNext()) {
            return null;
        }
        AdvAction act = parse(c);
        c.close();
        return act;
    }

    private AdvAction parse(Cursor c) {
        if (c == null) {
            return null;
        }
        int req = c.getInt(c.getColumnIndex(COL_ACT_REQ));
        int show = c.getInt(c.getColumnIndex(COL_ACT_SHOW));
        int click = c.getInt(c.getColumnIndex(COL_ACT_CLICK));
        int advDb = c.getInt(c.getColumnIndex(COL_ACT_ADV_DB));
        int gpDb = c.getInt(c.getColumnIndex(COL_ACT_GP_DB));
        int getRef = c.getInt(c.getColumnIndex(COL_ACT_GET_REF));
        int gp = c.getInt(c.getColumnIndex(COL_GP));
        int down = c.getInt(c.getColumnIndex(COL_ACT_DOWN_START));
        int downSuc = c.getInt(c.getColumnIndex(COL_ACT_DOWN_SUCCESS));
        int install = c.getInt(c.getColumnIndex(COL_ACT_INSTALL));
        int installSucc = c.getInt(c.getColumnIndex(COL_ACT_INSTALL_SUCC));
        int sendRef = c.getInt(c.getColumnIndex(COL_ACT_SEND_REF));
        int start = c.getInt(c.getColumnIndex(COL_ACT_START));
        String pkg = c.getString(c.getColumnIndex(COL_PKG));
        AdvAction adv = new AdvAction();
        adv.setReq(req);
        adv.setShow(show);
        adv.setClick(click);
        adv.setAdsDb(advDb);
        adv.setHmDb(gpDb);
        adv.setGetRef(getRef);
        adv.setIsGp(gp);
        adv.setDown(down);
        adv.setDownSucc(downSuc);
        adv.setInstall(install);
        adv.setInstallSecc(installSucc);
        adv.setPkgName(pkg);
        adv.setSendRef(sendRef);
        adv.setStart(start);
        return adv;
    }
}
