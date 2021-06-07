package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.Model.Users;
import com.example.realestate.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login2 extends AppCompatActivity {
    private TextView SignUp, ForgotPassword;
    private EditText Email, Password;
    private ProgressBar progressBar;
    private FloatingActionButton SignInButton;
    private FirebaseAuth mAuth;
    private CheckBox RememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        SignUp = (TextView) findViewById(R.id.SignUp);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login2.this, SignUpFirebase.class);
                startActivity(intent);
            }
        });

        SignInButton = (FloatingActionButton) findViewById(R.id.SignInButton);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInButton();
            }
        });

        Email = (EditText) findViewById(R.id.Phone);
        Password = (EditText) findViewById(R.id.Password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ForgotPassword = (TextView) findViewById(R.id.ForgotPassword);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login2.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();

        RememberMe = (CheckBox) findViewById(R.id.RememberMe);
        Paper.init(this);

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

    }

    private void SignInButton() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (email.isEmpty()) {
            Email.setError("This field can't be empty");
            Email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("please enter valid email");
            Email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Password.setError("This field can't be empty");
            Password.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Password.setError("password should not be less than 6");
            Password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            final DatabaseReference DataRef;
                            DataRef = FirebaseDatabase.getInstance().getReference();

                            DataRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Users userData = snapshot.child("Users").child("4561237890").getValue(Users.class);
                                    Prevalent.currentonlineUser = userData;

                                    if (user.isEmailVerified()) {
                                        startActivity(new Intent(Login2.this, HomeActivity.class));
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

//                            if (user.isEmailVerified()) {
//                                startActivity(new Intent(Login2.this, HomeActivity.class));
//                                progressBar.setVisibility(View.GONE);
//                            }
//                            else{
//                                user.sendEmailVerification();
//                                Toast.makeText(Login2.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
//                                progressBar.setVisibility(View.GONE);
//                            }
//                        }else{
//                            Toast.makeText(Login2.this, "failed to login", Toast.LENGTH_LONG).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
                        }
                    }
                });

    }
}