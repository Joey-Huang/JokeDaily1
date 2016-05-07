package com.joey.jokedaily.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Joey on 2016/3/16.
 */
public class InputMethodUtils {
    public static void hide(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
        {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }
}
