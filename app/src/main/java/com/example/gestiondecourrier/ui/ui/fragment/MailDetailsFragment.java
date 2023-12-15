package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.editor;
import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;
import static com.example.gestiondecourrier.ui.ui.MainActivity.userId;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentMailDetailsBinding;
import com.example.gestiondecourrier.ui.pojo.Folder;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.pojo.Path;
import com.example.gestiondecourrier.ui.pojo.User;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewEmail;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewEmailBytes;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSource;
import okio.Okio;


public class MailDetailsFragment extends Fragment {

    FragmentMailDetailsBinding binding;
    RecyclerViewEmailBytes recyclerViewEmailBytes;
    RecyclerViewEmail recyclerViewEmail;
    List<Folder> data;
    ArrayList<Uri> list;
    String source="";
    MailViewModel mailViewModel=new MailViewModel();
    MailResponse mailResponse=null;
    int place=-1;
    public MailDetailsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mailViewModel= ViewModelProviders.of(requireActivity()).get(MailViewModel.class);
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentMailDetailsBinding.inflate(inflater,container,false);

        if (getArguments()!=null){
            place=getArguments().getInt("mailPosition");
            source=getArguments().getString("mailSource");
        }

        //Recycler View
        data=new ArrayList<>();
        recyclerViewEmailBytes=new RecyclerViewEmailBytes(data,source);
        binding.rvSendOldDocumentDetails.setAdapter(recyclerViewEmailBytes);
        binding.rvSendOldDocumentDetails.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvSendOldDocumentDetails.setHasFixedSize(true);




        if (place!=-1 && source!=null && !source.isEmpty()){
            switch (source){
                case "all" :
                    if (mailViewModel.getLiveAllMails().getValue()!=null && place <= mailViewModel.getLiveAllMails().getValue().size()-1)
                        mailResponse=mailViewModel.getLiveAllMails().getValue().get(place);
                    break;
                case "archive" :
                    if (mailViewModel.getLiveArchiveMails().getValue()!=null && place<=mailViewModel.getLiveArchiveMails().getValue().size()-1)
                        mailResponse=mailViewModel.getLiveArchiveMails().getValue().get(place);
                    break;
                case "favorite" :
                    if (mailViewModel.getLiveFavoriteMails().getValue()!=null && place<=mailViewModel.getLiveFavoriteMails().getValue().size()-1)
                        mailResponse=mailViewModel.getLiveFavoriteMails().getValue().get(place);
                    break;
                case "received" :
                    if (mailViewModel.getLiveReceivedMails().getValue()!=null && place<=mailViewModel.getLiveReceivedMails().getValue().size()-1)
                        mailResponse=mailViewModel.getLiveReceivedMails().getValue().get(place);
                    break;
                case "send" :
                    if (mailViewModel.getLiveSendMails().getValue()!=null && place<=mailViewModel.getLiveSendMails().getValue().size()-1)
                        mailResponse=mailViewModel.getLiveSendMails().getValue().get(place);
                    break;
                case "toTrait" :
                    if (mailViewModel.getLiveToTraitMails().getValue()!=null && place<=mailViewModel.getLiveToTraitMails().getValue().size()-1)
                        mailResponse=mailViewModel.getLiveToTraitMails().getValue().get(place);
                    break;
                case "trait" :
                    if (mailViewModel.getLiveTraitMails().getValue()!=null && place<=mailViewModel.getLiveTraitMails().getValue().size()-1)
                        mailResponse=mailViewModel.getLiveTraitMails().getValue().get(place);
                    break;
            }
        }

        if (source!=null && (!source.equals("received") && !source.equals("toTrait"))){
            binding.layoutEdtEntryDate.setVisibility(View.GONE);
            binding.layoutEdtMailReference.setVisibility(View.GONE);
            binding.layoutEdtObjectReceived.setVisibility(View.GONE);
        }else
            if (source!=null){
                binding.layoutEdtEntryDate.setVisibility(View.VISIBLE);
                binding.layoutEdtMailReference.setVisibility(View.VISIBLE);
                binding.layoutEdtObjectReceived.setVisibility(View.VISIBLE);
            }

