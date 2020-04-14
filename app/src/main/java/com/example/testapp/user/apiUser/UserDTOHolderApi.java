package com.example.testapp.user.apiUser;

import com.example.testapp.productview.dto.ProductDTO;
import com.example.testapp.user.dtoUser.UserDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserDTOHolderApi {
    @GET("home/profile")
    public Call<UserDTO> getUserProfile();

}
