package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.gestiondecourrier.databinding.FragmentAddReceivedBinding;
import com.example.gestiondecourrier.ui.pojo.Category;
import com.example.gestiondecourrier.ui.pojo.CategoryInfo;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.pojo.ReceivedMail;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.ui.viewmodel.CategoryViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AddReceivedFragment extends DialogFragment {

    FragmentAddReceivedBinding binding;
    MailViewModel mailViewModel=new MailViewModel();
    CategoryViewModel categoryViewModel=new CategoryViewModel();
    int mailPosition =-1;
    MailResponse mail=null;
    int positionCategory=0;
    String categoryReceived="";
    String source="";

    public AddReceivedFragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();
        // Set the width and height of the dialog
        if (getDialog()!=null)
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mailViewModel= ViewModelProviders.of(requireActivity()).get(MailViewModel.class);
        categoryViewModel= ViewModelProviders.of(requireActivity()).get(CategoryViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!isAdded()) {
            return null; // Fragment not attached to activity, so return null
        }

        // Inflate the layout for this fragment
        binding=FragmentAddReceivedBinding.inflate(inflater,container,false);


        if (getArguments()!=null) {
            mailPosition = getArguments().getInt("mailPosition",-1);
            source=getArguments().getString("source","");
            if(source.equals("received") && mailViewModel.getLiveReceivedMails().getValue()!=null)
                mail=mailViewModel.getLiveReceivedMails().getValue().get(mailPosition);
            else
                if(source.equals("toTrait") && mailViewModel.getLiveToTraitMails().getValue()!=null)
                     mail=mailViewModel.getLiveToTraitMails().getValue().get(mailPosition);
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());

        initialiseSpinner();

        binding.edtEntryDateReceived.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));

        binding.imgSendReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.edtObjectReceived.getText().toString().isEmpty() && mailPosition !=-1 && mail!=null
                        && categoryViewModel.getLiveGetArrivedCategoryInfo().getValue()!=null
                        && !categoryViewModel.getLiveGetArrivedCategoryInfo().getValue().isEmpty()
                        && !categoryReceived.isEmpty()){
                    binding.imgSendReceived.setVisibility(View.GONE);
                    binding.progressAddReceivedDocument.setVisibility(View.VISIBLE);

                    enableComponent(false);
                    ReceivedMail receivedMail=new ReceivedMail(new Structure(userDetails.getStructure_id()),new Category(categoryViewModel.getLiveGetArrivedCategoryInfo().getValue().get(positionCategory).getId()),categoryReceived,binding.edtMailReferenceReceived.getText().toString()
                    ,binding.edtObjectReceived.getText().toString());
                    mailViewModel.updateReceivedMails(mail.getId(),receivedMail);
                    mailViewModel.getLiveUpdateReceivedMail().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (aBoolean){
                                if (source.equals("received") && mailViewModel.getLiveReceivedMails().getValue()!=null){
                                    MailResponse mail2=mailViewModel.getLiveReceivedMails().getValue().get(mailPosition);
                                    mail2.setEntryDateReceived(Calendar.getInstance().getTime());
                                    mail2.setEntryDateReceivedToShow(simpleDateFormat.format(Calendar.getInstance().getTime()));
                                    mail2.setReceivedCategory(categoryReceived);
                                    mail2.setMailReference(receivedMail.getMailReference());//
                                    mail2.setObjectReceived(receivedMail.getObjectReceived());
                                    mailViewModel.getLiveReceivedMails().getValue().set(mailPosition,mail2);
                                }else
                                    if (source.equals("toTrait") && mailViewModel.getLiveToTraitMails().getValue()!=null){
                                        MailResponse mail2=mailViewModel.getLiveToTraitMails().getValue().get(mailPosition);
                                        mail2.setEntryDateReceived(Calendar.getInstance().getTime());
                                        mail2.setEntryDateReceivedToShow(simpleDateFormat.format(Calendar.getInstance().getTime()));
                                        mail2.setReceivedCategory(categoryReceived);
                                        mail2.setMailReference(receivedMail.getMailReference());
                                        mail2.setObjectReceived(receivedMail.getObjectReceived());
                                        mailViewModel.getLiveToTraitMails().getValue().set(mailPosition,mail2);
                                    }

                                Toast.makeText(requireActivity(), "updated", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }else {
                                binding.imgSendReceived.setVisibility(View.VISIBLE);
                                binding.progressAddReceivedDocument.setVisibility(View.GONE);
                                enableComponent(true);
                                Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(requireActivity(), "fill the void", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.spinnerCategoryReceived.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionCategory=i;
                categoryReceived=(String) adapterView.getItemAtPosition(i);
                makeAReference();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return binding.getRoot();
    }
    private void initialiseSpinner(){
        categoryViewModel.getArrivedCategoryInfo(userDetails.getStructure_id());
        categoryViewModel.getLiveGetArrivedCategoryInfo().observe(getViewLifecycleOwner(), new Observer<List<CategoryInfo>>() {
            @Override
            public void onChanged(List<CategoryInfo> categoryInfos) {
                if (categoryInfos!=null && !categoryInfos.isEmpty()){
                    List<String> arrivedCategoryDesignation=new ArrayList<>();
                    for (int i=0;i<categoryInfos.size();i++) {
                        arrivedCategoryDesignation.add(categoryInfos.get(i).getDesignation());
                    }
                    ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, arrivedCategoryDesignation);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerCategoryReceived.setAdapter(adapterCategory);
                }
            }
        });

    }
    private void makeAReference() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = simpleDateFormat.format(Calendar.getInstance().getTime()) + "";
        if (categoryViewModel.getLiveGetArrivedCategoryInfo().getValue()!=null){
            String reference = reachTheNumberOfBits(String.valueOf(categoryViewModel.getLiveGetArrivedCategoryInfo().getValue().get(positionCategory).getCpt()));
            reference = "ATM/DG/" + userDetails.getStructureCode() + "/".concat(reference).concat("/") + date.charAt(8) + "" + date.charAt(9);
            binding.edtMailReferenceReceived.setText(reference);}
    }

    private String reachTheNumberOfBits(String s){
        if (s.length()==1)
            for(int i=0;i<5;i++)
                s="0".concat(s);
        else
        if (s.length()==2)
            for(int i=0;i<4;i++)
                s="0".concat(s);
        else
        if (s.length()==3)
            for(int i=0;i<3;i++)
                s="0".concat(s);
        else
        if (s.length()==4)
            for(int i=0;i<2;i++)
                s="0".concat(s);
        else
        if (s.length()==5)
            for(int i=0;i<1;i++)
                s="0".concat(s);

        return s;
    }
    private void enableComponent(boolean b){
        binding.edtMailReferenceReceived.setEnabled(b);
        binding.edtObjectReceived.setEnabled(b);
        binding.spinnerCategoryReceived.setEnabled(b);
    }
}