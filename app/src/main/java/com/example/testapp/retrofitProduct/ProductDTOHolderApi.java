package com.example.testapp.retrofitProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductDTOHolderApi {
    @GET("products")
    public Call<List<ProductDTO>> getAllProducts();
}
