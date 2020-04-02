package com.example.testapp.productview.api;

import com.example.testapp.productview.dto.ProductCreateDTO;
import com.example.testapp.productview.dto.ProductCreateResultDTO;
import com.example.testapp.productview.dto.ProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProductDTOHolderApi {
    @GET("products")
    public Call<List<ProductDTO>> getAllProducts();
    @POST("products/create")
    public Call<ProductCreateResultDTO> createProduct(@Body ProductCreateDTO product);
}
