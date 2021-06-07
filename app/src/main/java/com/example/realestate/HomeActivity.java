package com.example.realestate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.Model.Users;
import com.example.realestate.Prevalent.Prevalent;
import com.example.realestate.ViewHolder.PropertyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference PropertyRef;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String uri, userPhone;
    private FirebaseAuth mAuth;
    private DatabaseReference UserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        PropertyRef = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
        recyclerView =  findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();
//        userPhone = mAuth.getCurrentUser().getEmail();
        UserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddProperty.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent intent = new Intent(HomeActivity.this, SearchPropertyActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_liked) {

                    Intent intent = new Intent(HomeActivity.this, FavActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_property) {
                    Intent intent = new Intent(HomeActivity.this, MyProperty.class);
                    startActivity(intent);

                } else if (id == R.id.nav_setting) {
                    Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_notification) {
                    Intent intent = new Intent(HomeActivity.this, notificationActivity.class);
                    startActivity(intent);

                }
                else if (id == R.id.nav_addproperty) {
                    Intent intent = new Intent(HomeActivity.this, AddProperty.class);
                    startActivity(intent);

                }
                else if (id == R.id.nav_order) {
                    Intent intent = new Intent(HomeActivity.this, UserOrdersActivity.class);
                    startActivity(intent);

                }
                else if (id == R.id.nav_news) {
                    Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
                    startActivity(intent);

                }
                else if (id == R.id.nav_logout) {
                    Paper.book().destroy();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    Toast.makeText(HomeActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    finish();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        View heraderView = navigationView.getHeaderView(0);
        final TextView userNameTextView = heraderView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = heraderView.findViewById(R.id.user_profile_image);

        Picasso.get().load(Prevalent.currentonlineUser.getImage()).into(profileImageView);
        userNameTextView.setText(Prevalent.currentonlineUser.getFullname());

//        UserDatabase.child(userPhone).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Users userProfile = snapshot.getValue(Users.class);
//                if (snapshot != null){
//                    String fullname = userProfile.getFullname();
//                    String phone = userProfile.getPhone();
//                    String email = userProfile.getEmail();
//
//                    userNameTextView.setText(fullname);
//
//
////                    String image = dataSnapshot.child("profileimage").getValue().toString();
////                    Picasso.get().load(image).into(profliePic);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

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
                                Intent intent = new Intent(HomeActivity.this, insideProperty.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);


        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}