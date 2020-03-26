package com.example.testapp.userview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.testapp.R;

public class UserCardViewHolder extends RecyclerView.ViewHolder {

    public NetworkImageView userImage;
    public TextView userFirstname;
    public TextView userLastname;
    public TextView userCountry;
    public TextView userCity;
    public TextView userAge;
    public UserCardViewHolder(@NonNull View itemView) {
        super(itemView);
        userImage = itemView.findViewById(R.id.user_image);
        userFirstname = itemView.findViewById(R.id.user_firstname);
        userLastname = itemView.findViewById(R.id.user_lastname);
        userCountry = itemView.findViewById(R.id.user_country);
        userCity = itemView.findViewById(R.id.user_city);
        userAge = itemView.findViewById(R.id.user_age);
    }
}