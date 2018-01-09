package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.title_eidt, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item_qiehuan:
                mViewPager.setCurrentItem(4);
                break;
            /*case R.id.item_shezhi:
                replaceFragment(new Fragment_shezhi());
                break;*/
            case R.id.item_zhuxiao:
                finish();
                break;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return true;
    }

    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_devices:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_analysis:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_finder:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_settings:
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mViewPager=(ViewPager)findViewById(R.id.viewpager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new DeviceFragment());
        fragments.add(new AnalysisFragment());
        fragments.add(new FinderFragment());
        fragments.add(new SettingsFragment());
        fragments.add(new Fragment_qiehuan());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(adapter);
    }

    protected void onStart(){
        super.onStart();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}