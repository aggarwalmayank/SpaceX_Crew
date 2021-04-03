package com.appsaga.spacex;

import com.appsaga.spacex.WebModel.Crew;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface WebService {

    String BASE_URL = "https://api.spacexdata.com/v4/";
    String FIELD = "crew";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET(FIELD)
    Call<List<Crew>> getPosts();
}
