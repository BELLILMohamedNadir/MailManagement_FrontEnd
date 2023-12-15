package com.example.gestiondecourrier.ui.ui.fragment;


import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentShowPdfBinding;
import com.example.gestiondecourrier.ui.pojo.Folder;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.GmailViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ShowPdfFragment extends Fragment {

    FragmentShowPdfBinding binding;
    Folder folder;
    String uri;
    File pdfFile = null;
    MailViewModel mailViewModel=new MailViewModel();
    GmailViewModel gmailViewModel=new GmailViewModel();
    String source="";
    int position =-1,place=-1;
    public ShowPdfFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mailViewModel= ViewModelProviders.of(requireActivity()).get(MailViewModel.class);
        gmailViewModel= ViewModelProviders.of(requireActivity()).get(GmailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentShowPdfBinding.inflate(inflater,container,false);

        AdminActivity.navigationView.observe(requireActivity(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        if (getArguments()!=null && getArguments().getString("pdfUri")!=null){
            uri=getArguments().getString("pdfUri");
            binding.pdfShow.fromUri(Uri.parse(uri)).load();
            binding.imgPrintMail.setVisibility(View.GONE);
        }else{
            if (getArguments()!=null && getArguments().getString("source")!=null){
                source=getArguments().getString("source","");
                position =getArguments().getInt("mailPosition",-1);
                place =getArguments().getInt("folderPosition",-1);
                folder=dataDependToTheSource();
                if (folder==null)
                    navController.popBackStack(R.id.fileFragment,false);
                else
                    if (folder.getBytes()!=null)
                         binding.pdfShow.fromBytes(folder.getBytes()).load();
                    else
                        navController.popBackStack(R.id.fileFragment,false);

                binding.imgPrintMail.setVisibility(View.VISIBLE);
            }
        }

        binding.imgPrintMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (folder!=null)
                    printPdf(folder.getBytes());
            }
        });

        return binding.getRoot();
    }
    void printPdf(byte[] bytes){
        // Save the PDF content to a temporary file in device storage
        try {
            pdfFile = File.createTempFile("temp", ".pdf", requireActivity().getCacheDir()); // Create a temporary file
            FileOutputStream fos = new FileOutputStream(pdfFile);
            fos.write(bytes); // Write the PDF content to the file
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (pdfFile != null) {

            String authority = "com.example.gestiondecourrier.fileprovider";

            // Generate a content URI for the temporary file
            Uri contentUri = FileProvider.getUriForFile(requireContext(), authority, pdfFile);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/pdf");
            if (contentUri!=null)
                intent.putExtra(Intent.EXTRA_STREAM,contentUri); // Attach the PDF file to the Intent

            // Start the print activity
            startActivity(Intent.createChooser(intent, "Print PDF"));
        }

    }

    private Folder dataDependToTheSource() {
        if (source!=null && !source.isEmpty() && position!=-1)
            switch (source){
                case "all" :
                    if (mailViewModel.getLiveAllMails().getValue()!=null)
                        return new Folder(mailViewModel.getLiveAllMails().getValue().get(position).getFileName().get(place),mailViewModel.getLiveAllMails().getValue().get(position).getBytes().get(place));
                case "archive" :
                    if (mailViewModel.getLiveArchiveMails().getValue()!=null)
                        return new Folder(mailViewModel.getLiveArchiveMails().getValue().get(position).getFileName().get(place),mailViewModel.getLiveArchiveMails().getValue().get(position).getBytes().get(place));
                case "favorite" :
                    if (mailViewModel.getLiveFavoriteMails().getValue()!=null)
                        return new Folder(mailViewModel.getLiveFavoriteMails().getValue().get(position).getFileName().get(place),mailViewModel.getLiveFavoriteMails().getValue().get(position).getBytes().get(place));
                case "received" :
                    if (mailViewModel.getLiveReceivedMails().getValue()!=null)
                        return new Folder(mailViewModel.getLiveReceivedMails().getValue().get(position).getFileName().get(place),mailViewModel.getLiveReceivedMails().getValue().get(position).getBytes().get(place));
                case "send" :
                    if (mailViewModel.getLiveSendMails().getValue()!=null)
                        return new Folder(mailViewModel.getLiveSendMails().getValue().get(position).getFileName().get(place),mailViewModel.getLiveSendMails().getValue().get(position).getBytes().get(place));
                case "toTrait" :
                    if (mailViewModel.getLiveToTraitMails().getValue()!=null)
                        return new Folder(mailViewModel.getLiveToTraitMails().getValue().get(position).getFileName().get(place),mailViewModel.getLiveToTraitMails().getValue().get(position).getBytes().get(place));
                case "trait" :
                    if (mailViewModel.getLiveTraitMails().getValue()!=null)
                        return new Folder(mailViewModel.getLiveTraitMails().getValue().get(position).getFileName().get(place),mailViewModel.getLiveTraitMails().getValue().get(position).getBytes().get(place));
                case "gmailDetails":
                    if (gmailViewModel.getLiveGetGmail().getValue()!=null)
                        return new Folder(gmailViewModel.getLiveGetGmail().getValue().get(position).getFileName().get(place),gmailViewModel.getLiveGetGmail().getValue().get(position).getBytes().get(place));
            }

        return null;
    }


}