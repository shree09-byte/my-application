package com.example.realestate.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.Interface.ItemClickListener;
import com.example.realestate.R;

public class FavViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textPropertyAddress, textPropertyPrice, heartButton, callButton;
    public ImageView propertyImage;
    private ItemClickListener listener;


    public FavViewHolder(@NonNull View itemView) {
        super(itemView);

        textPropertyAddress = (TextView) itemView.findViewById(R.id.fav_address);
        textPropertyPrice = (TextView) itemView.findViewById(R.id.fav_price);

        propertyImage = (ImageView) itemView.findViewById(R.id.fav_property_image);
        heartButton = (TextView) itemView.findViewById(R.id.liked);
        callButton = (TextView) itemView.findViewById(R.id.call);

    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }
    
    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
    
    

}
