package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.ViewHolder.PropertyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AllProperty extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference PropertyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_property);

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

        final FirebaseRecyclerAdapter<PropertyDetails, PropertyViewHolder> adapter =
                new FirebaseRecyclerAdapter<PropertyDetails, PropertyViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PropertyViewHolder holder, int position, @NonNull final PropertyDetails model) {

                        holder.textPropertyAddress.setText(model.getBhkType()+ " At " + model.getAddress());
                        holder.textPropertyPrice.setText("$ "+ model.getPrice());
                        holder.textPropertySize.setText(model.getPropertySize()+" Sq.ft");
                        holder.textPropertyBhk.setText(model.getBhkType());
                        Picasso.get().load(model.getDownloadImageUrl0()).into(holder.imageView);

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
                    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_item_layout, parent, false);
                        PropertyViewHolder holder = new PropertyViewHolder(view);
                        return holder;

                    }

                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
}