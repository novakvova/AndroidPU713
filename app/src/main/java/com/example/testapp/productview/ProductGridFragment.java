package com.example.testapp.productview;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.testapp.NavigationHost;
import com.example.testapp.R;
import com.example.testapp.productview.click_listeners.OnDeleteListener;
import com.example.testapp.productview.click_listeners.OnEditListener;
import com.example.testapp.network.ProductEntry;
import com.example.testapp.productview.dto.ProductDTO;
import com.example.testapp.productview.api.ProductDTOService;
import com.example.testapp.network.utils.CommonUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductGridFragment extends Fragment implements OnDeleteListener, OnEditListener {

    private static final String TAG = ProductGridFragment.class.getSimpleName();
    private RecyclerView recyclerView;

    private ProductCardRecyclerViewAdapter productAdapter;
    private List<ProductEntry> listProductEntry;

    private Button btnAdd;

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

        setupViews(view);

        setButtonAddListener();

        setRecyclerView();

        loadProductEntryList();

        return view;
    }

    private void setupViews(View view) {
        btnAdd = view.findViewById(R.id.btnAdd);
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    private void setButtonAddListener() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new ProductCreateFragment(), true);
            }
        });
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, false));
        listProductEntry = new ArrayList<>();
        productAdapter = new ProductCardRecyclerViewAdapter(listProductEntry, this, this, getContext());

        recyclerView.setAdapter(productAdapter);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
    }

    private void loadProductEntryList() {
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

                            listProductEntry.clear();
                            //    List<ProductEntry> newlist = new ArrayList<ProductEntry>();//ProductEntry.initProductEntryList(getResources());
                            for (ProductDTO item : list) {
                                ProductEntry pe = new ProductEntry(item.getId(), item.getTitle(), item.getUrl(), item.getUrl(), item.getPrice(), "sdfasd");
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
    }

    private void deleteConfirm(final ProductEntry productEntry) {
        CommonUtils.showLoading(getContext());
        ProductDTOService.getInstance()
                .getJSONApi()
                .DeleteRequest(productEntry.id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        CommonUtils.hideLoading();

                        if (response.isSuccessful()) {
                            listProductEntry.remove(productEntry);
                            productAdapter.notifyDataSetChanged();
                        } else {
                            //  Log.e(TAG, "_______________________" + response.errorBody().charStream());

                            try {
//                                                String json = response.errorBody().string();
//                                                Gson gson  = new Gson();
//                                                ProductCreateInvalidDTO resultBad = gson.fromJson(json, ProductCreateInvalidDTO.class);
                                //Log.d(TAG,"++++++++++++++++++++++++++++++++"+response.errorBody().string());
                                //errormessage.setText(resultBad.getInvalid());
                            } catch (Exception e) {
                                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        Log.e("ERROR", "*************ERORR request***********");
                        t.printStackTrace();

                    }
                });
    }

    @Override
    public void deleteItem(final ProductEntry productEntry) {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Видалення")
                .setMessage("Ви дійсно бажаєте видалити \"" + productEntry.title + "\"?")
                .setNegativeButton("Скасувати", null)
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteConfirm(productEntry);
                    }
                })
                .show();
//        new AlertDialog.Builder(getActivity())
//                .setTitle("Delete entry")
//                .setMessage("Are you sure you want to delete this entry?")
//
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Continue with delete operation
//                        listProductEntry.remove(productEntry);
//                        productAdapter.notifyDataSetChanged();
//                    }
//                })
//
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();

    }

    @SuppressLint("ResourceType")
    @Override
    public void editItem(int id) {
        Intent intent = new Intent(getActivity(), ProductEditActivity.class);
        intent.putExtra(Constants.PRODUCT_INTENT_ID, id);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT) {
            if (resultCode == -1) {
                loadProductEntryList();
            }
        }
    }

}
