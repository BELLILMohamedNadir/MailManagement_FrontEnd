package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.Attendance;
import com.example.gestiondecourrier.ui.pojo.Email;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EmailViewModel extends ViewModel {
    private final MutableLiveData<Boolean> liveSendEmail=new MutableLiveData<>();
    private final String TAG="emailViewModel";

    public void sendEmail(Email email){
        ApiCall.getInstance().sendEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveSendEmail.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveSendEmail.setValue(false);
                        Log.e(TAG, "send email onError: "+e.getMessage());
                    }
                });
    }

    public MutableLiveData<Boolean> getLiveSendEmail() {
        return liveSendEmail;
    }
}
