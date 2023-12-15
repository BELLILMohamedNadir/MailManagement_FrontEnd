package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.GmailResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GmailViewModel extends ViewModel {

    private final MutableLiveData<List<GmailResponse>> liveGetGmail =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUploadPdfFromGmail=new MutableLiveData<>();
    private final MutableLiveData<List<String>> liveEmails=new MutableLiveData<>();
    private final String TAG="GmailViewModel";

    public void uploadPdfFromGmail(List<MultipartBody.Part> file, RequestBody description){
        ApiCall.getInstance().uploadPdfFromGmail(file,description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUploadPdfFromGmail.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUploadPdfFromGmail.setValue(false);
                        Log.d(TAG, "pdf upload onError: "+e.getMessage());
                    }
                });
    }

    public void getGmail(long id){
        ApiCall.getInstance().getGmail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GmailResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<GmailResponse> gmailResponses) {
                        if (gmailResponses!=null)
                            liveGetGmail.setValue(gmailResponses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getAllFromGmail onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void getEmails(){
        ApiCall.getInstance().getEmails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<String> email) {
                        if (email!=null)
                            liveEmails.setValue(email);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getEmails onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<List<GmailResponse>> getLiveGetGmail() {
        return liveGetGmail;
    }

    public MutableLiveData<Boolean> getLiveUploadPdfFromGmail() {
        return liveUploadPdfFromGmail;
    }

    public MutableLiveData<List<String>> getLiveEmails() {
        return liveEmails;
    }
}

