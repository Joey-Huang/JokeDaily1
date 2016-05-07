package com.joey.jokedaily;

import android.os.Environment;

/**
 * Created by joey on 2016/4/29.
 */
public class Constant {
    //url>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public static final String KEY_URL_ARG1 = "key";
    public static final String KEY_URL_ARG2 = "pagesize";//单页最大支持20
    public static final String KEY_URL_ARG3 = "page";
    public static final String URL_ARG1 = "48e27760def440378a57cf614d4f7acd";
    public static final int URL_ARG2 = 20;
    public static final String JSON_URL = "http://apis.haoservice.com/lifeservice/Joke/ContentList";


    //cache>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public static final String EXTERNAL_PATH = App.getContext().getFilesDir().getPath();
    public static final String CACHE_JOEY = EXTERNAL_PATH + "/joey";
    public static final String CACHE_JOKE = CACHE_JOEY + "/joke";

    public static String getTextCachePath(int page) {//获取缓存地址
        return EXTERNAL_PATH + "/" + page + ".txt";
    }


    //key pref>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public static final String KEY_NEWS_PAGE = "key_news_page";//当前页面位置 文本笑话
    public static final String KEY_NEWS_PAGE_SELECTED = "key_news_page_selected";//当前页面位置 文本笑话
    public static final String KEY_PIC_PAGE = "key_news_page";//当前页面位置  图片笑话
    public static final String KEY_LOGIN = "key_login";
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_USER_PASSWORD = "key_user_name";


    //db>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public static final String USER_TABLE_NAME = "user";
    public static final String[][] USER_TABLE_COLUMNS = {{"name", "password", "protectcode"}, {"varchar(20)", "varchar(20)", "varchar(20)"}};
    public static final int USER_TABLE_VERSION = 1;

}
