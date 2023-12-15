package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentReportDetailsBinding;
import com.example.gestiondecourrier.ui.pojo.Report;
import com.example.gestiondecourrier.ui.ui.viewmodel.ReportViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;


public class ReportDetailsFragment extends Fragment {

    FragmentReportDetailsBinding binding;
    Report report=null;
    File pdfFile = null;
    int position=-1;
    ReportViewModel reportViewModel=new ReportViewModel();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        reportViewModel= ViewModelProviders.of(requireActivity()).get(ReportViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentReportDetailsBinding.inflate(inflater,container,false);

        if (getArguments()!=null ){
            position=getArguments().getInt("reportPosition");
            if (position!=-1) {
                if (userDetails.getStructureDesignation().equals("Direction des ressources humaines") && reportViewModel.getLiveAllReports().getValue()!=null)
                report=reportViewModel.getLiveAllReports().getValue().get(position);
                else
                    if (reportViewModel.getLiveReportsByStructure().getValue()!=null)
                        report=reportViewModel.getLiveReportsByStructure().getValue().get(position);

                if (report!=null) {
                    if (report.isApproved()) {
                        binding.btnApproved.setVisibility(View.GONE);
                        binding.imgChecked.setVisibility(View.VISIBLE);
                    } else {
                        binding.btnApproved.setVisibility(View.VISIBLE);
                        binding.imgChecked.setVisibility(View.GONE);
                    }
                    binding.pdfReport.fromBytes(report.getBytes()).load();
                }
            }
        }
        binding.imgPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (report!=null)
                    printPdf(report.getBytes());
            }
        });

        binding.btnApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (report!=null)
                    reportViewModel.updateApproved(report.getId());
                reportViewModel.getLiveUpdateApproved().observe(requireActivity(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            binding.btnApproved.setVisibility(View.GONE);
                            binding.imgChecked.setVisibility(View.VISIBLE);
                            Toast.makeText(requireActivity(), "approved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pdfFile!=null)
            pdfFile.delete();

    }
}