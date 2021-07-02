package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.Model.AdminOrders;
import com.example.realestate.Model.Users;
import com.example.realestate.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AllUsers extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigation;
    private RecyclerView userList;
    private DatabaseReference orderref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.BottomAppBar);
        bottomNavigation.setBackground(null);
        bottomNavigation.getMenu().getItem(2).setChecked(true);
        bottomNavigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#6200EE")));
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        orderref = FirebaseDatabase.getInstance().getReference().child("Users");

        userList = findViewById(R.id.UsersList);
        userList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {

        super.onStart();


        FirebaseRecyclerOptions<Users> options=
                new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(orderref, Users.class)
                .build();

        FirebaseRecyclerAdapter<Users, AllUsers.UsersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull final Users model) {
                        holder.userName.setText(model.getFullname());
                        holder.userPhonenumber.setText(model.getPhone());
                        holder.userAddress.setText(model.getAddress());
                        holder.userEmail.setText(model.getEmail());

                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.Deleteuser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                orderref.child(model.getPhone())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(AllUsers.this, "User Banned", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allusers, parent, false);
                        return  new AllUsers.UsersViewHolder(view);
                    }
                };
        userList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhonenumber, userEmail, userAddress;
        public TextView Deleteuser;
        public ImageView imageView;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            userPhonenumber = itemView.findViewById(R.id.userPhone);
            userEmail = itemView.findViewById(R.id.userEmail);
            userAddress = itemView.findViewById(R.id.userAddress);
            imageView = itemView.findViewById(R.id.UserProfileImage);
            Deleteuser = itemView.findViewById(R.id.Userdeletebtn);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.miHome:
                startActivity(new Intent(this, AdminHome.class));


                return true;
            case R.id.miUsers:
                startActivity(new Intent(this, AllUsers.class));

                return true;
            case R.id.miProperty:
                startActivity(new Intent(this, AllProperty.class));


                return true;

        }
        return false;
    }
}