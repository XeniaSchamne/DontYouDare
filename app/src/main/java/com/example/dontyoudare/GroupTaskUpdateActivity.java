package com.example.dontyoudare;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.dontyoudare.Start.image_uri;

public class GroupTaskUpdateActivity extends AppCompatActivity {
    private TextView textView;
    private EditText rulesUser;
    private ImageView proofPicUser;
    private DatabaseReference RootRef;
    private String currentTaskName;
    private String currentGroupName;
    private String rules;

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private StorageReference UserProofImageRef;

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
        UserProofImageRef = FirebaseStorage.getInstance().getReference().child(currentGroupName).child(currentTaskName);

        RootRef.child(currentGroupName).child(currentTaskName).child("regel").addValueEventListener(new ValueEventListener() {
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
        proofPicUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });



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
        intent.putExtra("Gruppenname", currentGroupName);
        startActivity(intent);
    }


    private void updateNewTask() {
        String newRules = rulesUser.getText().toString().trim();
        RootRef.child(currentGroupName).child(currentTaskName).child("regel").setValue(newRules);

    }

    //Methode zum öffnen der Kamera
    public void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);


    }

    // Permission Anfrage
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }else{
                    Toast.makeText(GroupTaskUpdateActivity.this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Test ob unser Permissionergebnis OK ist und wenn ja, upload des aufgenommenen Bildes
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            proofPicUser.setImageURI(image_uri);

            StorageReference filePath = UserProofImageRef.child(currentTaskName + ".jpg");

            filePath.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(GroupTaskUpdateActivity.this, "Bild erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();

                        final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();


                        RootRef.child(currentGroupName).child(currentTaskName).child("images").setValue(downloadUrl)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(GroupTaskUpdateActivity.this, "Bild gespeichert", Toast.LENGTH_SHORT).show();
                                        }else{
                                            String message = task.getException().toString();
                                            Toast.makeText(GroupTaskUpdateActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else {
                        String message = task.getException().toString();
                        Toast.makeText(GroupTaskUpdateActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}