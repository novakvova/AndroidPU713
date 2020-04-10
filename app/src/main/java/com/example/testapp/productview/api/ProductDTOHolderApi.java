package com.example.testapp.productview.api;

import com.example.testapp.productview.dto.ProductCreateDTO;
import com.example.testapp.productview.dto.ProductCreateResultDTO;
import com.example.testapp.productview.dto.ProductDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ProductDTOHolderApi {
    @GET("products")
    public Call<List<ProductDTO>> getAllProducts();
    @POST("products/create")
    public Call<ProductCreateResultDTO> createProduct(@Body ProductCreateDTO product);
    @DELETE("products/delete/{id}")
    public Call<ResponseBody> DeleteRequest(@Path("id") int id);
    @GET("products/edit/{id}")
    public Call<ProductDTO> getEditProduct(@Path("id") int id);
    @POST("products/edit")
    public Call<Void> editProduct(@Body ProductDTO productDTO);

}
