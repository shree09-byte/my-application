package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.Button;
import android.widget.TextView;

import com.example.realestate.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class AdminHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView logout;

    private AdView mAdView;

    BottomNavigationView bottomNavigation;
    private RecyclerView orderlist;
    private DatabaseReference orderref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.BottomAppBar);
        bottomNavigation.setBackground(null);
        bottomNavigation.getMenu().getItem(2).setChecked(false);
        bottomNavigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#6200EE")));
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        logout = (TextView) findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHome.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        orderref = FirebaseDatabase.getInstance().getReference().child("Orders").child("AdminView");

        orderlist = findViewById(R.id.order_list);
        orderlist.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderref, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position, @NonNull final AdminOrders model) {
                        holder.userName.setText(model.getFullname());
                        holder.userPhonenumber.setText(model.getPhone());
                        holder.userAddress.setText(model.getAddress());
                        holder.userDate.setText(model.getDate());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(AdminHome.this, insideProperty.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AdminOrderViewHolder(view);

                    }
                };
        orderlist.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhonenumber, userPropertyPrice, userDate, userAddress;
        public Button ShowPropertyBtn;
        public AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.Propertyusername);
            userPhonenumber = itemView.findViewById(R.id.Propertyuserphone);
            userPropertyPrice = itemView.findViewById(R.id.Propertyorderprice);
            userAddress = itemView.findViewById(R.id.PropertyUserAddress);
            userDate = itemView.findViewById(R.id.Propertyorderdate);

            ShowPropertyBtn = itemView.findViewById(R.id.PropertyshowProperty);

        }
    }



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