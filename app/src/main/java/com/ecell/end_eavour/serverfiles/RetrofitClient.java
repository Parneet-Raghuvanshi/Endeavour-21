package com.ecell.end_eavour.serverfiles;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //10.0.2.2 for Emulators
    //your IPV4 for personal phone
    //localhost for PC
    public static final String BASE_URL = "http://65.2.72.84:7000/main/";
    public static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if (mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }
    public Api getApi(){
        return retrofit.create(Api.class);
    }
}
