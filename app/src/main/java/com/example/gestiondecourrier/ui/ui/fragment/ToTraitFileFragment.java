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
import android.widget.ArrayAdapter;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentToTraitFileBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsReceivedMails;
import com.example.gestiondecourrier.ui.ui.viewmodel.CategoryViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;

import java.util.ArrayList;
import java.util.List;


public class ToTraitFileFragment extends Fragment {

   FragmentToTraitFileBinding binding;
   RecyclerViewHome recyclerViewHome;
   List<MailResponse> data;
   Bundle bundle;
   MailViewModel mailViewModel=new MailViewModel();
   Feature feature=new Feature();
   SearchWithOptionsReceivedMails filter;
   CategoryViewModel categoryViewModel=new CategoryViewModel();
    public ToTraitFileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mailViewModel= ViewModelProviders.of(requireActivity()).get(MailViewModel.class);
        categoryViewModel= ViewModelProviders.of(requireActivity()).get(CategoryViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentToTraitFileBinding.inflate(inflater,container,false);

        feature.initializeStructureSpinner(binding.spinnerStructuresToTraitFile,requireActivity());
        feature.actionBarBehavior(binding.imgMenuToTraitFile,binding.imgUserToTraitFile);
        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserToTraitFile);

        bundle=new Bundle();
        data=new ArrayList<>();

        recyclerViewHome=new RecyclerViewHome(data,binding.relativeToTraitFile,binding.rvToTrait,"to_trait_fragment", new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("mailPosition",position);
                bundle.putString("mailSource","toTrait");
                navController.navigate(R.id.fileFragment,bundle);
            }
        },requireActivity());
        binding.rvToTrait.setAdapter(recyclerViewHome);
        binding.rvToTrait.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvToTrait.setHasFixedSize(true);

        filter=new SearchWithOptionsReceivedMails(new ArrayList<>(),recyclerViewHome);
        feature.search(filter,binding.edtSearchToTraitFile);
        feature.getInfoFromOptionLayout(requireActivity(),binding.cardEntryDateToTraitFile,binding.cardDepartureDateToTraitFile
                ,binding.spinnerStructuresToTraitFile,binding.spinnerCategoriesToTraitFile,filter);


        //refresh layout
        feature.refreshOptions(binding.refreshToTraitMails,binding.rvToTrait);

        binding.refreshToTraitMails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getToTraitMails(userDetails.getStructureDesignation());
                binding.refreshToTraitMails.setRefreshing(false);
            }
        });


        binding.relativeFilterToTrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.hsToTrait.getVisibility()==View.VISIBLE) {
                    binding.hsToTrait.setVisibility(View.GONE);
                    binding.txtFilterToTrait.setVisibility(View.GONE);
                }else {
                    binding.hsToTrait.setVisibility(View.VISIBLE);
                    binding.txtFilterToTrait.setVisibility(View.VISIBLE);
                }
            }
        });
        getToTraitMails(userDetails.getStructureDesignation());
        return binding.getRoot();
    }

    private void getToTraitMails(String structure){
        mailViewModel.toTraitMails(structure,userDetails.getId());
        mailViewModel.getLiveToTraitMails().observe(getViewLifecycleOwner(), new Observer<List<MailResponse>>() {
            @Override
            public void onChanged(List<MailResponse> pdfs) {
                binding.shimmerToTrait.stopShimmer();
                binding.shimmerToTrait.setVisibility(View.GONE);
                if (pdfs != null && !pdfs.isEmpty()) {
                    for (int i=0;i<pdfs.size();i++)
                        pdfs.get(i).setPlaceInTheViewModel(i);
                    filter.setOriginalList(pdfs);
                    binding.relativeToTraitFile.setVisibility(View.GONE);
                    binding.rvToTrait.setVisibility(View.VISIBLE);
                    recyclerViewHome.setData(pdfs);
                } else {
                    binding.relativeToTraitFile.setVisibility(View.VISIBLE);
                    binding.rvToTrait.setVisibility(View.GONE);
                }
            }
        });
        getCategories();
    }

    private void getCategories(){
        categoryViewModel.getArrivedCategoryDesignation(userDetails.getStructure_id());
        categoryViewModel.getLiveGetArrivedCategoryDesignation().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (strings!=null){
                    List<String> list=new ArrayList<>();
                    list.add("Catégorie");
                    list.addAll(strings);
                    ArrayAdapter<String> adapterStructure = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, list);
                    adapterStructure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerCategoriesToTraitFile.setAdapter(adapterStructure);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (filter!=null){
            filter.setCategory("");
            filter.setStructure("");
            filter.setEntryDate("");
            filter.setDepartureDate("");
        }
    }
}