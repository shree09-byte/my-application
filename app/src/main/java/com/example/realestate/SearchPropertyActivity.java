package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.realestate.Interface.ItemClickListener;
import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.ViewHolder.PropertyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class SearchPropertyActivity extends AppCompatActivity {

//    private Button searchbtn;
//    private EditText inputtext;
//    private RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
//    SearchView sv;
//    private String Searchinput;
//    ArrayList<PropertyDetails> details;

    SearchView searchView;
    RecyclerView recyclerView;
    DatabaseReference ref;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PropertyDetails> list;
    Button bhk1, bhk2,bhk3,bhk4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_property);

        ref = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
        recyclerView = findViewById(R.id.search_list);
        searchView = findViewById(R.id.Search);
        EditText searchEditText = (EditText) searchView.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.White));
        searchEditText.setHintTextColor(getResources().getColor(R.color.White));

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bhk1 = (Button) findViewById(R.id.bhk1);
        bhk2 = (Button) findViewById(R.id.bhk2);
        bhk3 = (Button) findViewById(R.id.bhk3);
        bhk4 = (Button) findViewById(R.id.bhk4);



//        inputtext = (EditText) findViewById(R.id.search_property_name);
//        searchbtn = (Button) findViewById(R.id.search_button);
//        recyclerView = findViewById(R.id.search_list);
//        recyclerView.setHasFixedSize(true);
//
//        sv = (SearchView) findViewById(R.id.Search);
//
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }

//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Searchinput = sv.getQuery().toString();
//                onStart();
//                search(newText);
//                return true;
//            }
//        });

//        sv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Searchinput = sv.getQuery().toString();
//                onStart();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bhk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search("1bhk");
            }
        });
        bhk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search("2bhk");
            }
        });
        bhk3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search("3bhk");
            }
        });
        bhk4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search("4bhk");
            }
        });

        if (ref!=null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){

                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            list.add(ds.getValue(PropertyDetails.class));
                        }
                        AdapterClass adapterClass = new AdapterClass(list);
                        recyclerView.setAdapter(adapterClass);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




        if (searchView!= null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);

                    bhk1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            search("3bhk");
                        }
                    });
                    bhk2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            search("2bhk");
                        }
                    });

                    bhk3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            search("3bhk");
                        }
                    });

                    bhk4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            search("4bhk");
                        }
                    });
                    return true;
                }
            });
        }
    }


    private void search(String str){
        ArrayList<PropertyDetails> mylist = new ArrayList<>();


        for (PropertyDetails obect: list){
            if (obect.getAddress().toLowerCase().contains(str.toLowerCase()) || obect.getBhkType().toLowerCase().contains(str.toLowerCase()) )
            {
                mylist.add(obect);
            }
        }
        AdapterClass adapterClass = new AdapterClass(mylist);
        recyclerView.setAdapter(adapterClass);
    }


    //    private void search(String str) {
//
//        ArrayList<PropertyDetails> mylist = new ArrayList<>();
//        for (PropertyDetails object: details){
//            if (object.getAddress().toLowerCase().contains(str.toLowerCase()))
//            {
//                mylist.add(object);
//                onStart();
//            }
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
//
//        FirebaseRecyclerOptions<PropertyDetails> options = new FirebaseRecyclerOptions.Builder<PropertyDetails>()
//                .setQuery(reference, PropertyDetails.class)
//                .build();
//
//        final FirebaseRecyclerAdapter<PropertyDetails, PropertyViewHolder> adapter =
//                new FirebaseRecyclerAdapter<PropertyDetails, PropertyViewHolder>(options) {
//
//                    @Override
//                    protected void onBindViewHolder(@NonNull PropertyViewHolder holder, int position, @NonNull final PropertyDetails model) {
//
//
//
//                        holder.textPropertyAddress.setText(model.getAddress());
//                        holder.textPropertyPrice.setText("$ "+model.getPrice());
//                        holder.textPropertySize.setText(model.getPropertySize()+" Sq.ft");
//                        holder.textPropertyBhk.setText(model.getbhkType());
//                        Picasso.get().load(model.getDownloadImageUrl()).into(holder.imageView);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(SearchPropertyActivity.this, insideProperty.class);
//                                intent.putExtra("pid", model.getProductRandomKey());
//                                startActivity(intent);
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_item_layout, parent, false);
//                        PropertyViewHolder holder = new PropertyViewHolder(view);
//                        return holder;
//                    }
//                };
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    details = new ArrayList<>();
//                    for (DataSnapshot ds: snapshot.getChildren())
//                    {
//                        details.add(ds.getValue(PropertyDetails.class));
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//
//
//    }
}