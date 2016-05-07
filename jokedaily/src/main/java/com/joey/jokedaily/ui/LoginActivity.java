package com.joey.jokedaily.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.joey.jokedaily.Constant;
import com.joey.jokedaily.R;
import com.joey.jokedaily.bean.User;
import com.joey.jokedaily.event.Event;
import com.joey.jokedaily.utils.L;
import com.joey.jokedaily.utils.PrefUtils;
import com.joey.jokedaily.utils.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * Created by joey on 2016/5/4.
 */
public class LoginActivity extends AppCompatActivity {
    private  final String TAG=getClass().getSimpleName();
    private static final int POSITION_LOGIN=1;
    private static final int POSITION_FIND=2;
    private static final int POSITION_REGISTER=3;
    private int mPosition=0;
    private int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("登陆");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示默认返回按钮
        EventBus.getDefault().register(this);
        switchToLoginFg();//进入登陆fragment
    }

    private void switchToLoginFg() {
        getSupportActionBar().setTitle("登录");
        mPosition=POSITION_LOGIN;
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment,new LoginFragment()).commit();
    }

    private void switchToFindFg() {
        getSupportActionBar().setTitle("找回密码");
        mPosition=POSITION_FIND;
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment,new FindBackFragment()).commit();
    }

    private void switchToRegisterFg() {
        getSupportActionBar().setTitle("注册账号");
        mPosition=POSITION_REGISTER;
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment,new RegisterFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://左上角默认返回按钮
                switch (mPosition){
                    case POSITION_FIND:
                        switchToLoginFg();
                        break;
                    case POSITION_LOGIN:
                        finish();
                        break;
                    case POSITION_REGISTER:
                        switchToLoginFg();
                        break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==Event.FIND_SUCCESS||msg.what==Event.LOGIN_SUCCESS||msg.what==Event.REGISTER_SUSSES){
                count++;
                if (msg.obj instanceof User){
                    L.e(TAG+"\tmsg.obj instanceof User"+"\tcount="+count);

                    User user= (User) msg.obj;
                    PrefUtils.putString(Constant.KEY_USER_NAME,user.getName());
                    LoginActivity.this.finish();
                }else {
                    L.e(TAG+"\t!msg.obj instanceof User"+"\tcount="+count);
                    T.showShort("登陆异常，请重新登录");
                }
            }

            switch(msg.what){
                case Event.FIND_SUCCESS:
                    break;
                case Event.LOGIN_SUCCESS:
                    break;
                case Event.REGISTER_SUSSES:
                    break;
                case Event.TO_FIND_BACK:
                    switchToFindFg();
                    break;
                case Event.TO_REGISTER:
                    switchToRegisterFg();
                    break;
            }
        }
    };



    @Subscribe
    public void onEvent(Event event){//发给handler运行
        L.e(TAG+"\tonEventMainThread("+event.toString()+")");
        Message message = handler.obtainMessage();
        message.what=event.getWhat();
        message.arg1=event.getArg1();
        message.arg2=event.getArg2();
        message.obj=event.getObj();
        handler.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
