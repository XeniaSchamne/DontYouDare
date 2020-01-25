package com.example.dontyoudare;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Start extends Fragment {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private Button UpdateAccountSettings;
    private EditText userName, userStatus;
    private CircleImageView userProfileImage;
    static Uri image_uri;



    private DatabaseReference RootRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private StorageReference UserProfileImageRef;


    public Start() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        UpdateAccountSettings = v.findViewById(R.id.update_settings_button);
        userName = v.findViewById(R.id.set_user_name);
        userStatus = v.findViewById(R.id.set_profile_status);
        userProfileImage = v.findViewById(R.id.set_profile_image);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Images");



        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        return v;
    }


    //Methode zum öffnen der Kamera
    public void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

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
                    Toast.makeText(getActivity(), "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Test ob unser Permissionergebnis OK ist und wenn ja, upload des aufgenommenen Bildes
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            userProfileImage.setImageURI(image_uri);

            StorageReference filePath = UserProfileImageRef.child(currentUserId + ".jpg");

            filePath.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Bild erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();

                        final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();


                        RootRef.child("Users").child(mAuth.getCurrentUser().getEmail()).child("image").setValue(downloadUrl)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getActivity(), "Bild gespeichert", Toast.LENGTH_SHORT).show();
                                        }else{
                                            String message = task.getException().toString();
                                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else {
                        String message = task.getException().toString();
                        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
