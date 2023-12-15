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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentReceivedBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.CategoryInfo;
import com.example.gestiondecourrier.ui.pojo.Mail;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsReceivedMails;
import com.example.gestiondecourrier.ui.ui.viewmodel.CategoryViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;

import java.util.ArrayList;
import java.util.List;


public class ReceivedFragment extends Fragment {

    FragmentReceivedBinding binding;
    RecyclerViewHome recyclerViewHome;
    List<MailResponse> data;
    Bundle bundle;
    MailViewModel mailViewModel=new MailViewModel();
    CategoryViewModel categoryViewModel=new CategoryViewModel();
    Feature feature=new Feature();
    SearchWithOptionsReceivedMails filter;
    public ReceivedFragment() {
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
        binding=FragmentReceivedBinding.inflate(inflater,container,false);

        feature.initializeStructureSpinner(binding.spinnerStructuresReceived, requireActivity());
        feature.actionBarBehavior(binding.imgMenuReceived,binding.imgUserReceived);
        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserReceived);

        data=new ArrayList<>();
        bundle=new Bundle();

        recyclerViewHome=new RecyclerViewHome(data,binding.relativeReceived,binding.rvReceivedMails,"received_fragment", new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("mailPosition",position);
                bundle.putString("mailSource","received");
                navController.navigate(R.id.fileFragment,bundle);
            }
        },requireActivity());

        binding.rvReceivedMails.setAdapter(recyclerViewHome);
        binding.rvReceivedMails.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvReceivedMails.setHasFixedSize(true);

        filter=new SearchWithOptionsReceivedMails(new ArrayList<>(),recyclerViewHome);

        //common features
        feature.search(filter,binding.edtSearchReceived);
        feature.getInfoFromOptionLayout(requireActivity(),binding.cardEntryDateReceived,binding.cardDepartureDateReceived
                ,binding.spinnerStructuresReceived,binding.spinnerCategoriesReceived,filter);


        //refresh layout
        feature.refreshOptions(binding.refreshReceivedMails,binding.rvReceivedMails);

        binding.refreshReceivedMails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReceivedMails();
                binding.refreshReceivedMails.setRefreshing(false);
            }
        });


        binding.relativeFilterReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.hsReceived.getVisibility()==View.VISIBLE) {
                    binding.hsReceived.setVisibility(View.GONE);
                    binding.txtFilterReceived.setVisibility(View.GONE);
                }else {
                    binding.hsReceived.setVisibility(View.VISIBLE);
                    binding.txtFilterReceived.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.spinnerCategoriesReceived.setSelection(Spinner.INVALID_POSITION);
        binding.spinnerCategoriesReceived.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                    filter.setCategory("");
                else
                    filter.setCategory((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                filter.setCategory("");
            }
        });



        getReceivedMails();

        return binding.getRoot();
    }

    private void getReceivedMails() {
        mailViewModel.receivedMails(userDetails.getStructureDesignation(),userDetails.getId());
        mailViewModel.getLiveReceivedMails().observe(getViewLifecycleOwner(), new Observer<List<MailResponse>>() {
            @Override
            public void onChanged(List<MailResponse> mailResponses) {
                binding.shimmerReceived.stopShimmer();
                binding.shimmerReceived.setVisibility(View.GONE);
                if (mailResponses != null && !mailResponses.isEmpty()) {
                    for (int i=0;i<mailResponses.size();i++)
                        mailResponses.get(i).setPlaceInTheViewModel(i);
                    filter.setOriginalList(mailResponses);
                    binding.relativeReceived.setVisibility(View.GONE);
                    binding.rvReceivedMails.setVisibility(View.VISIBLE);
                    recyclerViewHome.setData(mailResponses);
                } else {
                    binding.relativeReceived.setVisibility(View.VISIBLE);
                    binding.rvReceivedMails.setVisibility(View.GONE);
                }
            }
        });
        getCategories();
    }


    public void getCategories(){
        categoryViewModel.getArrivedCategoryInfo(userDetails.getStructure_id());
        categoryViewModel.getLiveGetArrivedCategoryInfo().observe(getViewLifecycleOwner(), new Observer<List<CategoryInfo>>() {
            @Override
            public void onChanged(List<CategoryInfo> categoryInfos) {
                if (categoryInfos!=null && !categoryInfos.isEmpty()){
                    List<String> arrivedCategoryDesignation=new ArrayList<>();
                    for (int i=0;i<categoryInfos.size();i++) {
                        arrivedCategoryDesignation.add(categoryInfos.get(i).getDesignation());
                    }
                    List<String> designation=new ArrayList<>();
                    designation.add("Catégorie");
                    designation.addAll(arrivedCategoryDesignation);
                    ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, designation);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerCategoriesReceived.setAdapter(adapterCategory);
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