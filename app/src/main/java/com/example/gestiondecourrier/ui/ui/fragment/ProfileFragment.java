package com.example.gestiondecourrier.ui.ui.fragment;


import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.editor;
import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.sp;
import static com.example.gestiondecourrier.ui.ui.MainActivity.bitmap;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;
import static com.example.gestiondecourrier.ui.ui.MainActivity.userId;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentProfileBinding;
import com.example.gestiondecourrier.ui.LocaleHelper;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.ui.AuthenticationActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.UserViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSource;
import okio.Okio;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    Uri uri=null;
    Context context;
    boolean clicked=false,clickedN=false;
    Feature feature=new Feature();

    UserViewModel userViewModel=new UserViewModel();
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userViewModel= ViewModelProviders.of(requireActivity()).get(UserViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false);

        feature.profilePicture(bitmap,userDetails.getName().charAt(0),binding.imgUserProfile);

        if (sp.getString("LanguageSelected","fr").equals("fr")){
            binding.cardFr.setCardBackgroundColor(Color.rgb(149,213,253));
            binding.cardEn.setCardBackgroundColor(Color.rgb(255,255,255));
        }else{
            binding.cardEn.setCardBackgroundColor(Color.rgb(149,213,253));
            binding.cardFr.setCardBackgroundColor(Color.rgb(255,255,255));
        }
        if (sp.getBoolean("notification",true))
            binding.cardNotification.setCardBackgroundColor(Color.rgb(149,213,253));
        else
            binding.cardNotification.setCardBackgroundColor(Color.rgb(255, 255, 255));


        binding.imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.imgLogOut.setVisibility(View.GONE);
                if (binding.progressLogout!=null)
                    binding.progressLogout.setVisibility(View.VISIBLE);
                userViewModel.logout();
                enableComponent(false);
                userViewModel.getLiveLogout().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean){
                            if (getActivity()!=null) {
                                editor.putString("jwtToken",null).commit();
                                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                binding.imgLogOut.setVisibility(View.GONE);
                                binding.imgLogOut.setVisibility(View.VISIBLE);
                                enableComponent(true);
                                Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            binding.progressLogout.setVisibility(View.GONE);
                            binding.imgLogOut.setVisibility(View.VISIBLE);
                            enableComponent(true);
                            Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        ActivityResultLauncher<String[]> arl=registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result!=null) {
                    binding.imgUpdate.setImageResource(R.drawable.ic_check);
                    binding.imgUserProfile.setImageURI(result);
                    uri = result;
                }
            }
        });


        binding.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri!=null)
                    if (!clicked) {
                        uploadToServer(uri);
                    }else{
                        if (getActivity()!=null)
                            Toast.makeText(getActivity(), "wait", Toast.LENGTH_SHORT).show();
                    }
                else {
                    editor.putBoolean("shouldInvokeOnStop",false).commit();
                    arl.launch(new String[]{"image/*"});
                }
            }
        });

        binding.imgUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch(new String[]{"image/*"});
            }
        });

        if (userDetails!=null){
            binding.txtNameProfile.setText(userDetails.getName().toUpperCase(Locale.ROOT).concat(" "+userDetails.getFirstName()));
            binding.edtEmailProfile.setText(userDetails.getEmail());
            binding.edtStructureProfile.setText(userDetails.getStructureDesignation());
            binding.edtJobProfile.setText(userDetails.getFun());

        }


        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.allDocumentsFragment,false);
            }
        });

        binding.cardFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity()!=null) {
                    context = LocaleHelper.setLocale(getActivity(), "fr");
                    Toast.makeText(getActivity(), getResources().getString(R.string.language_changed)+"", Toast.LENGTH_SHORT).show();
                    binding.cardFr.setCardBackgroundColor(Color.rgb(149,213,253));
                    binding.cardEn.setCardBackgroundColor(Color.rgb(255,255,255));
                }
            }
        });

        binding.cardEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity()!=null) {
                    context = LocaleHelper.setLocale(getActivity(), "en");
                    Toast.makeText(getActivity(), getResources().getString(R.string.language_changed)+"", Toast.LENGTH_SHORT).show();
                    binding.cardEn.setCardBackgroundColor(Color.rgb(149,213,253));
                    binding.cardFr.setCardBackgroundColor(Color.rgb(255,255,255));
                }
            }
        });
        binding.cardNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userViewModel.updateNotification(userDetails.getId());
                clickedN=true;

            }
        });
        userViewModel.getLiveUpdateNotification().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    if (clickedN) {
                        if (!sp.getBoolean("notification", true))
                            binding.cardNotification.setCardBackgroundColor(Color.rgb(149, 213, 253));
                        else
                            binding.cardNotification.setCardBackgroundColor(Color.rgb(255, 255, 255));
                        editor.putBoolean("notification", !sp.getBoolean("notification", true)).commit();
                    }
                    clickedN=false;
                }
            }
        });
        return binding.getRoot();
    }


     private void uploadToServer(Uri uri) {
         if (getActivity() != null) {
             DocumentFile file = DocumentFile.fromSingleUri(getActivity(), uri);
             if (file != null) {
                 try {

                     // Open an InputStream to read the contents of the file
                     InputStream inputStream = getActivity().getContentResolver().openInputStream(file.getUri());

                     // Use Okio library to read the contents of the InputStream into a BufferedSource object
                     BufferedSource bufferedSource = Okio.buffer(Okio.source(inputStream));
                     RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), bufferedSource.readByteString());
                     MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                     userViewModel.updateUserPhoto(userDetails.getId(),body);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }

                 clicked=true;
                 if (getActivity() != null)
                     userViewModel.getLiveUpdateUserPhoto().observe(getActivity(), new Observer<Boolean>() {
                         @Override
                         public void onChanged(Boolean aBoolean) {
                             if (aBoolean) {
                                 userViewModel.getUserById(userId);
                                 if (getActivity()!=null)
                                     userViewModel.getLiveGetUser().observe(getActivity(), new Observer<UserResponse>() {
                                         @Override
                                         public void onChanged(UserResponse userResponse) {
                                             navController.popBackStack(R.id.allDocumentsFragment, false);
                                         }
                                     });

                             }
                             else
                                 if (getActivity()!=null) {
                                     Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                                     clicked=false;
                                 }
                         }
                     });
             }

         }
     }

     private void enableComponent(boolean b){
        binding.cardNotification.setEnabled(b);
        binding.cardEn.setEnabled(b);
        binding.cardFr.setEnabled(b);
        binding.imgUpdate.setEnabled(b);
        binding.imgUserProfile.setEnabled(b);
     }

}