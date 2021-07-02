package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.Model.Fav;
import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class insideProperty extends AppCompatActivity {
    ImageView imageView, favourite;
    TextView address, price, size, bedroom, bathroom, balcony, date, sanction, water, propertyage, furnish, propertydescription;
    public String propertyId = "",phone, downloadUrl, checkLike = "";
    Button contactusbtn, buypropertybtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_property);

        propertyId = getIntent().getStringExtra("pid");
        phone = getIntent().getStringExtra("phone");


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("View Property");
        setSupportActionBar(toolbar);

        final String name = Prevalent.currentonlineUser.getFullname();
        final String useraddress = Prevalent.currentonlineUser.getAddress();
        final String phone = Prevalent.currentonlineUser.getPhone();
        final String email = Prevalent.currentonlineUser.getEmail();

        favourite = (ImageView) findViewById(R.id.favourite);
        imageView = (ImageView) findViewById(R.id.property_image);
        address = (TextView) findViewById(R.id.property_address);
        price = (TextView) findViewById(R.id.price);
        size = (TextView) findViewById(R.id.size);
        bathroom = (TextView) findViewById(R.id.bathroom);
        balcony = (TextView) findViewById(R.id.balcony);
        date = (TextView) findViewById(R.id.date);
        bedroom = (TextView) findViewById(R.id.bedroom);
        water = (TextView) findViewById(R.id.water);
        furnish = (TextView) findViewById(R.id.furnished);
//        sanction = (TextView) findViewById(R.id.sanction1);
        propertyage = (TextView) findViewById(R.id.propertyage);
        propertydescription = (TextView) findViewById(R.id.propertydescription);


        contactusbtn = (Button) findViewById(R.id.contactUs);
        buypropertybtn = (Button) findViewById(R.id.buyProperty);
        final DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Orders");
        final DatabaseReference userorderref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getPhone()).child("UserOrder");
        final DatabaseReference notificaitonorderref = FirebaseDatabase.getInstance().getReference().child("Users");


        final DatabaseReference DataRef;
        DataRef = FirebaseDatabase.getInstance().getReference().child("Favourite List").child("User View").child(Prevalent.currentonlineUser.getPhone()).child("Property").child(propertyId);

        //VeryVeryHard been awake all night @lordBakayarou
        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Liked").exists())
                {
                    favourite.setImageResource(R.drawable.ic_baseline_favorite_24_red);
                }
                else {

                    favourite.setImageResource(R.drawable.ic_baseline_favorite_24_white);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buypropertybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(insideProperty.this, FavActivity.class);
                startActivity(intent);
//                orderref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.hasChild(Prevalent.currentonlineUser.getPhone())){
//                            Toast.makeText(insideProperty.this, "You will be able to buy new property once current property is already purchased  ", Toast.LENGTH_LONG).show();
//                            Toast.makeText(insideProperty.this, "Go to orders for further process", Toast.LENGTH_SHORT).show();
//                        }
//
//                        else{
//
//                            final DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
//                                            productref.child(model.getPid()).addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    if (snapshot.exists()){
//                                                        Fav property = snapshot.getValue(Fav.class);
//
//                                                        coustphone = property.getAddress();
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                }
//                                            });
//
//                            final String savecurrenttime, savecurrentdate,orderID;
//                            Calendar callfordate = Calendar.getInstance();
//                            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYYY");
//                            savecurrentdate = currentDate.format(callfordate.getTime());
//
//                            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
//                            savecurrenttime = currentDate.format(callfordate.getTime());
//
//                            orderID = savecurrentdate+ savecurrenttime;
//
//                            productref.child(propertyId).addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if (snapshot.exists()){
//                                        Fav property = snapshot.getValue(Fav.class);
//                                        HashMap<String, Object> userdataMap = new HashMap<>();
//                                        userdataMap.put("fullname", name);
//                                        userdataMap.put("phone", phone);
//                                        userdataMap.put("email", email);
//                                        userdataMap.put("user address", useraddress);
//                                        userdataMap.put("address", property.getAddress());
//                                        userdataMap.put("date", savecurrentdate);
//                                        userdataMap.put("time", savecurrenttime);
//                                        userdataMap.put("Orderid", orderID);
//                                        userdataMap.put("status", "waiting");
//                                        userdataMap.put("pid", property.getPid());
//                                        userdataMap.put("price", property.getPrice());
//                                        userdataMap.put("image", property.getImage());
//                                        userdataMap.put("ownerPhone", property.getPhone());
//                                        userorderref.setValue(userdataMap);
//                                        orderref.child(Prevalent.currentonlineUser.getPhone()).setValue(userdataMap);
//                                        notificaitonorderref.child(property.getPhone()).child("Notifications").child(property.getPid()).setValue(userdataMap);
//                                        Intent intent = new Intent(insideProperty.this, ContactProperty.class);
//                                        intent.putExtra("pid", property.getPid());
//                                        startActivity(intent);
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });


