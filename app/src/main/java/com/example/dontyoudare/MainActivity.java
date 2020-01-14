package com.example.dontyoudare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    /* Attribute, die für die Tabs benötigt werden */

    private TabLayout tablayout;
    private ViewPager viewPager;
    private TabItem me, friends, start;
    public PageAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* Fragments */
        tablayout = findViewById(R.id.tablayout);
        me = findViewById(R.id.tabMe);
        friends = findViewById(R.id.tabFriends);
        start = findViewById(R.id.tabStart);
        viewPager = findViewById(R.id.viewpager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tablayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    pageAdapter.notifyDataSetChanged();
                    Start f = new Start();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
                } else if (tab.getPosition() == 1) {
                    pageAdapter.notifyDataSetChanged();
                    Me f = new Me();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
                } else if (tab.getPosition() == 2) {
                    pageAdapter.notifyDataSetChanged();
                    Friends f = new Friends();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

    }
}