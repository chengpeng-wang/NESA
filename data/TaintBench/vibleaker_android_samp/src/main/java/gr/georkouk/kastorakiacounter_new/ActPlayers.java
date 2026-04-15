package gr.georkouk.kastorakiacounter_new;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.ArrayList;
import java.util.List;

public class ActPlayers extends AppCompatActivity implements OnItemClickListener {
    MyDB db;
    ListView listView;
    List<List<String>> players;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.act_players);
        setTitle(R.string.str_act_players);
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final EditText etName = new EditText(ActPlayers.this);
                etName.setInputType(4096);
                etName.setHint(R.string.str_name_hint);
                final AlertDialog dialog = new Builder(ActPlayers.this).setTitle(R.string.str_new_player).setView(etName).setPositiveButton(R.string.str_ok, null).setNegativeButton(R.string.str_cancel, null).create();
                dialog.setOnShowListener(new OnShowListener() {
                    public void onShow(DialogInterface dialog1) {
                        dialog.getButton(-1).setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                if (etName.getText().toString().equals("") || etName.getText().toString().equals(" ") || etName.getText().toString().equals("  ")) {
                                    Toast.makeText(ActPlayers.this, R.string.str_empty_name, 0).show();
                                    return;
                                }
                                dialog.cancel();
                                ActPlayers.this.db = MyDB.getInstance().open();
                                ActPlayers.this.db.savePlayer(etName.getText().toString());
                                MyDB.getInstance().close();
                                ActPlayers.this.refreshListView();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        refreshListView();
    }

    /* access modifiers changed from: private */
    public void refreshListView() {
        this.listView = (ListView) findViewById(R.id.listView);
        this.listView.setOnItemClickListener(this);
        this.db = MyDB.getInstance().open();
        this.players = this.db.getPlayers("0");
        MyDB.getInstance().close();
        List<String> listPlayers = new ArrayList();
        for (int i = 0; i < this.players.size(); i++) {
            listPlayers.add(((List) this.players.get(i)).get(1));
        }
        this.listView.setAdapter(new ArrayAdapter(this, R.layout.list_textview, listPlayers));
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        final String selectedId = (String) ((List) this.players.get(position)).get(0);
        String selectedName = (String) ((List) this.players.get(position)).get(1);
        final EditText etName = new EditText(this);
        etName.setInputType(4096);
        etName.setText(selectedName);
        new Builder(this).setTitle(R.string.str_edit).setView(etName).setPositiveButton(R.string.str_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ActPlayers.this.db = MyDB.getInstance().open();
                ActPlayers.this.db.updatePlayer(selectedId, etName.getText().toString());
                MyDB.getInstance().close();
                ActPlayers.this.refreshListView();
            }
        }).setNegativeButton(R.string.str_cancel, null).setNeutralButton(R.string.str_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ActPlayers.this.db = MyDB.getInstance().open();
                ActPlayers.this.db.deletePlayer(selectedId);
                MyDB.getInstance().close();
                ActPlayers.this.refreshListView();
            }
        }).show();
    }
}
