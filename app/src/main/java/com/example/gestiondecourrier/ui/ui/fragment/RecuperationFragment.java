package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.fragment.EmployeeAttendanceFragment.id;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentRecuperationBinding;
import com.example.gestiondecourrier.ui.ui.viewmodel.AttendanceViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.EmployeeViewModel;

public class RecuperationFragment extends DialogFragment {


    FragmentRecuperationBinding binding;
    long att1=0;
    EmployeeViewModel employeeViewModel=new EmployeeViewModel();
    boolean clicked=false;

    public RecuperationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        employeeViewModel= ViewModelProviders.of(requireActivity()).get(EmployeeViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!isAdded())
            return null;
        // Inflate the layout for this fragment
        binding=FragmentRecuperationBinding.inflate(inflater,container,false);
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtRecovery.getText().toString().trim().equals(""))
                    att1=0;
                else
                    att1=Long.parseLong(binding.edtRecovery.getText().toString().trim());
                binding.edtRecovery.setText(String.valueOf(++att1));
            }
        });
        binding.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtRecovery.getText().toString().trim().equals(""))
                    att1=0;
                else
                    att1=Long.parseLong(binding.edtRecovery.getText().toString().trim());
                binding.edtRecovery.setText(String.valueOf(--att1));
            }
        });

        binding.imgSendAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.edtRecovery.getText().toString().isEmpty() && Integer.parseInt(binding.edtRecovery.getText().toString())>0 && id != -1 && !clicked){
                    clicked=true;
                    employeeViewModel.updateRecuperation(id, Integer.parseInt(binding.edtRecovery.getText().toString()));
                }else
                    if (Integer.parseInt(binding.edtRecovery.getText().toString())<=0)
                        Toast.makeText(requireActivity(), "add number greater than 0", Toast.LENGTH_SHORT).show();
                employeeViewModel.getLiveUpdateRecuperation().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean){
                            Toast.makeText(requireActivity(), "updated", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else {
                            clicked=false;
                            Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return binding.getRoot();
    }
}