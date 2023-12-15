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
import com.example.gestiondecourrier.databinding.FragmentUserBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewUser;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchUser;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {

    FragmentUserBinding binding;
    RecyclerViewUser recyclerViewUser;
    List<UserResponse> data;
    SearchUser filter;
    UserViewModel userViewModel=new UserViewModel();
    Bundle bundle;
    Feature feature=new Feature();

    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userViewModel= ViewModelProviders.of(requireActivity()).get(UserViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentUserBinding.inflate(inflater,container,false);
        data=new ArrayList<>();
        bundle=new Bundle();

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
                    binding.imgUserHomeUser.setImageBitmap(bitmap);
            }
        });

        adminDetails.observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse!=null)
                    feature.profilePicture(bitmap,userResponse.getName().charAt(0),binding.imgUserHomeUser);
            }
        });

        recyclerViewUser=new RecyclerViewUser(data, new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("userPosition",position);
                navControllerAdmin.navigate(R.id.userDetailsFragment,bundle);
            }
        });
        binding.rvUsers.setAdapter(recyclerViewUser);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        binding.rvUsers.setHasFixedSize(true);

        //refresh layout
        feature.refreshOptions(binding.refreshUser,binding.rvUsers);

        binding.refreshUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                binding.refreshUser.setRefreshing(false);
            }
        });


        filter=new SearchUser(recyclerViewUser,data);
        binding.floatAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navControllerAdmin.navigate(R.id.addUserFragment);
            }
        });

        binding.edtSearchUser.addTextChangedListener(new TextWatcher() {
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

        getUsers();

        return binding.getRoot();
    }
    private void getUsers(){
        userViewModel.getUsers();
        userViewModel.getLiveGetUsers().observe(getViewLifecycleOwner(), new Observer<List<UserResponse>>() {
            @Override
            public void onChanged(List<UserResponse> users) {
                binding.shimmerUser.stopShimmer();
                binding.shimmerUser.setVisibility(View.GONE);
                if (users!=null && !users.isEmpty()){
                    for (int i=0;i<users.size();i++)
                        users.get(i).setPositionInTheViewModel(i);
                    filter.setOriginalList(users);
                    binding.relativeUser.setVisibility(View.GONE);
                    binding.rvUsers.setVisibility(View.VISIBLE);
                    recyclerViewUser.setData(users);
                }else{
                    binding.relativeUser.setVisibility(View.VISIBLE);
                    binding.rvUsers.setVisibility(View.GONE);
                }

            }
        });

    }
}