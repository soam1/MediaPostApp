package com.akashsoam.mediapostappfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSignUp, btnSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseApp.initializeApp(this);

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);


        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(v -> {
            signUp();
            transitionToSocialMediaActivity();
        });

        btnSignIn.setOnClickListener(v -> {
            signIn();
            transitionToSocialMediaActivity();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //TODO:Transition to next activity
            transitionToSocialMediaActivity();
        }

    }

    private void signUp() {
        mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {

                Toast.makeText(MainActivity.this, "Signing up SUCCESSFUL", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("my_users").child(task.getResult().getUser().getUid()).child("username").setValue(edtUsername.getText().toString());


                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(edtUsername.getText().toString()).build();
                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "User profile updated", Toast.LENGTH_SHORT).show();
                    }
                });

                transitionToSocialMediaActivity();
            } else {
                Toast.makeText(MainActivity.this, "Signing up failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn() {
        mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Signing in SUCCESS", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(MainActivity.this, "Signing In failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(this, SocialMediaActivity.class);
        startActivity(intent);
    }
}