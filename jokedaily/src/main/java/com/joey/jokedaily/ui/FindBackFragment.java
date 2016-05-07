package com.joey.jokedaily.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gc.materialdesign.widgets.Dialog;
import com.joey.jokedaily.R;
import com.joey.jokedaily.bean.User;
import com.joey.jokedaily.db.UserDao;
import com.joey.jokedaily.event.Event;
import com.joey.jokedaily.utils.InputMethodUtils;
import com.joey.jokedaily.utils.T;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 找回   fragment
 */
public class FindBackFragment extends Fragment {
    @BindView(R.id.et_name)EditText etName;
    @BindView(R.id.et_protect_code)EditText etProtectCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_back, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.bt_find)void find(){
        InputMethodUtils.hide(getView());

        /**
         * 通过安全码找回
         * 如果正确，弹对话框，显示密码
         * 如果错误，弹土司，通知
         */
        final String name=etName.getText().toString().trim();
        String protectCode= etProtectCode.getText().toString().trim();

        if (!contain(new User(name,""))){
            T.showShort("用户名不存在");
            return;
        }

        final String password = findPassword(name, protectCode);
        if (!password.equals("")){
            //找回成功，弹对话框
            Dialog dialog=new Dialog(getContext(),"找回密码","找回成功\n"+password);
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Event event=new Event(Event.FIND_SUCCESS);
                    event.setObj(new User(name,password));
                    EventBus.getDefault().post(event);
                }
            });
            dialog.show();
            dialog.getButtonAccept().setText("确定");
        }else {
            T.showShort("找回密码失败");
            etName.setText("");
            etProtectCode.setText("");
        }
    }

    //判断用户是否存在
    private boolean contain(User user) {
        return UserDao.getInstance(getContext()).contain(user);
    }

    //根据安全码找回
    private String findPassword(String name, String protectCode) {
        List<User> users = UserDao.getInstance(getContext()).query();
        for (User user:users){
            if (user.getName().equals(name)&&user.getProtectCode().equals(protectCode)){
                return user.getPassword();
            }
        }
        return "";
    }
}
