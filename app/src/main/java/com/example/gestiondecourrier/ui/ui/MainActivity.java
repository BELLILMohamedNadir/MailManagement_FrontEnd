package com.example.gestiondecourrier.ui.ui;

import static android.content.ContentValues.TAG;
import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.editor;
import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.sp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestiondecourrier.BuildConfig;
import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.ActivityMainBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;
import com.example.gestiondecourrier.ui.ui.viewmodel.api.ApiCall;
import com.example.gestiondecourrier.ui.ui.viewmodel.CategoryViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    public static NavController navController;
    static ActivityMainBinding binding;
    public static NavHostFragment navHostFragment;
    public static DrawerLayout drawerLayout;
    public static UserResponse userDetails=null;
    public static List<String> items;
    public static List<Structure> structure;
    public static long userId;
    public static Bitmap bitmap=null;
    TextView textStructure,textName;
    CircleImageView imgProfile;
    Feature feature=new Feature();

    public static MutableLiveData<List<String>> liveCategoryDesignation=new MutableLiveData<>();
    public static MutableLiveData<List<String>> liveItems=new MutableLiveData<>();
    public static MutableLiveData<String> livePicture=new MutableLiveData<>();
    public static MutableLiveData<Long> liveStructureId=new MutableLiveData<>();
    public static MutableLiveData<Bitmap> liveBitmap=new MutableLiveData<>();
    public static final String CHANNEL_ID="2000";
    public static final int NOTIFICATION_ID=10;
    MailViewModel mailViewModel=new MailViewModel();
    UserViewModel userViewModel=new UserViewModel();
    StructureViewModel structureViewModel=new StructureViewModel();
    CategoryViewModel categoryViewModel=new CategoryViewModel();


    @Override
    protected void onResume() {
        super.onResume();
        editor.putBoolean("shouldInvokeOnStop",true).commit();
        if (sp.getString(BuildConfig.TOKEN,null)==null){
            Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            editor.putBoolean("shouldInvokeOnStop",true).commit();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mailViewModel= ViewModelProviders.of(MainActivity.this).get(MailViewModel.class);
        userViewModel= ViewModelProviders.of(MainActivity.this).get(UserViewModel.class);
        structureViewModel=ViewModelProviders.of(MainActivity.this).get(StructureViewModel.class);
        categoryViewModel=ViewModelProviders.of(MainActivity.this).get(CategoryViewModel.class);
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        structure=new ArrayList<>();
        items=new ArrayList<>();



        //initialise TextView and ImageView in the header
        textStructure=binding.navigation.getHeaderView(0).findViewById(R.id.txt_structure_user_header);
        textName=binding.navigation.getHeaderView(0).findViewById(R.id.txt_name_user_header);
        imgProfile=binding.navigation.getHeaderView(0).findViewById(R.id.img_user_header);

        // set drawerLayout
        drawerLayout=binding.drawerLayout;
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (navHostFragment!=null)
            navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navigation,navController);




        //navigate to ProfileFragment from ImageView in the header
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.profileFragment);
            }
        });

        //check permissions READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE and MANAGE_EXTERNAL_STORAGE
        ActivityResultLauncher<String> arl=registerForActivityResult(new ActivityResultContracts.RequestPermission()
                , new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        //Toast.makeText(getApplicationContext(), result+"", Toast.LENGTH_SHORT).show();
                    }
                });
        arl.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        arl.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            arl.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arl.launch(Manifest.permission.POST_NOTIFICATIONS);
        }

        //get userId from AuthenticationActivity
        if (getIntent()!=null && getIntent().getLongExtra("userId",0)!=0 ) {
            userId = getIntent().getLongExtra("userId", 0);
            getUserInformation();
        }



    }

    private void firebaseTokenControl() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        if (token!=null && userDetails!=null)
                            if (userDetails.getFirebaseToken()!=null) {
                                if (!userDetails.getFirebaseToken().equals(token))
                                    userViewModel.updateToken(userDetails.getId(), token);
                            }else
                                userViewModel.updateToken(userDetails.getId(), token);



                    }
                });
    }


    private void getUserInformation(){
        //get user information from server
        userViewModel.getUserById(userId);
        // check if data received with MutableLiveData
        userViewModel.getLiveGetUser().observe(MainActivity.this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                //get userResponse to make static
                userDetails=userResponse;

                // set image if it's exists
                if (userResponse.getBytes()!=null)
                    bitmap = BitmapFactory.decodeByteArray(userDetails.getBytes(), 0, userDetails.getBytes().length);

                if (bitmap!=null) {
                    imgProfile.setImageBitmap(bitmap);
                    liveBitmap.setValue(bitmap);
                }else {
                    livePicture.setValue(userDetails.getName());
                    imgProfile.setImageResource(feature.picture(userDetails.getName().charAt(0)));
                }
                editor.putBoolean("notification", userDetails.isNotification()).commit();

                if (userResponse.getName()!=null && userResponse.getFirstName()!=null)
                    textName.setText(userDetails.getName().toUpperCase(Locale.ROOT).concat(" "+userDetails.getFirstName()));

                liveStructureId.setValue(userDetails.getStructure_id());

                //control the firebase token
                firebaseTokenControl();

                getStructures();


            }
        });
    }

    private void getStructures(){

        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(MainActivity.this, new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                if (!structures.isEmpty()){
                    //clear the items list before add new structure
                    items.clear();
                    structure.clear();
                    for (int i=0;i<structures.size();i++){
                        // initialise the structure name in the header
                        if (structures.get(i).getId()==userDetails.getStructure_id()) {
                            textStructure.setText(structures.get(i).getDesignation());
                            continue;
                        }
                        items.add(structures.get(i).getDesignation());
                        //initialise the structure to be static
                        structure.add(structures.get(i));
                    }
                    liveItems.setValue(items);

                }
            }
        });
        getCategoryDesignation();

    }

    private void getCategoryDesignation() {
        categoryViewModel.getCategoryDesignation(userDetails.getStructure_id());
        categoryViewModel.getLiveGetCategoryDesignation().observe(MainActivity.this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (strings!=null){
                    liveCategoryDesignation.setValue(strings);
                }
            }
        });
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