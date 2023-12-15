package com.example.gestiondecourrier.ui.ui.viewmodel;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StructureViewModel extends ViewModel {
    private final MutableLiveData<List<Structure>> liveGetStructures=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateStructure=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveAddStructure=new MutableLiveData<>();
    private final MutableLiveData<List<String>> liveStructureDesignation=new MutableLiveData<>();
    private final MutableLiveData<Structure> liveGetStructure=new MutableLiveData<>();

    private final String TAG="structureViewModel";


    public void getStructure(long id){
        ApiCall.getInstance().getStructure(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Structure>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Structure structure) {
                        if (structure!=null)
                            liveGetStructure.setValue(structure);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getStructure onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void addStructure(Structure structure){
        ApiCall.getInstance().addStructure(structure)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveAddStructure.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveAddStructure.setValue(false);
                        Log.d(TAG, "addStructure onError: ");
                    }
                });

    }

    public void getStructureDesignation(){
        ApiCall.getInstance().getStructureDesignation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        if (strings!=null)
                            liveStructureDesignation.setValue(strings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getStructureDesignation onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getStructures(){
        ApiCall.getInstance().getStructures()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Structure>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Structure> structures) {
                        if (structures!=null)
                            liveGetStructures.setValue(structures);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getStructures onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public void updateStructure(long id,Structure structure){

        ApiCall.getInstance().updateStructure(id,structure)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateStructure.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateStructure.setValue(false);
                        Log.d(TAG, "updateStructure onError: "+e.getMessage());
                    }
                });
    }




    public MutableLiveData<List<Structure>> getLiveGetStructures() {
        return liveGetStructures;
    }

    public MutableLiveData<Boolean> getLiveUpdateStructure() {
        return liveUpdateStructure;
    }

    public MutableLiveData<Boolean> getLiveAddStructure() {
        return liveAddStructure;
    }

    public MutableLiveData<List<String>> getLiveStructureDesignation() {
        return liveStructureDesignation;
    }

    public MutableLiveData<Structure> getLiveGetStructure() {
        return liveGetStructure;
    }
}
