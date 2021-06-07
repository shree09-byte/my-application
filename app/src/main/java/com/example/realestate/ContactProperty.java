package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ContactProperty extends AppCompatActivity {
    ImageView imageView, favourite;
    TextView address, price, userdetails;
    public String propertyId = "", downloadUrl, checkLike = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_property);

        propertyId = getIntent().getStringExtra("pid");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Contact Property");
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.property_image);
        address = (TextView) findViewById(R.id.property_address);
        price = (TextView) findViewById(R.id.price);
        userdetails = (TextView) findViewById(R.id.user_details);


        getPropertyDetails(propertyId);

    }

    private void getPropertyDetails(String propertyId) {

        DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");

        final DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference().child("Favourite List").child("User View").child(Prevalent.currentonlineUser.getPhone()).child("Property").child(propertyId);
        productref.child(propertyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    PropertyDetails property = snapshot.getValue(PropertyDetails.class);
                    address.setText(property.getAddress());
                    userdetails.setText("Contact Details will be send to your " + Prevalent.currentonlineUser.getPhone() + " and " + Prevalent.currentonlineUser.getEmail());
                    price.setText("$ " + property.getPrice());

                    Picasso.get().load(property.getDownloadImageUrl()).into(imageView);
                    downloadUrl = property.getDownloadImageUrl();




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}