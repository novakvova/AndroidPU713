package com.example.testapp.productview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.testapp.Constants;
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
        productAdapter = new ProductCardRecyclerViewAdapter(listProductEntry, this, this, getContext());

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
                ((NavigationHost) getActivity()).navigateTo(new ProductCreateFragment(), true);
            }
        });
        return view;
    }


    @Override
    public void deleteItem(final ProductEntry productEntry) {

        new AlertDialog.Builder(getActivity())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        listProductEntry.remove(productEntry);
                        productAdapter.notifyDataSetChanged();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @SuppressLint("ResourceType")
    @Override
    public void editItem(ProductEntry productEntry, int index) {
//        Intent intent = new Intent(getActivity(),ProductEditFragment.class);

////
//        intent.putExtra(Constants.PRODUCT_INTENT_EDIT, true);
//        intent.putExtra(Constants.PRODUCT_INTENT_INDEX, index);
//        intent.putExtra(Constants.PRODUCT_INTENT_OBJECT, productEntry);
//       startActivityForResult(intent, REQUEST_CODE_EDIT);

        ProductEditFragment fragment = new ProductEditFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.PRODUCT_INTENT_EDIT, true);
        bundle.putInt(Constants.PRODUCT_INTENT_INDEX, index);
        bundle.putParcelable(Constants.PRODUCT_INTENT_OBJECT, productEntry);


        fragment.setArguments(bundle);//passing data to fragment
//        getChildFragmentManager().beginTransaction()
//                .replace(R.layout.fragment_product_grid, fragment)
//                .addToBackStack(null)
//                .commit();
        ((NavigationHost) getActivity()).navigateTo(fragment, true);

    }
}
