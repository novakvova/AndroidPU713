package com.example.testapp.userview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.network.ImageRequester;
import com.example.testapp.network.UserEntry;

import java.util.List;

/**
 * Adapter used to show a simple grid of products.
 */
public class UserCardRecyclerViewAdapter extends RecyclerView.Adapter<UserCardViewHolder> {

    private List<UserEntry> userList;
    private ImageRequester imageRequester;

    UserCardRecyclerViewAdapter(List<UserEntry> userList) {
        this.userList = userList;
        imageRequester = ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public UserCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new UserCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCardViewHolder holder, int position) {
        if (userList != null && position < userList.size()) {
            UserEntry user = userList.get(position);
            holder.userFirstname.setText(user.first_name);
            holder.userLastname.setText(user.last_name);
            holder.userCountry.setText(user.country);
            holder.userCity.setText(user.city);
            holder.userAge.setText(user.age);
            imageRequester.setImageFromUrl(holder.userImage, user.image);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

