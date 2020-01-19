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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    /* Attribute, die für die Tabs benötigt werden */

    private TabLayout tablayout;
    private ViewPager viewPager;
    private TabItem me, friends, start;
    public PageAdapter pageAdapter;

    // Attribute, die für die Authentifizierung in Firebase benötigt werden
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       mAuth = FirebaseAuth.getInstance();
       currentUser = mAuth.getCurrentUser();



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
                    getSupportFragmentManager().beginTransaction().replace(R.id.me, f).commit();
                } else if (tab.getPosition() == 1) {
                    pageAdapter.notifyDataSetChanged();
                    Me f = new Me();
                    getSupportFragmentManager().beginTransaction().replace(R.id.starttab, f).commit();
                } else if (tab.getPosition() == 2) {
                    pageAdapter.notifyDataSetChanged();
                    Friends f = new Friends();
                    getSupportFragmentManager().beginTransaction().replace(R.id.me, f).commit();

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

    //Firebase Methoden

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null){
            SendUserToLoginActivity();
        }

    }



    private void SendUserToLoginActivity() {
        Intent logIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(logIntent);
    }
}