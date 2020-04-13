package com.example.testapp.productview;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.NavigationHost;
import com.example.testapp.R;
import com.example.testapp.network.utils.FileUtils;
import com.example.testapp.productview.api.ProductDTOService;
import com.example.testapp.productview.dto.ProductCreateDTO;
import com.example.testapp.productview.dto.ProductCreateInvalidDTO;
import com.example.testapp.productview.dto.ProductCreateResultDTO;
import com.example.testapp.network.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductCreateFragment extends Fragment {
    public static final int PICKFILE_RESULT_CODE = 1;
    private ImageView chooseImage;
    private String chooseImageBase64;
    private Button btnSelectImage;
    private Button btnAdd;

    private TextInputLayout titleTextInput;
    private TextInputEditText titleEditText;
    private TextInputLayout priceTextInput;
    private TextInputEditText priceEditText;

    private TextView errormessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_create, container, false);

        //Ініціалізація зміних
        setupViews(view);

        //Обрати фото
        setBtnSelectImageListener();

        //Додати продукт
        setBtnAddListener();

        return view;
    }

    private void setupViews(View view) {

        titleTextInput = view.findViewById(R.id.product_title_input_text);
        titleEditText = view.findViewById(R.id.product_title_editor);

        priceTextInput = view.findViewById(R.id.price_text_input);
        priceEditText = view.findViewById(R.id.product_price_editor);

        errormessage = view.findViewById(R.id.invalid);

        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        chooseImage = view.findViewById(R.id.chooseImage);
        btnAdd = view.findViewById(R.id.btnAddProduct);



    }

    private void setBtnSelectImageListener() {
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Hello select image", Toast.LENGTH_SHORT).show();
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Оберіть фото");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });
    }

    private void setBtnAddListener() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String price = priceEditText.getText().toString();

                ProductCreateDTO productCreateDTO = new ProductCreateDTO(title, price, chooseImageBase64);
                CommonUtils.showLoading(getActivity());
                ProductDTOService.getInstance()
                        .getJSONApi()
                        .createProduct(productCreateDTO)
                        .enqueue(new Callback<ProductCreateResultDTO>() {
                            @Override
                            public void onResponse(@NonNull Call<ProductCreateResultDTO> call, @NonNull Response<ProductCreateResultDTO> response) {
                                errormessage.setText("");
                                titleTextInput.setError(null);
                                priceTextInput.setError(null);
                                CommonUtils.hideLoading();
                                if (response.isSuccessful()) {
                                    ProductCreateResultDTO resultDTO = response.body();
                                    ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the products Fragment
                                    //  Log.e(TAG, "*************GOOD Request***********" + tokenDTO.getToken());
                                } else {
                                    //  Log.e(TAG, "_______________________" + response.errorBody().charStream());

                                    try {
                                        String json = response.errorBody().string();
                                        Gson gson = new Gson();
                                        ProductCreateInvalidDTO resultBad = gson.fromJson(json, ProductCreateInvalidDTO.class);
                                        if(resultBad.getTitle() != null && !resultBad.getTitle().isEmpty()) {
                                            titleTextInput.setError(resultBad.getTitle());
                                        }

                                        if(resultBad.getPrice() != null && !resultBad.getPrice().isEmpty()) {
                                            priceTextInput.setError(resultBad.getPrice());
                                        }

                                        if(resultBad.getImageBase64() != null && !resultBad.getImageBase64().isEmpty()) {
                                            errormessage.setText(resultBad.getImageBase64());
                                        }

                                        if(resultBad.getInvalid() != null && !resultBad.getInvalid().isEmpty()) {
                                            errormessage.setText(resultBad.getInvalid());
                                        }

                                    } catch (Exception e) {
                                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }


                                //Log.d(TAG,tokenDTO.toString());
                                //CommonUtils.hideLoading();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ProductCreateResultDTO> call, @NonNull Throwable t) {
                                //CommonUtils.hideLoading();
                                Log.e("ERROR", "*************ERORR request***********");
                                t.printStackTrace();
                                CommonUtils.hideLoading();
                            }
                        });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    try {
                        File imgFile = FileUtils.from(this.getActivity(), fileUri);
                        byte [] buffer = new byte[(int)imgFile.length()+100];
                        int length = new FileInputStream(imgFile).read(buffer);
                        chooseImageBase64 = Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        chooseImage.setImageBitmap(myBitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
