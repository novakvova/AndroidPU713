package com.example.testapp.user.apiUser;

import com.example.testapp.productview.dto.ProductDTO;
import com.example.testapp.user.dtoUser.UserDTO;
import com.example.testapp.user.dtoUser.UserEditDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserDTOHolderApi {
    @GET("home/profile")
    public Call<UserDTO> getUserProfile();

    @GET("home/profile/edit")
    public Call<UserEditDTO> getEditUser();

    @PUT("home/profile/edit")
    public Call<Void> editUser(@Body UserEditDTO userEditDTO);

}
