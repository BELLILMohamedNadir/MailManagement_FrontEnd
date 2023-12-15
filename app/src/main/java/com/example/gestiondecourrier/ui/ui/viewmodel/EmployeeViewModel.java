package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.Employee;
import com.example.gestiondecourrier.ui.pojo.EmployeeResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EmployeeViewModel extends ViewModel {
    private final MutableLiveData<List<EmployeeResponse>> liveGetEmployees=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveAddEmployees=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateEmployee=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateRecuperation=new MutableLiveData<>();
    private final String TAG="employeeViewModel";

    public void addEmployees(Employee employee){
        ApiCall.getInstance().addEmployee(employee)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveAddEmployees.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "add employee onError: "+e.getMessage());
                        liveAddEmployees.setValue(false);
                    }
                });
    }

    public void getEmployees(long id){
        ApiCall.getInstance().getEmployees(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EmployeeResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<EmployeeResponse> employeeResponses) {
                        if (employeeResponses!=null)
                            liveGetEmployees.setValue(employeeResponses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "get employees onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateEmployee(long id,Employee employee){
        ApiCall.getInstance().updateEmployee(id,employee)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateEmployee.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateEmployee.setValue(false);
                        Log.d(TAG, "update employee onError: "+e.getMessage());
                    }
                });
    }

    public void updateRecuperation(long id,int recuperation){
        ApiCall.getInstance().updateRecuperation(id,recuperation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateRecuperation.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateRecuperation.setValue(false);
                        Log.d(TAG, "update recuperation onError: "+e.getMessage());
                    }
                });
    }

    public MutableLiveData<List<EmployeeResponse>> getLiveGetEmployees() {
        return liveGetEmployees;
    }

    public MutableLiveData<Boolean> getLiveAddEmployees() {
        return liveAddEmployees;
    }

    public MutableLiveData<Boolean> getLiveUpdateEmployee() {
        return liveUpdateEmployee;
    }

    public MutableLiveData<Boolean> getLiveUpdateRecuperation() {
        return liveUpdateRecuperation;
    }
}
