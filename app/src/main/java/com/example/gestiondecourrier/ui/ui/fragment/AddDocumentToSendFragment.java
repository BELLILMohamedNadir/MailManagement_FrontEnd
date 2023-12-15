package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.editor;
import static com.example.gestiondecourrier.ui.ui.MainActivity.items;
import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;
import static com.example.gestiondecourrier.ui.ui.MainActivity.structure;
import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;
import static com.example.gestiondecourrier.ui.ui.fragment.SendFragment.sendCategoryDesignation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentAddDocumentToSendBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.Category;
import com.example.gestiondecourrier.ui.pojo.CategoryInfo;
import com.example.gestiondecourrier.ui.pojo.MailDetails;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.pojo.User;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewEmail;
import com.example.gestiondecourrier.ui.ui.viewmodel.CategoryViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSource;
import okio.Okio;

public class AddDocumentToSendFragment extends Fragment {

    FragmentAddDocumentToSendBinding binding;
    SimpleDateFormat simpleDateFormat,simpleDateFormat1;
    RecyclerViewEmail recyclerViewEmail;
    ArrayList<Uri> data;
    String type="",priority="";
    // to manage the categories info such as the counter of category ...
    List<CategoryInfo> categoryInfoBeforeChanges,categoryInfoAfterChanges;
    CategoryViewModel categoryViewModel;
    Feature feature=new Feature();

    boolean classed=false,responseOf=false;
    // position for evey structure
    int position=0,position1=0,position2=0,position3=0,position4=0;
    // position of every category
    int positionCategory=0,positionCategory1=0,positionCategory2=0,positionCategory3=0,positionCategory4=0;
    // to store the cpt of each category to avoid the ambiguity when we change the forStructure
    long cat=0,cat1=0,cat2=0,cat3=0,cat4=0;
    // to know if the user change the category so if yes we need to reinitialise the categories to the first value;
    // and make new references
    int click=-1,click1=-1,click2=-1,click3=-1,click4=-1;
    // reference for every structure
    String reference="",reference1="",reference2="",reference3="",reference4="";
    Date time;
    MailViewModel mailViewModel=new MailViewModel();
    // to add forStructures and categories
    List<Category> categoryListToSend;
    List<String> forStructureListToSend,referenceToSend;


    public AddDocumentToSendFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mailViewModel= ViewModelProviders.of(requireActivity()).get(MailViewModel.class);
        categoryViewModel=ViewModelProviders.of(requireActivity()).get(CategoryViewModel.class);
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddDocumentToSendBinding.inflate(inflater,container,false);

        initializeSpinners();

        time=Calendar.getInstance().getTime();
        simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (time!=null)
            binding.edtEntryDate.setText(simpleDateFormat.format(time));

        categoryInfoBeforeChanges=new ArrayList<>();
        categoryInfoAfterChanges=new ArrayList<>();

        categoryListToSend=new ArrayList<>();
        forStructureListToSend =new ArrayList<>();
        referenceToSend=new ArrayList<>();

        if (categoryViewModel.getLiveGetSendCategoryInfo().getValue()!=null)
            categoryInfoBeforeChanges.addAll(categoryViewModel.getLiveGetSendCategoryInfo().getValue());

        //this is gonna make a pointer to the element categoryInfoAfterChanges.addAll(categoryInfoBeforeChanges);   so
        for (int i=0;i<categoryInfoBeforeChanges.size();i++){
            CategoryInfo categoryInfo=categoryInfoBeforeChanges.get(i);
            categoryInfoAfterChanges.add(new CategoryInfo(categoryInfo.getId(),categoryInfo.getCpt(),categoryInfo.getCode(),categoryInfo.getDesignation()));
        }


        //RecyclerView
        data=new ArrayList<>();
        recyclerViewEmail=new RecyclerViewEmail(data);
        binding.rvSendDocument.setAdapter(recyclerViewEmail);
        binding.rvSendDocument.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvSendDocument.setHasFixedSize(true);

        //Get MailResponse
        ActivityResultLauncher<String[]> arl = registerForActivityResult(new ActivityResultContracts.OpenDocument(), result -> {
            if (result != null) {
                binding.rvSendDocument.setVisibility(View.VISIBLE);
                recyclerViewEmail.add(result);
            }
        });

        //get Data from spinner

        manageForStructure();
        manageCategories();

