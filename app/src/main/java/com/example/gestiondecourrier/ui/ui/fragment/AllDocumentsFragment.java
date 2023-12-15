package com.example.gestiondecourrier.ui.ui.fragment;


import static com.example.gestiondecourrier.ui.ui.MainActivity.liveBitmap;
import static com.example.gestiondecourrier.ui.ui.MainActivity.livePicture;
import static com.example.gestiondecourrier.ui.ui.MainActivity.liveStructureId;
import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;


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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentAllDocumentsBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsNoStructure;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;

import java.util.ArrayList;
import java.util.List;


public class AllDocumentsFragment extends Fragment {

    FragmentAllDocumentsBinding binding;
    RecyclerViewHome recyclerViewHome;
    List<MailResponse> data;
    Bundle bundle;
    MailViewModel mailViewModel=new MailViewModel();
    Feature feature=new Feature();
    SearchWithOptionsNoStructure filter;
    public AllDocumentsFragment() {
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
        binding=FragmentAllDocumentsBinding.inflate(inflater,container,false);

        feature.initializeCategorySpinner(binding.spinnerCategories,requireActivity());
        feature.actionBarBehavior(binding.imgMenuMailAll,binding.imgUserMailAll);



        liveBitmap.observe(requireActivity(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if (userDetails!=null && bitmap!=null)
                    feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserMailAll);
            }
        });

        livePicture.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s!=null && !s.isEmpty()){
                    binding.imgUserMailAll.setImageResource(feature.picture(s.charAt(0)));
                }
            }
        });


        binding.floatSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.sendEmailFragment);
            }
        });

        data=new ArrayList<>();
        bundle=new Bundle();

        recyclerViewHome=new RecyclerViewHome(data,binding.relativeAllDocument,binding.rvAllMails,"all_document_fragment", new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("mailPosition",position);
                bundle.putString("mailSource","all");
                navController.navigate(R.id.fileFragment,bundle);
            }
        },requireActivity());
        binding.rvAllMails.setAdapter(recyclerViewHome);
        binding.rvAllMails.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvAllMails.setHasFixedSize(true);


        filter=new SearchWithOptionsNoStructure(new ArrayList<>(),recyclerViewHome);
        feature.search(filter,binding.edtSearchMailAll);
        feature.getInfoFromOptionLayout(requireActivity(),binding.cardEntryDate,binding.cardDepartureDate
                ,binding.spinnerCategories,filter);


        //refresh layout
        feature.refreshOptions(binding.refreshAllMails,binding.rvAllMails);

        binding.refreshAllMails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllMails();
                binding.refreshAllMails.setRefreshing(false);
            }
        });



        binding.relativeFilterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.hsAll.getVisibility()==View.VISIBLE) {
                    binding.hsAll.setVisibility(View.GONE);
                    binding.txtFilterAll.setVisibility(View.GONE);
                }else {
                    binding.hsAll.setVisibility(View.VISIBLE);
                    binding.txtFilterAll.setVisibility(View.VISIBLE);
                }
            }
        });


        getAllMails();

        return binding.getRoot();
    }


    private void getAllMails(){

        //to ensure that it's initialized
        liveStructureId.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if (aLong!=0)
                    mailViewModel.allMails(aLong,userDetails.getId(),userDetails.getStructureDesignation());
            }
        });


        mailViewModel.getLiveAllMails().observe(getViewLifecycleOwner(), new Observer<List<MailResponse>>() {
            @Override
            public void onChanged(List<MailResponse> mailResponses) {
                binding.shimmerMailAll.stopShimmer();
                binding.shimmerMailAll.setVisibility(View.GONE);
                if (mailResponses != null && !mailResponses.isEmpty()) {
                    for (int i=0;i<mailResponses.size();i++)
                        mailResponses.get(i).setPlaceInTheViewModel(i);
                    filter.setOriginalList(mailResponses);
                    binding.relativeAllDocument.setVisibility(View.GONE);
                    binding.rvAllMails.setVisibility(View.VISIBLE);
                    recyclerViewHome.setData(mailResponses);
                } else {
                    binding.relativeAllDocument.setVisibility(View.VISIBLE);
                    binding.rvAllMails.setVisibility(View.GONE);
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