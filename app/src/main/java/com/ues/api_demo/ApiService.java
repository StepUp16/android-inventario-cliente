package com.ues.api_demo;

import com.ues.api_demo.models.LoginRequest;
import com.ues.api_demo.models.LoginResponse;
import com.ues.api_demo.models.Product;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE; // Nuevo
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;    // Nuevo
import retrofit2.http.Path;   // Nuevo

public interface ApiService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("products")
    Call<List<Product>> getProducts(@Header("Authorization") String token);

    @POST("auth/logout")
    Call<ResponseBody> logout(@Header("Authorization") String token);

    // --- NUEVOS MÉTODOS CRUD ---

    @POST("products")
    Call<Product> createProduct(@Header("Authorization") String token, @Body Product product);

    @PUT("products")
    Call<Product> updateProduct(@Header("Authorization") String token, @Body Product product);

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Header("Authorization") String token, @Path("id") int id);
}