package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;
import static com.example.gestiondecourrier.ui.ui.MainActivity.structure;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.gestiondecourrier.databinding.FragmentEmployeeDetailsBinding;
import com.example.gestiondecourrier.ui.pojo.Employee;
import com.example.gestiondecourrier.ui.pojo.EmployeeResponse;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.ui.viewmodel.EmployeeViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;

import java.util.ArrayList;
import java.util.List;


public class EmployeeDetailsFragment extends Fragment {


    FragmentEmployeeDetailsBinding binding;
    EmployeeResponse employee;
    EmployeeViewModel employeeViewModel=new EmployeeViewModel();
    StructureViewModel structureViewModel=new StructureViewModel();
    int position=-1;
    List<Structure> struct;
    boolean clicked=false;
    public EmployeeDetailsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        employeeViewModel= ViewModelProviders.of(requireActivity()).get(EmployeeViewModel.class);
        structureViewModel= ViewModelProviders.of(requireActivity()).get(StructureViewModel.class);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentEmployeeDetailsBinding.inflate(inflater,container,false);

        struct=new ArrayList<>();
        if (getArguments()!=null){
            position=getArguments().getInt("employeePosition",-1);
            if (position!=-1 && employeeViewModel.getLiveGetEmployees().getValue()!=null) {
                employee = employeeViewModel.getLiveGetEmployees().getValue().get(position);
                binding.edtNameEmployeeDetails.setText(employee.getName());
                binding.edtFirstNameEmployeeDetails.setText(employee.getFirstName());
                binding.edtRegistrationKeyEmployeeDetails.setText(employee.getRegistrationKey());
                binding.edtEmailEmployeeDetails.setText(employee.getEmail());
                binding.edtStructureEmployeeDetails.getEditText().setText(employee.getStructure());

                binding.btnUpdateEmployeeDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!checked() && !clicked) {
                            if (!binding.edtStructureEmployeeDetails.validateResult()){
                                Toast.makeText(requireActivity(), "enter an existing structure", Toast.LENGTH_SHORT).show();
                            }else{
                                clicked=true;
                                employeeViewModel.updateEmployee(employee.getId(),new Employee(employee.getId(),0
                                        , binding.edtNameEmployeeDetails.getText().toString(), binding.edtFirstNameEmployeeDetails.getText().toString()
                                        , binding.edtRegistrationKeyEmployeeDetails.getText().toString(), new Structure(struct.get(binding.edtStructureEmployeeDetails.getPosition()).getId()), binding.edtEmailEmployeeDetails.getText().toString()));

                                employeeViewModel.getLiveUpdateEmployee().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                    @Override
                                    public void onChanged(Boolean aBoolean) {
                                        if (aBoolean) {
                                            Toast.makeText(requireActivity(), "updated", Toast.LENGTH_SHORT).show();
                                            navController.popBackStack(R.id.employeeFragment, false);
                                        } else {
                                            clicked=false;
                                            Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                        }else
                            if (checked())
                                Toast.makeText(requireActivity(), "fill the void", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(requireActivity(), "wait", Toast.LENGTH_SHORT).show();

                    }
                });

                binding.btnCancelEmployeeDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navController.popBackStack(R.id.employeeFragment, false);
                    }
                });
            }
        }

        getStructure();

        return binding.getRoot();
    }
    void getStructure(){
        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(getViewLifecycleOwner(), new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                struct.addAll(structures);
                List<String> list=new ArrayList<>();
                for (int i=0;i<structures.size();i++)
                    list.add(structures.get(i).getDesignation());
                binding.edtStructureEmployeeDetails.setList(list);
            }
        });

    }
    private boolean checked() {
        return binding.edtNameEmployeeDetails.getText().toString().isEmpty()
                ||binding.edtFirstNameEmployeeDetails.getText().toString().isEmpty()
                ||binding.edtRegistrationKeyEmployeeDetails.getText().toString().isEmpty()
                ||binding.edtStructureEmployeeDetails.getEditText().getText().toString().isEmpty()
                ||binding.edtEmailEmployeeDetails.getText().toString().isEmpty();
    }
}