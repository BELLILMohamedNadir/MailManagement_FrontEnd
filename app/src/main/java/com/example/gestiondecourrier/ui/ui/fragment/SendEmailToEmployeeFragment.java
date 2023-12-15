package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AdminActivity.navControllerAdmin;
import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentSendEmailToEmployeeBinding;
import com.example.gestiondecourrier.ui.pojo.Email;
import com.example.gestiondecourrier.ui.ui.viewmodel.EmailViewModel;


public class SendEmailToEmployeeFragment extends Fragment {

    FragmentSendEmailToEmployeeBinding binding;
    EmailViewModel emailViewModel=new EmailViewModel();
    String email="",source="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        emailViewModel= ViewModelProviders.of(requireActivity()).get(EmailViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentSendEmailToEmployeeBinding.inflate(inflater,container,false);

        if (getArguments()!=null && getArguments().getString("email")!=null && getArguments().getString("source")!=null) {
            email=getArguments().getString("email","");
            source=getArguments().getString("source","");
        }

        binding.imgBackEmailGmail.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view) {
            getParentFragmentManager().popBackStack();
        }
        });

        binding.imgSendEmailGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.isEmpty() && !binding.edtSubjectEmailGmail.getText().toString().isEmpty()
                && !binding.edtComposeEmailGmail.getText().toString().isEmpty()) {
                    binding.imgSendEmailGmail.setVisibility(View.GONE);
                    binding.progressAddEmailToEmployee.setVisibility(View.VISIBLE);
                    enableComponent(false);
                    emailViewModel.sendEmail(new Email(email, binding.edtSubjectEmailGmail.getText().toString(),
                            binding.edtComposeEmailGmail.getText().toString()));
                } else
                    Toast.makeText(requireActivity(), "fill the void", Toast.LENGTH_SHORT).show();
            }
        });

        emailViewModel.getLiveSendEmail().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    if (source.equals("employee"))
                        navController.popBackStack(R.id.employeeFragment, false);
                    else
                        if (source.equals("user"))
                            navControllerAdmin.popBackStack(R.id.userFragment,false);
                        else
                            navController.popBackStack(R.id.contactFragment, false);
                }else {
                    binding.imgSendEmailGmail.setVisibility(View.VISIBLE);
                    binding.progressAddEmailToEmployee.setVisibility(View.GONE);
                    enableComponent(true);
                    Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.edtToEmailGmail.setText(email);
        return binding.getRoot();
    }
    private void enableComponent(boolean b){
        binding.edtToEmailGmail.setEnabled(b);
        binding.edtComposeEmailGmail.setEnabled(b);
        binding.edtSubjectEmailGmail.setEnabled(b);
    }
}