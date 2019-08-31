package com.disruption.mombasatwoone.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.disruption.mombasatwoone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEt, passwordEt, passwodConfirmEt;
    private static final String TAG = "RegisterActivity";
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       // FirebaseApp.initializeApp(this);

        emailEt = findViewById(R.id.register_email);
        passwordEt = findViewById(R.id.register_password);
        passwodConfirmEt = findViewById(R.id.register_confirm_password);
        register = findViewById(R.id.register_user);

        registerUser();
    }

    private void registerUser() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String passwordConfirm = passwodConfirmEt.getText().toString().trim();

                if ((!email.isEmpty()) && (!password.isEmpty()) && (!passwordConfirm.isEmpty())){
                    if (password.equals(passwordConfirm)) {
                        //Continue with the reg
                        registerNewUser(email, passwordConfirm);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerNewUser(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User();
                            user.setAge(12);
                            user.setCity("Mombasa");
                            user.setName("User Name");

                            FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseAuth.getInstance().signOut();

//
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "onFailure: qq" + e.getMessage() );
                                    Toast.makeText(RegisterActivity.this, "Something went wrong. Try again",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();

                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                            });
                        }
                    }
                });
    }
}
