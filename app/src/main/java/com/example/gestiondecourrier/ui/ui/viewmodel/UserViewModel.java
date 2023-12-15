package com.example.gestiondecourrier.ui.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestiondecourrier.ui.pojo.AuthRequest;
import com.example.gestiondecourrier.ui.pojo.JwtToken;
import com.example.gestiondecourrier.ui.pojo.User;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiAuthenticationCall;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<List<UserResponse>> liveGetUsers=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdatePassword =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateUserInfo=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveCreateUser =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveGeneratePassword =new MutableLiveData<>();
    private final MutableLiveData<JwtToken> liveAuthenticate =new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveLogout=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateToken=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateUserPhoto=new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveUpdateNotification=new MutableLiveData<>();
    private final MutableLiveData<UserResponse> liveGetUser=new MutableLiveData<>();


    private final String TAG="userViewModel";

    public void createUser(User user){
        ApiCall.getInstance().createUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveCreateUser.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "createUser onError: "+e.getMessage());
                        liveCreateUser.setValue(false);
                    }
                });

    }

    public void getUsers(){
        ApiCall.getInstance().getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<UserResponse> users) {
                        if (users!=null)
                            liveGetUsers.setValue(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getUsers onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void updatePassword(long id, User user){

        ApiCall.getInstance().updatePassword(id,user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdatePassword.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdatePassword.setValue(false);
                        Log.d(TAG, "updatePassword onError: "+e.getMessage());
                    }
                });
    }

    public void updateUserInfo(long id, User user){

        ApiCall.getInstance().updateUserInfo(id,user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateUserInfo.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateUserInfo.setValue(false);
                        Log.d(TAG, "updateUserInfo onError: "+e.getMessage());
                    }
                });
    }

    public void updateNotification(long id){

        ApiCall.getInstance().updateNotification(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateNotification.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateNotification.setValue(false);
                        Log.d(TAG, "updateNotification onError: "+e.getMessage());
                    }
                });
    }

    public void updateUserPhoto(long id, MultipartBody.Part m){

        ApiCall.getInstance().updateUserPhoto(id,m)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateUserPhoto.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveUpdateUserPhoto.setValue(false);
                        Log.d(TAG, "updateUserPhoto onError: "+e.getMessage());
                    }
                });
    }

    public void getUserById(long id){
        ApiCall.getInstance().getUserById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        if (userResponse!=null)
                            liveGetUser.setValue(userResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getUserById onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void generatePassword(long id){
        ApiCall.getInstance().generatePassword(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveGeneratePassword.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveGeneratePassword.setValue(false);
                        Log.d(TAG, "generatePassword onError: "+e.getMessage());
                    }
                });
    }

    public void authenticate(AuthRequest signIn){
        ApiAuthenticationCall.getInstance().authenticate(signIn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JwtToken>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JwtToken s) {
                        if (s!=null)
                            liveAuthenticate.setValue(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveAuthenticate.setValue(new JwtToken("JwtTokenError",null));
                        Log.d(TAG, "authentication  onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void logout(){
        ApiCall.getInstance().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {

                        if (voidResponse.isSuccessful()){
                            liveLogout.setValue(true);
                        }else{
                            liveLogout.setValue(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        liveLogout.setValue(false);
                        Log.d(TAG, "logout onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateToken(long id, String token){
        ApiCall.getInstance().updateToken(id,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        liveUpdateToken.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "update firebase token onError: "+e.getMessage());
                        liveUpdateToken.setValue(false);
                    }
                });
    }

    public MutableLiveData<List<UserResponse>> getLiveGetUsers() {
        return liveGetUsers;
    }


    public MutableLiveData<Boolean> getLiveUpdatePassword() {
        return liveUpdatePassword;
    }

    public MutableLiveData<Boolean> getLiveCreateUser() {
        return liveCreateUser;
    }

    public MutableLiveData<Boolean> getLiveUpdateUserPhoto() {
        return liveUpdateUserPhoto;
    }

    public MutableLiveData<UserResponse> getLiveGetUser() {
        return liveGetUser;
    }

    public MutableLiveData<Boolean> getLiveGeneratePassword() {
        return liveGeneratePassword;
    }

    public MutableLiveData<JwtToken> getLiveAuthenticate() {
        return liveAuthenticate;
    }

    public MutableLiveData<Boolean> getLiveUpdateToken() {
        return liveUpdateToken;
    }

    public MutableLiveData<Boolean> getLiveUpdateNotification() {
        return liveUpdateNotification;
    }

    public MutableLiveData<Boolean> getLiveUpdateUserInfo() {
        return liveUpdateUserInfo;
    }

    public MutableLiveData<Boolean> getLiveLogout() {
        return liveLogout;
    }
}
