package com.joey.jokedaily.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.joey.jokedaily.R;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private int mPosition;
    private final int POSITION_JOKE=1;
    private final int POSITION_SET=2;
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.view_main);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();
        setUpProfileImage();
        setContentDrawer(mNavigationView);
        //初始化fragment
        switchToJoke();
    }

    private MenuItem checkedItem;

    //菜单点击监听
    private void setContentDrawer(NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_news:
                        switchToJoke();
                        break;
                    case R.id.menu_set:
                        switchToSet();
                        break;
                    default:
                        break;
                }
                if (checkedItem != null) {
                    checkedItem.setChecked(false);
                }
                checkedItem = item;
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });


    }

    private void switchToJoke() {
        mPosition=POSITION_JOKE;
        getSupportActionBar().setTitle("趣闻");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new JokeFragment()).commit();
    }

    private void setUpProfileImage() {
        View headerView=  mNavigationView.inflateHeaderView(R.layout.navigation_header);
        View profileView = headerView.findViewById(R.id.profile_image);
        if (profileView != null) {
            profileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //图片点击事件
                    switchToSet();
                    mDrawerLayout.closeDrawers();
                    mNavigationView.getMenu().getItem(1).setChecked(true);
                }
            });
        }

    }

    private void switchToSet() {
        mPosition=POSITION_SET;
        getSupportActionBar().setTitle("设置中心");
        getSupportFragmentManager().beginTransaction().replace(R.id.content,new SetFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        switch(mPosition){
            case POSITION_SET:
                switchToJoke();
                break;
            case POSITION_JOKE:
                exitAPP();
                break;
        }

    }

    private void exitAPP() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }


}
