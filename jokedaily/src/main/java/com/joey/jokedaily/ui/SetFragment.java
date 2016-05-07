package com.joey.jokedaily.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.joey.jokedaily.Constant;
import com.joey.jokedaily.R;
import com.joey.jokedaily.utils.FileUtil;
import com.joey.jokedaily.utils.L;
import com.joey.jokedaily.utils.PrefUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 设置中心
 */
public class SetFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private boolean mIsLogin;
    private Context mContext;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_clear)
    TextView tvClear;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        String name=PrefUtils.getString(Constant.KEY_USER_NAME, "");
        if (name.equals("")){
            setIsLogin(false);
        }else {
            setIsLogin(true);
        }
        //获取缓存大小
        String filesSize = FileUtil.getAutoFileOrFilesSize(Constant.EXTERNAL_PATH);
        tvClear.setText("清理缓存("+filesSize+")");
    }



    @OnClick(R.id.ll_clear)
    void gotoClear() {
        L.e(TAG + "\tOnClick\tgotoSet()");
        final Dialog dialog=new Dialog(mContext,getResources().getString(R.string.dialog_clear_title),getResources().getString(R.string.dialog_clear_message));
        dialog.addCancelButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清理缓存
                clearCache();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getButtonAccept().setText("确定");
    }

    //清理缓存
    private void clearCache() {
        File directory=new File(Constant.EXTERNAL_PATH);
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
        //获取缓存大小
        String filesSize = FileUtil.getAutoFileOrFilesSize(Constant.EXTERNAL_PATH);
        tvClear.setText("清理缓存("+filesSize+")");
        PrefUtils.putInt(Constant.KEY_NEWS_PAGE, 1);
    }

    @OnClick(R.id.ll_about)
    void gotoAbout() {
        L.e(TAG + "\tOnClick\tgotoSet()");

        final Dialog dialog=new Dialog(mContext,getResources().getString(R.string.dialog_about_title),getResources().getString(R.string.dialog_about_message));
        dialog.show();
        dialog.getButtonAccept().setText("取消");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    @OnClick(R.id.ll_login)
    void gotoLogin() {
        L.e(TAG + "\tOnClick\tgotoSet()");
        if (isLogin()) {
            //弹出对话框，是否取消登陆
            final Dialog dialog = new Dialog(getContext(), "退出登陆", "确定退出登陆");
            dialog.addCancelButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setIsLogin(false);
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getButtonAccept().getTextView().setText("确定");
        } else {
            //跳转至登陆界面
            getmContext().startActivity(new Intent(getmContext(), LoginActivity.class));
        }
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isLogin() {
        return mIsLogin;
    }

    public void setIsLogin(boolean isLogin) {
        String name=PrefUtils.getString(Constant.KEY_USER_NAME, "");
        if (isLogin) {
            tvLogin.setText("退出登录");
            tvTitle.setText(name);
        } else {
            PrefUtils.putString(Constant.KEY_USER_NAME,"");
            tvLogin.setText("登录");
            tvTitle.setText("未登录");
        }
        this.mIsLogin = isLogin;
    }

    private void clear() {
        this.mContext = null;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clear();
    }
}
