package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AdminActivity.adminDetails;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.liveBitmapAdmin;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.adminId;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.navControllerAdmin;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.navigationView;
import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.editor;
import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.sp;
import static com.example.gestiondecourrier.ui.ui.MainActivity.bitmap;
import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentProfileAdminBinding;
import com.example.gestiondecourrier.ui.LocaleHelper;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.ui.AdminActivity;
import com.example.gestiondecourrier.ui.ui.AuthenticationActivity;
import com.example.gestiondecourrier.ui.ui.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSource;
import okio.Okio;


public class ProfileAdminFragment extends Fragment {
    FragmentProfileAdminBinding binding;
    Uri uri=null;
    Context context;
    boolean clicked=false;
    UserViewModel userViewModel=new UserViewModel();
    Feature feature=new Feature();

    public ProfileAdminFragment() {
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
        binding=FragmentProfileAdminBinding.inflate(inflater,container,false);


        if (sp.getString("LanguageSelected","fr").equals("fr")){
            binding.cardFrAdmin.setCardBackgroundColor(Color.rgb(149,213,253));
            binding.cardEnAdmin.setCardBackgroundColor(Color.rgb(255,255,255));
        }else{
            binding.cardEnAdmin.setCardBackgroundColor(Color.rgb(149,213,253));
            binding.cardFrAdmin.setCardBackgroundColor(Color.rgb(255,255,255));
        }

        adminDetails.observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse!=null)
                    feature.profilePicture(bitmap,userResponse.getName().charAt(0),binding.imgUserProfileAdmin);
            }
        });
        navigationView.observe(getViewLifecycleOwner(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.GONE);
            }
        });
        binding.imgLogOutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.imgLogOutAdmin.setVisibility(View.GONE);
                binding.progressLogoutAdmin.setVisibility(View.VISIBLE);
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
                                binding.progressLogoutAdmin.setVisibility(View.GONE);
                                binding.imgLogOutAdmin.setVisibility(View.VISIBLE);
                                enableComponent(true);
                                Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            binding.progressLogoutAdmin.setVisibility(View.GONE);
                            binding.imgLogOutAdmin.setVisibility(View.VISIBLE);
                            enableComponent(true);
                            Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.imgBackAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navControllerAdmin.popBackStack(R.id.userFragment,false);
            }
        });
        ActivityResultLauncher<String[]> arl=registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result!=null) {
                    binding.imgUpdateAdmin.setImageResource(R.drawable.ic_check);
                    binding.imgUserProfileAdmin.setImageURI(result);
                    uri = result;
                }
            }
        });

        binding.imgUpdateAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri!=null) {
                    if (!clicked) {
                        uploadToServer(uri);
                    } else {
                        Toast.makeText(requireActivity(), "wait", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    editor.putBoolean("shouldInvokeOnStop",false).commit();
                    arl.launch(new String[]{"image/*"});
                }

            }
        });

        liveBitmapAdmin.observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if (bitmap!=null)
                    binding.imgUserProfileAdmin.setImageBitmap(bitmap);

            }
        });



        binding.imgUserProfileAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch(new String[]{"image/*"});
            }
        });


        adminDetails.observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse!=null){
                    binding.txtNameProfileAdmin.setText(userResponse.getName().toUpperCase(Locale.ROOT).concat(" "+userResponse.getFirstName()));
                    binding.edtEmailProfileAdmin.setText(userResponse.getEmail());
                    binding.edtStructureProfileAdmin.setText(userResponse.getStructureDesignation());
                    binding.edtJobProfileAdmin.setText(userResponse.getFun());
                }
            }
        });

        binding.cardFrAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity()!=null) {
                    context = LocaleHelper.setLocale(getActivity(), "fr");
                    Toast.makeText(requireActivity(), getResources().getString(R.string.language_changed)+"", Toast.LENGTH_SHORT).show();
                    binding.cardFrAdmin.setCardBackgroundColor(Color.rgb(149,213,253));
                    binding.cardEnAdmin.setCardBackgroundColor(Color.rgb(255,255,255));
                }
            }
        });

        binding.cardEnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = LocaleHelper.setLocale(getActivity(), "en");
                Toast.makeText(requireActivity(), getResources().getString(R.string.language_changed)+"", Toast.LENGTH_SHORT).show();
                binding.cardEnAdmin.setCardBackgroundColor(Color.rgb(149,213,253));
                binding.cardFrAdmin.setCardBackgroundColor(Color.rgb(255,255,255));
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
                     if (adminId != -1)
                        userViewModel.updateUserPhoto(adminId,body);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 clicked=true;
                 userViewModel.getLiveUpdateUserPhoto().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                     @Override
                     public void onChanged(Boolean aBoolean) {
                         if (aBoolean) {
                             if (adminId!=-1)
                                 userViewModel.getUserById(adminId);
                             userViewModel.getLiveGetUser().observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
                                 @Override
                                 public void onChanged(UserResponse userResponse) {
                                     navControllerAdmin.popBackStack(R.id.userFragment, false);
                                 }
                             });
                         }
                         else {
                             Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                             clicked=false;
                         }
                     }
                 });

             }

         }
     }

    private void enableComponent(boolean b){
        binding.cardEnAdmin.setEnabled(b);
        binding.cardFrAdmin.setEnabled(b);
        binding.imgUpdateAdmin.setEnabled(b);
        binding.imgUserProfileAdmin.setEnabled(b);
    }

}