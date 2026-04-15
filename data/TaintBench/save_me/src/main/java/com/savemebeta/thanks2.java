package com.savemebeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class thanks2 extends Activity {
    public static String fname;
    public static String phone;
    Context ctx = this;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Informations");
        DatabaseOperationslogin DB = new DatabaseOperationslogin(this.ctx);
        DB.putInformation(DB, "PHONE APP", "PHONE APP", "PHONE APP");
        setContentView(R.layout.tnx2);
        ((Button) findViewById(R.id.savecon)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.savecon /*2131034179*/:
                        DatabaseOperationslogin DOP = new DatabaseOperationslogin(thanks2.this.ctx);
                        Cursor CR = DOP.getInformation(DOP);
                        Log.d("OTHMAN", "CR  " + CR);
                        CR.moveToFirst();
                        Log.d("OTHMAN", "CR  f " + CR.moveToFirst());
                        String NAME = "";
                        boolean login = true;
                        do {
                            NAME = CR.getString(2);
                            Log.d("OTHMAN", "NAME  " + NAME);
                            if (NAME.equals("1")) {
                                login = false;
                                thanks2.fname = CR.getString(0);
                                thanks2.phone = CR.getString(1);
                            }
                        } while (CR.moveToNext());
                        CR.close();
                        if (login) {
                            thanks2.this.startActivity(new Intent(thanks2.this, Analyse.class));
                            return;
                        }
                        Intent intent = new Intent(thanks2.this.getBaseContext(), Scan.class);
                        intent.putExtra("var2", thanks2.fname);
                        Bundle extras = new Bundle();
                        extras.putString("var", thanks2.phone);
                        intent.putExtras(extras);
                        thanks2.this.startActivity(intent);
                        return;
                    default:
                        return;
                }
            }
        });
    }
}
