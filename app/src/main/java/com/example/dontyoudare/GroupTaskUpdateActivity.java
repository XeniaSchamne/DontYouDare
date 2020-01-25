package com.example.dontyoudare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupTaskUpdateActivity extends AppCompatActivity {
    private TextView textView;
    private EditText rulesUser;
    private ImageView proofPicUser;
    private DatabaseReference RootRef;
    private String currentTaskName;
    private String currentGroupName;
    private String rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_task_update);


        currentTaskName = getIntent().getExtras().get("Aufgabe").toString();
        currentGroupName = getIntent().getExtras().get("Groupname").toString();

        RootRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        Button button = findViewById(R.id.send_button);
        textView = findViewById(R.id.group_aufgabe_titel);
        rulesUser = findViewById(R.id.group_aufgabe_regeln);
        proofPicUser = findViewById(R.id.set_proof_image);

        RootRef.child(currentGroupName).child(currentTaskName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rules = dataSnapshot.getValue().toString();
                rulesUser.setText(rules);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textView.setText(currentTaskName);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNewTask();
                Toast.makeText(GroupTaskUpdateActivity.this, currentTaskName, Toast.LENGTH_SHORT).show();
                sendUserToGroupAufgabe();
            }
        });


    }

    private void sendUserToGroupAufgabe() {
        Intent intent = new Intent(GroupTaskUpdateActivity.this, GroupsAufgaben.class);
        startActivity(intent);
    }


    private void updateNewTask() {
        String newRules = rulesUser.getText().toString().trim();
        RootRef.child(currentGroupName).child(currentTaskName).setValue(newRules);

    }
}