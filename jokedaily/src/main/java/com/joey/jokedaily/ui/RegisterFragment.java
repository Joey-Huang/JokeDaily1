package com.joey.jokedaily.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.joey.jokedaily.R;
import com.joey.jokedaily.bean.User;
import com.joey.jokedaily.db.UserDao;
import com.joey.jokedaily.event.Event;
import com.joey.jokedaily.utils.InputMethodUtils;
import com.joey.jokedaily.utils.T;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册   fragment
 */
public class RegisterFragment extends Fragment {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password1)
    EditText etPassword1;
    @BindView(R.id.et_protect_code)
    EditText etProtectCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.bt_register)
    void register() {
        InputMethodUtils.hide(getView());

        //注册
        String name = etName.getText().toString().trim();
        String password1 = etPassword1.getText().toString().trim();
        String protectCode = etProtectCode.getText().toString().trim();

        if (name.equals("")) {
            T.showShort("用户名不能为空");
            return;
        }

        if (password1.equals("")) {
            T.showShort("密码不能为空");
            return;
        }

        if (contain(new User(name,password1))){
            T.showShort("用户已经存在");
            return;
        }

        if (password1.equals(protectCode)){
            T.showShort("密码和安全码不能相同");
            return;
        }

        if (name.length()<=6||password1.length()<=6){
            T.showShort("用户名和密码长度必须大于6位");
            return;
        }

        if (protectCode.length()<=6){
            T.showShort("密保长度必须大于6位");
            return;
        }

        //注册成功
        T.showShort("注册成功");
        User user=new User(name,password1,protectCode);
        UserDao.getInstance(getContext()).insert(user);
        Event event=new Event(Event.REGISTER_SUSSES);
        event.setObj(user);
        EventBus.getDefault().post(event);
    }

    private boolean contain(User user) {
        return  UserDao.getInstance(getContext()).contain(user);
    }


}
