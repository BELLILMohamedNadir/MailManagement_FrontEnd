package com.example.gestiondecourrier.ui.ui;

import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.editor;
import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.sp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.gestiondecourrier.BuildConfig;
import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.ActivityAdminBinding;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;
import com.example.gestiondecourrier.ui.ui.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    ActivityAdminBinding binding;
    NavHostFragment navHostFragment;
    public static NavController navControllerAdmin;
    public static long adminId=-1;
    public static MutableLiveData<UserResponse> adminDetails=new MutableLiveData<>();
    public static MutableLiveData<Bitmap> liveBitmapAdmin =new MutableLiveData<>();
    public static MutableLiveData<BottomNavigationView> navigationView=new MutableLiveData<>();


    UserViewModel userViewModel=new UserViewModel();


    @Override
    protected void onResume() {
        super.onResume();
        editor.putBoolean("shouldInvokeOnStop",true).commit();
        if (sp.getString(BuildConfig.TOKEN,null)==null){
            Intent intent = new Intent(AdminActivity.this, AuthenticationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        userViewModel= ViewModelProviders.of(AdminActivity.this).get(UserViewModel.class);
        super.onCreate(savedInstanceState);
        binding= ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.admin_fragment);
        if (navHostFragment!=null)
            navControllerAdmin = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navAdmin, navControllerAdmin);
        navigationView.setValue(binding.navAdmin);
        if (getIntent()!=null && getIntent().getLongExtra("userId",0)!=0) {
            adminId = getIntent().getLongExtra("userId", 0);

            userViewModel.getUserById(adminId);
            userViewModel.getLiveGetUser().observe(AdminActivity.this, new Observer<UserResponse>() {
                @Override
                public void onChanged(UserResponse userResponse) {
                    adminDetails.setValue(userResponse);
                    Bitmap bitmap=null;
                    if (userResponse.getBytes()!=null)
                        bitmap = BitmapFactory.decodeByteArray(userResponse.getBytes(), 0, userResponse.getBytes().length);
                    liveBitmapAdmin.setValue(bitmap);
                }
            });

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //check if we will navigate to the next activity ex .choose photo or choose a pdf
        if (sp.getBoolean("shouldInvokeOnStop",true)) {
            // clear the shared preferences and make jwtToken null
            userViewModel.logout();
            editor.putString(BuildConfig.TOKEN, null).commit();
            //to ensure that we create a new ApiCall object every time we initialise the activity
            //this process when we get out of the app and we didn't destroy it the previous object has the previous token
            ApiCall.setINSTANCE(null);
        }
    }
}