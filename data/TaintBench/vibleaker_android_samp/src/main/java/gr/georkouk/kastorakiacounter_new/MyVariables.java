package gr.georkouk.kastorakiacounter_new;

public class MyVariables {
    public static final String APPKEY = "kastorakiaCounter";
    public static final String DATABASE_NAME = "kastorakiaCounterDB";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_FINALDATE = "finaldate";
    public static final String KEY_FINISHED = "finished";
    public static final String KEY_FROMDATE = "fromdate";
    public static final String KEY_NAME = "name";
    public static final String KEY_ROUND = "round";
    public static final String KEY_SKOR = "skor";
    public static final String KEY_WINNER = "winner";
    public static final String SERVERURL = "http://myvf.no-ip.biz:8086//app";
    public static final String SERVERURL2 = "http://myvf.no-ip.biz:8086//app//do.php";
    public static final String SqlCreateGame = "Create table game ( game integer primary key autoincrement, fromdate text, finaldate text, winner integer );";
    public static final String SqlCreateGameLines = "Create table gamelines ( gamelines integer primary key autoincrement, game integer, round integer, players integer, skor integer, finished integer );";
    public static final String SqlCreatePlayers = "Create table players ( players integer primary key autoincrement, name text );";
    public static final String TABLE_GAME = "game";
    public static final String TABLE_GAMELINES = "gamelines";
    public static final String TABLE_PLAYERS = "players";
}
