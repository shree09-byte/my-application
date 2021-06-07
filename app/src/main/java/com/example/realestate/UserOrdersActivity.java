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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.Model.Fav;
import com.example.realestate.Model.Fav;
import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.Prevalent.Prevalent;
import com.example.realestate.ViewHolder.FavViewHolder;
import com.example.realestate.ViewHolder.UserOrderViewHolder;
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

import org.w3c.dom.Text;

public class UserOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

//    public TextView  userPropertyPrice, userAddress, deletebtn;
//    public ImageView imageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        recyclerView = findViewById(R.id.userOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Orders");
        setSupportActionBar(toolbar);


//        userPropertyPrice =(TextView) findViewById(R.id.order_price);
//        userAddress = (TextView)findViewById(R.id.order_address);
//        imageView  = (ImageView) findViewById(R.id.order_property_image);
//        deletebtn = (TextView) findViewById(R.id.deletebtn);


//        final DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getPhone()).child("UserOrder");




//        orderref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    Fav property = snapshot.getValue(Fav.class);
//                     userPropertyPrice.setText("Sale for " + property.getPrice());
//                     userAddress.setText(property.getAddress());
//                     Picasso.get().load(property.getImage()).into(imageView);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("UserView").child(Prevalent.currentonlineUser.getPhone());
        final DatabaseReference favref = FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<Fav> options  =
        new FirebaseRecyclerOptions.Builder<Fav>()
        .setQuery(orderRef, Fav.class)
        .build();

        FirebaseRecyclerAdapter<Fav, UserOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Fav, UserOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserOrderViewHolder holder, int position, @NonNull final Fav model) {
                        holder.textPropertyAddress.setText(model.getAddress());
                        holder.textPropertyPrice.setText(model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.propertyImage);

                     holder.deletebtn.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             favref.child("Users")
                                     .child(Prevalent.currentonlineUser.getPhone())
                                     .child("UserOrder")
                                     .child(model.getPid())
                                     .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()){
                                         Toast.makeText(UserOrdersActivity.this, "Order Removed", Toast.LENGTH_SHORT).show();

                                     }

                                 }
                             });

                             orderRef
                                     .removeValue()
                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {

                                         }
                                     });

                         }
                     });
                            }



                    @NonNull
                    @Override
                    public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userorders, parent,false);

                        UserOrderViewHolder holder = new UserOrderViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


//        FirebaseRecyclerOptions<Fav> options =
//                new FirebaseRecyclerOptions.Builder<Fav>()
//                        .setQuery(favref.child("Users").child(Prevalent.currentonlineUser.getPhone()).child("UserOrder"), Fav.class).build();
//
//        FirebaseRecyclerAdapter<Fav, UserOrderViewHolder> adapter =
//        new FirebaseRecyclerAdapter<Fav, UserOrderViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull UserOrderViewHolder holder, int position, @NonNull final Fav model) {
//                holder.userPropertyPrice.setText("For Sale for " + model.getPrice());
//                holder.userAddress.setText(model.getAddress());
//                Picasso.get().load(model.getImage()).into(holder.imageView);
//
//                holder.deletebtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        favref.child("Users")
//                                .child(Prevalent.currentonlineUser.getPhone())
//                                .child("UserOrder")
//                                .child(model.getPid())
//                                .removeValue()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()){
//                                            Toast.makeText(UserOrdersActivity.this, "Order Removed", Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    }
//                                });
//                        favref.child("Orders")
//                                .child(Prevalent.currentonlineUser.getPhone())
//                                .removeValue()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                    }
//                                });
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userorders, parent,false);
//
//                return new UserOrderViewHolder(view);
//            }
//        };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//
//
//    }
//    public static class UserOrderViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView  userPropertyPrice, userAddress, deletebtn;
//        public ImageView imageView;
//
//        public UserOrderViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            userPropertyPrice = itemView.findViewById(R.id.order_price);
//            userAddress = itemView.findViewById(R.id.order_address);
//            imageView  = itemView.findViewById(R.id.order_property_image);
//            deletebtn = itemView.findViewById(R.id.deletebtn);
//
//
//        }
    }
}