package com.example.gestiondecourrier.ui.ui.fragment;


import static com.example.gestiondecourrier.ui.ui.AdminActivity.adminDetails;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.navControllerAdmin;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.navigationView;
import static com.example.gestiondecourrier.ui.ui.MainActivity.bitmap;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentTraceBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.TraceResponse;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewTrace;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnDateSelectedListener;
import com.example.gestiondecourrier.ui.search.SearchTrace;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.TraceViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class TraceFragment extends Fragment {

    FragmentTraceBinding binding;
    List<TraceResponse> data;
    RecyclerViewTrace recyclerViewTrace;
    Bundle bundle;
    SearchTrace filter;
    TraceViewModel traceViewModel=new TraceViewModel();
    Feature feature=new Feature();

    public TraceFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        traceViewModel= ViewModelProviders.of(requireActivity()).get(TraceViewModel.class);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentTraceBinding.inflate(inflater,container,false);

        adminDetails.observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse!=null)
                    feature.profilePicture(bitmap,userResponse.getName().charAt(0),binding.imgUserTrace);

            }
        });

        navigationView.observe(getViewLifecycleOwner(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        data=new ArrayList<>();
        bundle=new Bundle();

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),R.array.mail_changes,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerActionTrace.setAdapter(adapter);

        recyclerViewTrace=new RecyclerViewTrace(data, new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("tracePosition",position);
                navControllerAdmin.navigate(R.id.traceDetailsFragment,bundle);
            }
        });
        binding.rvTrace.setAdapter(recyclerViewTrace);
        binding.rvTrace.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvTrace.setHasFixedSize(true);

        //refresh layout
        feature.refreshOptions(binding.refreshTrace,binding.rvTrace);

        binding.refreshTrace.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTraces();
                binding.refreshTrace.setRefreshing(false);
            }
        });

        filter=new SearchTrace(data,recyclerViewTrace);

        binding.cardDuTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              feature.showCalender(requireActivity(), new OnDateSelectedListener() {
                  @Override
                  public void onDateSelected(String date) {
                      filter.setFromDate(date);
                  }
              });
            }
        });
        binding.cardOuTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               feature.showCalender(requireActivity(), new OnDateSelectedListener() {
                   @Override
                   public void onDateSelected(String date) {
                       filter.setToDate(date);
                   }
               });
            }
        });

        binding.spinnerActionTrace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                    filter.setAction("");
                else
                    filter.setAction((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.edtSearchTrace.addTextChangedListener(new TextWatcher() {
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

        getTraces();

        return binding.getRoot();
    }

    private void getTraces(){
        traceViewModel.getTraces();
        traceViewModel.getLiveGetTraces().observe(getViewLifecycleOwner(), new Observer<List<TraceResponse>>() {
            @Override
            public void onChanged(List<TraceResponse> trace) {
                binding.shimmerTrace.stopShimmer();
                binding.shimmerTrace.setVisibility(View.GONE);
                if (trace!=null && !trace.isEmpty()) {
                    for (int i=0;i<trace.size();i++)
                        trace.get(i).setPositionInTheViewModel(i);
                    filter.setOriginalList(trace);
                    binding.relativeTrace.setVisibility(View.GONE);
                    binding.rvTrace.setVisibility(View.VISIBLE);
                    recyclerViewTrace.setData(trace);

                } else {
                    binding.relativeTrace.setVisibility(View.VISIBLE);
                    binding.rvTrace.setVisibility(View.GONE);
                }
            }
        });

    }


}