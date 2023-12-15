package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.TraceResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TraceViewModel extends ViewModel {
    private final MutableLiveData<List<TraceResponse>> liveGetTraces=new MutableLiveData<>();
    private final String TAG="traceViewModel";

    public void getTraces(){
        ApiCall.getInstance().getTraces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TraceResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<TraceResponse> traceResponse) {
                        if (traceResponse!=null)
                            liveGetTraces.setValue(traceResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getTraces onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }

    public MutableLiveData<List<TraceResponse>> getLiveGetTraces() {
        return liveGetTraces;
    }
}
