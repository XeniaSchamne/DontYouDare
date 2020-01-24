package com.example.dontyoudare;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Set;

public class GroupActivity extends AppCompatActivity {

    private Users member;
    private DatabaseReference findUser;
    private FirebaseAuth mAuth;
    TextView a, b;

    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mAuth = FirebaseAuth.getInstance();
        findUser = FirebaseDatabase.getInstance().getReference().child("Users");

        list_view = findViewById(R.id.list_view); //FEHerquelle
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);


        //currentGroupName = getIntent().getExtras().get("Gruppenname").toString();
        //Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Kann man umbenennen wenn man will
                addUserRequest();
                //   Intent intent = new Intent(GroupActivity.this, AddEditNoteActivity.class);
                //  startActivityForResult(intent, ADD_NOTE_REQUEST );
            }
        });
    }

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
                    findUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Set<String> set = new HashSet<String>();
                           // Iterator iterator = dataSnapshot.getChildren().iterator();

                            String Username = UserNameField.getText().toString();

                            for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                Users users = childSnapshot.getValue(Users.class);
                                set.add(users.getUser());
                                if(set.contains(Username)){
                                    member = users;
                                }
                            }
                            list_of_groups.clear();
                           // list_of_groups.addAll(member.getUser().toString());
                            arrayAdapter.notifyDataSetChanged();

                        /*   while (iterator.hasNext()) {
                                // member = iterator.toString();
                                set.add(((DataSnapshot) iterator.next()).getKey());
                                if (set.contains("-LzMYcpgoH2wSzQHEv6i")) {
                                    for (String s:set){
                                        for(String p : dataSnapshot.getChildren())
                                        if (s.equalsIgnoreCase(Username)) {
                                            s = Username;
                                        }
                                        list_of_groups.clear();
                                        list_of_groups.add(s);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(GroupActivity.this, "Person nicht gefunden BITCH", Toast.LENGTH_SHORT).show();
                                }
                            }*/
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

    private void addUserToGroup(String username, String member) {
        //   if(username == member ){

        //   }

    }
}
