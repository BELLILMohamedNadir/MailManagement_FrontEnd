package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.Category;
import com.example.gestiondecourrier.ui.pojo.CategoryInfo;
import com.example.gestiondecourrier.ui.pojo.CategoryResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryViewModel extends ViewModel {
    private final MutableLiveData<List<CategoryResponse>> liveGetCategory=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveAddCategory=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateCategory=new MutableLiveData<>();
    private final MutableLiveData<List<CategoryInfo>> liveGetArrivedCategoryInfo=new MutableLiveData<>();
    private final MutableLiveData<List<CategoryInfo>> liveGetSendCategoryInfo =new MutableLiveData<>();
    private final MutableLiveData<List<String>> liveGetCategoryDesignation=new MutableLiveData<>();
    private final MutableLiveData<List<String>> liveGetArrivedCategoryDesignation=new MutableLiveData<>();
    private final String TAG="categoryViewModel";

    public void getCategories(){
        ApiCall.getInstance().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CategoryResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CategoryResponse> categories) {
                        if (categories!=null)
                            liveGetCategory.setValue(categories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "category onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getCategoryDesignation(long struct_id){
        ApiCall.getInstance().getCategoryDesignation(struct_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> categories) {
                        if (categories!=null)
                            liveGetCategoryDesignation.setValue(categories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, " category  designation onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getArrivedCategoryDesignation(long struct_id){
        ApiCall.getInstance().getArrivedCategoryDesignation(struct_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> categories) {
                        if (categories!=null)
                            liveGetArrivedCategoryDesignation.setValue(categories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getArrivedCategoryDesignation  onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void addCategory(Category category){
        ApiCall.getInstance().addCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveAddCategory.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveAddCategory.setValue(false);
                    }
                });
    }

    public void updateCategory(long id,Category category){
        ApiCall.getInstance().updateCategory(id,category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateCategory.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateCategory.setValue(false);
                    }
                });
    }

    public void getArrivedCategoryInfo(long id){
        ApiCall.getInstance().getArrivedCategoryInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CategoryInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CategoryInfo> categoryInfos) {
                        if (categoryInfos!=null)
                            liveGetArrivedCategoryInfo.setValue(categoryInfos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getArrivedCategoryInfo onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getSendCategoryInfo(long id){
        ApiCall.getInstance().getSendCategoryInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CategoryInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CategoryInfo> categoryInfos) {
                        if (categoryInfos!=null)
                            liveGetSendCategoryInfo.setValue(categoryInfos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getSendCategoryInfo onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<List<CategoryResponse>> getLiveGetCategory() {
        return liveGetCategory;
    }

    public MutableLiveData<Boolean> getLiveAddCategory() {
        return liveAddCategory;
    }

    public MutableLiveData<Boolean> getLiveUpdateCategory() {
        return liveUpdateCategory;
    }

    public MutableLiveData<List<CategoryInfo>> getLiveGetArrivedCategoryInfo() {
        return liveGetArrivedCategoryInfo;
    }

    public MutableLiveData<List<CategoryInfo>> getLiveGetSendCategoryInfo() {
        return liveGetSendCategoryInfo;
    }

    public MutableLiveData<List<String>> getLiveGetCategoryDesignation() {
        return liveGetCategoryDesignation;
    }

    public MutableLiveData<List<String>> getLiveGetArrivedCategoryDesignation() {
        return liveGetArrivedCategoryDesignation;
    }
}
