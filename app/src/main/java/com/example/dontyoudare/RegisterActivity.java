package com.example.dontyoudare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText UserEmail, UserPasswort;
    private TextView AlreadyHaveAccountLink;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        InitializeFields();

        AlreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        String email = UserEmail.getText().toString();
        String password = UserPasswort.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"Bitte alle Felder ausfüllen",Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Account wird erstellt");
            loadingBar.setMessage("Bitte warten, Account wird erstellt");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        SendUserToLoginActivity();
                        Toast.makeText(RegisterActivity.this,"Account erfolgreich erstellt",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, "Error: "+ message , Toast.LENGTH_SHORT ).show();
                        loadingBar.dismiss();
                    }

                }
            });
        }
    }

    private void InitializeFields() {
        CreateAccountButton = findViewById(R.id.register_button);
        UserEmail = findViewById(R.id.register_email);
        UserPasswort = findViewById(R.id.register_password);
        AlreadyHaveAccountLink = findViewById(R.id.already_have_acc_link);
        loadingBar = new ProgressDialog(this);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
