package gr.georkouk.kastorakiacounter_new;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    Context context;

    public MyDBHelper(Context context_) {
        super(context_, MyVariables.DATABASE_NAME, null, 1);
        this.context = context_;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyVariables.SqlCreatePlayers);
        db.execSQL(MyVariables.SqlCreateGame);
        db.execSQL(MyVariables.SqlCreateGameLines);
    }
}
