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
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
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
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentStructureBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.StructureResponse;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchStructure;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewStructure;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class StructureFragment extends Fragment {

    FragmentStructureBinding binding;
    RecyclerViewStructure recyclerViewStructure;
    List<StructureResponse> data;
    SearchStructure filter;
    StructureViewModel structureViewModel=new StructureViewModel();
    Feature feature=new Feature();

    public StructureFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        structureViewModel= ViewModelProviders.of(requireActivity()).get(StructureViewModel.class);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentStructureBinding.inflate(inflater,container,false);

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
                    binding.imgUserHomeStructure.setImageBitmap(bitmap);
            }
        });


        recyclerViewStructure=new RecyclerViewStructure(data, new OnClick() {
            @Override
            public void onClick(int position) {
                Bundle bundle=new Bundle();
                bundle.putInt("StructurePosition",position);
                navControllerAdmin.navigate(R.id.structureDetailsFragment,bundle);
            }
        });
        binding.rvStructure.setAdapter(recyclerViewStructure);
        binding.rvStructure.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        binding.rvStructure.setHasFixedSize(true);

        //refresh layout
        feature.refreshOptions(binding.refreshStructure,binding.rvStructure);

        binding.refreshStructure.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStructures();
                binding.refreshStructure.setRefreshing(false);
            }
        });

        adminDetails.observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse!=null)
                    feature.profilePicture(bitmap,userResponse.getName().charAt(0),binding.imgUserHomeStructure);

            }
        });
        filter=new SearchStructure(recyclerViewStructure,data);

        binding.floatAddStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navControllerAdmin.navigate(R.id.addStructureFragment);

            }
        });

        binding.edtSearchStructure.addTextChangedListener(new TextWatcher() {
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

        getStructures();

        return binding.getRoot();
    }
    private void getStructures(){
        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(getViewLifecycleOwner(), new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                binding.shimmerStructure.stopShimmer();
                binding.shimmerStructure.setVisibility(View.GONE);
                if (structures!=null && !structures.isEmpty()){

                    List<StructureResponse> structureResponses=new ArrayList<>();
                    for (int i=0;i<structures.size();i++){
                        Structure s=structures.get(i);
                        structureResponses.add(new StructureResponse(s.getId(),s.getDesignation(),s.getCode(),s.getMotherStructure(),i));
                    }
                    filter.setOriginalList(structureResponses);
                    binding.relativeStructure.setVisibility(View.GONE);
                    binding.rvStructure.setVisibility(View.VISIBLE);
                    recyclerViewStructure.setData(structureResponses);
                }else{
                    binding.relativeStructure.setVisibility(View.VISIBLE);
                    binding.rvStructure.setVisibility(View.GONE);
                }
            }
        });

    }
}