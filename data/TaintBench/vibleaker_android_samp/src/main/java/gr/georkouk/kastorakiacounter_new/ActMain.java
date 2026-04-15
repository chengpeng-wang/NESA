package gr.georkouk.kastorakiacounter_new;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.splunk.mint.Mint;

public class ActMain extends Activity implements OnClickListener {
    Button btGame;
    Button btHelp;
    Button btPlayers;
    Button btStats;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.disableNetworkMonitoring();
        Mint.initAndStartSession(this, "15beda05");
        Mint.addExtraData("SN", new GenerateUID(this).generate());
        setContentView(R.layout.act_main);
        new MyServerPost(this, "checkConnection", this).execute(new String[0]);
        this.btPlayers = (Button) findViewById(R.id.btPlayers);
        this.btPlayers.setOnClickListener(this);
        this.btGame = (Button) findViewById(R.id.btNewGame);
        this.btGame.setOnClickListener(this);
        this.btStats = (Button) findViewById(R.id.btStatistics);
        this.btStats.setOnClickListener(this);
        this.btHelp = (Button) findViewById(R.id.btHelp);
        this.btHelp.setOnClickListener(this);
        MyDB.initializeInstance(this);
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btNewGame /*2131558568*/:
                startActivity(new Intent("gr.georkouk.kastorakiacounter_new.ACTGAME"));
                return;
            case R.id.btStatistics /*2131558569*/:
                startActivity(new Intent("gr.georkouk.kastorakiacounter_new.ACTSTATS"));
                return;
            case R.id.btHelp /*2131558570*/:
                startActivity(new Intent("gr.georkouk.kastorakiacounter_new.ACTHELP"));
                return;
            case R.id.btPlayers /*2131558571*/:
                startActivity(new Intent("gr.georkouk.kastorakiacounter_new.ACTPLAYERS"));
                return;
            default:
                return;
        }
    }
}
