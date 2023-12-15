package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AdminActivity.navControllerAdmin;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.navigationView;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentAddCategoryBinding;
import com.example.gestiondecourrier.ui.pojo.Category;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.CategoryViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class AddCategoryFragment extends Fragment {

    FragmentAddCategoryBinding binding;
    String type="",structure="";
    List<Structure> struct;
    int id=0;
    CategoryViewModel categoryViewModel=new CategoryViewModel();
    StructureViewModel structureViewModel=new StructureViewModel();
    public AddCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        categoryViewModel= ViewModelProviders.of(requireActivity()).get(CategoryViewModel.class);
        structureViewModel=ViewModelProviders.of(requireActivity()).get(StructureViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddCategoryBinding.inflate(inflater,container,false);
        struct=new ArrayList<>();

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),R.array.document_type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeCategory.setAdapter(adapter);

        navigationView.observe(getViewLifecycleOwner(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        binding.spinnerTypeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type=(String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                type=(String) adapterView.getItemAtPosition(0);
            }
        });
        binding.spinnerStructureCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id=i;
                structure=(String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                structure=(String) adapterView.getItemAtPosition(0);
            }
        });

        binding.imgBackAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navControllerAdmin.popBackStack(R.id.categoryFragment,false);
            }
        });


        binding.imgCheckAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checked())
                    addCategory();
                else
                    Toast.makeText(requireActivity(), "fill the void", Toast.LENGTH_SHORT).show();
            }
        });

        getStructures();

        return binding.getRoot();
    }

    private boolean checked() {
        return !binding.edtDesignationCategory.getText().toString().isEmpty()
                && !binding.edtCodeCategory.getText().toString().isEmpty()
                && !binding.edtNumberCategory.getText().toString().isEmpty();
    }

    private void getStructures() {
        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(getViewLifecycleOwner(), new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                List<String> designations=new ArrayList<>();
                if (!structures.isEmpty()){
                    for (int i=0;i<structures.size();i++){
                        designations.add(structures.get(i).getDesignation());
                        struct.add(structures.get(i));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, designations);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerStructureCategory.setAdapter(adapter);
                }
            }
        });
    }

    public void addCategory(){
        enableComponent(false);
        binding.imgCheckAddCategory.setVisibility(View.GONE);
        binding.progressAddCategory.setVisibility(View.VISIBLE);
        Category category = new Category(type, binding.edtDesignationCategory.getText().toString()
                , binding.edtCodeCategory.getText().toString(), Long.parseLong(binding.edtNumberCategory.getText().toString()), new Structure(struct.get(id).getId()), 0);
        categoryViewModel.addCategory(category);
        categoryViewModel.getLiveAddCategory().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(requireActivity(), "created", Toast.LENGTH_SHORT).show();
                    navControllerAdmin.popBackStack(R.id.categoryFragment, false);
                } else {
                    enableComponent(true);
                    binding.imgCheckAddCategory.setVisibility(View.VISIBLE);
                    binding.progressAddCategory.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void enableComponent(boolean b) {
        binding.edtNumberCategory.setEnabled(b);
        binding.edtDesignationCategory.setEnabled(b);
        binding.edtCodeCategory.setEnabled(b);
        binding.spinnerStructureCategory.setEnabled(b);
        binding.spinnerTypeCategory.setEnabled(b);
    }
}