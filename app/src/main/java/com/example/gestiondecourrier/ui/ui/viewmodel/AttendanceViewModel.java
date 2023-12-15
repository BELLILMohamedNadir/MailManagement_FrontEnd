package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.Attendance;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AttendanceViewModel extends ViewModel {
    private MutableLiveData<Boolean> liveAddAttendance=new MutableLiveData<>();
    private final String TAG="attendanceViewModel";

    public void addAttendance(Attendance attendance){
        ApiCall.getInstance().addAttendance(attendance)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveAddAttendance.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveAddAttendance.setValue(false);
                        Log.d(TAG, "add attendance onError: "+e.getMessage());
                    }
                });
    }


    public MutableLiveData<Boolean> getLiveAddAttendance() {
        return liveAddAttendance;
    }

}