        if (mailResponse!=null){
            setData(mailResponse);
            recyclerViewEmailBytes.clearData();
            for (int i = 0; i < mailResponse.getBytes().size(); i++) {
                recyclerViewEmailBytes.add(new Folder(mailResponse.getPaths().get(i),mailResponse.getFileName().get(i),mailResponse.getBytes().get(i),mailResponse.getPlaceInTheViewModel()));
            }
        }

        list=new ArrayList<>();
        recyclerViewEmail=new RecyclerViewEmail(list);
        binding.rvSendNewDocumentDetails.setAdapter(recyclerViewEmail);
        binding.rvSendNewDocumentDetails.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.rvSendNewDocumentDetails.setHasFixedSize(true);

        //Get MailResponse
        ActivityResultLauncher<String[]> arl = registerForActivityResult(new ActivityResultContracts.OpenDocument(), result -> {
            if (result != null) {
                list.add(result);
                binding.rvSendNewDocumentDetails.setVisibility(View.VISIBLE);
                binding.txtNew.setVisibility(View.VISIBLE);
                recyclerViewEmail.setData(list);
            }
        });

        binding.imgSendMailDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mailResponse!=null && (list.size()!=0 || recyclerViewEmailBytes.isChanged()) && (recyclerViewEmailBytes.getItemCount()!=0 || recyclerViewEmail.getItemCount()!=0))
                    uploadPdf();
                else
                    if ((recyclerViewEmailBytes.getItemCount()==0 || recyclerViewEmail.getItemCount()==0))
                        Toast.makeText(requireActivity(), "add some documents", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(requireActivity(), "You didn't change anything", Toast.LENGTH_SHORT).show();
            }
        });

        binding.floatAddPdfDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mailResponse!=null){
                    editor.putBoolean("shouldInvokeOnStop",false).commit();
                    arl.launch(new String[]{"application/pdf"});
                }



                //important

