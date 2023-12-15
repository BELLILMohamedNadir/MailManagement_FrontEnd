package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.Report;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReportViewModel extends ViewModel {
    private final MutableLiveData<List<Report>> liveReportsByStructure=new MutableLiveData<>();
    private final MutableLiveData<List<Report>> liveAllReports=new MutableLiveData<>();
    private final MutableLiveData<Report> liveDailyReport=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateApproved=new MutableLiveData<>();
    private final String TAG="reportViewModel";

    public void getReports(long id){
        ApiCall.getInstance().getReports(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Report>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Report> reports) {
                        liveAllReports.setValue(reports);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveAllReports.setValue(null);
                        Log.e(TAG, "getReports onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getDailyReport(long id){
        ApiCall.getInstance().getDailyReport(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Report>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Report reports) {
                        liveDailyReport.setValue(reports);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveDailyReport.setValue(null);
                        Log.e(TAG, "getDailyReport onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getReportsByStructure(long id){
        ApiCall.getInstance().getReportsByStructure(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Report>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Report> reports) {
                        liveReportsByStructure.setValue(reports);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveReportsByStructure.setValue(null);
                        Log.e(TAG, "getReportsByStructure onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateApproved(long id){
        ApiCall.getInstance().updateApproved(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateApproved.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateApproved.setValue(false);
                        Log.e(TAG, "updateApproved onError: "+e.getMessage());
                    }
                });
    }

    public MutableLiveData<List<Report>> getLiveReportsByStructure() {
        return liveReportsByStructure;
    }

    public MutableLiveData<List<Report>> getLiveAllReports() {
        return liveAllReports;
    }

    public MutableLiveData<Boolean> getLiveUpdateApproved() {
        return liveUpdateApproved;
    }

    public MutableLiveData<Report> getLiveDailyReport() {
        return liveDailyReport;
    }
}
