package com.example.realestate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.Model.PropertyDetails;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterClass extends  RecyclerView.Adapter<AdapterClass.MyViewHolder> {
    private Context context;
    ArrayList<PropertyDetails> list;

    public AdapterClass(ArrayList<PropertyDetails> list){
        this.list = list;

    }
    public AdapterClass(Context context){
        this.context = context;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.textPropertyAddress.setText(list.get(position).getAddress());
        holder.textPropertyPrice.setText("$ "+list.get(position).getPrice());
        holder.textPropertySize.setText(list.get(position).getPropertySize()+" Sq.ft");
        holder.textPropertyBhk.setText(list.get(position).getBhkType());
        Picasso.get().load(list.get(position).getDownloadImageUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), insideProperty.class);
                intent.putExtra("pid", list.get(position).getProductRandomKey());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textPropertyAddress, textPropertyPrice,textPropertySize, textPropertyBhk;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.property_image);
            textPropertyAddress = (TextView) itemView.findViewById(R.id.property_address);
            textPropertyPrice = (TextView) itemView.findViewById(R.id.property_price);
            textPropertySize = (TextView) itemView.findViewById(R.id.property_size);
            textPropertyBhk = (TextView) itemView.findViewById(R.id.property_bhk);

        }
    }
}
