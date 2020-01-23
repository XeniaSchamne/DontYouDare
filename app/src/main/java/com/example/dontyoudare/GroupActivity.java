package com.example.dontyoudare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupActivity extends AppCompatActivity {

    private DatabaseReference findUser;
    private String currentGroupName;
    private FirebaseAuth mAuth;
    TextView a,b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mAuth = FirebaseAuth.getInstance();
        findUser = FirebaseDatabase.getInstance().getReference().child("Users");


        currentGroupName = getIntent().getExtras().get("Gruppenname").toString();
        Toast.makeText(this, currentGroupName,Toast.LENGTH_SHORT).show();

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Kann man umbenennen wenn man will
                addUserRequest();
            //    Intent intent = new Intent(getActivity(), AddEditNoteActivity.class);
            //    startActivityForResult(intent, ADD_NOTE_REQUEST );
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
                if (TextUtils.isEmpty(Username)){
                    Toast.makeText(GroupActivity.this,"Bitte Username einfügen", Toast.LENGTH_SHORT).show();
                }else{
                    findUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String member = dataSnapshot.child("user").getValue().toString();
                            String Username = UserNameField.getText().toString();
                            a.setText(member);
                            b.setText(Username);
                            addUserToGroup(Username, member);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //String member = findUser.child("").child("user").toString();
                    //addUserToGroup(Username, member);
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
        if(username == member ){
            Toast.makeText(this,"Wuderschön du bitch", Toast.LENGTH_SHORT).show();
        }

    }
}
