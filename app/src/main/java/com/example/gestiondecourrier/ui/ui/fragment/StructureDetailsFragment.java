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
import com.example.gestiondecourrier.databinding.FragmentStructureDetailsBinding;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class StructureDetailsFragment extends Fragment {

    FragmentStructureDetailsBinding binding;
    Structure structure=null;
    StructureViewModel structureViewModel=new StructureViewModel();
    int position=-1;
    boolean first=true;
    Structure s=null;
    List<Structure> struct;
    public StructureDetailsFragment() {
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
        binding=FragmentStructureDetailsBinding.inflate(inflater,container,false);
        navigationView.observe(getViewLifecycleOwner(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        enableComponent(false);
        struct=new ArrayList<>();
        if (getArguments()!=null){
            position=getArguments().getInt("StructurePosition",-1);
            if (position!=-1 && structureViewModel.getLiveGetStructures().getValue()!=null) {
                structure = structureViewModel.getLiveGetStructures().getValue().get(position);
                binding.edtDesignationDetails.setText(structure.getDesignation());
                binding.edtCodeDetails.setText(structure.getCode());
                if (!structure.getMotherStructure().isEmpty())
                    binding.edtMotherStructureDetails.getEditText().setText(structure.getMotherStructure());
                else
                    binding.edtMotherStructureDetails.getEditText().setHint("no");
            }

        }
        binding.imgBackDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navControllerAdmin.popBackStack(R.id.structureFragment,false);
            }
        });
        binding.imgCheckUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (first){
                    binding.imgCheckUpdateDetails.setImageResource(R.drawable.ic_check);
                    enableComponent(true);
                    first=false;
                }else{
                    if (!checked()) {
                        if (!binding.edtMotherStructureDetails.validateResult() && !binding.edtMotherStructureDetails.getEditText().getText().toString().isEmpty()){
                            Toast.makeText(requireActivity(), "enter an existing structure", Toast.LENGTH_SHORT).show();
                            binding.imgCheckUpdateDetails.setVisibility(View.VISIBLE);
                            binding.progressStructure.setVisibility(View.GONE);
                            enableComponent(true);
                        }else{
                            s = new Structure(binding.edtDesignationDetails.getText().toString()
                                    , binding.edtCodeDetails.getText().toString(), binding.edtMotherStructureDetails.getEditText().getText().toString());

                            binding.imgCheckUpdateDetails.setVisibility(View.GONE);
                            binding.progressStructure.setVisibility(View.VISIBLE);
                            enableComponent(false);
                            structureViewModel.updateStructure(structure.getId(), s);
                            structureViewModel.getLiveUpdateStructure().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean aBoolean) {
                                    if (aBoolean) {
                                        Toast.makeText(requireActivity(), "updated", Toast.LENGTH_SHORT).show();
                                        navControllerAdmin.popBackStack(R.id.structureFragment, false);
                                    } else {
                                        binding.imgCheckUpdateDetails.setVisibility(View.VISIBLE);
                                        binding.progressStructure.setVisibility(View.GONE);
                                        enableComponent(true);
                                        Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    } else
                        Toast.makeText(requireActivity(), "You didn't change anything", Toast.LENGTH_SHORT).show();
                }

            }
        });


        getStructure();

        return binding.getRoot();
    }
    private void enableComponent(boolean b){
        binding.edtCodeDetails.setEnabled(b);
        binding.edtDesignationDetails.setEnabled(b);
        binding.edtMotherStructureDetails.getEditText().setEnabled(b);
    }

    private void getStructure(){
        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(getViewLifecycleOwner(), new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                List<String> list=new ArrayList<>();
                for (int i=0;i<structures.size();i++) {
                    list.add(structures.get(i).getDesignation());
                    struct.add(structures.get(i));
                }
                binding.edtMotherStructureDetails.setList(list);
            }
        });

    }
    private boolean checked(){
        return  binding.edtDesignationDetails.getText().toString().equals(structure.getDesignation())
                && binding.edtCodeDetails.getText().toString().equals(structure.getCode())
                && binding.edtMotherStructureDetails.getEditText().getText().toString().equals(structure.getMotherStructure());
    }
}