package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.editor;
import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;
import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentSendEmailBinding;
import com.example.gestiondecourrier.ui.pojo.Email;
import com.example.gestiondecourrier.ui.pojo.Gmail;
import com.example.gestiondecourrier.ui.pojo.User;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewEmail;
import com.example.gestiondecourrier.ui.ui.viewmodel.EmailViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.GmailViewModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSource;
import okio.Okio;


public class SendEmailFragment extends Fragment {

    FragmentSendEmailBinding binding;
    RecyclerViewEmail recyclerViewEmail;
    ArrayList<Uri> data;
    GmailViewModel gmailViewModel=new GmailViewModel();
    EmailViewModel emailViewModel=new EmailViewModel();
    public SendEmailFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        gmailViewModel= ViewModelProviders.of(requireActivity()).get(GmailViewModel.class);
        emailViewModel= ViewModelProviders.of(requireActivity()).get(EmailViewModel.class);
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentSendEmailBinding.inflate(inflater,container,false);

        if (isAdded() && getActivity() != null) {
            data=new ArrayList<>();
//            intent=new Intent(Intent.ACTION_SEND_MULTIPLE);
//            intent.setType("application/pdf");
            recyclerViewEmail=new RecyclerViewEmail(data);
            binding.rvEmail.setAdapter(recyclerViewEmail);
            binding.rvEmail.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            binding.rvEmail.setHasFixedSize(true);
            ActivityResultLauncher<String> arl=registerForActivityResult(new ActivityResultContracts.GetContent()
                    , new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri result) {
                            if (result!=null){
                                binding.rvEmail.setVisibility(View.VISIBLE);
                                recyclerViewEmail.add(result);
                            }
                        }
                    });

            binding.imgAttachDocumentEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putBoolean("shouldInvokeOnStop",false).commit();
                    arl.launch("application/pdf");
                }
            });

            binding.imgBackEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getParentFragmentManager().popBackStack();
                }
            });

            binding.imgSendEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checked()){
                        if (!data.isEmpty())
                            uploadPdf();
                        else
                            sendASimpleEmail();
                    }else
                        Toast.makeText(requireActivity(), "fill the void", Toast.LENGTH_SHORT).show();


                }
            });

            gmailViewModel.getEmails();
            gmailViewModel.getLiveEmails().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> strings) {
                    binding.edtToEmail.setList(strings);
                }
            });

            binding.linearEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.edtToEmail.setVisibility();
                }
            });

        }

        return binding.getRoot();
    }

    private void uploadPdf() {
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

    private void uploadToServer(List<DocumentFile> documentFiles) {
        binding.relativeSendEmail.setVisibility(View.GONE);
        binding.progressAddEmail.setVisibility(View.VISIBLE);
        enableComponent(false);
        Gmail gmail;
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
        if (!list.isEmpty() && checked()) {
            gmail = new Gmail(new User(userDetails.getId()), binding.edtToEmail.getEditText().getText().toString(), binding.edtSubjectEmail
                    .getText().toString(), binding.edtComposeEmail.getText().toString());
            Gson gson = new Gson();
            String json = gson.toJson(gmail);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
            gmailViewModel.uploadPdfFromGmail(list, requestBody);

            gmailViewModel.getLiveUploadPdfFromGmail().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        Toast.makeText(requireActivity(), "sent", Toast.LENGTH_SHORT).show();
                        navController.popBackStack(R.id.allDocumentsFragment,false);
                    }else {
                        binding.relativeSendEmail.setVisibility(View.VISIBLE);
                        binding.progressAddEmail.setVisibility(View.GONE);
                        enableComponent(true);
                        Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean checked() {
        return !binding.edtSubjectEmail.getText().toString().isEmpty()
                && !binding.edtComposeEmail.getText().toString().isEmpty()
                && !binding.edtToEmail.getEditText().getText().toString().isEmpty();
    }

    private void enableComponent(boolean b){
        binding.edtToEmail.setEnabled(b);
        binding.edtComposeEmail.setEnabled(b);
        binding.edtSubjectEmail.setEnabled(b);
        binding.rvEmail.setEnabled(b);
    }

    private void sendASimpleEmail(){
        binding.relativeSendEmail.setVisibility(View.GONE);
        binding.progressAddEmail.setVisibility(View.VISIBLE);
        enableComponent(false);
        emailViewModel.sendEmail(new Email(binding.edtToEmail.getEditText().getText().toString(),
                binding.edtSubjectEmail.getText().toString(),
                binding.edtComposeEmail.getText().toString()));
        emailViewModel.getLiveSendEmail().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Toast.makeText(requireActivity(), "sent", Toast.LENGTH_SHORT).show();
                    navController.popBackStack(R.id.allDocumentsFragment,false);
                }else{
                    binding.progressAddEmail.setVisibility(View.GONE);
                    binding.relativeSendEmail.setVisibility(View.VISIBLE);
                    enableComponent(true);
                    Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}