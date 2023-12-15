package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentAddContactBinding;
import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.ui.viewmodel.ContactViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;

import java.util.ArrayList;
import java.util.List;


public class AddContactFragment extends Fragment {
    FragmentAddContactBinding binding;
    Contact contact;
    ContactViewModel contactViewModel=new ContactViewModel();
    StructureViewModel structureViewModel=new StructureViewModel();
    public AddContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        contactViewModel= ViewModelProviders.of(requireActivity()).get(ContactViewModel.class);
        structureViewModel= ViewModelProviders.of(requireActivity()).get(StructureViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddContactBinding.inflate(inflater,container,false);
        binding.btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checked()){
                        Toast.makeText(requireActivity(), "fill the empty", Toast.LENGTH_SHORT).show();
                }else{
                    if (!binding.edtStructureContact.validateResult()){
                        Toast.makeText(requireActivity(), "enter an existing structure", Toast.LENGTH_SHORT).show();
                    }else{
                        if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmailContact.getText().toString()).matches())
                            binding.edtEmailContact.setError(getResources().getString(R.string.valid_email));
                        else{
                            contact=new Contact(binding.edtNameContact.getText().toString()
                                    ,binding.edtFirstNameContact.getText().toString()
                                    ,binding.edtStructureContact.getEditText().getText().toString()
                                    ,binding.edtEmailContact.getText().toString());
                            contactViewModel.addContact(contact);
                            contactViewModel.getLiveAddContact().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean aBoolean) {
                                    if (aBoolean){
                                        Toast.makeText(requireActivity(), "inserted", Toast.LENGTH_SHORT).show();
                                        navController.popBackStack(R.id.contactFragment,false);
                                    }else
                                        Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                }
            }
        });

        binding.btnCancelContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.contactFragment,false);
            }
        });
        getStructure();
        return binding.getRoot();
    }

    private boolean checked() {
        return binding.edtNameContact.getText().toString().isEmpty()
                ||binding.edtFirstNameContact.getText().toString().isEmpty()
                ||binding.edtStructureContact.getEditText().getText().toString().isEmpty()
                ||binding.edtEmailContact.getText().toString().isEmpty();
    }

    void getStructure(){
        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(getViewLifecycleOwner(), new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                List<String> list=new ArrayList<>();
                for (int i=0;i<structures.size();i++)
                    list.add(structures.get(i).getDesignation());
                binding.edtStructureContact.setList(list);
            }
        });

    }


}

