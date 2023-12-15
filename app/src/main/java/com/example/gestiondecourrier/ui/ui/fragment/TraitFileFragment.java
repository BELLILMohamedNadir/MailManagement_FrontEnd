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
import com.example.gestiondecourrier.databinding.FragmentTraitFileBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.CategoryInfo;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsReceivedMails;
import com.example.gestiondecourrier.ui.ui.viewmodel.CategoryViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;

import java.util.ArrayList;
import java.util.List;


public class TraitFileFragment extends Fragment {

    FragmentTraitFileBinding binding;
    RecyclerViewHome recyclerViewHome;
    List<MailResponse> data;
    Bundle bundle;
    MailViewModel mailViewModel=new MailViewModel();
    Feature feature=new Feature();
    SearchWithOptionsReceivedMails filter;
    CategoryViewModel categoryViewModel=new CategoryViewModel();

    public TraitFileFragment() {
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
        binding=FragmentTraitFileBinding.inflate(inflater,container,false);

        feature.initializeStructureSpinner(binding.spinnerStructuresTraitFile,requireActivity());
        feature.actionBarBehavior(binding.imgMenuTraitFile,binding.imgUserTraitFile);

        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserTraitFile);

        bundle=new Bundle();
        data=new ArrayList<>();
        recyclerViewHome=new RecyclerViewHome(data,binding.relativeTraitFile,binding.rvTrait,"trait_fragment", new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("mailPosition",position);
                bundle.putString("mailSource","trait");
                navController.navigate(R.id.fileFragment,bundle);
            }
        },requireActivity());
        binding.rvTrait.setAdapter(recyclerViewHome);
        binding.rvTrait.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvTrait.setHasFixedSize(true);

        filter=new SearchWithOptionsReceivedMails(new ArrayList<>(),recyclerViewHome);
        feature.search(filter,binding.edtSearchTraitFile);

        feature.getInfoFromOptionLayout(getActivity(),binding.cardEntryDateTraitFile,binding.cardDepartureDateTraitFile
                ,binding.spinnerStructuresTraitFile,binding.spinnerCategoriesTraitFile,filter);


        //refresh layout
        feature.refreshOptions(binding.refreshTraitMails,binding.rvTrait);

        binding.refreshTraitMails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMails(userDetails.getStructureDesignation());
                binding.refreshTraitMails.setRefreshing(false);
            }
        });


        binding.relativeFilterTrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.hsTrait.getVisibility()==View.VISIBLE) {
                    binding.hsTrait.setVisibility(View.GONE);
                    binding.txtFilterTrait.setVisibility(View.GONE);
                }else {
                    binding.hsTrait.setVisibility(View.VISIBLE);
                    binding.txtFilterTrait.setVisibility(View.VISIBLE);
                }
            }
        });

        getMails(userDetails.getStructureDesignation());

        return binding.getRoot();
    }

    private void getMails(String structure){
        mailViewModel.traitMails(structure,userDetails.getId());
        mailViewModel.getLiveTraitMails().observe(getViewLifecycleOwner(), new Observer<List<MailResponse>>() {
            @Override
            public void onChanged(@NonNull List<MailResponse> pdfs) {
                binding.shimmerTrait.stopShimmer();
                binding.shimmerTrait.setVisibility(View.GONE);
                if (!pdfs.isEmpty()) {
                    for (int i=0;i<pdfs.size();i++)
                        pdfs.get(i).setPlaceInTheViewModel(i);
                    filter.setOriginalList(pdfs);
                    binding.relativeTraitFile.setVisibility(View.GONE);
                    binding.rvTrait.setVisibility(View.VISIBLE);
                    recyclerViewHome.setData(pdfs);
                } else {
                    binding.relativeTraitFile.setVisibility(View.VISIBLE);
                    binding.rvTrait.setVisibility(View.GONE);
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
                    binding.spinnerCategoriesTraitFile.setAdapter(adapterStructure);
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