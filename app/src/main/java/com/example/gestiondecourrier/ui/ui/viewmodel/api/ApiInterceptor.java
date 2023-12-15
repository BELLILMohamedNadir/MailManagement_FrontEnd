package com.example.gestiondecourrier.ui.ui.viewmodel.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiInterceptor implements Interceptor {
    private String authToken;

    public ApiInterceptor(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Add the JWT token to the request header
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + authToken)
                .build();

        return chain.proceed(newRequest);
    }
}