package com.ptit.ezychat;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ptit.ezychat.adapter.FragmentAdapter;
import com.ptit.ezychat.socket.SocketRequests;

public class ContactActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigationView;
    private FragmentManager manager;

    private FragmentAdapter fragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initView();

        manager = getSupportFragmentManager();
        fragmentAdapter= new FragmentAdapter(manager,3);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        SocketRequests.getINSTANCE().sendGetContacts();
                        navigationView.getMenu().findItem(R.id.mContact).setChecked(true);
                        break;
                    case 1:
                        SocketRequests.getINSTANCE().sendGetOnlineContacts();
                        navigationView.getMenu().findItem(R.id.mOnline).setChecked(true);
                        break;
                    case 2:navigationView.getMenu().findItem(R.id.mSearch).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mContact:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.mOnline:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.mSearch:viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });

    }

    private void initView(){
        viewPager = findViewById(R.id.view_pager);
        navigationView = findViewById(R.id.navigation);
    }
}