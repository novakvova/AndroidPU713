package com.example.testapp.userview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.network.UserEntry;

import java.util.List;

public class UserGridFragment extends Fragment {

    private static final String TAG = UserGridFragment.class.getSimpleName();
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_grid, container, false);
        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));

        List<UserEntry> list = UserEntry.initUserEntryList(getResources());
        UserCardRecyclerViewAdapter adapter = new UserCardRecyclerViewAdapter(list);

        recyclerView.setAdapter(adapter);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_user_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_user_grid_spacing_small);
        recyclerView.addItemDecoration(new UserGridItemDecoration(largePadding, smallPadding));
        return view;
    }
}
