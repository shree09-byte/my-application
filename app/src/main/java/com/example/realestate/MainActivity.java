package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView SignUp, ForgotPassword, AdminLogin, NotAdminLogin, BannerText;
    private EditText Phone, Password;
    private ProgressBar progressBar;
    private CheckBox RememberMe;
    private String parentDbName = "Users";
    private FloatingActionButton SignInButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignUp = (TextView) findViewById(R.id.SignUp);
        SignUp.setOnClickListener(this);

        SignInButton = (FloatingActionButton) findViewById(R.id.SignInButton);
        SignInButton.setOnClickListener(this);

        RememberMe = (CheckBox) findViewById(R.id.RememberMe);
        Paper.init(this);

        AdminLogin = (TextView) findViewById(R.id.AdminLink);
        NotAdminLogin = (TextView) findViewById(R.id.NotAdminLink);
        AdminLogin.setOnClickListener(this);

        BannerText = (TextView) findViewById(R.id.BannerText);

        Phone = (EditText) findViewById(R.id.Phone);
        Password = (EditText) findViewById(R.id.Password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ForgotPassword = (TextView) findViewById(R.id.ForgotPassword);
        ForgotPassword.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserPhoneKey)  && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccessToAccount(UserPhoneKey, UserPasswordKey);
            }
        }

        AdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BannerText.setText("Welcome \nAdmin");
                AdminLogin.setVisibility(View.INVISIBLE);
                NotAdminLogin.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
                SignUp.setVisibility(View.INVISIBLE);
                ForgotPassword.setVisibility(View.INVISIBLE);
            }
        });
        NotAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BannerText.setText("Welcome \nBack");
                AdminLogin.setVisibility(View.VISIBLE);
                NotAdminLogin.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
                SignUp.setVisibility(View.VISIBLE);
                ForgotPassword.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SignUp:
                startActivity(new Intent(this, Login2.class));
                break;
            case R.id.SignInButton:
                SignInButton();
                break;
            case R.id.ForgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));

        }
    }


    private void SignInButton(){
        String phone = Phone.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (phone.isEmpty()){
            Phone.setError("This field can't be empty");
            Phone.requestFocus();
            return;
        }


        if (password.isEmpty()){
            Password.setError("This field can't be empty");
            Password.requestFocus();
            return;
        }

        if (password.length() < 6){
            Password.setError("password should not be less than 6");
            Password.requestFocus();
            return;
        }

        else
        {
            AllowAccessToAccount(phone, password);

        }
        progressBar.setVisibility(View.VISIBLE);
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {



        if(RememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);

        }

        final DatabaseReference DataRef;
        DataRef = FirebaseDatabase.getInstance().getReference();


        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phone).exists())
                {
                    Users userData = snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    assert userData != null;
                    if (userData.getPhone().equals(phone))
                    {
                        if (userData.getPassword().equals(password))
                        {
                                if (parentDbName.equals("Admins")){
                                    Toast.makeText(MainActivity.this, "Admin logged in succesfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, AdminHome.class);
                                    startActivity(intent);

                                    progressBar.setVisibility(View.GONE);
                                }
                                else if (parentDbName.equals("Users")){
                                    Toast.makeText(MainActivity.this, "User logged in succesfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    Prevalent.currentonlineUser = userData;
                                    startActivity(intent);

                                    progressBar.setVisibility(View.GONE);
                                }
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Password is wrong", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}