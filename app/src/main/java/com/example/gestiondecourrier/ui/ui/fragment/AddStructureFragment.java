package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AdminActivity.navControllerAdmin;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.navigationView;

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
import com.example.gestiondecourrier.databinding.FragmentAddStructureBinding;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class AddStructureFragment extends Fragment {

    FragmentAddStructureBinding binding;

    StructureViewModel structureViewModel=new StructureViewModel();
    boolean clicked=false;
    public AddStructureFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        structureViewModel= ViewModelProviders.of(requireActivity()).get(StructureViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddStructureBinding.inflate(inflater,container,false);

        navigationView.observe(getViewLifecycleOwner(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        binding.imgCheckAddStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked) {
                    if (!binding.edtMotherStructure.validateResult() && !binding.edtMotherStructure.getEditText().getText().toString().isEmpty()){
                        Toast.makeText(requireActivity(), "enter an existing structure", Toast.LENGTH_SHORT).show();
                        enableComponent(true);
                        binding.progressAddStructure.setVisibility(View.GONE);
                        binding.imgCheckAddStructure.setVisibility(View.VISIBLE);
                    }else{
                        binding.imgCheckAddStructure.setVisibility(View.GONE);
                        binding.progressAddStructure.setVisibility(View.VISIBLE);
                        enableComponent(false);
                        clicked=true;
                        structureViewModel.addStructure(new Structure(binding.edtDesignation.getText().toString(),
                                binding.edtCode.getText().toString(), binding.edtMotherStructure.getEditText().getText().toString()));
                        structureViewModel.getLiveAddStructure().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean) {
                                    Toast.makeText(requireActivity(), "created", Toast.LENGTH_SHORT).show();
                                    navControllerAdmin.popBackStack(R.id.structureFragment,false);
                                } else {
                                    enableComponent(true);
                                    binding.progressAddStructure.setVisibility(View.GONE);
                                    binding.imgCheckAddStructure.setVisibility(View.VISIBLE);
                                    clicked=false;
                                    Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                }
            }

        });

        binding.imgBackAddStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navControllerAdmin.popBackStack(R.id.structureFragment,false);
            }
        });

        getStructure();

        return binding.getRoot();
    }

    private void getStructure(){
        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(getViewLifecycleOwner(), new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                List<String> list=new ArrayList<>();
                for (int i=0;i<structures.size();i++)
                    list.add(structures.get(i).getDesignation());
                binding.edtMotherStructure.setList(list);
            }
        });
    }

    private void enableComponent(boolean b){
        binding.edtMotherStructure.setEnabled(b);
        binding.edtCode.setEnabled(b);
        binding.edtDesignation.setEnabled(b);
    }

}