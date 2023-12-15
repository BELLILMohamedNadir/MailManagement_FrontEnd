package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.ArchivePk;
import com.example.gestiondecourrier.ui.pojo.FavoritePk;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.pojo.ReceivedMail;
import com.example.gestiondecourrier.ui.pojo.TraitPk;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MailViewModel extends ViewModel {

    private final MutableLiveData<List<MailResponse>> liveSendMails =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveCreateMail =new MutableLiveData<>();
    private final MutableLiveData<List<MailResponse>> liveAllMails =new MutableLiveData<>();
    private final MutableLiveData<List<MailResponse>> liveFavoriteMails =new MutableLiveData<>();
    private final MutableLiveData<List<MailResponse>> liveArchiveMails =new MutableLiveData<>();
    private final MutableLiveData<List<MailResponse>> liveTraitMails =new MutableLiveData<>();
    private final MutableLiveData<List<MailResponse>> liveToTraitMails =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateMail =new MutableLiveData<>();
    private final MutableLiveData<List<String>> liveReferences=new MutableLiveData<>();
    private final MutableLiveData<List<MailResponse>> liveReceivedMails=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateMailBehavior =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateReceivedMail =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveInsertFavorite =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateFavorite =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveInsertArchive =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateArchive =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveInsertTrait =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateTrait =new MutableLiveData<>();

    private final String TAG = "MailViewModel";

    public void sendMails(long id,long userId){
        ApiCall.getInstance().sendMails(id,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MailResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MailResponse> pdfs) {
                        if (pdfs!=null)
                            liveSendMails.setValue(pdfs);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "sendMails onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateMail(long id, List<MultipartBody.Part> file, RequestBody requestBody){
        ApiCall.getInstance().updateMail(id,file,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateMail.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateMail.setValue(false);
                        Log.d(TAG, "updateMail onError: "+e.getMessage());
                    }
                });
    }

    public void createMail(List<MultipartBody.Part> file, RequestBody description){
        ApiCall.getInstance().createMail(file,description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveCreateMail.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveCreateMail.setValue(false);
                        Log.d(TAG, "uploadPdf onError: "+e.getMessage());
                    }
                });
    }

    public void allMails(long id,long userId,String reference){
        ApiCall.getInstance().allMails(id,userId,reference)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MailResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<MailResponse> pdfs) {
                        if (pdfs!=null)
                            liveAllMails.setValue(pdfs);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "allMails onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void favoriteMails(long id,long userId){
        ApiCall.getInstance().favoriteMails(id,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MailResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MailResponse> pdfs) {
                        if (pdfs!=null)
                            liveFavoriteMails.setValue(pdfs);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "favoriteMails onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void archiveMails(long id,long userId,String reference){
        ApiCall.getInstance().archiveMails(id,userId,reference)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MailResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MailResponse> pdfs) {
                        if (pdfs!=null)
                            liveArchiveMails.setValue(pdfs);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "archiveMails onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void updateReceivedMails(long id, ReceivedMail mail){
        ApiCall.getInstance().updateReceivedMails(id,mail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateReceivedMail.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateReceivedMail.setValue(false);
                        Log.e(TAG, "updateReceivedMails onError: "+e.getMessage());
                    }
                });


    }

    public void receivedMails(String reference,long userId){
        ApiCall.getInstance().receivedMails(reference,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MailResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MailResponse> mailResponses) {
                        if (mailResponses!=null)
                            liveReceivedMails.setValue(mailResponses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "receivedMails onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void traitMails(String reference,long userId){
        ApiCall.getInstance().traitMails(reference,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MailResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<MailResponse> pdfs) {
                        if (pdfs!=null)
                            liveTraitMails.setValue(pdfs);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "traitMails onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void toTraitMails(String reference,long userId){
        ApiCall.getInstance().toTraitMails(reference,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MailResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MailResponse> pdfs) {
                        liveToTraitMails.setValue(pdfs);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "toTraitMails onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void reference(long id){
        ApiCall.getInstance().reference(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        if (strings!=null)
                            liveReferences.setValue(strings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "reference onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void insertFavorite(FavoritePk favorite){
        ApiCall.getInstance().insertFavorite(favorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveInsertFavorite.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveInsertFavorite.setValue(false);
                        Log.d(TAG, "insertFavorite onError: "+e.getMessage());
                    }
                });
    }

    public void updateFavorite(FavoritePk favorite){
        ApiCall.getInstance().updateFavorite(favorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateFavorite.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateFavorite.setValue(false);
                        Log.d(TAG, "updateFavorite onError: "+e.getMessage());
                    }
                });
    }

    public void insertArchive(ArchivePk archive){
        ApiCall.getInstance().insertArchive(archive)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveInsertArchive.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveInsertArchive.setValue(false);
                        Log.d(TAG, "insertArchive onError: "+e.getMessage());
                    }
                });
    }

    public void updateArchive(ArchivePk archive){
        ApiCall.getInstance().updateArchive(archive)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateArchive.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateArchive.setValue(false);
                        Log.d(TAG, "updateArchive onError: "+e.getMessage());
                    }
                });
    }

    public void insertTrait(TraitPk trait){
        ApiCall.getInstance().insertTrait(trait)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveInsertTrait.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveInsertTrait.setValue(false);
                        Log.d(TAG, "insertTrait onError: "+e.getMessage());
                    }
                });
    }



    public MutableLiveData<List<MailResponse>> getLiveSendMails() {
        return liveSendMails;
    }

    public MutableLiveData<Boolean> getLiveCreateMail() {
        return liveCreateMail;
    }

    public MutableLiveData<List<MailResponse>> getLiveAllMails() {
        return liveAllMails;
    }

    public MutableLiveData<List<MailResponse>> getLiveFavoriteMails() {
        return liveFavoriteMails;
    }

    public MutableLiveData<List<MailResponse>> getLiveArchiveMails() {
        return liveArchiveMails;
    }

    public MutableLiveData<List<MailResponse>> getLiveTraitMails() {
        return liveTraitMails;
    }

    public MutableLiveData<List<MailResponse>> getLiveToTraitMails() {
        return liveToTraitMails;
    }

    public MutableLiveData<Boolean> getLiveUpdateMail() {
        return liveUpdateMail;
    }

    public MutableLiveData<List<String>> getLiveReferences() {
        return liveReferences;
    }

    public MutableLiveData<List<MailResponse>> getLiveReceivedMails() {
        return liveReceivedMails;
    }

    public MutableLiveData<Boolean> getLiveUpdateMailBehavior() {
        return liveUpdateMailBehavior;
    }

    public MutableLiveData<Boolean> getLiveUpdateReceivedMail() {
        return liveUpdateReceivedMail;
    }

    public MutableLiveData<Boolean> getLiveInsertFavorite() {
        return liveInsertFavorite;
    }

    public MutableLiveData<Boolean> getLiveUpdateFavorite() {
        return liveUpdateFavorite;
    }

    public MutableLiveData<Boolean> getLiveInsertArchive() {
        return liveInsertArchive;
    }

    public MutableLiveData<Boolean> getLiveUpdateArchive() {
        return liveUpdateArchive;
    }

    public MutableLiveData<Boolean> getLiveInsertTrait() {
        return liveInsertTrait;
    }

    public MutableLiveData<Boolean> getLiveUpdateTrait() {
        return liveUpdateTrait;
    }
}
