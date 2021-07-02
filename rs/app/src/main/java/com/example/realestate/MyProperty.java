package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.Prevalent.Prevalent;
import com.example.realestate.ViewHolder.MypropertyViewHolder;
import com.example.realestate.ViewHolder.PropertyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MyProperty extends AppCompatActivity {
    private DatabaseReference PropertyRef, MainPropertyRef;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_property);

        Toolbar toolbar = findViewById(R.id.Mytoolbar);
        toolbar.setTitle("My Property");
        setSupportActionBar(toolbar);

        PropertyRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getPhone()).child("PropertyDetails");
        MainPropertyRef = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
        recyclerView =  findViewById(R.id.Mypropertyrecycler_menu);
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

        FirebaseRecyclerAdapter<PropertyDetails, MypropertyViewHolder> adapter =
                new FirebaseRecyclerAdapter<PropertyDetails, MypropertyViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MypropertyViewHolder holder, int position, @NonNull final PropertyDetails model) {
                        holder.textPropertyAddress.setText(model.getBhkType()+ "At " + model.getAddress());
                        holder.textPropertyPrice.setText("$ " + model.getPrice());
                        holder.textPropertySize.setText(model.getPropertySize() + " Sq.ft");
                        holder.textPropertyBhk.setText(model.getBhkType());
                        Picasso.get().load(model.getDownloadImageUrl0()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MyProperty.this, insideProperty.class);
                                intent.putExtra("pid", model.getProductRandomKey());
                                startActivity(intent);
                            }
                        });

                        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainPropertyRef.child(model.getProductRandomKey())
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        
                                    }
                                });

                                PropertyRef
                                        .child(model.getProductRandomKey())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(MyProperty.this, "Property Unliked", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
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
}