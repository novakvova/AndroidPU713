package com.example.testapp.productview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testapp.ErrorFragment;
import com.example.testapp.NavigationHost;
import com.example.testapp.R;
import com.example.testapp.network.ProductEntry;
import com.example.testapp.retrofitProduct.ProductDTO;
import com.example.testapp.retrofitProduct.ProductDTOService;
import com.example.testapp.utils.CommonUtils;
import com.example.testapp.utilsintrnet.NoConnectivityException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductGridFragment extends Fragment {

    private static final String TAG = ProductGridFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    //Handler h;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_grid, container, false);
        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, false));

//        List<ProductEntry> list = ProductEntry.initProductEntryList(getResources());
//        ProductCardRecyclerViewAdapter adapter = new ProductCardRecyclerViewAdapter(list);
//
//        recyclerView.set  Adapter(adapter);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
        //h = new Handler();
        CommonUtils.showLoading(getActivity());
        ProductDTOService.getInstance()
                .getJSONApi()
                .getAllProducts()
                .enqueue(new Callback<List<ProductDTO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ProductDTO>> call, @NonNull Response<List<ProductDTO>> response) {
                        CommonUtils.hideLoading();
                        if(response.isSuccessful()) {
                            List<ProductDTO> list = response.body();
                            List<ProductEntry> newlist = new ArrayList<ProductEntry>();//ProductEntry.initProductEntryList(getResources());
                            for (ProductDTO item : list) {
                                ProductEntry pe = new ProductEntry(item.getTitle(), item.getUrl(), item.getUrl(), item.getPrice(), "sdfasd");
                                newlist.add(pe);
                            }
                            ProductCardRecyclerViewAdapter newAdapter = new ProductCardRecyclerViewAdapter(newlist);
                            recyclerView.swapAdapter(newAdapter, false);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ProductDTO>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        if (t instanceof NoConnectivityException) {
                            Log.d(TAG, "------------------------------ffffffffffffffffffffffffffffffffff-----------------");
                            ((NavigationHost) getActivity()).navigateTo(new ErrorFragment(), false); // Navigate to the next Fragment
                            // показываем информацию
                            //h.post(showError);
                        }
                    }
                });
        Log.d(TAG, "----------Hello my friends-------------");


        return view;
    }

}