        binding.spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priority=(String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type=(String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.imgSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checked()){
                    if (!data.isEmpty())
                        uploadPdf();
                    else
                        Toast.makeText(requireActivity(), "add document", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireActivity(), "fill the void", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.floatAddPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putBoolean("shouldInvokeOnStop",false).commit();
                arl.launch(new String[]{"application/pdf"});

            }
        });

        binding.checkResponse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b)
                    binding.relativeResponseOf.setVisibility(View.GONE);
                else
                    binding.relativeResponseOf.setVisibility(View.VISIBLE);
                responseOf=b;
            }
        });

        binding.checkBoxClassed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                classed=b;
            }
        });

        showCalenderInTextView(binding.cardDateDepart,binding.txtDepartDate);

        showCalenderInTextView(binding.cardDateMail,binding.txtMailDate);

        getReferences(userDetails.getStructure_id());

        binding.imgAddStructureSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLayout();
            }
        });

        binding.imgMinusStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideLayout();
            }
        });

        return binding.getRoot();
    }

    private void manageForStructure() {
        binding.spinnerForStructure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position=i;
                if (!categoryInfoAfterChanges.isEmpty() && !structure.isEmpty()) {
                    makeAReferenceForStructure(position,binding.edtInternalReference,cat);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerForStructure1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position1=i;
                if (!categoryInfoAfterChanges.isEmpty() && !structure.isEmpty()) {
                    makeAReferenceForStructure(position1,binding.edtInternalReference1,cat1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerForStructure2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position2=i;
                if (!categoryInfoAfterChanges.isEmpty() && !structure.isEmpty()) {
                    makeAReferenceForStructure(position2,binding.edtInternalReference2,cat2);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerForStructure3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position3=i;
                if (!categoryInfoAfterChanges.isEmpty() && !structure.isEmpty()) {
                    makeAReferenceForStructure(position3,binding.edtInternalReference3,cat3);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerForStructure4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position4=i;
                if (!categoryInfoAfterChanges.isEmpty() && !structure.isEmpty()) {
                    makeAReferenceForStructure(position4,binding.edtInternalReference4,cat4);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void manageCategories() {
        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionCategory=i;
                click++;
                if (!categoryInfoAfterChanges.isEmpty() && !structure.isEmpty())
                    makeAReference(position,positionCategory,binding.edtInternalReference);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerCategory1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionCategory1=i;
                click1++;
                if (!categoryInfoAfterChanges.isEmpty() && !structure.isEmpty())
                    makeAReference(position1,positionCategory1,binding.edtInternalReference1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerCategory2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionCategory2=i;
                click2++;
                if (!categoryInfoAfterChanges.isEmpty() && !structure.isEmpty())
                    makeAReference(position2,positionCategory2,binding.edtInternalReference2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerCategory3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionCategory3=i;
                click3++;
                if (!categoryInfoAfterChanges.isEmpty() && !structure.isEmpty())
                    makeAReference(position3,positionCategory3,binding.edtInternalReference3);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerCategory4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionCategory4=i;
                click4++;
                if (!categoryInfoAfterChanges.isEmpty() && !structure.isEmpty())
                    makeAReference(position4,positionCategory4,binding.edtInternalReference4);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void uploadPdf() {
     if (!data.isEmpty()) {
         binding.imgSendMail.setVisibility(View.GONE);
         binding.progressAddDocument.setVisibility(View.VISIBLE);
         enableComponent(false);
         List<DocumentFile> documentFiles=new ArrayList<>();
         for (int i=0;i<data.size();i++) {
             DocumentFile file = DocumentFile.fromSingleUri(requireActivity(), data.get(i));
             if (file != null && file.exists())
                 documentFiles.add(file);
         }
         if (!documentFiles.isEmpty())
             uploadToServer(documentFiles);
         else
             Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();

     }
}

    private void enableComponent(boolean b) {
        binding.edtInternalReference.setEnabled(b);
        binding.edtInternalReference1.setEnabled(b);
        binding.edtInternalReference2.setEnabled(b);
        binding.edtInternalReference3.setEnabled(b);
        binding.edtInternalReference4.setEnabled(b);
        binding.edtResponseOf.getEditText().setEnabled(b);
        binding.floatAddPdf.setEnabled(b);
        binding.cardDateDepart.setEnabled(b);
        binding.cardDateMail.setEnabled(b);
        binding.spinnerCategory.setEnabled(b);
        binding.spinnerCategory1.setEnabled(b);
        binding.spinnerCategory2.setEnabled(b);
        binding.spinnerCategory3.setEnabled(b);
        binding.spinnerCategory4.setEnabled(b);
        binding.spinnerForStructure.setEnabled(b);
        binding.spinnerForStructure1.setEnabled(b);
        binding.spinnerForStructure2.setEnabled(b);
        binding.spinnerForStructure3.setEnabled(b);
        binding.spinnerForStructure4.setEnabled(b);
        binding.spinnerType.setEnabled(b);
        binding.spinnerPriority.setEnabled(b);
        binding.checkBoxClassed.setEnabled(b);
        binding.checkResponse.setEnabled(b);
        binding.edtObject.setEnabled(b);
        binding.rvSendDocument.setEnabled(b);
    }

    private void makeAReference(int pos, int posCategory, TextInputEditText textInputEditText) {
        if (time != null) {
            if (click>0 || click1>0 || click2>0 || click3>0 || click4>0 ) {
                categoryInfoAfterChanges.clear();
                for (int i=0;i<categoryInfoBeforeChanges.size();i++){
                    CategoryInfo categoryInfo=categoryInfoBeforeChanges.get(i);
                    categoryInfoAfterChanges.add(new CategoryInfo(categoryInfo.getId(),categoryInfo.getCpt(),categoryInfo.getCode(),categoryInfo.getDesignation()));
                }
                reinitialiseReferences(position,positionCategory,binding.edtInternalReference);
                reinitialiseReferences(position1,positionCategory1,binding.edtInternalReference1);
                reinitialiseReferences(position2,positionCategory2,binding.edtInternalReference2);
                reinitialiseReferences(position3,positionCategory3,binding.edtInternalReference3);
                reinitialiseReferences(position4,positionCategory4,binding.edtInternalReference4);
                click=0;click1=0;click2=0;click3=0;click4=0;
            }else {
                String date = simpleDateFormat1.format(time) + "";
                CategoryInfo categoryInfo=categoryInfoAfterChanges.get(posCategory);
                String reference = reachTheNumberOfBits(String.valueOf(categoryInfo.getCpt()));
                reference = "ATM/" + userDetails.getStructureCode() + "/" + structure.get(pos).getCode() + "/".concat(reference).concat("/") + date.charAt(2) + "" + date.charAt(3);
                textInputEditText.setText(reference);
                initializeCounter(textInputEditText,categoryInfo.getCpt());
                categoryInfoAfterChanges.get(posCategory).setCpt(categoryInfo.getCpt() + 1);
            }
        }
    }

    // to avoid the ambiguity when when the forStructure
    private void initializeCounter(TextInputEditText textInputEditText, long cpt){
        if (binding.edtInternalReference.equals(textInputEditText)) {
            cat = cpt;
        } else if (binding.edtInternalReference1.equals(textInputEditText)) {
            cat1 = cpt;
        } else if (binding.edtInternalReference2.equals(textInputEditText)) {
            cat2 = cpt;
        } else if (binding.edtInternalReference3.equals(textInputEditText)) {
            cat3 = cpt;
        } else if (binding.edtInternalReference4.equals(textInputEditText)) {
            cat4 = cpt;
        }
    }

    private void makeAReferenceForStructure(int pos,TextInputEditText textInputEditText,long cpt){
        String date = simpleDateFormat1.format(time) + "";
        String reference = reachTheNumberOfBits(String.valueOf(cpt));
        reference = "ATM/" + userDetails.getStructureCode() + "/" + structure.get(pos).getCode() + "/".concat(reference).concat("/") + date.charAt(2) + "" + date.charAt(3);
        textInputEditText.setText(reference);
    }

    private void reinitialiseReferences(int position,int positionCategory,TextInputEditText textInputEditText){
        String date = simpleDateFormat1.format(time) + "";
        String reference = reachTheNumberOfBits(String.valueOf(categoryInfoAfterChanges.get(positionCategory).getCpt()));
        reference = "ATM/"+userDetails.getStructureCode()+"/" + structure.get(position).getCode() + "/".concat(reference).concat("/") + date.charAt(2) + "" + date.charAt(3);
        textInputEditText.setText(reference);
        initializeCounter(textInputEditText,categoryInfoAfterChanges.get(positionCategory).getCpt());
        categoryInfoAfterChanges.get(positionCategory).setCpt(categoryInfoAfterChanges.get(positionCategory).getCpt()+1);
    }

    private void addCategoriesAndStructuresAndReferences(){
        initialiseInternalReferences();
        forStructureListToSend.clear();
        categoryListToSend.clear();
        referenceToSend.clear();
        forStructureListToSend.add(items.get(position));
        categoryListToSend.add(new Category(categoryInfoAfterChanges.get(positionCategory).getId()));
        referenceToSend.add(reference);
        if (binding.linearForStructure1.getVisibility()==View.VISIBLE){
            forStructureListToSend.add(items.get(position1));
            categoryListToSend.add(new Category(categoryInfoAfterChanges.get(positionCategory1).getId()));
            referenceToSend.add(reference1);
        }
        if (binding.linearForStructure2.getVisibility()==View.VISIBLE){
            forStructureListToSend.add(items.get(position2));
            categoryListToSend.add(new Category(categoryInfoAfterChanges.get(positionCategory2).getId()));
            referenceToSend.add(reference2);
        }
        if (binding.linearForStructure3.getVisibility()==View.VISIBLE){
            forStructureListToSend.add(items.get(position3));
            categoryListToSend.add(new Category(categoryInfoAfterChanges.get(positionCategory3).getId()));
            referenceToSend.add(reference3);
        }
        if (binding.linearForStructure4.getVisibility()==View.VISIBLE){
            forStructureListToSend.add(items.get(position4));
            categoryListToSend.add(new Category(categoryInfoAfterChanges.get(positionCategory4).getId()));
            referenceToSend.add(reference4);
        }
    }

    private void uploadToServer(List<DocumentFile> documentFiles) {

        // add categories and structure concern by the mail
        addCategoriesAndStructuresAndReferences();

        MailDetails detail;
        List<MultipartBody.Part> list=new ArrayList<>();
        for (int i=0;i<documentFiles.size();i++) {
            try {

                // Open an InputStream to read the contents of the file
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(documentFiles.get(i).getUri());

                // Use Okio library to read the contents of the InputStream into a BufferedSource object
                BufferedSource bufferedSource = Okio.buffer(Okio.source(inputStream));
                RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), bufferedSource.readByteString());
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", documentFiles.get(i).getName(), requestFile);
                list.add(body);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!list.isEmpty()) {
            detail = new MailDetails(new User(userDetails.getId()),new Structure(userDetails.getStructure_id()),categoryListToSend, forStructureListToSend,referenceToSend,binding.edtEntryDate.getText().toString(), binding.txtDepartDate.getText().toString()
                    , binding.txtMailDate.getText().toString(), binding.edtObject.getText().toString(), binding.edtRecipient.getText().toString()
                    , priority, type, binding.edtResponseOf.getEditText().getText().toString(), false, false, responseOf, classed, false);

            Gson gson = new Gson();
            String json = gson.toJson(detail);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

            mailViewModel.createMail(list, requestBody);

            mailViewModel.getLiveCreateMail().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        navController.popBackStack(R.id.sendFragment, false);
                    } else {
                        binding.progressAddDocument.setVisibility(View.GONE);
                        binding.imgSendMail.setVisibility(View.VISIBLE);
                        enableComponent(true);
                        Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showCalenderInTextView(CardView cardView,TextView txt){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feature.showCalenderInTextView(requireActivity(),txt);
            }
        });
    }

    private void initializeSpinners(){

        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerForStructure.setAdapter(adapter);
        binding.spinnerForStructure1.setAdapter(adapter);
        binding.spinnerForStructure2.setAdapter(adapter);
        binding.spinnerForStructure3.setAdapter(adapter);
        binding.spinnerForStructure4.setAdapter(adapter);
        ArrayAdapter<String> adapterCategory=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,sendCategoryDesignation);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapterCategory);
        binding.spinnerCategory1.setAdapter(adapterCategory);
        binding.spinnerCategory2.setAdapter(adapterCategory);
        binding.spinnerCategory3.setAdapter(adapterCategory);
        binding.spinnerCategory4.setAdapter(adapterCategory);
        ArrayAdapter<CharSequence> adapterPriority=ArrayAdapter.createFromResource(getContext(),R.array.priotity,android.R.layout.simple_spinner_item);
        adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPriority.setAdapter(adapterPriority);
        ArrayAdapter<CharSequence> adapterType=ArrayAdapter.createFromResource(getContext(),R.array.type,android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerType.setAdapter(adapterType);
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

    private void getReferences(long id){
        mailViewModel.reference(id);
        mailViewModel.getLiveReferences().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (!strings.isEmpty())
                    binding.edtResponseOf.setList(strings);
            }
        });

    }

    private void showLayout(){
        if (binding.linearForStructure1.getVisibility()==View.GONE){
            binding.linearForStructure1.setVisibility(View.VISIBLE);
            binding.linearCategory1.setVisibility(View.VISIBLE);
            binding.textFieldInternalReference1.setVisibility(View.VISIBLE);
            binding.imgMinusStructure.setVisibility(View.VISIBLE);
        }else{
            if (binding.linearForStructure2.getVisibility()==View.GONE){
                binding.linearForStructure2.setVisibility(View.VISIBLE);
                binding.linearCategory2.setVisibility(View.VISIBLE);
                binding.textFieldInternalReference2.setVisibility(View.VISIBLE);
            }else{
                if (binding.linearForStructure3.getVisibility()==View.GONE){
                    binding.linearForStructure3.setVisibility(View.VISIBLE);
                    binding.linearCategory3.setVisibility(View.VISIBLE);
                    binding.textFieldInternalReference3.setVisibility(View.VISIBLE);
                }else{
                    if (binding.linearForStructure4.getVisibility()==View.GONE){
                        binding.linearForStructure4.setVisibility(View.VISIBLE);
                        binding.linearCategory4.setVisibility(View.VISIBLE);
                        binding.textFieldInternalReference4.setVisibility(View.VISIBLE);
                        binding.imgAddStructureSend.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void hideLayout(){
        if (binding.linearForStructure4.getVisibility()==View.VISIBLE){
            binding.linearForStructure4.setVisibility(View.GONE);
            binding.linearCategory4.setVisibility(View.GONE);
            binding.textFieldInternalReference4.setVisibility(View.GONE);
            binding.imgAddStructureSend.setVisibility(View.VISIBLE);
        }else{
            if (binding.linearForStructure3.getVisibility()==View.VISIBLE){
                binding.linearForStructure3.setVisibility(View.GONE);
                binding.linearCategory3.setVisibility(View.GONE);
                binding.textFieldInternalReference3.setVisibility(View.GONE);
            }else{
                if (binding.linearForStructure2.getVisibility()==View.VISIBLE){
                    binding.linearForStructure2.setVisibility(View.GONE);
                    binding.linearCategory2.setVisibility(View.GONE);
                    binding.textFieldInternalReference2.setVisibility(View.GONE);
                }else{
                    if (binding.linearForStructure1.getVisibility()==View.VISIBLE){
                        binding.linearForStructure1.setVisibility(View.GONE);
                        binding.linearCategory1.setVisibility(View.GONE);
                        binding.textFieldInternalReference1.setVisibility(View.GONE);
                        binding.imgMinusStructure.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void initialiseInternalReferences(){
        reference=binding.edtInternalReference.getText().toString();
        reference1=binding.edtInternalReference1.getText().toString();
        reference2=binding.edtInternalReference2.getText().toString();
        reference3=binding.edtInternalReference3.getText().toString();
        reference4=binding.edtInternalReference4.getText().toString();
    }

    private boolean checked(){
        if (binding.relativeResponseOf.getVisibility()==View.VISIBLE){
            return binding.edtEntryDate.getText().toString().isEmpty()
                    || binding.edtRecipient.getText().toString().isEmpty()
                    || binding.edtObject.getText().toString().isEmpty()
                    || binding.txtDepartDate.getText().toString().isEmpty()
                    || binding.txtMailDate.getText().toString().isEmpty()
                    || binding.edtResponseOf.getEditText().getText().toString().isEmpty();
        }else{
            return binding.edtEntryDate.getText().toString().isEmpty()
                    || binding.edtRecipient.getText().toString().isEmpty()
                    || binding.edtObject.getText().toString().isEmpty()
                    || binding.txtDepartDate.getText().toString().isEmpty()
                    || binding.txtMailDate.getText().toString().isEmpty();
        }

    }
}