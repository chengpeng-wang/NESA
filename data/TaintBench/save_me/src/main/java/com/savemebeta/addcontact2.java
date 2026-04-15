package com.savemebeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class addcontact2 extends Activity {
    public String Email;
    public String LName;
    public String Name;
    String[] StatusData;
    public String Tel;
    Boolean conx = Boolean.valueOf(false);
    Context ctx = this;
    public String var;
    public String var2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.contactadd);
        Intent intent = getIntent();
        this.var2 = intent.getStringExtra("var2");
        this.var = intent.getExtras().getString("var");
        final EditText editText = (EditText) findViewById(R.id.editText1);
        final EditText editText2 = (EditText) findViewById(R.id.editText2);
        final EditText editText3 = (EditText) findViewById(R.id.editText3);
        ((Button) findViewById(R.id.savecon)).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                addcontact2.this.LName = editText2.getText().toString();
                addcontact2.this.Name = editText.getText().toString();
                addcontact2.this.Tel = editText3.getText().toString();
                addcontact2.this.addContact(addcontact2.this.Name, addcontact2.this.LName, addcontact2.this.Tel);
            }
        });
    }

    /* access modifiers changed from: private */
    public void addContact(String N, String L, String T) {
        String NA = N;
        String LA = L;
        String TE = T;
        try {
            if (COOP.isTheNumberExistsinContacts(getApplicationContext(), TE)) {
                Log.i(COOP.TAG, "Exists");
                Toast.makeText(this, "Number Already Exists", 1).show();
            } else {
                COOP.Insert2Contacts(getApplicationContext(), new StringBuilder(String.valueOf(NA)).append(" ").append(LA).toString(), TE);
            }
        } catch (Exception e) {
        }
        Toast.makeText(this, "ADD " + N + " " + L + "\n" + T, 1).show();
        Toast.makeText(this, "Contact Added Successfuly", 1).show();
        new Timer().schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(addcontact2.this.getBaseContext(), Scan.class);
                intent.putExtra("var2", addcontact2.this.var2);
                Bundle extras = new Bundle();
                extras.putString("var", addcontact2.this.var);
                intent.putExtras(extras);
                addcontact2.this.startActivity(intent);
            }
        }, 2000);
    }
}
