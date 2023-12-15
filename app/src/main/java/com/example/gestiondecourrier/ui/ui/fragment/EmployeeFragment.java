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
import com.example.gestiondecourrier.databinding.FragmentEmployeeBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.EmployeeResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewEmployee;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchByName;
import com.example.gestiondecourrier.ui.search.SearchByNameEmployee;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.EmailViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.EmployeeViewModel;

import java.util.ArrayList;
import java.util.List;


public class EmployeeFragment extends Fragment {

    FragmentEmployeeBinding binding;
    RecyclerViewEmployee recyclerViewEmployee;
    List<EmployeeResponse> data;
    EmployeeViewModel employeeViewModel=new EmployeeViewModel();
    Bundle bundle;
    Feature feature=new Feature();
    SearchByNameEmployee filter;

    public EmployeeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        employeeViewModel= ViewModelProviders.of(requireActivity()).get(EmployeeViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentEmployeeBinding.inflate(inflater,container,false);

        data=new ArrayList<>();
        bundle=new Bundle();



        binding.floatAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.addEmployeeFragment);
            }
        });

        feature.actionBarBehavior(binding.imgMenuEmployee,binding.imgUserHomeEmployee);

        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserHomeEmployee);


        recyclerViewEmployee=new RecyclerViewEmployee(data, new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("employeePosition",position);
                navController.navigate(R.id.employeeDetailsFragment,bundle);
            }
        });

        binding.rvEmployee.setAdapter(recyclerViewEmployee);
        binding.rvEmployee.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvEmployee.setHasFixedSize(true);


        //refresh layout
        feature.refreshOptions(binding.refreshEmployee,binding.rvEmployee);

        binding.refreshEmployee.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEmployees(userDetails.getStructure_id());
                binding.refreshEmployee.setRefreshing(false);
            }
        });


        filter=new SearchByNameEmployee(recyclerViewEmployee,data);

        binding.edtSearchEmployee.addTextChangedListener(new TextWatcher() {
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

        getEmployees(userDetails.getStructure_id());

        return binding.getRoot();
    }

    void getEmployees(long id){
        employeeViewModel.getLiveGetEmployees().setValue(null);
        employeeViewModel.getEmployees(id);
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    getViewLifecycleOwner().getLifecycle().removeObserver(this);
                    employeeViewModel.getLiveGetEmployees().observe(getViewLifecycleOwner(), new Observer<List<EmployeeResponse>>() {
                        @Override
                        public void onChanged(List<EmployeeResponse> employeeResponses) {
                            binding.shimmerEmployee.stopShimmer();
                            binding.shimmerEmployee.setVisibility(View.GONE);
                            if (employeeResponses!=null && !employeeResponses.isEmpty()){
                                for (int i=0;i<employeeResponses.size();i++)
                                    employeeResponses.get(i).setPositionInTheViewModel(i);
                                filter.setOriginalList(employeeResponses);
                                binding.relativeEmployee.setVisibility(View.GONE);
                                binding.rvEmployee.setVisibility(View.VISIBLE);
                                recyclerViewEmployee.setData(employeeResponses);
                            }else{
                                binding.relativeEmployee.setVisibility(View.VISIBLE);
                                binding.rvEmployee.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }


}