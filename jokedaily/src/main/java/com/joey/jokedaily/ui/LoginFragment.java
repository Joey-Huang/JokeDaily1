package com.joey.jokedaily.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.joey.jokedaily.R;
import com.joey.jokedaily.bean.User;
import com.joey.jokedaily.db.UserDao;
import com.joey.jokedaily.event.Event;
import com.joey.jokedaily.utils.InputMethodUtils;
import com.joey.jokedaily.utils.L;
import com.joey.jokedaily.utils.T;


import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by joey on 2016/5/4.
 */
public class LoginFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_find_back)
    TextView tvFindBack;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.bt_login)
    com.gc.materialdesign.views.ButtonRectangle btLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.bt_login)
    void login() {
        /**
         * 登陆按钮被点击
         * 1.匹配账号密码是否正确
         * 2.如果正确，销毁登陆activity，跳转至MainActivity
         * 3.如果错误，弹土司
         */
        InputMethodUtils.hide(getView());
        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        User user = new User(name, password);

        if (name.equals("") || password.equals("")) {
            T.showShort(getResources().getString(R.string.toast_user_null));
            return;
        }

        if (contains(user)) {//登陆成功
            L.e(TAG + "\tlogin():success\t"+user.toString());
            Event event = new Event(Event.LOGIN_SUCCESS);
            event.setObj(user);
            EventBus.getDefault().post(event);
        } else {
            L.e(TAG + "\tlogin():failed\t"+user.toString());
            T.showShort(getResources().getString(R.string.toast_user_failed));
            etName.setText("");
            etPassword.setText("");
        }
    }

    @OnClick(R.id.tv_find_back)void findBack(){
        //跳转至找回界面
        EventBus.getDefault().post(new Event(Event.TO_FIND_BACK));
    }

    @OnClick(R.id.tv_register)void register(){
        //跳转至找回界面
        EventBus.getDefault().post(new Event(Event.TO_REGISTER));
    }

    private boolean contains(User user) {
        //通过查询数据库匹配
        return UserDao.getInstance(getContext()).contain(user);
    }


}
