package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realestate.Model.Fav;
import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.Model.notification;
import com.example.realestate.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class notificationActivity extends AppCompatActivity {
    private DatabaseReference PropertyRef, MainPropertyRef;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = findViewById(R.id.notification_toolbar);
        toolbar.setTitle("Notification");
        setSupportActionBar(toolbar);

        PropertyRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getPhone()).child("PropertyDetails");
        MainPropertyRef = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
        recyclerView =  findViewById(R.id.notificationrecycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();


        final DatabaseReference notificationref = FirebaseDatabase.getInstance().getReference();



        FirebaseRecyclerOptions<notification> options =
                new FirebaseRecyclerOptions.Builder<notification>()
                .setQuery(notificationref.child("Users").child(Prevalent.currentonlineUser.getPhone()).child("Notifications"), notification.class).build();

        FirebaseRecyclerAdapter<notification, NotificationViewHolder> adapter =
                new FirebaseRecyclerAdapter<notification, NotificationViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull notification model) {

                        holder.userAddress.setText("Property Address: "+model.getAddress());
                        holder.contact.setText("Contact Details: "+model.getPhone()+", "+ model.getEmail());
                        holder.userName.setText(model.getFullname()  + " wants to buy your property");
                    }

                    @NonNull
                    @Override
                    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_items, parent,false);
                        return new NotificationViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userAddress, contact;
        public ImageView imageView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.notificaiton_name);
            userAddress = itemView.findViewById(R.id.notification_des);
            imageView  = itemView.findViewById(R.id.notification_property_image);
            contact = itemView.findViewById(R.id.notification_contact);


        }
    }
}