//package com.example.realestate;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.HashMap;
//
//public class SignUp extends AppCompatActivity implements View.OnClickListener {
//    private EditText FullName, Email, Phone, Password;
//    private ProgressBar progressBar;
//    private FloatingActionButton SignUpButton;
//
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        SignUpButton = findViewById(R.id.SignUpButton);
//        SignUpButton.setOnClickListener(this);
//
//        FullName = (EditText) findViewById(R.id.FullName);
//        Email = (EditText) findViewById(R.id.Email);
//        Phone = (EditText) findViewById(R.id.PhoneNumber);
//        Password = (EditText) findViewById(R.id.Password);
//
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.SignUpButton:
//                SignUpButton();
//                break;
//        }
//    }
//
//    private void SignUpButton() {
//        final String email = Email.getText().toString().trim();
//        String password = Password.getText().toString().trim();
//        final String fullname = FullName.getText().toString().trim();
//        final String phone = Phone.getText().toString().trim();
//
//        if (fullname.isEmpty()) {
//            FullName.setError("This field can't be empty");
//            FullName.requestFocus();
//            return;
//        }
//
//        if (phone.isEmpty()) {
//            Phone.setError("This field can't be empty");
//            Phone.requestFocus();
//            return;
//        }
//
//        if (email.isEmpty()) {
//            Email.setError("This field can't be empty");
//            Email.requestFocus();
//            return;
//        }
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            Email.setError("provide valid email");
//            Email.requestFocus();
//            return;
//        }
//        if (password.isEmpty()) {
//            Password.setError("This field can't be empty");
//            Password.requestFocus();
//            return;
//        }
//
//        if (password.length() < 6) {
//            Password.setError("enter password more than 6");
//            Password.requestFocus();
//            return;
//        }
//
//        if (phone.length() < 10) {
//            Phone.setError("This field can't be empty");
//            Phone.requestFocus();
//            return;
//        }
//        else {
//            ValidatephoneNumber(fullname, phone, password, email);
//        }
//
//        progressBar.setVisibility(View.VISIBLE);
//    }
//
//    private void ValidatephoneNumber (final String fullname, final String phone, final String password, final String email){
//        final DatabaseReference DataRef;
//        DataRef = FirebaseDatabase.getInstance().getReference();
//
//        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(!(snapshot.child("Users").child(phone).exists()))
//                {
//                    HashMap<String, Object> userdataMap = new HashMap<>();
//                    userdataMap.put("phone", phone);
//                    userdataMap.put("fullname", fullname);
//                    userdataMap.put("email", email);
//                    userdataMap.put("password", password);
//
//                    DataRef.child("Users").child(phone).updateChildren(userdataMap)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task)
//                                {
//                                    if (task.isSuccessful())
//                                    {
//                                        Toast.makeText(SignUp.this, "Your account has been created", Toast.LENGTH_SHORT).show();
//                                        progressBar.setVisibility(View.GONE);
//                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
//                                        startActivity(intent);
//                                    }
//                                }
//                            });
//                }
//                else
//                {
//                    Toast.makeText(SignUp.this, "This" + phone + "Already exist", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//}