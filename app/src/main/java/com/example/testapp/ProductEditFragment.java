package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.testapp.network.ProductEntry;
import com.example.testapp.productview.ProductGridFragment;
import com.example.testapp.productview.dto.ProductDTO;

import java.util.List;


public class ProductEditFragment extends Fragment {

    private EditText editTextTitle;
    private EditText editTextPrice;
    private Button buttonEdit;
    private Button buttonCancel;

    private ProductEntry productEntry;
    private int index = -1;
    private boolean isEdit = false;

   // private Bundle bundle;

    public ProductEditFragment() {
        // Required empty public constructor
    }

    private void setupViews(View view) {
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        buttonEdit = view.findViewById(R.id.buttonEdit);
        buttonCancel = view.findViewById(R.id.buttonCancel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_edit, container, false);

        setupViews(view);
        //Bundle bundle = this.getArguments();

        //Log.e("product", bundle.getParcelable(Constants.PRODUCT_INTENT_OBJECT).toString());
        if( getArguments() != null){

            productEntry=getArguments().getParcelable(Constants.PRODUCT_INTENT_OBJECT);
        }

        initProduct();
        setButtonCancelListener();
        setButtonEditListener();

        return view;
    }

    private void initProduct() {
    Bundle   bundle = this.getArguments();
        Log.d("product", bundle.getParcelable(Constants.PRODUCT_INTENT_OBJECT).toString());
        isEdit = bundle.getBoolean(Constants.PRODUCT_INTENT_EDIT, false);
        if (isEdit) {
            if (bundle != null) {
                productEntry = bundle.getParcelable(Constants.PRODUCT_INTENT_OBJECT);
                Log.d("product", bundle.getParcelable(Constants.PRODUCT_INTENT_OBJECT).toString());
            }
            index = bundle.getInt(Constants.PRODUCT_INTENT_INDEX, -1);
            if (index == -1) {
                getActivity().setResult(getActivity().RESULT_CANCELED);
                //  getActivity().finish();
                getActivity().getFragmentManager().popBackStack();
            }
            editTextTitle.setText(productEntry.getTitle());
            editTextPrice.setText(productEntry.getPrice());
            buttonEdit.setText(getString(R.string.button_edit));
        } else {
            productEntry = new ProductEntry();
            buttonEdit.setText(getString(R.string.button_add));
        }

    }

    private void setButtonCancelListener() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(getActivity().RESULT_CANCELED);
                ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false);
            }
        });
    }

    private void setButtonEditListener() {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String price = editTextPrice.getText().toString().trim();
                productEntry.setTitle(title);
                productEntry.setPrice(price);

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
//                bundle.putParcelable(Constants.PRODUCT_INTENT_OBJECT, productEntry);
                bundle.putBoolean(Constants.PRODUCT_INTENT_EDIT, isEdit);
                bundle.putInt(Constants.PRODUCT_INTENT_INDEX, index);
                intent.putExtras(bundle);

                getActivity().setResult(getActivity().RESULT_OK, intent);
                ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false);
            }
        });
    }
}
