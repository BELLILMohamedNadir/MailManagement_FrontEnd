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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentArchiveBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsNoStructure;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;

import java.util.ArrayList;
import java.util.List;


public class ArchiveFragment extends Fragment {

    FragmentArchiveBinding binding;
    RecyclerViewHome recyclerViewHome;
    Bundle bundle;
    ArrayList<MailResponse> data;
    MailViewModel mailViewModel=new MailViewModel();
    Feature feature=new Feature();
    SearchWithOptionsNoStructure filter;
    public ArchiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mailViewModel= ViewModelProviders.of(requireActivity()).get(MailViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentArchiveBinding.inflate(inflater,container,false);

        feature.initializeCategorySpinner(binding.spinnerCategoriesArchive,requireActivity());
        feature.actionBarBehavior(binding.imgMenuArchive,binding.imgUserArchive);

        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserArchive);

        bundle=new Bundle();
        //Recycler View
        data=new ArrayList<>();
        recyclerViewHome=new RecyclerViewHome(data,binding.relativeArchive,binding.rvEmailArchive,"archive", new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("mailPosition",position);
                bundle.putString("mailSource","archive");
                navController.navigate(R.id.fileFragment,bundle);
            }
        },requireActivity());
        binding.rvEmailArchive.setAdapter(recyclerViewHome);
        binding.rvEmailArchive.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvEmailArchive.setHasFixedSize(true);

        filter=new SearchWithOptionsNoStructure(new ArrayList<>(),recyclerViewHome);
        feature.search(filter,binding.edtSearchArchive);
        feature.getInfoFromOptionLayout(requireActivity(),binding.cardEntryDateArchive,binding.cardDepartureDateArchive
                ,binding.spinnerCategoriesArchive,filter);


        //refresh layout
        feature.refreshOptions(binding.refreshArchive,binding.rvEmailArchive);

        binding.refreshArchive.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMails(userDetails.getStructure_id());
                binding.refreshArchive.setRefreshing(false);
            }
        });


        binding.relativeFilterArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.hsArchive.getVisibility()==View.VISIBLE) {
                    binding.hsArchive.setVisibility(View.GONE);
                    binding.txtFilterArchive.setVisibility(View.GONE);
                }else {
                    binding.hsArchive.setVisibility(View.VISIBLE);
                    binding.txtFilterArchive.setVisibility(View.VISIBLE);
                }
            }
        });

        getMails(userDetails.getStructure_id());

        return binding.getRoot();
    }

    private void getMails(long id){

        mailViewModel.archiveMails(id,userDetails.getId(),userDetails.getStructureDesignation());
        mailViewModel.getLiveArchiveMails().observe(getViewLifecycleOwner(), new Observer<List<MailResponse>>() {
            @Override
            public void onChanged(List<MailResponse> mailResponses) {
                binding.shimmerArchive.stopShimmer();
                binding.shimmerArchive.setVisibility(View.GONE);
                if (mailResponses != null && !mailResponses.isEmpty()) {
                    for (int i=0;i<mailResponses.size();i++)
                        mailResponses.get(i).setPlaceInTheViewModel(i);

                    filter.setOriginalList(mailResponses);
                    binding.relativeArchive.setVisibility(View.GONE);
                    binding.rvEmailArchive.setVisibility(View.VISIBLE);
                    recyclerViewHome.setData(mailResponses);

                } else {
                    binding.relativeArchive.setVisibility(View.VISIBLE);
                    binding.rvEmailArchive.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (filter!=null){
            filter.setCategory("");
            filter.setEntryDate("");
            filter.setDepartureDate("");
        }
    }
}