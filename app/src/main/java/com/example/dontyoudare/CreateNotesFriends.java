package com.example.dontyoudare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNotesFriends extends AppCompatActivity {

    private EditText aufgabeUser, rulesUser;
    private ImageView proofpicUser;
    private DatabaseReference RootRef;
    private Task1 task1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes_friends);



        RootRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        Button button = findViewById(R.id.send_button);
        aufgabeUser = findViewById(R.id.group_aufgabe_titel);
        rulesUser = findViewById(R.id.group_aufgabe_regeln);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                String groupName = intent.getStringExtra(GroupsAufgaben.EXTRA_GROUP);

                //createNewTask(groupName);
                Toast.makeText(CreateNotesFriends.this, groupName,Toast.LENGTH_SHORT).show();
                //sendUserToGroupAufgabe();
            }
        });





    }

    private void sendUserToGroupAufgabe() {
        Intent intent = new Intent(CreateNotesFriends.this, GroupsAufgaben.class);
        startActivity(intent);
    }


    private void createNewTask(String groupName) {

        String titel = aufgabeUser.getText().toString().trim();
        String rules = rulesUser.getText().toString().trim();
        RootRef.child(groupName).child(titel).setValue(rules);

    }



}
