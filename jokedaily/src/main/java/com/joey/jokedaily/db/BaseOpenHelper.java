package com.joey.jokedaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * OpenHelper基类
 */
public class BaseOpenHelper extends SQLiteOpenHelper {
    public final String TAG = getClass().getSimpleName();
    private String tableName;
    private String[][] columns;

    public BaseOpenHelper(Context context, String tableName, String[][] columns, int version) {
        super(context, tableName + ".db", null, version);
        this.columns = columns;
        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String start = "create table " + tableName + "(_id integer primary key autoincrement";
        String end = ")";
        String body = "";
        for (int i = 0; i < columns[0].length; i++) {
            body = body + "," + columns[0][i] + " " + columns[1][i];
        }
        db.execSQL(start + body + end);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
