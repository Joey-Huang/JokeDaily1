package com.joey.jokedaily.db;

import android.content.Context;
import com.joey.jokedaily.Constant;

/**
 * user db
 */
public class UserOpenHelper extends BaseOpenHelper {
    public UserOpenHelper(Context context) {
        super(context, Constant.USER_TABLE_NAME, Constant.USER_TABLE_COLUMNS, Constant.USER_TABLE_VERSION);
    }
}
