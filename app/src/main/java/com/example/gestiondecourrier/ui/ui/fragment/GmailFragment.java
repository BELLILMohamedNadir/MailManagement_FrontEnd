package com.example.gestiondecourrier.ui.ui.fragment;


import static com.example.gestiondecourrier.ui.ui.MainActivity.bitmap;
import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;
import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentGmailBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchBySubject;
import com.example.gestiondecourrier.ui.pojo.GmailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewGmail;
import com.example.gestiondecourrier.ui.ui.viewmodel.GmailViewModel;

import java.util.ArrayList;
import java.util.List;


public class GmailFragment extends Fragment {

    FragmentGmailBinding binding;
    RecyclerViewGmail recyclerViewGmail;
    List<GmailResponse> data;
    Bundle bundle;
    SearchBySubject filter;
    Feature feature=new Feature();

    GmailViewModel gmailViewModel=new GmailViewModel();
    public GmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        gmailViewModel= ViewModelProviders.of(requireActivity()).get(GmailViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentGmailBinding.inflate(inflater,container,false);

        feature.actionBarBehavior(binding.imgMenuSendWithEmail,binding.imgUserSendWithEmail);


        bundle=new Bundle();
        data=new ArrayList<>();
        recyclerViewGmail=new RecyclerViewGmail(data, new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("gmailPosition",position);
                navController.navigate(R.id.gmailDetailsFragment,bundle);
            }
        });
        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserSendWithEmail);

        binding.rvSendWithEmail.setAdapter(recyclerViewGmail);
        binding.rvSendWithEmail.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvSendWithEmail.setHasFixedSize(true);

        //refresh layout
        feature.refreshOptions(binding.refreshGmail,binding.rvSendWithEmail);

        binding.refreshGmail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGmail(userDetails.getId());
                binding.refreshGmail.setRefreshing(false);
            }
        });

        filter=new SearchBySubject(recyclerViewGmail,data);
        binding.edtSearchSendWithEmail.addTextChangedListener(new TextWatcher() {
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

        getGmail(userDetails.getId());

        return binding.getRoot();
    }

    private void getGmail(long id){
        gmailViewModel.getGmail(id);
        gmailViewModel.getLiveGetGmail().observe(getViewLifecycleOwner(), new Observer<List<GmailResponse>>() {
            @Override
            public void onChanged(List<GmailResponse> gmailResponses) {
                binding.shimmerGmail.stopShimmer();
                binding.shimmerGmail.setVisibility(View.GONE);
                if (gmailResponses!=null && !gmailResponses.isEmpty()){
                    for (int i=0;i<gmailResponses.size();i++)
                        gmailResponses.get(i).setPositionInTheViewModel(i);
                    filter.setOriginalList(gmailResponses);
                    binding.relativeGmail.setVisibility(View.GONE);
                    binding.rvSendWithEmail.setVisibility(View.VISIBLE);
                    recyclerViewGmail.setData(gmailResponses);
                }else{
                    binding.relativeGmail.setVisibility(View.VISIBLE);
                    binding.rvSendWithEmail.setVisibility(View.GONE);
                }
            }
        });
    }

}