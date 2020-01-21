package com.example.dontyoudare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

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
    private NoteViewModel noteViewModel;

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

        /*TabLayout*/

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

        /*NoteViewModel*/
        final NoteAdapter adapter = new NoteAdapter();

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);

            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.me_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "Alle Aufgaben gelöscht", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.main_find_friends_option:
                //TODO muss noch implementiert werden
                return true;
            case R.id.main_logout_option:
                mAuth.signOut();
                SendUserToLoginActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}