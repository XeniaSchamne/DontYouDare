package com.example.dontyoudare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupsAufgaben extends AppCompatActivity {

    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();
    private DatabaseReference groupRef;
    private String currentGroupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_aufgaben);
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        list_view = findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);

        currentGroupName = getIntent().getExtras().get("Gruppenname").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();

        FloatingActionButton addPerson = findViewById(R.id.person_add_button);
        FloatingActionButton addNote = findViewById(R.id.button_add_note);


        RetrieveAndDisplayTasks();

        //OnClickListener um Aufgaben hinzuzufügen
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notesFriends = new Intent(GroupsAufgaben.this, GroupCreateNotes.class);
                notesFriends.putExtra("Groupname", currentGroupName);
                startActivity(notesFriends);
            }
        });

        //OnClickListener um Aufgaben zu bearbeiten
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentTaskName = parent.getItemAtPosition(position).toString();

                Intent taskIntent = new Intent(GroupsAufgaben.this, GroupTaskUpdateActivity.class);
                taskIntent.putExtra("Aufgabe", currentTaskName);
                taskIntent.putExtra("Groupname", currentGroupName);
                startActivity(taskIntent);
            }
        });

        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToGroupActivity();           }
        });
    }

    private void SendUserToGroupActivity() {
        Intent groupChatIntent = new Intent(this, GroupActivity.class);
        groupChatIntent.putExtra("Gruppenname", currentGroupName);
        startActivity(groupChatIntent);
    }

    private void RetrieveAndDisplayTasks(){
        groupRef.child(currentGroupName).child("Tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
