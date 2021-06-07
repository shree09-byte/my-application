package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.realestate.Model.Fav;
import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.Prevalent.Prevalent;
import com.example.realestate.ViewHolder.FavViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FavActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public String coustphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        recyclerView = findViewById(R.id.fav_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();

        final String name = Prevalent.currentonlineUser.getFullname();
        final String address = Prevalent.currentonlineUser.getAddress();
        final String phone = Prevalent.currentonlineUser.getPhone();
        final String email = Prevalent.currentonlineUser.getEmail();


        final DatabaseReference favref = FirebaseDatabase.getInstance().getReference().child("Favourite List");
        final DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Orders").child("UserView");
        final DatabaseReference adminorderref = FirebaseDatabase.getInstance().getReference().child("Orders").child("AdminView");
        final DatabaseReference notificaitonorderref = FirebaseDatabase.getInstance().getReference().child("Users");


        FirebaseRecyclerOptions<Fav> options =
                new FirebaseRecyclerOptions.Builder<Fav>()
                .setQuery(favref.child("User View").child(Prevalent.currentonlineUser.getPhone()).child("Property"), Fav.class).build();

        FirebaseRecyclerAdapter<Fav, FavViewHolder> adapter =
                new FirebaseRecyclerAdapter<Fav, FavViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FavViewHolder holder, int position, @NonNull final Fav model) {
                        holder.textPropertyPrice.setText("For Sale for " + model.getPrice());
                        holder.textPropertyAddress.setText(model.getAddress());
                        Picasso.get().load(model.getImage()).into(holder.propertyImage);
                        coustphone = model.getPhone();
//
//                        heartButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                favref.child("User View")
//                                        .child(Prevalent.currentonlineUser.getPhone())
//                                        .child("Property")
//                                        .child(model.getPid())
//                                        .removeValue()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//                                                    Toast.makeText(FavActivity.this, "Property Unliked", Toast.LENGTH_SHORT).show();
//
//                                                }
//                                            }
//                                        });
//                            }
//                        });

                        holder.heartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                favref.child("User View")
                                        .child(Prevalent.currentonlineUser.getPhone())
                                        .child("Property")
                                        .child(model.getPid())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(FavActivity.this, "Property Unliked", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                            }
                        });

                        holder.callButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {




                                orderref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild(Prevalent.currentonlineUser.getPhone())){
                                            Toast.makeText(FavActivity.this, "You will be able to buy new property once current property is already purchased  ", Toast.LENGTH_LONG).show();
                                            Toast.makeText(FavActivity.this, "Go to orders for further process", Toast.LENGTH_SHORT).show();
                                            }

                                        else{
                                            final DatabaseReference userorderref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getPhone()).child("UserOrder").child(model.getPid());

                                            DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("PropertyDetails").child(model.getPid());
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

                                            String savecurrenttime, savecurrentdate,orderID;
                                            Calendar callfordate = Calendar.getInstance();
                                            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYYY");
                                            savecurrentdate = currentDate.format(callfordate.getTime());

                                            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
                                            savecurrenttime = currentDate.format(callfordate.getTime());

                                            orderID = savecurrentdate+ savecurrenttime;

                                            HashMap<String, Object> userdataMap = new HashMap<>();
                                            userdataMap.put("fullname", name);
                                            userdataMap.put("phone", phone);
                                            userdataMap.put("email", email);
                                            userdataMap.put("user address", address);
                                            userdataMap.put("address", model.getAddress());
                                            userdataMap.put("date", savecurrentdate);
                                            userdataMap.put("time", savecurrenttime);
                                            userdataMap.put("Orderid", orderID);
                                            userdataMap.put("status", "waiting");
                                            userdataMap.put("pid", model.getPid());
                                            userdataMap.put("price", model.getPrice());
                                            userdataMap.put("image", model.getImage());
                                            userdataMap.put("ownerPhone", coustphone);


                                            userorderref.setValue(userdataMap);
                                            orderref.child(Prevalent.currentonlineUser.getPhone()).child(model.getPid()).setValue(userdataMap);
                                            adminorderref.child(Prevalent.currentonlineUser.getPhone()).setValue(userdataMap);
                                            notificaitonorderref.child(coustphone).child("Notifications").child(model.getPid()).setValue(userdataMap);
                                            Intent intent = new Intent(FavActivity.this, ContactProperty.class);
                                            intent.putExtra("pid", model.getPid());
                                            startActivity(intent);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(FavActivity.this, insideProperty.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);

                                }
                        });
                    }

                    @NonNull
                    @Override
                    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_items_layout, parent,false);

                        FavViewHolder holder = new FavViewHolder(view);
                        return holder;
                    }
                };

                recyclerView.setAdapter(adapter);
                adapter.startListening();

    }


}