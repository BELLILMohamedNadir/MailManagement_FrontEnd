package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.CommentResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentViewModel extends ViewModel {
    private MutableLiveData<List<CommentResponse>> liveGetComments=new MutableLiveData<>();
    private final String TAG="commentViewModel";

    public void getComments(long id){
        ApiCall.getInstance().getComments(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CommentResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CommentResponse> comments) {
                        if (comments!=null)
                            liveGetComments.setValue(comments);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, " getComments onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<List<CommentResponse>> getLiveGetComments() {
        return liveGetComments;
    }
}
