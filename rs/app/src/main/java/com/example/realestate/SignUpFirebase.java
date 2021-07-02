package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.realestate.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFirebase extends AppCompatActivity {
    private EditText FullName, Email, Phone, Password,image, address;
    private ProgressBar progressBar;
    private FloatingActionButton SignUpButton;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_firebase);

        mAuth = FirebaseAuth.getInstance();

        SignUpButton = findViewById(R.id.SignUpButton);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpButton();
            }
        });

        FullName = (EditText) findViewById(R.id.FullName);
        Email = (EditText) findViewById(R.id.Email);
        Phone = (EditText) findViewById(R.id.PhoneNumber);
        Password = (EditText) findViewById(R.id.Password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.SignUpButton:
//                SignUpButton();
//                break;
//        }
//    }

    private void SignUpButton(){
        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        final String fullname = FullName.getText().toString().trim();
        final String phone = Phone.getText().toString().trim();
        final String image = "https://firebasestorage.googleapis.com/v0/b/realestate-3692d.appspot.com/o/ProfilePic%2Fdownload.png?alt=media&token=57a1347c-1df7-4c0b-b42a-6134ca8281e3";
        final String address = "";


        if (fullname.isEmpty()){
            FullName.setError("This field can't be empty");
            FullName.requestFocus();
            return;
        }

        if (phone.isEmpty()){
            Phone.setError("This field can't be empty");
            Phone.requestFocus();
            return;
        }

        if (email.isEmpty()){
            Email.setError("This field can't be empty");
            Email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("provide valid email");
            Email.requestFocus();
            return;
        }
        if (password.isEmpty()){
            Password.setError("This field can't be empty");
            Password.requestFocus();
            return;
        }

        if (password.length()<6){
            Password.setError("enter password more than 6");
            Password.requestFocus();
            return;
        }

        if (phone.length()<10){
            Phone.setError("This field can't be empty");
            Phone.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Users user = new Users(fullname, phone, password, email, image, address);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(phone)
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignUpFirebase.this,"user has register", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(SignUpFirebase.this, "failed to register", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(SignUpFirebase.this, "failed to register", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}

