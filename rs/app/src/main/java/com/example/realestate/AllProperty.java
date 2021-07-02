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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.Interface.ItemClickListener;
import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.ViewHolder.MypropertyViewHolder;
import com.example.realestate.ViewHolder.PropertyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AllProperty extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigation;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference PropertyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_property);


        bottomNavigation = (BottomNavigationView) findViewById(R.id.BottomAppBar);
        bottomNavigation.setBackground(null);
        bottomNavigation.getMenu().getItem(2).setChecked(false);
        bottomNavigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#6200EE")));
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        PropertyRef = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
        recyclerView =  findViewById(R.id.PropertyList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<PropertyDetails> options =
                new FirebaseRecyclerOptions.Builder<PropertyDetails>()
                        .setQuery(PropertyRef, PropertyDetails.class)
                        .build();

        final FirebaseRecyclerAdapter<PropertyDetails, MypropertyViewHolder> adapter =
                new FirebaseRecyclerAdapter<PropertyDetails, MypropertyViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MypropertyViewHolder holder, int position, @NonNull final PropertyDetails model) {

                        holder.textPropertyAddress.setText(model.getBhkType()+ " At " + model.getAddress());
                        holder.textPropertyPrice.setText("$ "+ model.getPrice());
                        holder.textPropertySize.setText(model.getPropertySize()+" Sq.ft");
                        holder.textPropertyBhk.setText(model.getBhkType());
                        Picasso.get().load(model.getDownloadImageUrl0()).into(holder.imageView);

                        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PropertyRef.child(model.getProductRandomKey())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(AllProperty.this, "PropertyDeleted", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(AllProperty.this, insideProperty.class);
                                intent.putExtra("pid", model.getProductRandomKey());
                                intent.putExtra("phone", model.getPhone());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MypropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myproperty_items, parent, false);
                        MypropertyViewHolder holder = new MypropertyViewHolder(view);
                        return holder;

                    }

                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

//    public static class MypropertyViewHolder extends RecyclerView.ViewHolder {
//        public TextView textPropertyAddress, textPropertyPrice, textPropertySize, textPropertyBhk, deleteBtn;
//        public ImageView imageView;
//
//
//        public MypropertyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            imageView = (ImageView) itemView.findViewById(R.id.Myproperty_image);
//            textPropertyAddress = (TextView) itemView.findViewById(R.id.Myproperty_address);
//            textPropertyPrice = (TextView) itemView.findViewById(R.id.Myproperty_price);
//            textPropertySize = (TextView) itemView.findViewById(R.id.Myproperty_size);
//            textPropertyBhk = (TextView) itemView.findViewById(R.id.Myproperty_bhk);
//            deleteBtn = (TextView) itemView.findViewById(R.id.MyDelete);
//
//
//        }
//    }


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