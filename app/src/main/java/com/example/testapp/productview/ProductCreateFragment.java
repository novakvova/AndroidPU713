package com.example.testapp.productview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.NavigationHost;
import com.example.testapp.R;
import com.example.testapp.productview.api.ProductDTOService;
import com.example.testapp.productview.dto.ProductCreateDTO;
import com.example.testapp.productview.dto.ProductCreateInvalidDTO;
import com.example.testapp.productview.dto.ProductCreateResultDTO;
import com.example.testapp.network.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductCreateFragment extends Fragment {
    private int i=0;
    public static final int PICKFILE_RESULT_CODE = 1;

    private Uri fileUri;
    private String filePath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_product_create, container, false);
        final TextInputEditText titleEditText = view.findViewById(R.id.product_title_editor);
        final TextInputEditText priceEditText = view.findViewById(R.id.product_price_editor);
        final TextView errormessage=view.findViewById(R.id.invalid);

        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Hello select image", Toast.LENGTH_SHORT).show();
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });



        Button btnAdd = view.findViewById(R.id.btnAddProduct);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String price = priceEditText.getText().toString();
                i++;
                errormessage.setText(String.valueOf(i));
                ProductCreateDTO productCreateDTO=new ProductCreateDTO(title,price);
                CommonUtils.showLoading(getActivity());
                ProductDTOService.getInstance()
                        .getJSONApi()
                        .createProduct(productCreateDTO)
                        .enqueue(new Callback<ProductCreateResultDTO>() {
                            @Override
                            public void onResponse(@NonNull Call<ProductCreateResultDTO> call, @NonNull Response<ProductCreateResultDTO> response) {
                                errormessage.setText("");
                                if (response.isSuccessful()) {
                                    ProductCreateResultDTO resultDTO = response.body();
                                    ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the products Fragment
                                    //  Log.e(TAG, "*************GOOD Request***********" + tokenDTO.getToken());
                                } else {
                                    //  Log.e(TAG, "_______________________" + response.errorBody().charStream());

                                    try {
                                        String json = response.errorBody().string();
                                        Gson gson  = new Gson();
                                        ProductCreateInvalidDTO resultBad = gson.fromJson(json, ProductCreateInvalidDTO.class);
                                        //Log.d(TAG,"++++++++++++++++++++++++++++++++"+response.errorBody().string());
                                        errormessage.setText(resultBad.getInvalid());
                                    } catch (Exception e) {
                                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                CommonUtils.hideLoading();

                                //Log.d(TAG,tokenDTO.toString());
                                //CommonUtils.hideLoading();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ProductCreateResultDTO> call, @NonNull Throwable t) {
                                //CommonUtils.hideLoading();
                                Log.e("ERROR","*************ERORR request***********");
                                t.printStackTrace();
                                CommonUtils.hideLoading();
                            }
                        });
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    filePath = fileUri.getPath();
                    //String uriString=fileUri.
                    //File file = new File(filePath);
                    File file = new File(fileUri.getPath());
                    File imgFile = new File(filePath+".jpg");
                    if (imgFile.exists() && imgFile.length() > 0) {
                        Bitmap bm = BitmapFactory.decodeFile(filePath);
                        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
                        String base64Image = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
                    }
                    Toast.makeText(getContext(), filePath, Toast.LENGTH_SHORT).show();
                    //tvItemPath.setText(filePath);
                }

                break;
        }
    }

}
