package com.example.gestiondecourrier.ui.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gestiondecourrier.databinding.ActivityChangePasswordBinding;
import com.example.gestiondecourrier.ui.pojo.User;
import com.example.gestiondecourrier.ui.ui.viewmodel.UserViewModel;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;
    UserViewModel userViewModel;
    long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userViewModel= ViewModelProviders.of(ChangePasswordActivity.this).get(UserViewModel.class);

        super.onCreate(savedInstanceState);
        binding= ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent()!=null)
            userId=getIntent().getLongExtra("userId",-1);
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.edtChangePassword.getText().toString().isEmpty() &&
                        !binding.edtChangeConfirmPassword.getText().toString().isEmpty()){
                    if (userId!=-1 && binding.edtChangePassword.getText().toString().equals(binding.edtChangeConfirmPassword.getText().toString())){
                        binding.btnChangePassword.setVisibility(View.GONE);
                        binding.progressChangePassword.setVisibility(View.VISIBLE);
                        binding.edtChangePassword.setEnabled(false);
                        binding.edtChangeConfirmPassword.setEnabled(false);
                        userViewModel.updatePassword(userId,new User(binding.edtChangePassword.getText().toString()));
                    }
                    userViewModel.getLiveUpdatePassword().observe(ChangePasswordActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (aBoolean){
                                Toast.makeText(ChangePasswordActivity.this, "updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangePasswordActivity.this, AuthenticationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                binding.btnChangePassword.setVisibility(View.VISIBLE);
                                binding.progressChangePassword.setVisibility(View.GONE);
                                binding.edtChangePassword.setEnabled(true);
                                binding.edtChangeConfirmPassword.setEnabled(true);
                                Toast.makeText(ChangePasswordActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else
                    Toast.makeText(ChangePasswordActivity.this, "fill the void", Toast.LENGTH_SHORT).show();
            }
        });

    }
}