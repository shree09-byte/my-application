package com.example.realestate.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.Interface.ItemClickListener;
import com.example.realestate.R;

public class UserOrderViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textPropertyAddress, textPropertyPrice, deletebtn;
    public ImageView propertyImage;
    private ItemClickListener listener;


    public UserOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        textPropertyAddress = (TextView) itemView.findViewById(R.id.order_address);
        textPropertyPrice = (TextView) itemView.findViewById(R.id.order_price);

        propertyImage = (ImageView) itemView.findViewById(R.id.order_property_image);

        deletebtn = itemView.findViewById(R.id.deletebtn);

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
