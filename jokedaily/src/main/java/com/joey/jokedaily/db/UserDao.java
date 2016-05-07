package com.joey.jokedaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.joey.jokedaily.Constant;
import com.joey.jokedaily.bean.User;
import com.joey.jokedaily.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * User Dao
 */
public class UserDao extends BaseDao {
    private static UserDao mDao;
    private UserOpenHelper mHelper;
    private String mTableName;
    private String[][] mColumns;
    private final String TAG = getClass().getSimpleName();


    private UserDao(Context context) {
        mHelper = new UserOpenHelper(context);
        this.mTableName = Constant.USER_TABLE_NAME;
        this.mColumns = Constant.USER_TABLE_COLUMNS;
    }

    public static UserDao getInstance(Context context) {
        if (mDao == null) {
            mDao = new UserDao(context);
        }
        return mDao;
    }

    /**
     * insert
     * name         varchar(20)
     * password     varchar(20)
     * protectCode  varchar(20)
     */
    public long insert(User user) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mColumns[0][0], user.getName());
        values.put(mColumns[0][1], user.getPassword());
        values.put(mColumns[0][2], user.getProtectCode());
        long result = db.insert(mTableName, null, values);
        db.close();
        values.clear();
        L.e(TAG, "\tinsert(" + user.toString() + ")");
        return result;
    }

    /**
     * deleteAll
     */
    public int deleteAll() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.delete(mTableName, null, null);
        db.close();
        L.e(TAG, "\tdeleteAll()");
        return count;
    }

    /**
     * delete
     * name         varchar(20)
     */
    public int delete(User user) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.delete(mTableName, "name = ?",
                new String[]{String.valueOf(user.getName())});
        db.close();
        L.e(TAG, "\tdelete(" + user.toString() + ")");
        return count;
    }


    /**
     * contain
     * name         varchar(20)
     * password     varchar(20)
     * protectCode  varchar(20)
     */
    public boolean contain(User user) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(mTableName, mColumns[0], "name = ? and password=?", new String[]{user.getName(),user.getPassword()}, null, null, null);
        while (cursor.moveToNext()) {
            L.e(TAG, "\tquery(" + user.toString() + ")="+true);
            return true;
        }
        L.e(TAG, "\tquery(" + user.toString() + ")="+false);
        return false;
    }

    /**
     * insert   查
     * classes  varchar(10)
     * classify smallint
     * page     smallint
     */
    public List<User> query() {
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(mTableName, mColumns[0], null, null, null, null, null);
        while (cursor.moveToNext()) {
            String userName = cursor.getString(0);
            String password = cursor.getString(1);
            String protectCode = cursor.getString(2);
            userList.add(new User(userName, password, protectCode));
        }
        L.e(TAG, "\tList<User> query().size="+userList.size());
        return userList;
    }

    public void clear() {
        mColumns = null;
        mTableName = null;
        mHelper = null;
        mDao = null;
        L.e(TAG, "\tclear()");
    }
}
