package com.example.dontyoudare;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupActivity extends AppCompatActivity {

    private Users member;
    private DatabaseReference findUser;
    private FirebaseAuth mAuth;

    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();
    private String currentGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mAuth = FirebaseAuth.getInstance();
        findUser = FirebaseDatabase.getInstance().getReference();

        list_view = findViewById(R.id.list_view); //FEHerquelle
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);


        currentGroupName = getIntent().getExtras().get("Gruppenname").toString();

        RetrieveAndDisplayUsers();

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Kann man umbenennen wenn man will
                addUserRequest();
            }
        });
    }

    //User zu der Gruppe hinzufügen

    private void addUserRequest() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle("Freund hinzufügen");

        final EditText UserNameField = new EditText(this);
        UserNameField.setHint("Username");
        builder.setView(UserNameField);

        builder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String Username = UserNameField.getText().toString();
                if (TextUtils.isEmpty(Username)) {
                    Toast.makeText(GroupActivity.this, "Bitte Username einfügen", Toast.LENGTH_SHORT).show();
                } else {
                    findUser.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Set<String> set = new HashSet<String>();

                            String Username = UserNameField.getText().toString();

                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                Users users = childSnapshot.getValue(Users.class);
                                set.add(users.getUser());
                                if (users.getUser().equals(Username)) {
                                    Toast.makeText(GroupActivity.this, users.getUser(), Toast.LENGTH_SHORT).show();
                                    member = users;

                                }
                            }
                            findUser.child("Groups").child(currentGroupName).child("Mitglieder").child(member.getUser()).setValue(member);
                            // list_of_groups.clear();
                            list_of_groups.add(member.getUser());
                            arrayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        //Abbruch Button
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void RetrieveAndDisplayUsers() {
        findUser.child("Groups").child(currentGroupName).child("Mitglieder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    set.add(((DataSnapshot) iterator.next()).getKey());
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
