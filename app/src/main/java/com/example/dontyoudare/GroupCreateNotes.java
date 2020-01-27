package com.example.dontyoudare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GroupCreateNotes extends AppCompatActivity {

    private EditText aufgabeUser, rulesUser;
    private ImageView proofpicUser;
    private DatabaseReference RootRef;
    private String currentGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes_friends);


        currentGroupName = getIntent().getExtras().get("Groupname").toString();
        RootRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        Button button = findViewById(R.id.send_button);
        aufgabeUser = findViewById(R.id.group_aufgabe_titel);
        rulesUser = findViewById(R.id.group_aufgabe_regeln);





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNewTask();
                Toast.makeText(GroupCreateNotes.this, currentGroupName,Toast.LENGTH_SHORT).show();
                sendUserToGroupAufgabe();
            }
        });





    }

    private void sendUserToGroupAufgabe() {
        Intent intent = new Intent(GroupCreateNotes.this,GroupsAufgaben.class);
        intent.putExtra("Gruppenname", currentGroupName);
        startActivity(intent);
    }


    private void createNewTask() {

        String titel = aufgabeUser.getText().toString().trim();
        String rules = rulesUser.getText().toString().trim();
        RootRef.child(currentGroupName).child("Tasks").child(titel).child("regel").setValue(rules);
        RootRef.child(currentGroupName).child("Tasks").child(titel).child("images").setValue("");


    }



}
