package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AdminActivity.adminDetails;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.liveBitmapAdmin;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.navControllerAdmin;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.navigationView;
import static com.example.gestiondecourrier.ui.ui.MainActivity.bitmap;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentCategoryBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchCategory;
import com.example.gestiondecourrier.ui.pojo.CategoryResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewCategory;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.CategoryViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment {

    FragmentCategoryBinding binding;
    RecyclerViewCategory recyclerViewCategory;
    Bundle bundle;
    List<CategoryResponse> data;
    SearchCategory filter;
    CategoryViewModel categoryViewModel=new CategoryViewModel();
    Feature feature=new Feature();
    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        categoryViewModel= ViewModelProviders.of(requireActivity()).get(CategoryViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding=FragmentCategoryBinding.inflate(inflater,container,false);

        bundle=new Bundle();
        data=new ArrayList<>();
        navigationView.observe(getViewLifecycleOwner(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });


        liveBitmapAdmin.observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if (bitmap!=null)
                    binding.imgUserHomeCategory.setImageBitmap(bitmap);
            }
        });

        adminDetails.observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse!=null)
                    feature.profilePicture(bitmap,userResponse.getName().charAt(0),binding.imgUserHomeCategory);

            }
        });

        recyclerViewCategory=new RecyclerViewCategory(data, new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("categoryPosition",position);
                navControllerAdmin.navigate(R.id.categoryDetailsFragment,bundle);
            }
        });
        binding.rvCategory.setAdapter(recyclerViewCategory);
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        binding.rvCategory.setHasFixedSize(true);

        //refresh layout
        feature.refreshOptions(binding.refreshCategory,binding.rvCategory);

        binding.refreshCategory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategories();
                binding.refreshCategory.setRefreshing(false);
            }
        });

        filter=new SearchCategory(recyclerViewCategory,data);

        binding.floatAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navControllerAdmin.navigate(R.id.addCategoryFragment);
            }
        });

        binding.edtSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter.filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getCategories();

        return binding.getRoot();
    }

    private void getCategories(){
        categoryViewModel.getCategories();
        categoryViewModel.getLiveGetCategory().observe(getViewLifecycleOwner(), new Observer<List<CategoryResponse>>() {
            @Override
            public void onChanged(List<CategoryResponse> categories) {
                binding.shimmerCategory.stopShimmer();
                binding.shimmerCategory.setVisibility(View.GONE);
                if (!categories.isEmpty()){
                    for (int i=0;i<categories.size();i++)
                        categories.get(i).setPositionInTheViewModel(i);
                    filter.setOriginalList(categories);
                    binding.relativeCategory.setVisibility(View.GONE);
                    binding.rvCategory.setVisibility(View.VISIBLE);
                    recyclerViewCategory.setData(categories);
                }else{
                    binding.relativeCategory.setVisibility(View.VISIBLE);
                    binding.rvCategory.setVisibility(View.GONE);
                }
            }
        });

    }

}