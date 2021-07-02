package com.example.realestate.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.Interface.ItemClickListener;
import com.example.realestate.R;

public class MypropertyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textPropertyAddress, textPropertyPrice,textPropertySize, textPropertyBhk, deleteBtn;
    public ImageView imageView;
    public ItemClickListener listener;

    public MypropertyViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.Myproperty_image);
        textPropertyAddress = (TextView) itemView.findViewById(R.id.Myproperty_address);
        textPropertyPrice = (TextView) itemView.findViewById(R.id.Myproperty_price);
        textPropertySize = (TextView) itemView.findViewById(R.id.Myproperty_size);
        textPropertyBhk = (TextView) itemView.findViewById(R.id.Myproperty_bhk);
        deleteBtn = (TextView) itemView.findViewById(R.id.MyDelete);



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
