package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;
import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.gestiondecourrier.databinding.FragmentAddEmployeeBinding;
import com.example.gestiondecourrier.ui.pojo.Employee;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.ui.viewmodel.EmployeeViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;

import java.util.ArrayList;
import java.util.List;


public class AddEmployeeFragment extends Fragment {


    FragmentAddEmployeeBinding binding;
    EmployeeViewModel employeeViewModel=new EmployeeViewModel();
    StructureViewModel structureViewModel=new StructureViewModel();
    boolean clicked=false;

    public AddEmployeeFragment() {
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
        binding=FragmentAddEmployeeBinding.inflate(inflater,container,false);

        binding.edtStructureEmployee.getEditText().setText(userDetails.getStructureDesignation());

        binding.btnSaveEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checked() && !clicked) {
                    if (!binding.edtStructureEmployee.validateResult()){
                        Toast.makeText(requireActivity(), "enter an existing structure", Toast.LENGTH_SHORT).show();
                    }else{
                        clicked=true;
                        employeeViewModel.addEmployees(new Employee(0,binding.edtNameEmployee.getText().toString()
                                , binding.edtFirstNameEmployee.getText().toString(), binding.edtRegistrationKeyEmployee.getText().toString()
                                , new Structure(userDetails.getStructure_id()), binding.edtEmailEmployee.getText().toString()));
                        employeeViewModel.getLiveAddEmployees().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean)
                                    navController.popBackStack(R.id.employeeFragment, false);
                                else {
                                    Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                                    clicked = false;
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

        binding.btnCancelEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.employeeFragment,false);
            }
        });

        getStructure();

        return binding.getRoot();
    }

    private boolean checked() {
        return binding.edtNameEmployee.getText().toString().isEmpty()
                ||binding.edtFirstNameEmployee.getText().toString().isEmpty()
                ||binding.edtRegistrationKeyEmployee.getText().toString().isEmpty()
                ||binding.edtStructureEmployee.getEditText().getText().toString().isEmpty()
                ||binding.edtEmailEmployee.getText().toString().isEmpty();
    }

    void getStructure(){
        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(getViewLifecycleOwner(), new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                List<String> list=new ArrayList<>();
                for (int i=0;i<structures.size();i++)
                    list.add(structures.get(i).getDesignation());
                binding.edtStructureEmployee.setList(list);
            }
        });

    }


}