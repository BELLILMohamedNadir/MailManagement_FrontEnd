package com.example.gestiondecourrier.ui.ui.fragment;


import static com.example.gestiondecourrier.ui.ui.MainActivity.bitmap;
import static com.example.gestiondecourrier.ui.ui.MainActivity.items;
import static com.example.gestiondecourrier.ui.ui.MainActivity.liveItems;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentReportBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.Report;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewReport;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnDateSelectedListener;
import com.example.gestiondecourrier.ui.search.SearchReport;
import com.example.gestiondecourrier.ui.ui.viewmodel.ReportViewModel;

import java.util.ArrayList;
import java.util.List;


public class ReportFragment extends Fragment {


    FragmentReportBinding binding;
    RecyclerViewReport recyclerViewReport;
    List<Report> data;
    Bundle bundle;
    ReportViewModel reportViewModel=new ReportViewModel();
    Feature feature=new Feature();
    SearchReport filter;
    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        reportViewModel= ViewModelProviders.of(requireActivity()).get(ReportViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentReportBinding.inflate(inflater,container,false);
        data=new ArrayList<>();
        bundle=new Bundle();
        //initialise the profile picture
        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserReport);
        //
        feature.actionBarBehavior(binding.imgMenuReport,binding.imgUserReport);
        //
        feature.initializeStructureSpinner(binding.spinnerStructuresReport,requireContext());
        initializeSpinner();


        //initialise recyclerView
        recyclerViewReport=new RecyclerViewReport(data, new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("reportPosition",position);
                navController.navigate(R.id.reportDetailsFragment,bundle);
            }
        });
        binding.rvReport.setAdapter(recyclerViewReport);
        binding.rvReport.setHasFixedSize(true);
        binding.rvReport.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

        //initialise the  filter
        filter=new SearchReport(new ArrayList<>(),recyclerViewReport);

        //refresh layout
        feature.refreshOptions(binding.refreshReport,binding.rvReport);




        binding.cardFromDateReport.setOnClickListener(new View.OnClickListener() {
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

        binding.cardToDateReport.setOnClickListener(new View.OnClickListener() {
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

        binding.spinnerTypeReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0)
                    filter.setType((String) adapterView.getItemAtPosition(i));
                else
                    filter.setType("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.spinnerStructuresReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0)
                    filter.setStructure((String) adapterView.getItemAtPosition(i));
                else
                    filter.setStructure("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.edtSearchReport.addTextChangedListener(new TextWatcher() {
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

        if (userDetails.getStructureDesignation().equals("Direction des resources humain"))
            binding.cardStructureReport.setVisibility(View.VISIBLE);
        else
            binding.cardStructureReport.setVisibility(View.GONE);



        binding.refreshReport.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReports();
                binding.refreshReport.setRefreshing(false);
            }
        });


        binding.relativeFilterReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.hsReport.getVisibility()==View.VISIBLE) {
                    binding.hsReport.setVisibility(View.GONE);
                    binding.txtFilterReport.setVisibility(View.GONE);
                }else {
                    binding.hsReport.setVisibility(View.VISIBLE);
                    binding.txtFilterReport.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.imgDailyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.dailyFragment);
            }
        });


        getReports();

        return binding.getRoot();
    }

    private void initializeSpinner() {
        ArrayAdapter<CharSequence> adapterType=ArrayAdapter.createFromResource(getContext(),R.array.report,android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeReport.setAdapter(adapterType);
    }

    private void getReports(){
        if (userDetails!=null) {
            if (userDetails.getStructureDesignation().equals("Direction des ressources humaines")) {
                reportViewModel.getReports(userDetails.getStructure_id());
                reportViewModel.getLiveAllReports().observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
                    @Override
                    public void onChanged(List<Report> reports) {
                        binding.shimmerSend.stopShimmer();
                        binding.shimmerSend.setVisibility(View.GONE);
                        if (reports != null && !reports.isEmpty()) {
                            for (int i=0;i<reports.size();i++)
                                reports.get(i).setPositionInTheViewModel(i);
                            binding.relativeReport.setVisibility(View.GONE);
                            filter.setOriginalList(reports);
                            binding.rvReport.setVisibility(View.VISIBLE);
                            recyclerViewReport.setData(reports);
                        } else {
                            binding.relativeReport.setVisibility(View.VISIBLE);
                            binding.rvReport.setVisibility(View.GONE);
                        }
                    }
                });

            } else {
                reportViewModel.getReportsByStructure(userDetails.getStructure_id());
                reportViewModel.getLiveReportsByStructure().observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
                    @Override
                    public void onChanged(List<Report> reports) {
                        binding.shimmerSend.stopShimmer();
                        binding.shimmerSend.setVisibility(View.GONE);
                        if (reports != null && !reports.isEmpty()) {
                            for (int i=0;i<reports.size();i++)
                                reports.get(i).setPositionInTheViewModel(i);
                            binding.relativeReport.setVisibility(View.GONE);
                            binding.rvReport.setVisibility(View.VISIBLE);
                            filter.setOriginalList(reports);
                            recyclerViewReport.setData(reports);
                        } else {
                            binding.relativeReport.setVisibility(View.VISIBLE);
                            binding.rvReport.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }
    }
}