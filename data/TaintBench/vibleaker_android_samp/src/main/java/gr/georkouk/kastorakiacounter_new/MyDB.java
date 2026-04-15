package gr.georkouk.kastorakiacounter_new;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class MyDB {
    private static MyDB instance;
    private static MyDBHelper ourHelper;
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private SQLiteDatabase ourDatabase;

    public MyDB(Context context_) {
        ourHelper = new MyDBHelper(context_);
    }

    public static synchronized void initializeInstance(Context c_) {
        synchronized (MyDB.class) {
            if (instance == null) {
                instance = new MyDB(c_);
            }
        }
    }

    public static synchronized MyDB getInstance() {
        MyDB myDB;
        synchronized (MyDB.class) {
            if (instance == null) {
                throw new IllegalStateException(MyDB.class.getSimpleName() + " is not initialized...");
            }
            myDB = instance;
        }
        return myDB;
    }

    public synchronized MyDB open() throws SQLException {
        if (this.mOpenCounter.incrementAndGet() == 1) {
            this.ourDatabase = ourHelper.getWritableDatabase();
        }
        return this;
    }

    public synchronized void close() {
        if (this.mOpenCounter.decrementAndGet() == 0 && isOpened()) {
            ourHelper.close();
        }
    }

    public boolean isOpened() {
        return this.ourDatabase.isOpen();
    }

    public void savePlayer(String name) {
        ContentValues cv = new ContentValues();
        cv.put(MyVariables.KEY_NAME, name);
        try {
            this.ourDatabase.insertWithOnConflict(MyVariables.TABLE_PLAYERS, null, cv, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlayer(String playerId, String name) {
        Cursor c = this.ourDatabase.rawQuery("update players set name = ? where players = ?", new String[]{name, playerId});
        if (c != null) {
            c.moveToFirst();
            c.close();
        }
    }

    public void deletePlayer(String playerId) {
        Cursor c = this.ourDatabase.rawQuery("delete from players where players = ?", new String[]{playerId});
        if (c != null) {
            c.moveToFirst();
            c.close();
        }
        c = this.ourDatabase.rawQuery("delete from gamelines where players = ?", new String[]{playerId});
        if (c != null) {
            c.moveToFirst();
            c.close();
        }
        c = this.ourDatabase.rawQuery("update game set winner = 0 where winner = ?", new String[]{playerId});
        if (c != null) {
            c.moveToFirst();
            c.close();
        }
    }

    public List<List<String>> getPlayers(String id) {
        List<List<String>> data = new ArrayList();
        String sql = "select * from players";
        if (!(id.equals("0") || id.equals(""))) {
            sql = sql + " where players in (" + id + ")";
        }
        Cursor c = this.ourDatabase.rawQuery(sql, null);
        if (c != null) {
            int iplayers = c.getColumnIndex(MyVariables.TABLE_PLAYERS);
            int iname = c.getColumnIndex(MyVariables.KEY_NAME);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                List<String> row = new ArrayList();
                row.add(c.getString(iplayers));
                row.add(c.getString(iname));
                data.add(row);
                c.moveToNext();
            }
            c.close();
        }
        return data;
    }

    public String createGame() {
        long game;
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(new Date());
        ContentValues cv = new ContentValues();
        cv.put(MyVariables.KEY_FROMDATE, date);
        cv.put(MyVariables.KEY_FINALDATE, "0");
        cv.put(MyVariables.KEY_WINNER, "0");
        try {
            game = this.ourDatabase.insertWithOnConflict(MyVariables.TABLE_GAME, null, cv, 5);
        } catch (Exception e) {
            game = 0;
        }
        return String.valueOf(game);
    }

    public void saveGameRoundSkor(String gameId, HashMap<String, String> clickedRow, List<List<String>> players) {
        String gamelines = "0";
        Cursor c = this.ourDatabase.rawQuery("select ifnull(gamelines, 0) as gamelines, round from gamelines where game = ? and round = ?", new String[]{gameId, (String) clickedRow.get("rowNum")});
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                if (c.getString(1).equalsIgnoreCase((String) clickedRow.get("rowNum"))) {
                    gamelines = c.getString(0);
                }
                c.moveToNext();
            }
            c.close();
        }
        int i;
        if (gamelines.equals("0")) {
            String sql = "insert into gamelines (game, round, players, skor, finished) select " + gameId + ", " + ((String) clickedRow.get("rowNum")) + ", " + ((String) ((List) players.get(0)).get(0)) + ", " + ((String) clickedRow.get("pointsPl1")) + ", 0";
            for (i = 1; i < players.size(); i++) {
                sql = sql + " union all select " + gameId + ", " + ((String) clickedRow.get("rowNum")) + ", " + ((String) ((List) players.get(i)).get(0)) + ", " + ((String) clickedRow.get("pointsPl" + (i + 1))) + ", 0";
            }
            c = this.ourDatabase.rawQuery(sql, null);
            if (c != null) {
                c.moveToFirst();
                c.close();
                return;
            }
            return;
        }
        for (i = 0; i < players.size(); i++) {
            c = this.ourDatabase.rawQuery("update gamelines set skor = " + ((String) clickedRow.get("pointsPl" + (i + 1))) + " where game = " + gameId + " and round = " + ((String) clickedRow.get("rowNum")) + " and players = " + ((String) ((List) players.get(i)).get(0)), null);
            if (c != null) {
                c.moveToFirst();
                c.close();
            }
        }
    }

    public void finishGame(String gameId, String winnerId) {
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(new Date());
        Cursor c = this.ourDatabase.rawQuery("update game set finaldate = ?, winner = ? where game = ?", new String[]{date, winnerId, gameId});
        if (c != null) {
            c.moveToFirst();
            c.close();
        }
        c = this.ourDatabase.rawQuery("update gamelines set finished = 1 where game = ?", new String[]{gameId});
        if (c != null) {
            c.moveToFirst();
            c.close();
        }
    }

    public void clearNonFinishedGames() {
        Cursor c = this.ourDatabase.rawQuery("delete from gamelines where finished = 0", null);
        if (c != null) {
            c.moveToFirst();
            c.close();
        }
        c = this.ourDatabase.rawQuery("delete from game where game not in (select distinct(game) from gamelines)", null);
        if (c != null) {
            c.moveToFirst();
            c.close();
        }
    }

    public List<List<String>> getStatsData(String type) {
        List<List<String>> data = new ArrayList();
        String sql = "";
        Object obj = -1;
        switch (type.hashCode()) {
            case -982754077:
                if (type.equals("points")) {
                    obj = 1;
                    break;
                }
                break;
            case -47074970:
                if (type.equals("lastWin")) {
                    obj = 2;
                    break;
                }
                break;
            case 3649559:
                if (type.equals("wins")) {
                    obj = null;
                    break;
                }
                break;
            case 1235477320:
                if (type.equals("smallerWin")) {
                    obj = 3;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                sql = "select name, ifnull((select count(winner) as a from game where game.winner = players.players), 0) as num from players";
                break;
            case 1:
                sql = "select name, ifnull((select sum(skor) as sunolo from gamelines where gamelines.players = players.players), 0) as num from players";
                break;
            case 2:
                sql = "select name, ifnull((select substr(finaldate, 7 ,2) || '/' || substr(finaldate, 5, 2) || '/' || substr(finaldate, 1, 4) from game where game.winner = players.players order by finaldate desc limit 1), '') as num from players";
                break;
            case 3:
                sql = "select name, ifnull((select sum(skor) as a from gamelines where gamelines.game in (select game.game from game where game.winner = players.players group by game.game) group by gamelines.game, players order by a  limit 1), '') as num from players";
                break;
        }
        Cursor c = this.ourDatabase.rawQuery(sql, null);
        if (c != null) {
            int inum = c.getColumnIndex("num");
            int iname = c.getColumnIndex(MyVariables.KEY_NAME);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                List<String> row = new ArrayList();
                row.add(c.getString(iname));
                row.add(c.getString(inum));
                data.add(row);
                c.moveToNext();
            }
            c.close();
        }
        return data;
    }

    public String getNotSavedGame() {
        String game = "0";
        Cursor c = this.ourDatabase.rawQuery("select game from game where winner = 0 and finaldate = 0 and (select count(gamelines) from gamelines where gamelines.game = game.game) > 0 order by fromdate desc limit 1", null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                game = c.getString(0);
                c.moveToNext();
            }
            c.close();
        }
        return game;
    }

    public int getGameRoundNum(String gameId) {
        int num = 0;
        Cursor c = this.ourDatabase.rawQuery("select count(distinct(round)) from gamelines where game = ?", new String[]{gameId});
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                num = c.getInt(0);
                c.moveToNext();
            }
            c.close();
        }
        return num;
    }

    public List<String> getGameRoundPoints(String gameId, String round) {
        List<String> data = new ArrayList();
        Cursor c = this.ourDatabase.rawQuery("select * from gamelines where game = ? and round = ?", new String[]{gameId, round});
        if (c != null) {
            int iskor = c.getColumnIndex(MyVariables.KEY_SKOR);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                data.add(c.getString(iskor));
                c.moveToNext();
            }
            c.close();
        }
        return data;
    }
}
