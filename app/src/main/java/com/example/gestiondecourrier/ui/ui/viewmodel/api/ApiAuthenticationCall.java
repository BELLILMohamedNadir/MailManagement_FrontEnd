package com.example.gestiondecourrier.ui.ui.viewmodel.api;

import com.example.gestiondecourrier.ui.deserializer.AuthenticationDeserializer;
import com.example.gestiondecourrier.ui.deserializer.CommentDeserializer;
import com.example.gestiondecourrier.ui.deserializer.GmailDeserializer;
import com.example.gestiondecourrier.ui.deserializer.MailDeserializer;
import com.example.gestiondecourrier.ui.deserializer.ReportDeserializer;
import com.example.gestiondecourrier.ui.deserializer.TraceDeserializer;
import com.example.gestiondecourrier.ui.deserializer.UserDeserializer;
import com.example.gestiondecourrier.ui.pojo.AuthRequest;
import com.example.gestiondecourrier.ui.pojo.CommentResponse;
import com.example.gestiondecourrier.ui.pojo.GmailResponse;
import com.example.gestiondecourrier.ui.pojo.JwtToken;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.pojo.Report;
import com.example.gestiondecourrier.ui.pojo.TraceResponse;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiAuthenticationCall {
    private static volatile ApiAuthenticationCall INSTANCE=null;
    public static final String IP_ADDRESS="192.168.67.96";
    final String BASE_URL ="http://"+IP_ADDRESS+":8080/";
    Api api;


    public ApiAuthenticationCall(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MailResponse.class, new MailDeserializer())
                .registerTypeAdapter(GmailResponse.class,new GmailDeserializer())
                .registerTypeAdapter(UserResponse.class,new UserDeserializer())
                .registerTypeAdapter(JwtToken.class,new AuthenticationDeserializer())
                .registerTypeAdapter(CommentResponse.class,new CommentDeserializer())
                .registerTypeAdapter(TraceResponse.class,new TraceDeserializer())
                .registerTypeAdapter(Report.class, new ReportDeserializer())
                .create();


        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        api = retrofit.create(Api.class);
    }
    public static ApiAuthenticationCall getInstance(){
        if (INSTANCE==null){
            synchronized (MailViewModel.class){
                if (INSTANCE==null){
                    INSTANCE=new ApiAuthenticationCall();
                }
            }
        }
        return INSTANCE;
    }

    public Observable<JwtToken> authenticate(AuthRequest signIn){return api.authenticate(signIn);}


}
