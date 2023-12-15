package com.example.gestiondecourrier.ui.ui;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.gestiondecourrier.BuildConfig;
import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.ActivityAuthentificationBinding;
import com.example.gestiondecourrier.ui.LocaleHelper;
import com.example.gestiondecourrier.ui.pojo.JwtToken;
import com.example.gestiondecourrier.ui.pojo.AuthRequest;
import com.example.gestiondecourrier.ui.ui.fragment.ErrorFragment;
import com.example.gestiondecourrier.ui.ui.viewmodel.UserViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AuthenticationActivity extends AppCompatActivity {
    ActivityAuthentificationBinding binding;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    Intent intent;
    UserViewModel userViewModel=new UserViewModel();
    public static boolean isShown=false;
    boolean checked=false;




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase,LocaleHelper.getLanguage(newBase)));
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userViewModel= ViewModelProviders.of(AuthenticationActivity.this).get(UserViewModel.class);
        super.onCreate(savedInstanceState);
        binding=ActivityAuthentificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sp.edit();


        String email=sp.getString("email","");
        String password=sp.getString("password","");
        if (!email.isEmpty() && !password.isEmpty()){
            binding.edtEmail.setText(email);
            binding.edtPassword.setText(password);
            binding.checkBox.setChecked(true);
        }else {
            binding.edtEmail.setText("");
            binding.edtPassword.setText("");
            binding.checkBox.setChecked(false);
        }


        // permissions

        ActivityResultLauncher<String> arl=registerForActivityResult(new ActivityResultContracts.RequestPermission()
                , new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {

                    }
                });
        arl.launch(Manifest.permission.INTERNET);
        arl.launch(Manifest.permission.POST_NOTIFICATIONS);
        arl.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        arl.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        arl.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checked=b;
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.edtEmail.getText().toString().isEmpty() && !binding.edtPassword.getText().toString().isEmpty()){
                    if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString()).matches())
                        binding.edtEmail.setError(getResources().getString(R.string.valid_email));
                    else {
                    binding.progressAuth.setVisibility(View.VISIBLE);
                    binding.btnSignIn.setVisibility(View.GONE);
                    if (checked){
                        editor.putString("email",binding.edtEmail.getText().toString()).commit();
                        editor.putString("password",binding.edtPassword.getText().toString()).commit();
                    }else{
                        editor.putString("email","").commit();
                        editor.putString("password","").commit();
                    }
                    userViewModel.getLiveAuthenticate().setValue(null);
                        userViewModel.authenticate(new AuthRequest(binding.edtEmail.getText().toString(),
                                binding.edtPassword.getText().toString()));

                        userViewModel.getLiveAuthenticate().observe(AuthenticationActivity.this, new Observer<JwtToken>() {
                            @Override
                            public void onChanged(JwtToken jwtToken) {
                                if (jwtToken != null && jwtToken.getUserResponse()!=null && jwtToken.getToken()!=null) {
                                    if (!jwtToken.getToken().isEmpty())
                                        editor.putString(BuildConfig.TOKEN, jwtToken.getToken()).commit();
                                    if (jwtToken.getUserResponse().getStatus()!=null && jwtToken.getUserResponse().getStatus().equals("change password")) {
                                        Intent intent1=new Intent(AuthenticationActivity.this, ChangePasswordActivity.class);
                                        intent1.putExtra("userId",jwtToken.getUserResponse().getId());
                                        startActivity(intent1);
                                    } else {
                                        if (jwtToken.getUserResponse().getRole().equals("admin"))
                                            intent = new Intent(AuthenticationActivity.this, AdminActivity.class);
                                        else
                                            intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                                        intent.putExtra("userId", jwtToken.getUserResponse().getId());
                                        intent.putExtra("structure_id", jwtToken.getUserResponse().getStructure_id());
                                        if (sp.getString(BuildConfig.TOKEN, null) != null)
                                            startActivity(intent);
                                    }
                                }
                                else {
                                    if (jwtToken!=null && jwtToken.getToken().equals("JwtTokenError")){
                                        binding.progressAuth.setVisibility(View.GONE);
                                        binding.btnSignIn.setVisibility(View.VISIBLE);

                                        if (!isShown) {
                                            ErrorFragment errorFragment = new ErrorFragment();
                                            errorFragment.show(getSupportFragmentManager(), "error_fragment");
                                            isShown=true;
                                        }
                                    }

                                }
                            }

                        });
                    }
                }else{
                    Toast.makeText(getBaseContext(), "fill the void", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.putString(BuildConfig.TOKEN,null).commit();

    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.progressAuth.setVisibility(View.GONE);
        binding.btnSignIn.setVisibility(View.VISIBLE);
    }



}