package gr.georkouk.kastorakiacounter_new;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActGame extends AppCompatActivity {
    public static int MAX_PLAYERS_NUM = 12;
    SpecialAdapter adapter;
    List<List<String>> allPlayers;
    HashMap<String, String> clickedRow;
    MyDB db;
    EditText[] etPointsDialog;
    String[] from = new String[]{"rowNum", "rowFlag", "pointsPl1", "pointsPl2", "pointsPl3", "pointsPl4", "pointsPl5", "pointsPl6", "pointsPl7", "pointsPl8", "pointsPl9", "pointsPl10", "pointsPl11", "pointsPl12"};
    String gameId;
    List<List<String>> gamePlayers;
    LinearLayout l;
    LinearLayout[] linearPointsDialog;
    CharSequence[] listPlayersForDialog;
    ListView listView;
    List<HashMap<String, String>> pointsListMap = new ArrayList();
    HashMap<String, String> row;
    boolean[] selections;
    int[] skor;
    int tableRowsNum;
    int[] textviews_width;
    int[] to = new int[]{R.id.tvRowNum, R.id.tvRowFlag, R.id.tvPointsPl1, R.id.tvPointsPl2, R.id.tvPointsPl3, R.id.tvPointsPl4, R.id.tvPointsPl5, R.id.tvPointsPl6, R.id.tvPointsPl7, R.id.tvPointsPl8, R.id.tvPointsPl9, R.id.tvPointsPl10, R.id.tvPointsPl11, R.id.tvPointsPl12};
    TextView[] tvPlayerNames;
    TextView[] tvPlayerSkor;
    TextView[] tvPointsDialog;

    public class PointsDialogClick implements OnClickListener {
        public void onClick(DialogInterface dialog, int clicked) {
            switch (clicked) {
                case -1:
                    int i;
                    if (((String) ActGame.this.clickedRow.get("rowFlag")).equals("0")) {
                        ActGame.this.addTableRow(true);
                    } else {
                        for (i = 0; i < ActGame.this.skor.length; i++) {
                            ActGame.this.skor[i] = ActGame.this.skor[i] - Integer.valueOf((String) ActGame.this.clickedRow.get("pointsPl" + (i + 1))).intValue();
                        }
                    }
                    ActGame.this.clickedRow.put("rowFlag", "1");
                    for (i = 0; i < ActGame.this.gamePlayers.size(); i++) {
                        ActGame.this.clickedRow.put("pointsPl" + (i + 1), ActGame.this.etPointsDialog[i].getText().toString());
                    }
                    for (i = 0; i < ActGame.this.skor.length; i++) {
                        ActGame.this.skor[i] = Integer.valueOf((String) ActGame.this.clickedRow.get("pointsPl" + (i + 1))).intValue() + ActGame.this.skor[i];
                    }
                    ActGame.this.adapter.checkSkor(ActGame.this.skor);
                    ActGame.this.db = MyDB.getInstance().open();
                    ActGame.this.db.saveGameRoundSkor(ActGame.this.gameId, ActGame.this.clickedRow, ActGame.this.gamePlayers);
                    MyDB.getInstance().close();
                    int tmpOut = 0;
                    int winnerPos = 0;
                    for (i = 0; i < ActGame.this.gamePlayers.size(); i++) {
                        ActGame.this.tvPlayerSkor[i].setText(String.valueOf(ActGame.this.skor[i]));
                        if (ActGame.this.skor[i] >= 100) {
                            tmpOut++;
                        } else {
                            winnerPos = i;
                        }
                    }
                    if (tmpOut == ActGame.this.gamePlayers.size() - 1) {
                        ActGame.this.showEndGameDialog(winnerPos);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.act_game);
        setTitle(R.string.str_act_game);
        this.tableRowsNum = -1;
        this.listView = (ListView) findViewById(R.id.listView);
        this.adapter = new SpecialAdapter(this, this.pointsListMap, R.layout.testrow, this.from, this.to);
        this.listView.setAdapter(this.adapter);
        this.skor = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        this.textviews_width = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        this.tvPlayerNames = new TextView[]{(TextView) findViewById(R.id.tvNamePl1), (TextView) findViewById(R.id.tvNamePl2), (TextView) findViewById(R.id.tvNamePl3), (TextView) findViewById(R.id.tvNamePl4), (TextView) findViewById(R.id.tvNamePl5), (TextView) findViewById(R.id.tvNamePl6), (TextView) findViewById(R.id.tvNamePl7), (TextView) findViewById(R.id.tvNamePl8), (TextView) findViewById(R.id.tvNamePl9), (TextView) findViewById(R.id.tvNamePl10), (TextView) findViewById(R.id.tvNamePl11), (TextView) findViewById(R.id.tvNamePl12)};
        this.tvPlayerSkor = new TextView[]{(TextView) findViewById(R.id.tvSkorPl1), (TextView) findViewById(R.id.tvSkorPl2), (TextView) findViewById(R.id.tvSkorPl3), (TextView) findViewById(R.id.tvSkorPl4), (TextView) findViewById(R.id.tvSkorPl5), (TextView) findViewById(R.id.tvSkorPl6), (TextView) findViewById(R.id.tvSkorPl7), (TextView) findViewById(R.id.tvSkorPl8), (TextView) findViewById(R.id.tvSkorPl9), (TextView) findViewById(R.id.tvSkorPl10), (TextView) findViewById(R.id.tvSkorPl11), (TextView) findViewById(R.id.tvSkorPl12)};
        this.db = MyDB.getInstance().open();
        this.gameId = this.db.getNotSavedGame();
        MyDB.getInstance().close();
        if (this.gameId.equals("0")) {
            this.db = MyDB.getInstance().open();
            this.db.clearNonFinishedGames();
            MyDB.getInstance().close();
            showDialogForSelection();
            return;
        }
        new Builder(this).setMessage((int) R.string.str_old_game_promt).setCancelable(false).setPositiveButton((int) R.string.str_yes, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActGame.this.loadGame();
            }
        }).setNegativeButton((int) R.string.str_new_game, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActGame.this.db = MyDB.getInstance().open();
                ActGame.this.db.clearNonFinishedGames();
                MyDB.getInstance().close();
                ActGame.this.showDialogForSelection();
            }
        }).create().show();
    }

    public void onBackPressed() {
        if (this.pointsListMap.size() > 0) {
            new Builder(this).setTitle((int) R.string.str_cancel_game).setCancelable(false).setPositiveButton((int) R.string.str_yes, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActGame.this.db = MyDB.getInstance().open();
                    ActGame.this.db.clearNonFinishedGames();
                    MyDB.getInstance().close();
                    ActGame.this.finish();
                }
            }).setNegativeButton((int) R.string.str_no, null).create().show();
            return;
        }
        this.db = MyDB.getInstance().open();
        this.db.clearNonFinishedGames();
        MyDB.getInstance().close();
        super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_game_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHelp /*2131558652*/:
                startActivityForResult(new Intent("gr.georkouk.kastorakiacounter_new.ACTHELP"), 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* access modifiers changed from: private */
    public void showDialogForSelection() {
        this.db = MyDB.getInstance().open();
        this.allPlayers = this.db.getPlayers("0");
        MyDB.getInstance().close();
        this.listPlayersForDialog = new String[this.allPlayers.size()];
        this.selections = new boolean[this.allPlayers.size()];
        for (int i = 0; i < this.allPlayers.size(); i++) {
            this.listPlayersForDialog[i] = (CharSequence) ((List) this.allPlayers.get(i)).get(1);
        }
        final AlertDialog dialog = new Builder(this).setTitle((int) R.string.str_select_players).setCancelable(false).setMultiChoiceItems(this.listPlayersForDialog, this.selections, new OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                ActGame.this.selections[which] = isChecked;
            }
        }).setPositiveButton((int) R.string.str_start, null).setNegativeButton((int) R.string.str_cancel, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActGame.this.finish();
            }
        }).create();
        dialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialog1) {
                dialog.getButton(-1).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        int tmpSelectedNum = 0;
                        for (int i = 0; i < ActGame.this.listPlayersForDialog.length; i++) {
                            if (ActGame.this.selections[i]) {
                                tmpSelectedNum++;
                            }
                        }
                        if (tmpSelectedNum < 2) {
                            Toast.makeText(ActGame.this, R.string.str_two_players_selection, 1).show();
                        } else if (tmpSelectedNum > ActGame.MAX_PLAYERS_NUM) {
                            Toast.makeText(ActGame.this, R.string.str_max_players_selection, 1).show();
                        } else {
                            ActGame.this.fillPlayers(true);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    /* access modifiers changed from: protected */
    public void fillPlayers(boolean newGame) {
        int i;
        if (newGame) {
            this.gamePlayers = new ArrayList();
            for (i = 0; i < this.listPlayersForDialog.length; i++) {
                if (this.selections[i]) {
                    this.gamePlayers.add(this.allPlayers.get(i));
                }
            }
            addTableRow(true);
            this.db = MyDB.getInstance().open();
            this.gameId = this.db.createGame();
            MyDB.getInstance().close();
        }
        this.adapter.addPlayers(this.gamePlayers);
        for (i = 0; i < this.gamePlayers.size(); i++) {
            this.tvPlayerNames[i].setVisibility(0);
            this.tvPlayerSkor[i].setVisibility(0);
            this.tvPlayerNames[i].setText((CharSequence) ((List) this.gamePlayers.get(i)).get(1));
            this.tvPlayerSkor[i].setText("0");
        }
        for (i = 0; i < this.tvPlayerNames.length; i++) {
            this.tvPlayerNames[i].measure(0, 0);
            this.textviews_width[i] = this.tvPlayerNames[i].getMeasuredWidth();
            LayoutParams params = this.tvPlayerSkor[i].getLayoutParams();
            if (this.gamePlayers.size() > 3) {
                params.width = this.textviews_width[i];
                this.tvPlayerSkor[i].setLayoutParams(params);
            } else {
                params.width = 0;
                this.tvPlayerSkor[i].setLayoutParams(params);
            }
        }
        this.adapter.setTextviews_width(this.textviews_width);
    }

    public void rowClick(View v) {
        this.clickedRow = (HashMap) this.pointsListMap.get(Integer.valueOf(((TextView) ((LinearLayout) v).getChildAt(0)).getText().toString()).intValue());
        showPointsDialog();
    }

    public void showPointsDialog() {
        final AlertDialog dialog = new Builder(this).setView(((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_points, null)).setCancelable(false).setPositiveButton((int) R.string.str_save, new PointsDialogClick()).setNegativeButton((int) R.string.str_cancel, null).create();
        dialog.show();
        this.tvPointsDialog = new TextView[]{(TextView) dialog.findViewById(R.id.tv1), (TextView) dialog.findViewById(R.id.tv2), (TextView) dialog.findViewById(R.id.tv3), (TextView) dialog.findViewById(R.id.tv4), (TextView) dialog.findViewById(R.id.tv5), (TextView) dialog.findViewById(R.id.tv6), (TextView) dialog.findViewById(R.id.tv7), (TextView) dialog.findViewById(R.id.tv8), (TextView) dialog.findViewById(R.id.tv9), (TextView) dialog.findViewById(R.id.tv10), (TextView) dialog.findViewById(R.id.tv11), (TextView) dialog.findViewById(R.id.tv12)};
        this.linearPointsDialog = new LinearLayout[]{(LinearLayout) dialog.findViewById(R.id.layPl1), (LinearLayout) dialog.findViewById(R.id.layPl2), (LinearLayout) dialog.findViewById(R.id.layPl3), (LinearLayout) dialog.findViewById(R.id.layPl4), (LinearLayout) dialog.findViewById(R.id.layPl5), (LinearLayout) dialog.findViewById(R.id.layPl6), (LinearLayout) dialog.findViewById(R.id.layPl7), (LinearLayout) dialog.findViewById(R.id.layPl8), (LinearLayout) dialog.findViewById(R.id.layPl9), (LinearLayout) dialog.findViewById(R.id.layPl10), (LinearLayout) dialog.findViewById(R.id.layPl11), (LinearLayout) dialog.findViewById(R.id.layPl12)};
        this.etPointsDialog = new EditText[]{(EditText) dialog.findViewById(R.id.et1), (EditText) dialog.findViewById(R.id.et2), (EditText) dialog.findViewById(R.id.et3), (EditText) dialog.findViewById(R.id.et4), (EditText) dialog.findViewById(R.id.et5), (EditText) dialog.findViewById(R.id.et6), (EditText) dialog.findViewById(R.id.et7), (EditText) dialog.findViewById(R.id.et8), (EditText) dialog.findViewById(R.id.et9), (EditText) dialog.findViewById(R.id.et10), (EditText) dialog.findViewById(R.id.et11), (EditText) dialog.findViewById(R.id.et12)};
        int i = 0;
        while (i < this.gamePlayers.size()) {
            this.tvPointsDialog[i].setText((CharSequence) ((List) this.gamePlayers.get(i)).get(1));
            this.linearPointsDialog[i].setVisibility(0);
            this.etPointsDialog[i].setRawInputType(3);
            this.etPointsDialog[i].setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        dialog.getWindow().setSoftInputMode(5);
                    }
                }
            });
            this.etPointsDialog[i].setText((CharSequence) this.clickedRow.get("pointsPl" + (i + 1)));
            this.etPointsDialog[i].setSelectAllOnFocus(true);
            if (this.skor[i] >= 100) {
                if (((String) this.clickedRow.get("rowFlag")).equals("0") || (((String) this.clickedRow.get("rowFlag")).equals("1") && ((String) this.clickedRow.get("pointsPl" + (i + 1))).equals("0"))) {
                    this.linearPointsDialog[i].setVisibility(8);
                } else {
                    this.linearPointsDialog[i].setVisibility(0);
                }
            }
            i++;
        }
        i = 0;
        while (i < this.gamePlayers.size()) {
            if (this.linearPointsDialog[i].getVisibility() == 0) {
                this.etPointsDialog[i].requestFocus();
                i = this.gamePlayers.size();
            }
            i++;
        }
        this.l = (LinearLayout) dialog.findViewById(R.id.skata);
    }

    public void addTableRow(boolean newRow) {
        List<String> roundPoints;
        this.tableRowsNum++;
        this.row = new HashMap();
        this.row.put("rowNum", String.valueOf(this.tableRowsNum));
        if (newRow) {
            this.row.put("rowFlag", "0");
            roundPoints = new ArrayList();
        } else {
            this.row.put("rowFlag", "1");
            this.db = MyDB.getInstance().open();
            roundPoints = this.db.getGameRoundPoints(this.gameId, String.valueOf(this.tableRowsNum));
            MyDB.getInstance().close();
        }
        for (int i = 0; i < MAX_PLAYERS_NUM; i++) {
            if (i < roundPoints.size()) {
                this.row.put("pointsPl" + (i + 1), roundPoints.get(i));
                this.skor[i] = Integer.valueOf((String) roundPoints.get(i)).intValue() + this.skor[i];
            } else {
                this.row.put("pointsPl" + (i + 1), "0");
            }
        }
        this.pointsListMap.add(this.row);
        this.adapter.notifyDataSetChanged();
    }

    public void loadGame() {
        int i;
        String sqlWhereForGamePlayers = "select distinct(players) from gamelines where game  = " + this.gameId + " group by players";
        this.db = MyDB.getInstance().open();
        this.gamePlayers = this.db.getPlayers(sqlWhereForGamePlayers);
        int rounds = this.db.getGameRoundNum(this.gameId);
        MyDB.getInstance().close();
        this.adapter.addPlayers(this.gamePlayers);
        fillPlayers(false);
        for (i = 0; i < rounds; i++) {
            addTableRow(false);
        }
        addTableRow(true);
        for (i = 0; i < this.gamePlayers.size(); i++) {
            this.tvPlayerSkor[i].setText(String.valueOf(this.skor[i]));
        }
        this.adapter.checkSkor(this.skor);
    }

    public void showEndGameDialog(int winnerPos_) {
        this.db = MyDB.getInstance().open();
        this.db.finishGame(this.gameId, (String) ((List) this.gamePlayers.get(winnerPos_)).get(0));
        MyDB.getInstance().close();
        ArrayList<Map<String, String>> list = new ArrayList();
        for (int i = 0; i < this.gamePlayers.size(); i++) {
            list.add(putRow((String) ((List) this.gamePlayers.get(i)).get(1), String.valueOf(this.skor[i])));
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.stats_list_grid, new String[]{MyVariables.KEY_NAME, "wins"}, new int[]{R.id.tvName, R.id.tvSkor});
        AlertDialog dialog2 = new Builder(this).setView(((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_game_end, null)).setCancelable(false).setPositiveButton((int) R.string.str_ok, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActGame.this.finish();
            }
        }).create();
        dialog2.show();
        ((TextView) dialog2.findViewById(R.id.tvTitle)).setText(R.string.str_game_finished);
        ((TextView) dialog2.findViewById(R.id.tvWinner)).setText(getResources().getString(R.string.str_winner) + ((String) ((List) this.gamePlayers.get(winnerPos_)).get(1)));
        ((ListView) dialog2.findViewById(R.id.listPoints)).setAdapter(adapter);
    }

    private HashMap<String, String> putRow(String name, String wins) {
        HashMap<String, String> row = new HashMap();
        row.put(MyVariables.KEY_NAME, name);
        row.put("wins", wins);
        return row;
    }
}
