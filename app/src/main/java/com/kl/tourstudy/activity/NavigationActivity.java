package com.kl.tourstudy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.kl.tourstudy.R;
import com.kl.tourstudy.adapter.SectionsPagerAdapter;
import com.kl.tourstudy.fargment.HomeFragment;
import com.kl.tourstudy.fargment.MessageFragment;
import com.kl.tourstudy.fargment.NoteFragment;

import java.util.ArrayList;
import java.util.List;

import c.b.BP;

/**
 * 主界面Activity，包含侧滑和三个主界面滑动
 */
public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;        //包含侧滑栏的一个Layout
    private NavigationView navigationView;  //侧滑栏

    private Toolbar toolbar;
    private TextView mUserName;     //侧滑栏头部用户名TextView

    //保存Fragment标题
    private String[] title = {"推荐路线","朋友圈","聊天"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BP.init("9547ff1068535a47e8e78dd30bd6e47e");

        setContentView(R.layout.activity_navigation);
        initHeaderView();
        initView();
        setSupportActionBar(toolbar);

        // ActionBarDrawerToggle将DrawerLayout和ToolBar绑在一起
        // 用于实现点击ToolBar侧滑菜单能划出，侧滑菜单滑动ToolBar上有相应的动态效果
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 初始化以及获取侧滑栏头部View
     */
    private void initHeaderView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        mUserName = (TextView) headerView.findViewById(R.id.tv_user_name);

        SharedPreferences preferences = getSharedPreferences("user_info",MODE_PRIVATE);
        mUserName.setText(preferences.getString("name","请登录"));
    }

    private void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tab_title = (TabLayout) findViewById(R.id.tabs);
        tab_title.setTabMode(TabLayout.MODE_FIXED);
        tab_title.setTabGravity(TabLayout.GRAVITY_FILL);

        Fragment homeFragment = new HomeFragment();
        Fragment noteFragment = new NoteFragment();
        Fragment messageFragment = new MessageFragment();

        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(homeFragment);
        mFragmentList.add(noteFragment);
        mFragmentList.add(messageFragment);
        /*
      PagerAdapter会使得每一个Fragment都保存在内存中
      如果应用内存占用过多，可以考虑替换成android.support.v4.app.FragmentStatePagerAdapter
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mFragmentList, title);
        //为viewPager设置适配器
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tab_title.setupWithViewPager(mViewPager);   //将页面的变动和Tab绑定，不然左右滑动页面Tab不改变
    }

    /**
     * ToolBar的选项菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    /**
     * toolBar菜单选择监听
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                Intent searchIntent = new Intent(NavigationActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 设置按下物理返回键后，侧滑菜单关闭
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 获得从登录界面返回的用户名
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            String name = data.getStringExtra("userName");
            Toast.makeText(NavigationActivity.this, "登录成功," + name, Toast.LENGTH_SHORT).show();
            mUserName.setText(name);
        }
    }

    /**
     * 侧边栏menu点击
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_user:
                SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
                int status = preferences.getInt("LOG_STATUS", 0);
                //根据用户的登录状态值判断是跳转到登录界面，还是跳转到个人信息界面
//                if (status == 0){
//                    Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
//                    startActivityForResult(intent, 1);
//                } else if (status == 1){
                    Intent intent = new Intent(NavigationActivity.this, UserInfoActivity.class);
                    startActivity(intent);
//                }
                break;
            case R.id.nav_setting:
                Intent intent2 = new Intent(NavigationActivity.this, NoteSettingsActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
