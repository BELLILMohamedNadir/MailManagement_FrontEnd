package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.pojo.ContactResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactViewModel extends ViewModel {

    private final MutableLiveData<List<Contact>> liveContact=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveAddContact=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveDeleteContact=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateContact=new MutableLiveData<>();

    private final String TAG="contactViewModel";

    public void getContacts(){
        ApiCall.getInstance().getContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Contact>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Contact> contacts) {
                        if (contacts!=null)
                            liveContact.setValue(contacts);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getContacts onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void addContact(Contact contact){
        ApiCall.getInstance().addContact(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveAddContact.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveAddContact.setValue(false);
                        Log.d(TAG, "addContact onError: "+e.getMessage());
                    }
                });
    }

    public void deleteContact(long id){
        ApiCall.getInstance().deleteContact(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveDeleteContact.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveDeleteContact.setValue(false);
                        Log.d(TAG, "deleteContact onError: "+e.getMessage());
                    }
                });
    }

    public void updateContact(long id,Contact contact){
        ApiCall.getInstance().updateContact(id,contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateContact.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateContact.setValue(false);
                        Log.d(TAG, "updateContact onError: "+e.getMessage());
                    }
                });
    }

    public MutableLiveData<List<Contact>> getLiveContact() {
        return liveContact;
    }

    public MutableLiveData<Boolean> getLiveAddContact() {
        return liveAddContact;
    }

    public MutableLiveData<Boolean> getLiveDeleteContact() {
        return liveDeleteContact;
    }

    public MutableLiveData<Boolean> getLiveUpdateContact() {
        return liveUpdateContact;
    }
}