//                            HashMap<String, Object> userdataMap = new HashMap<>();
//                            userdataMap.put("fullname", name);
//                            userdataMap.put("phone", phone);
//                            userdataMap.put("email", email);
//                            userdataMap.put("user address", useraddress);
//                            userdataMap.put("address", model.getAddress());
//                            userdataMap.put("date", savecurrentdate);
//                            userdataMap.put("time", savecurrenttime);
//                            userdataMap.put("Orderid", orderID);
//                            userdataMap.put("status", "waiting");
//                            userdataMap.put("pid", model.getPid());
//                            userdataMap.put("price", model.getPrice());
//                            userdataMap.put("image", model.getImage());
//                            userdataMap.put("ownerPhone", coustphone);



//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
            }
        });



        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference DataRef, Phoneref;
                DataRef = FirebaseDatabase.getInstance().getReference().child("Favourite List").child("User View").child(Prevalent.currentonlineUser.getPhone()).child("Property").child(propertyId);

                DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("Liked").exists())
                        {
                            DataRef.removeValue();
                            Toast.makeText(insideProperty.this, "Disliked", Toast.LENGTH_SHORT).show();
                            favourite.setImageResource(R.drawable.ic_baseline_favorite_24_white);
                        }
                        else {
                            checkLike = "yes";
                            favourite.setImageResource(R.drawable.ic_baseline_favorite_24_red);

                            addingToFavourite();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        getPropertyDetails(propertyId);
    }

    private void addingToFavourite() {
//        DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
//        productref.child(propertyId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    PropertyDetails property = snapshot.getValue(PropertyDetails.class);
//                    phone = property.getPhone();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        String savecurrenttime, savecurrentdate;
        Calendar callfordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYYY");
        savecurrentdate = currentDate.format(callfordate.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currentDate.format(callfordate.getTime());

       final  DatabaseReference favouriteref = FirebaseDatabase.getInstance().getReference().child("Favourite List");

        final HashMap<String, Object> favmap = new HashMap<>();
        favmap.put("pid", propertyId);
        favmap.put("address", address.getText().toString());
        favmap.put("price", price.getText().toString());
        favmap.put("date", savecurrentdate);
        favmap.put("time", savecurrenttime);
        favmap.put("size", size.getText().toString());
        favmap.put("image", downloadUrl);
        favmap.put("Liked", checkLike);
        favmap.put("phone", phone);
        favouriteref.child("User View").child(Prevalent.currentonlineUser.getPhone()).child("Property").child(propertyId)
                .updateChildren(favmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            favouriteref.child("Admin View").child(Prevalent.currentonlineUser.getPhone()).child("Property").child(propertyId)
                                    .updateChildren(favmap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(insideProperty.this, "Added to Fav", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

        finish();
    }

    private void getPropertyDetails(final String propertyId) {

        DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
        DatabaseReference productimageref = FirebaseDatabase.getInstance().getReference().child("PropertyDetails").child("PropertyImage");


        final DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference().child("Favourite List").child("User View").child(Prevalent.currentonlineUser.getPhone()).child("Property").child(propertyId);
        productref.child(propertyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    PropertyDetails property = snapshot.getValue(PropertyDetails.class);
                    address.setText(property.getAddress());
                    price.setText("$ " + property.getPrice());
                    size.setText(property.getPropertySize() + " Sq.ft");
                    balcony.setText(property.getBalcony() + " Balcony");
                    phone = property.getPhone();
                    char ch = property.getBhkType().toString().charAt(0);

                    date.setText(property.getSaveCurrentDate());
                    bedroom.setText(String.valueOf(ch)+ " Bedrooms");
                    bathroom.setText(property.getBathroom()+ " Bathroom");
                    downloadUrl = property.getDownloadImageUrl0();
                    Picasso.get().load(downloadUrl).into(imageView);
                    downloadUrl = property.getDownloadImageUrl();
                    propertyage.setText("Age "+ property.getPropertyAge());
                    water.setText(property.getWaterSupply());
//                    sanction.setText(property.getSanction());
                    furnish.setText(property.getFurnishing()+ " Furnish");
                    propertydescription.setText(property.getProperty_description());



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        productimageref.child(propertyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    PropertyDetails property = snapshot.getValue(PropertyDetails.class);

                    Picasso.get().load(property.getDownloadImageUrl()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}