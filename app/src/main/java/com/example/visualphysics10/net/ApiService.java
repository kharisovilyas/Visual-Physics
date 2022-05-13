package com.example.visualphysics10.net;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
        @GET("/zadachi/kinematika/koordinata-tochki-menyaetsya-so-vremenem-po-zakonu-x-11-35t-35t-3/")
        Call<TestingList> getTask();
}
