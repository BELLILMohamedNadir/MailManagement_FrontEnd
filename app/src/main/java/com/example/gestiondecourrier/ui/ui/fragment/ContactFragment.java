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
import com.example.gestiondecourrier.databinding.FragmentContactBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.ContactResponse;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.search.SearchByName;
import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewContact;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
    FragmentContactBinding binding;
    RecyclerViewContact recyclerViewContact;
    List<ContactResponse> data;
    SearchByName filter;
    ContactViewModel contactViewModel;
    Bundle bundle;
    Feature feature=new Feature();
    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        contactViewModel= ViewModelProviders.of(requireActivity()).get(ContactViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentContactBinding.inflate(inflater,container,false);

        data=new ArrayList<>();
        bundle=new Bundle();
        binding.floatAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.navigate(R.id.addContactFragment);

            }
        });

        feature.actionBarBehavior(binding.imgMenuContact,binding.imgUserHomeContact);

        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserHomeContact);

        recyclerViewContact=new RecyclerViewContact(data, new OnClick() {
            @Override
            public void onClick(int position) {
                bundle.putInt("contactPosition",position);
                navController.navigate(R.id.contactDetailsFragment,bundle);
            }
        });
        binding.rvContact.setAdapter(recyclerViewContact);
        binding.rvContact.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvContact.setHasFixedSize(true);

        //refresh layout
        feature.refreshOptions(binding.refreshContact,binding.rvContact);

        binding.refreshContact.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContacts();
                binding.refreshContact.setRefreshing(false);
            }
        });


        filter=new SearchByName(recyclerViewContact,data);

        binding.edtSearchContact.addTextChangedListener(new TextWatcher() {
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

        getContacts();
        return binding.getRoot();
    }
    void getContacts(){
        contactViewModel.getContacts();
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    getViewLifecycleOwner().getLifecycle().removeObserver(this);
                    contactViewModel.getLiveContact().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
                        @Override
                        public void onChanged(@NonNull List<Contact> contacts) {
                            binding.shimmerContact.stopShimmer();
                            binding.shimmerContact.setVisibility(View.GONE);
                            if (!contacts.isEmpty()){

                                List<ContactResponse> contactResponses=new ArrayList<>();
                                for (int i=0;i<contacts.size();i++){
                                    Contact c=contacts.get(i);
                                    contactResponses.add(new ContactResponse(c.getId(),c.getName(), c.getFirstName()
                                            ,c.getStructure(),c.getEmail(), i));
                                }
                                binding.relativeContact.setVisibility(View.GONE);
                                binding.rvContact.setVisibility(View.VISIBLE);
                                filter.setOriginalList(contactResponses);
                                recyclerViewContact.setData(contactResponses);
                            }else{
                                binding.relativeContact.setVisibility(View.VISIBLE);
                                binding.rvContact.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            }
        });
    }
}