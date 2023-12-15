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
import com.example.gestiondecourrier.databinding.FragmentFavoriteBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsNoStructure;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    FragmentFavoriteBinding binding;
    RecyclerViewHome recyclerViewHome;
    List<MailResponse> data;
    Bundle bundle;
    MailViewModel mailViewModel=new MailViewModel();
    Feature feature=new Feature();
    SearchWithOptionsNoStructure filter;
    public FavoriteFragment() {
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
        binding=FragmentFavoriteBinding.inflate(inflater,container,false);

        feature.initializeCategorySpinner(binding.spinnerCategoriesFavorite,requireActivity());
        feature.actionBarBehavior(binding.imgMenuFavorite,binding.imgUserFavorite);
        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserFavorite);


        bundle=new Bundle();
        //Recycler View
        data=new ArrayList<>();
        recyclerViewHome=new RecyclerViewHome(data,binding.relativeFavorite,binding.rvEmailFavorite,"favorite", new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("mailPosition",position);
                bundle.putString("mailSource","favorite");
                navController.navigate(R.id.fileFragment,bundle);
            }
        },requireActivity());
        binding.rvEmailFavorite.setAdapter(recyclerViewHome);
        binding.rvEmailFavorite.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvEmailFavorite.setHasFixedSize(true);

        filter=new SearchWithOptionsNoStructure(new ArrayList<>(),recyclerViewHome);
        feature.search(filter,binding.edtSearchFavorite);
        feature.getInfoFromOptionLayout(requireActivity(), binding.cardEntryDateFavorite, binding.cardDepartureDateFavorite
                ,binding.spinnerCategoriesFavorite,filter);



        //refresh layout
        feature.refreshOptions(binding.refreshFavoriteMails,binding.rvEmailFavorite);

        binding.refreshFavoriteMails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMails(userDetails.getStructure_id());
                binding.refreshFavoriteMails.setRefreshing(false);
            }
        });





        getMails(userDetails.getStructure_id());

        binding.relativeFilterFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.hsFavorite.getVisibility()==View.VISIBLE) {
                    binding.hsFavorite.setVisibility(View.GONE);
                    binding.txtFilterFavorite.setVisibility(View.GONE);
                }else {
                    binding.hsFavorite.setVisibility(View.VISIBLE);
                    binding.txtFilterFavorite.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding.getRoot();
    }

    private void getMails(long id){
        mailViewModel.favoriteMails(id,userDetails.getId());
        mailViewModel.getLiveFavoriteMails().observe(getViewLifecycleOwner(), new Observer<List<MailResponse>>() {
            @Override
            public void onChanged(List<MailResponse> mailResponses) {
                if (isAdded()) {
                    binding.shimmerFavorite.stopShimmer();
                    binding.shimmerFavorite.setVisibility(View.GONE);
                    if (mailResponses != null && !mailResponses.isEmpty()) {
                        for (int i=0;i<mailResponses.size();i++)
                            mailResponses.get(i).setPlaceInTheViewModel(i);

                        filter.setOriginalList(mailResponses);
                        binding.relativeFavorite.setVisibility(View.GONE);
                        binding.rvEmailFavorite.setVisibility(View.VISIBLE);
                        recyclerViewHome.setData(mailResponses);
                    } else {
                        binding.relativeFavorite.setVisibility(View.VISIBLE);
                        binding.rvEmailFavorite.setVisibility(View.GONE);
                    }
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