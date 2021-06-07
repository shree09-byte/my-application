package com.example.realestate.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.Interface.ItemClickListener;
import com.example.realestate.R;

public class PropertyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textPropertyAddress, textPropertyPrice,textPropertySize, textPropertyBhk;
    public ImageView imageView;
    public ItemClickListener listener;

    public PropertyViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.property_image);
        textPropertyAddress = (TextView) itemView.findViewById(R.id.property_address);
        textPropertyPrice = (TextView) itemView.findViewById(R.id.property_price);
        textPropertySize = (TextView) itemView.findViewById(R.id.property_size);
        textPropertyBhk = (TextView) itemView.findViewById(R.id.property_bhk);

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