//                if (getActivity()!=null)
//                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest
//                            .permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
//                        arl.launch(new String[]{"application/pdf"});
//                    else
//                        Toast.makeText(getContext(), "no", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    void setData(MailResponse mail){
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        binding.edtStructurePdfDetails.setText(mail.getForStructure());
        binding.edtEntryDatePdfDetails.setText(mail.getEntryDateToShow());
        binding.edtDepartureDatePdfDetails.setText(simpleDateFormat1.format(mail.getDepartureDate()));
        if (mail.getEntryDateReceivedToShow()!=null)
            binding.edtEntryDateReceivedPdfDetails.setText(mail.getEntryDateReceivedToShow());
        binding.edtObjectReceivedPdfDetails.setText(mail.getObjectReceived());
        binding.edtMailReferencePdfDetails.setText(mail.getMailReference());
        binding.edtMailDatePdfDetails.setText(simpleDateFormat1.format(mail.getMailDate()));
        binding.edtInternalReferencePdfDetails.setText(mail.getInternalReference());
        binding.edtObjectPdfDetails.setText(mail.getObject());
        binding.edtRecipientPdfDetails.setText(mail.getRecipient());
        binding.edtPriorityPdfDetails.setText(mail.getPriority());
        binding.edtTypeDocumentPdfDetails.setText(mail.getType());
        if (!source.equals("received") && !source.equals("toTrait"))
            binding.edtCategoryPdfDetails.setText(mail.getCategory());
        else
            binding.edtCategoryPdfDetails.setText(mail.getReceivedCategory());
        if (mail.isResponse()) {
            binding.checkResponseDetails.setChecked(true);
            binding.edtResponseOfPdfDetails.setVisibility(View.VISIBLE);
            binding.edtResponseOfPdfDetails.setText(mail.getResponseOf());
        }else{
            binding.checkResponseDetails.setChecked(false);
            binding.edtResponseOfPdfDetails.setVisibility(View.GONE);
        }
        binding.checkBoxClassedDetails.setChecked(mail.isClassed());
    }

    private void uploadPdf() {
        if (getActivity()!=null) {

            binding.imgSendMailDetails.setVisibility(View.GONE);
            binding.progressDocumentDetails.setVisibility(View.VISIBLE);
            enableComponent(false);

            List<DocumentFile> documentFiles=new ArrayList<>();
            for (int i=0;i<list.size();i++) {
                DocumentFile file = DocumentFile.fromSingleUri(getActivity(), list.get(i));
                if (file != null && file.exists())
                    documentFiles.add(file);
            }
            if (!documentFiles.isEmpty() || recyclerViewEmailBytes.isChanged()) {
                uploadToServer(documentFiles);
            }else {
                binding.progressDocumentDetails.setVisibility(View.GONE);
                binding.imgSendMailDetails.setVisibility(View.VISIBLE);
                enableComponent(true);
                Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // can we make a mail without pdfs


    private void uploadToServer(List<DocumentFile> documentFiles) {
        List<MultipartBody.Part> partList=new ArrayList<>();
        if (!documentFiles.isEmpty()) {

            for (int i = 0; i < documentFiles.size(); i++) {
                try {

                    // Open an InputStream to read the contents of the file
                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(documentFiles.get(i).getUri());

                    // Use Okio library to read the contents of the InputStream into a BufferedSource object
                    BufferedSource bufferedSource = Okio.buffer(Okio.source(inputStream));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), bufferedSource.readByteString());
                    MultipartBody.Part body = MultipartBody.Part.createFormData("updateFile", documentFiles.get(i).getName(), requestFile);
                    partList.add(body);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // make the newPath
            }
            // maybe we're gonna find an ambiguity when we have the same path  ex: CV.pdf

//            List<Folder> folders=recyclerViewEmailBytes.getData();
            List<Folder> delete = recyclerViewEmailBytes.getDelete();

            String deleted = "";
            for (int p = 0; p < delete.size(); p++) {
                if (p == 0) {
                    deleted = deleted.concat(delete.get(p).getPath());
                } else {
                    deleted = deleted.concat("," + delete.get(p).getPath());
                }
            }
            Path path1 = new Path(new User(userId), deleted, mailResponse.getInternalReference());

            Gson gson = new Gson();
            String json = gson.toJson(path1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
            mailViewModel.updateMail(mailResponse.getId(), partList, requestBody);
            mailViewModel.getLiveUpdateMail().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        Toast.makeText(requireActivity(), "updated", Toast.LENGTH_SHORT).show();
                        backToFragment();
                    } else {
                        binding.imgSendMailDetails.setVisibility(View.VISIBLE);
                        binding.progressDocumentDetails.setVisibility(View.GONE);
                        enableComponent(true);
                        Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void enableComponent(boolean b) {
        binding.rvSendOldDocumentDetails.setEnabled(b);
        binding.rvSendNewDocumentDetails.setEnabled(b);
    }

    private void backToFragment(){
        if (place!=-1 && source!=null && !source.isEmpty()){
            switch (source){
                case "all" :
                    navController.popBackStack(R.id.allDocumentsFragment,false);
                    break;
                case "archive" :
                    navController.popBackStack(R.id.archiveFragment,false);
                    break;
                case "favorite" :
                    navController.popBackStack(R.id.favoriteFragment,false);
                    break;
                case "received" :
                    navController.popBackStack(R.id.receivedFragment,false);
                    break;
                case "send" :
                    navController.popBackStack(R.id.sendFragment,false);
                    break;
                case "toTrait" :
                    navController.popBackStack(R.id.toTraitFileFragment,false);
                    break;
                case "trait" :
                    navController.popBackStack(R.id.traitFileFragment,false);
                    break;
            }
        }
    }

}