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
import com.example.gestiondecourrier.databinding.FragmentCategoryDetailsBinding;
import com.example.gestiondecourrier.ui.pojo.Category;
import com.example.gestiondecourrier.ui.pojo.CategoryResponse;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.CategoryViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class CategoryDetailsFragment extends Fragment {

    FragmentCategoryDetailsBinding binding;
    CategoryResponse category;
    Category c=null;
    boolean first=true;
    CategoryViewModel categoryViewModel=new CategoryViewModel();
    int position=-1;
    public CategoryDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        categoryViewModel= ViewModelProviders.of(requireActivity()).get(CategoryViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentCategoryDetailsBinding.inflate(inflater,container,false);
        navigationView.observe(getViewLifecycleOwner(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        enableComponent(false);

        if (getArguments()!=null){
            position=getArguments().getInt("categoryPosition",-1);
            if (position!=-1 && categoryViewModel.getLiveGetCategory().getValue()!=null) {
                category = categoryViewModel.getLiveGetCategory().getValue().get(position);
                binding.edtCodeCategoryDetailsDetails.setText(category.getCode());
                binding.edtDesignationCategoryDetails.setText(category.getDesignation());
                binding.edtNumberCategoryDetails.setText(category.getNumber()+"");
                binding.txtTypeCategoryDetails.setText(category.getType());
                binding.txtCategoryDetails.setText(category.getDesignation_struct());
            }
        }
        binding.imgCheckUpdateCategoryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (first){
                    binding.imgCheckUpdateCategoryDetails.setImageResource(R.drawable.ic_check);
                    enableComponent(true);
                    first = false;
                }else{
                    if (!checked()){
                        binding.imgCheckUpdateCategoryDetails.setVisibility(View.GONE);
                        binding.progressCategory.setVisibility(View.VISIBLE);
                        enableComponent(false);
                        c=new Category(binding.edtDesignationCategoryDetails.getText().toString(),
                                binding.edtCodeCategoryDetailsDetails.getText().toString());
                        categoryViewModel.updateCategory(category.getId(), c);
                        categoryViewModel.getLiveUpdateCategory().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean){
                                    Toast.makeText(requireActivity(), "updated", Toast.LENGTH_SHORT).show();
                                    navControllerAdmin.popBackStack(R.id.categoryFragment,false);
                                }else {
                                    binding.imgCheckUpdateCategoryDetails.setVisibility(View.VISIBLE);
                                    binding.progressCategory.setVisibility(View.GONE);
                                    enableComponent(true);
                                    Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else
                        Toast.makeText(requireActivity(), "you didn't change anything", Toast.LENGTH_SHORT).show();

                }

            }
        });

        binding.imgBackUpdateCategoryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navControllerAdmin.popBackStack(R.id.categoryFragment,false);
            }
        });
        return binding.getRoot();
    }
    private void enableComponent(boolean b){
        binding.edtCodeCategoryDetailsDetails.setEnabled(b);
        binding.edtDesignationCategoryDetails.setEnabled(b);
    }

    private boolean checked(){
        return binding.edtDesignationCategoryDetails.getText().toString().equals(category.getDesignation())
                && binding.edtCodeCategoryDetailsDetails.getText().toString().equals(category.getCode());
    }
}