package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AdminActivity.navigationView;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentTraceDetailsBinding;
import com.example.gestiondecourrier.ui.LocaleHelper;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.Folder;
import com.example.gestiondecourrier.ui.pojo.TraceResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewEmailBytes;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.TraceViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TraceDetailsFragment extends Fragment {

    FragmentTraceDetailsBinding binding;
    TraceViewModel traceViewModel=new TraceViewModel();
    int position=-1;
    Feature feature=new Feature();
    public TraceDetailsFragment() {
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
        binding=FragmentTraceDetailsBinding.inflate(inflater,container,false);



        navigationView.observe(getViewLifecycleOwner(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });


        if (getArguments()!=null){
            position=getArguments().getInt("tracePosition",-1);
            if (position!=-1 && traceViewModel.getLiveGetTraces().getValue()!=null) {
                TraceResponse trace = traceViewModel.getLiveGetTraces().getValue().get(position);

                if (trace.getFirstName()!=null)
                    binding.txtUserTrace.setText(trace.getName().concat(" " + trace.getFirstName()));
                else
                    binding.txtUserTrace.setText(trace.getName());
                binding.edtEmailTrace.setText(trace.getEmail());
                binding.edtActionTrace.setText(trace.getAction());
                if (trace.getTime()!=null)
                    binding.edtTimeTrace.setText(trace.getTimeToShow());
                binding.txtJobTrace.setText(job(trace.getJob()));
                binding.edtReferenceTrace.setText(trace.getReference());
                if (trace.getUpdateTime()!=null){
                    binding.edtLayoutUpdateTime.setVisibility(View.VISIBLE);
                    binding.edtUpdateTimeTrace.setText(trace.getUpdateTimeToShow());
                }

                if (trace.getBytes() != null)
                    binding.imgUserTrace.setImageBitmap(BitmapFactory.decodeByteArray(trace.getBytes(), 0, trace.getBytes().length));
                else
                    binding.imgUserTrace.setImageResource(feature.picture(trace.getName().charAt(0)));


            }

        }


        return binding.getRoot();
    }

    private String job(String job) {
        switch (job){
            case "acting_director" : if (LocaleHelper.getLanguage(requireActivity()).equals("fr"))
                                            return "Directeur par intérim";
                                       else
                                            return "Acting director";
            case "acting_secretary": if (LocaleHelper.getLanguage(requireActivity()).equals("fr"))
                                            return "Secrétaire par intérim";
                                      else
                                            return "Acting secretary";
            case "director": if (LocaleHelper.getLanguage(requireActivity()).equals("fr"))
                                            return "Directeur";
                                        else
                                            return "Director";
            case "secretary": if (LocaleHelper.getLanguage(requireActivity()).equals("fr"))
                                            return "Sectrétaire";
                                        else
                                            return "Secretary";
        }
        return "";
    }
}