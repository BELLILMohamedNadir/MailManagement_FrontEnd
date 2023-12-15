package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentContactDetailsBinding;
import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.ui.viewmodel.ContactViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailsFragment extends Fragment {

    FragmentContactDetailsBinding binding;
    Contact contact;
    ContactViewModel contactViewModel=new ContactViewModel();
    StructureViewModel structureViewModel=new StructureViewModel();
    int position=-1;
    public ContactDetailsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        structureViewModel= ViewModelProviders.of(requireActivity()).get(StructureViewModel.class);
        contactViewModel= ViewModelProviders.of(requireActivity()).get(ContactViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentContactDetailsBinding.inflate(inflater,container,false);


        if (getArguments()!=null){
            position=getArguments().getInt("contactPosition",-1);
            if (position!=-1 && contactViewModel.getLiveContact().getValue()!=null) {
                contact = contactViewModel.getLiveContact().getValue().get(position);
                if (contact != null) {
                    binding.edtNameContactDetails.setText(contact.getName());
                    binding.edtFirstNameContactDetails.setText(contact.getFirstName());
                    binding.edtStructureContactDetails.getEditText().setText(contact.getStructure());
                    binding.edtEmailContactDetails.setText(contact.getEmail());
                }
            }
        }
        binding.imgDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.alert));
                builder.setMessage(getString(R.string.sure));
                builder.setIcon(R.drawable.ic_alert);
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (contact!=null) {
                            contactViewModel.deleteContact(contact.getId());
                            contactViewModel.getLiveDeleteContact().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean aBoolean) {
                                    if (aBoolean){
                                        if (getActivity()!=null)
                                            Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();
                                        navController.popBackStack(R.id.contactFragment,false);
                                    }

                                }
                            });

                        }
                    }
                });
                builder.setNegativeButton(getString(R.string.no),null);
                builder.show();

            }
        });
        binding.btnCancelContactDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.contactFragment, false);
            }
        });
        binding.btnUpdateContactDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtNameContactDetails.getText().toString().isEmpty()
                        ||binding.edtFirstNameContactDetails.getText().toString().isEmpty()
                        ||binding.edtStructureContactDetails.getEditText().getText().toString().isEmpty()
                        ||binding.edtEmailContactDetails.getText().toString().isEmpty()){

                        Toast.makeText(requireActivity(), "fill the empty", Toast.LENGTH_SHORT).show();
                }else {
                    if (!binding.edtStructureContactDetails.validateResult()){
                        Toast.makeText(requireActivity(), "enter an existing structure", Toast.LENGTH_SHORT).show();
                    }else{
                        contact.setName(binding.edtNameContactDetails.getText().toString());
                        contact.setFirstName(binding.edtFirstNameContactDetails.getText().toString());
                        contact.setStructure(binding.edtStructureContactDetails.getEditText().getText().toString());
                        contact.setEmail(binding.edtEmailContactDetails.getText().toString());
                        contactViewModel.updateContact(contact.getId(),contact);
                        contactViewModel.getLiveUpdateContact().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean) {
                                    Toast.makeText(requireActivity(), "updated", Toast.LENGTH_SHORT).show();
                                    navController.popBackStack(R.id.contactFragment,false);
                                }else {
                                    Toast.makeText(requireActivity(), "update failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            }
        });
        getStructure();
        return binding.getRoot();
    }
    void getStructure(){
        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(getViewLifecycleOwner(), new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                List<String> list=new ArrayList<>();
                for (int i=0;i<structures.size();i++)
                    list.add(structures.get(i).getDesignation());
                binding.edtStructureContactDetails.setList(list);
            }
        });

    }
}