package com.joey.jokedaily;

import android.app.Application;
import android.content.Context;

import com.joey.jokedaily.utils.L;
import com.yolanda.nohttp.NoHttp;

import java.io.File;
import java.io.IOException;

/**
 * 初始化应用
 */
public class App extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.init(this);
        mContext =getApplicationContext();
        L.isDebug=false;
        firstLoadInit();


    }

    private void firstLoadInit()  {
//        File file=new File(Constant.CACHE_JOKE);
//        if (!file.exists()){
//            try {
//                file.createNewFile();
//                L.e("App\tfirstLoadInit()\tfile.createNewFile()");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public static Context getContext() {
        return mContext;
    }

    //首次加载

}
