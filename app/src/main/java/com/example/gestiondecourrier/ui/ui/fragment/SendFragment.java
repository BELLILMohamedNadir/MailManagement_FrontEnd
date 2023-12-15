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
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentSendBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.CategoryInfo;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsReceivedMails;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsSendMails;
import com.example.gestiondecourrier.ui.ui.viewmodel.CategoryViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;

import java.util.ArrayList;
import java.util.List;


public class SendFragment extends Fragment {

    FragmentSendBinding binding;
    List<MailResponse> data;
    RecyclerViewHome recyclerViewHome;
    Bundle bundle;
    MailViewModel mailViewModel=new MailViewModel();
    Feature feature=new Feature();
    public static List<String> sendCategoryDesignation;
    SearchWithOptionsSendMails filter;
    CategoryViewModel categoryViewModel=new CategoryViewModel();
    public SendFragment() {
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
        binding=FragmentSendBinding.inflate(inflater,container,false);


        feature.initializeStructureSpinner(binding.spinnerStructuresSend, requireActivity());
        feature.actionBarBehavior(binding.imgMenuSend,binding.imgUserSend);
        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserSend);
        data=new ArrayList<>();
        bundle=new Bundle();
        sendCategoryDesignation=new ArrayList<>();
        binding.floatAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.addDocumentToSendFragment);
            }
        });

        //initialise recyclerView
        recyclerViewHome=new RecyclerViewHome(data,binding.relativeSend,binding.rvSend,"send_fragment", new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("mailPosition",position);
                bundle.putString("mailSource","send");
                navController.navigate(R.id.fileFragment,bundle);
            }
        },requireActivity());
        binding.rvSend.setAdapter(recyclerViewHome);
        binding.rvSend.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvSend.setHasFixedSize(true);

        filter=new SearchWithOptionsSendMails(new ArrayList<>(),recyclerViewHome);
        feature.search(filter,binding.edtSearchSend);
        feature.getInfoFromOptionLayout(requireActivity(),binding.cardEntryDateSend,binding.cardDepartureDateSend
                ,binding.spinnerStructuresSend,binding.spinnerCategoriesSend,filter);

        //refresh layout
        feature.refreshOptions(binding.refreshSendMails,binding.rvSend);

        binding.refreshSendMails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSendMails();
                binding.refreshSendMails.setRefreshing(false);
            }
        });

        binding.relativeFilterSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.hsSend.getVisibility()==View.VISIBLE) {
                    binding.hsSend.setVisibility(View.GONE);
                    binding.txtFilterSend.setVisibility(View.GONE);
                }else {
                    binding.hsSend.setVisibility(View.VISIBLE);
                    binding.txtFilterSend.setVisibility(View.VISIBLE);
                }
            }
        });

        getSendMails();

        return binding.getRoot();
    }

    private void getSendMails(){

        mailViewModel.sendMails(userDetails.getStructure_id(),userDetails.getId());
        mailViewModel.getLiveSendMails().observe(getViewLifecycleOwner(), new Observer<List<MailResponse>>() {
            @Override
            public void onChanged(List<MailResponse> mailResponses) {
                binding.shimmerSend.stopShimmer();
                binding.shimmerSend.setVisibility(View.GONE);
                if (mailResponses != null && !mailResponses.isEmpty()) {
                    for (int i=0;i<mailResponses.size();i++)
                        mailResponses.get(i).setPlaceInTheViewModel(i);
                    filter.setOriginalList(mailResponses);
                    binding.relativeSend.setVisibility(View.GONE);
                    binding.rvSend.setVisibility(View.VISIBLE);
                    recyclerViewHome.setData(mailResponses);
                } else {
                    binding.relativeSend.setVisibility(View.VISIBLE);
                    binding.rvSend.setVisibility(View.GONE);
                }
            }

        });
        getCategories();
    }


    private void getCategories(){
        categoryViewModel.getSendCategoryInfo(userDetails.getStructure_id());

        /// the problem is here
        categoryViewModel.getLiveGetSendCategoryInfo().observe(getViewLifecycleOwner(), new Observer<List<CategoryInfo>>() {
            @Override
            public void onChanged(List<CategoryInfo> categoryInfos) {
                if (categoryInfos!=null && !categoryInfos.isEmpty()){
                    sendCategoryDesignation.clear();
                    for (CategoryInfo  categoryInfo:categoryInfos)
                        sendCategoryDesignation.add(categoryInfo.getDesignation());
                    List<String> list=new ArrayList<>();
                    list.add("Catégorie");
                    list.addAll(sendCategoryDesignation);
                    ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, list);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerCategoriesSend.setAdapter(adapterCategory);
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
