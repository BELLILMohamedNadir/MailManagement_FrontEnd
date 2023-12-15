package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;
import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;

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
import com.example.gestiondecourrier.databinding.FragmentDailyBinding;
import com.example.gestiondecourrier.ui.pojo.Report;
import com.example.gestiondecourrier.ui.ui.viewmodel.ReportViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class DailyFragment extends Fragment {


    FragmentDailyBinding binding;

    ReportViewModel reportViewModel=new ReportViewModel();

    File pdfFile = null;

    Report rep=null;

    public DailyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        reportViewModel= ViewModelProviders.of(requireActivity()).get(ReportViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentDailyBinding.inflate(inflater,container,false);

        reportViewModel.getDailyReport(userDetails.getStructure_id());
        reportViewModel.getLiveDailyReport().observe(getViewLifecycleOwner(), new Observer<Report>() {
            @Override
            public void onChanged(Report report) {
                rep=report;
                if (report!=null && report.getBytes()!=null && report.getBytes().length>0) {
                    binding.pdfReport.fromBytes(report.getBytes()).load();
                    binding.progressDaily.setVisibility(View.GONE);
                    binding.relativeDaily.setVisibility(View.VISIBLE);
                }else{
                    binding.relativeDaily.setVisibility(View.GONE);
                    binding.progressDaily.setVisibility(View.VISIBLE);
                    Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                    navController.popBackStack(R.id.reportFragment,false);
                }

            }
        });
        binding.imgPrintDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rep!=null)
                    printPdf(rep.getBytes());
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
}