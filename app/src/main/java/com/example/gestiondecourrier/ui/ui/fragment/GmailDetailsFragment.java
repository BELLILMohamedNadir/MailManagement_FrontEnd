package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentGmailDetailsBinding;
import com.example.gestiondecourrier.ui.pojo.Folder;
import com.example.gestiondecourrier.ui.pojo.GmailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewEmailBytes;
import com.example.gestiondecourrier.ui.ui.viewmodel.GmailViewModel;

import java.util.ArrayList;
import java.util.List;


public class GmailDetailsFragment extends Fragment {

    FragmentGmailDetailsBinding binding;
    RecyclerViewEmailBytes recyclerViewEmailBytes;
    List<Folder> data;
    GmailViewModel gmailViewModel=new GmailViewModel();
    int position=-1;
    public GmailDetailsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        gmailViewModel= ViewModelProviders.of(requireActivity()).get(GmailViewModel.class);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentGmailDetailsBinding.inflate(inflater,container,false);

        binding.edtComposeEmailDetails.setEnabled(false);
        binding.txtToDetails.setEnabled(false);
        binding.edtSubjectEmailDetails.setEnabled(false);
        data=new ArrayList<>();
        recyclerViewEmailBytes=new RecyclerViewEmailBytes(data,"gmailDetails");
        binding.rvEmailDetails.setAdapter(recyclerViewEmailBytes);
        binding.rvEmailDetails.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        binding.rvEmailDetails.setHasFixedSize(true);
        if (getArguments()!=null){
            position=getArguments().getInt("gmailPosition");
            if (position!=-1 && gmailViewModel.getLiveGetGmail().getValue()!=null) {
                GmailResponse gmailResponse = gmailViewModel.getLiveGetGmail().getValue().get(position);
                binding.edtToEmailDetails.setText(gmailResponse.getRecipient());
                binding.edtSubjectEmailDetails.setText(gmailResponse.getSubject());
                binding.edtComposeEmailDetails.setText(gmailResponse.getBody());
                recyclerViewEmailBytes.clearData();
                for (int i = 0; i < gmailResponse.getBytes().size(); i++) {
                    recyclerViewEmailBytes.add(new Folder(gmailResponse.getFileName().get(i), gmailResponse.getBytes().get(i)));
                }
            }

        }
        binding.imgBackEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.sendFileWithEmailFragment,false);
            }
        });


        return binding.getRoot();
    }
}