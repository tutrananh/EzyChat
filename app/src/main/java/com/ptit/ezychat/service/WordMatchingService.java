package com.ptit.ezychat.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WordMatchingService {
    @GET("/api/v2/entries/en/{word}")
    Call<ResponseBody> wordChecking(@Path("word") String word);

}
