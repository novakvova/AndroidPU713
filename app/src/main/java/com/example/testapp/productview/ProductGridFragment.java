package com.example.testapp.productview;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.testapp.NavigationHost;
import com.example.testapp.ProductEditFragment;
import com.example.testapp.R;
import com.example.testapp.click_listeners.OnDeleteListener;
import com.example.testapp.click_listeners.OnEditListener;
import com.example.testapp.network.ProductEntry;
import com.example.testapp.productview.dto.ProductDTO;
import com.example.testapp.productview.api.ProductDTOService;
import com.example.testapp.network.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductGridFragment extends Fragment implements OnDeleteListener, OnEditListener {

    private static final String TAG = ProductGridFragment.class.getSimpleName();
    private RecyclerView recyclerView;

    private ProductCardRecyclerViewAdapter productAdapter;
    private List<ProductEntry> listProductEntry;

    private final int REQUEST_CODE_EDIT = 101;

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
        listProductEntry = new ArrayList<>();
        productAdapter = new ProductCardRecyclerViewAdapter(listProductEntry, this, this);

//        List<ProductEntry> list = ProductEntry.initProductEntryList(getResources());
//        ProductCardRecyclerViewAdapter adapter = new ProductCardRecyclerViewAdapter(list);
//
        recyclerView.setAdapter(productAdapter);

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

                        if (response.isSuccessful()) {
                            List<ProductDTO> list = response.body();
                        //    List<ProductEntry> newlist = new ArrayList<ProductEntry>();//ProductEntry.initProductEntryList(getResources());
                            for (ProductDTO item : list) {
                                ProductEntry pe = new ProductEntry(item.getTitle(), item.getUrl(), item.getUrl(), item.getPrice(), "sdfasd");
                                listProductEntry.add(pe);
                            }
                         //   ProductCardRecyclerViewAdapter newAdapter = new ProductCardRecyclerViewAdapter(listProductEntry,this,this);
                   //         recyclerView.swapAdapter(newAdapter, false);
                            productAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ProductDTO>> call, @NonNull Throwable t) {
                        Log.e("ERROR", "*************ERORR request***********");
                        t.printStackTrace();
                        CommonUtils.hideLoading();

                    }
                });
        Log.d(TAG, "----------Hello my friends-------------");

        Button btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new ProductCreateFragment(), false);
            }
        });
        return view;
    }


    @Override
    public void deleteItem(ProductEntry productEntry) {
        listProductEntry.remove(productEntry);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void editItem(ProductEntry productEntry, int index) {
     //   Intent intent = new Intent(this, EditActivity.class);
        ((NavigationHost) getActivity()).navigateTo(new ProductEditFragment(), false);
//
//        intent.putExtra(Constants.PERSON_INTENT_EDIT, true);
//        intent.putExtra(Constants.PERSON_INTENT_INDEX, index);
//        intent.putExtra(Constants.PERSON_INTENT_OBJECT, person);
//        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }
}
