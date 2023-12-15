package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AdminActivity.navControllerAdmin;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.navigationView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentUserDetailsBinding;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.pojo.User;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class UserDetailsFragment extends Fragment {

    FragmentUserDetailsBinding binding;
    UserResponse user=null;
    int position=-1;
    String job="",endDate="";
    boolean first=true;
    List<Structure> struct;


    UserViewModel userViewModel=new UserViewModel();
    StructureViewModel structureViewModel=new StructureViewModel();

    public UserDetailsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userViewModel= ViewModelProviders.of(requireActivity()).get(UserViewModel.class);
        structureViewModel= ViewModelProviders.of(requireActivity()).get(StructureViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentUserDetailsBinding.inflate(inflater,container,false);
        navigationView.observe(getViewLifecycleOwner(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        enableComponent(false);
        struct=new ArrayList<>();
        if (getArguments()!=null){
            position=getArguments().getInt("userPosition");
            if (position!=-1 && userViewModel.getLiveGetUsers().getValue()!=null) {
                user = userViewModel.getLiveGetUsers().getValue().get(position);
                job = user.getRole();
                setData();
            }

        }

        binding.imgCheckUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.imgCheckUpdateDetails.setImageResource(R.drawable.ic_check);
                if (first){
                    enableComponent(true);
                    first=false;
                }else{
                    binding.imgCheckUpdateDetails.setVisibility(View.GONE);
                    binding.progressUserDetails.setVisibility(View.VISIBLE);
                    enableComponent(false);
                    //update the end date mof acting director or acting secretary
                    setEndDate(job);

                    if (!checked())
                        updateUser();
                    else {
                        Toast.makeText(requireActivity(), "You didn't update anything", Toast.LENGTH_SHORT).show();
                        binding.imgCheckUpdateDetails.setVisibility(View.VISIBLE);
                        binding.progressUserDetails.setVisibility(View.GONE);
                        enableComponent(true);
                    }
                }


            }
        });

        binding.relativeGeneratePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!first) {
                    enableComponent(false);
                    binding.imgCheckUpdateDetails.setVisibility(View.GONE);
                    binding.progressUserDetails.setVisibility(View.VISIBLE);
                    userViewModel.generatePassword(user.getId());
                    userViewModel.getLiveGeneratePassword().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (aBoolean){
                                Toast.makeText(requireActivity(), "verify your email", Toast.LENGTH_SHORT).show();
                                navControllerAdmin.popBackStack(R.id.userFragment,false);
                            }else {
                                enableComponent(true);
                                binding.imgCheckUpdateDetails.setVisibility(View.VISIBLE);
                                binding.progressUserDetails.setVisibility(View.GONE);
                                Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

        binding.imgBackUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navControllerAdmin.popBackStack(R.id.userFragment,false);
            }
        });

        getStructure();

        return binding.getRoot();
    }

    private boolean checked() {
        if (!user.getEndDate().isEmpty())
            if (binding.edtNameUserDetails.getText().toString().equals(user.getName())
            && binding.edtFirstNameUserDetails.getText().toString().equals(user.getFirstName())
            && binding.edtFunctionUser.getText().toString().equals(user.getFun())
                    && endDate.equals(user.getEndDate())
                    && struct.get(binding.edtStructureUserDetails.getPosition()).getId()==user.getStructure_id())
                return true;
        else
                return binding.edtNameUserDetails.getText().toString().equals(user.getName())
                        && binding.edtFirstNameUserDetails.getText().toString().equals(user.getFirstName())
                        && binding.edtFunctionUser.getText().toString().equals(user.getFun())
                        && struct.get(binding.edtStructureUserDetails.getPosition()).getId()==user.getStructure_id();
        return false;
    }


    public void setEndDate(String job) {
        if (job.equals("acting_director"))
            endDate=binding.edtEndDateActingDirectory.getText().toString();
        else
            if (job.equals("acting_secretary"))
                endDate=binding.edtEndDateActingSecretary.getText().toString();
    }

    void setData(){
        binding.checkBoxAdmin.setEnabled(false);
        binding.checkBoxDirector.setEnabled(false);
        binding.checkBoxActingDirector.setEnabled(false);
        binding.checkBoxSecretary.setEnabled(false);
        binding.checkBoxActingSecretary.setEnabled(false);

        binding.edtNameUserDetails.setText(user.getName());
        binding.edtFirstNameUserDetails.setText(user.getFirstName());
        binding.edtStructureUserDetails.getEditText().setText(user.getStructureDesignation());
        binding.edtFunctionUser.setText(user.getFun());
        binding.edtEmailUser.setText(user.getEmail());
        switch (user.getJob()) {
            case "admin":
                binding.checkBoxAdmin.setChecked(true);
                break;
            case "director":
                binding.checkBoxDirector.setChecked(true);
                break;
            case "acting_director":
                binding.checkBoxActingDirector.setChecked(true);
                binding.layoutActingDirectory.setVisibility(View.VISIBLE);
                binding.edtBeginDateActingDirectory.setText(user.getBeginDate());
                binding.edtBeginDateActingDirectory.setEnabled(false);
                binding.edtEndDateActingDirectory.setText(user.getEndDate());
                break;
            case "secretary":
                binding.checkBoxSecretary.setChecked(true);
                break;
            case "acting_secretary":
                binding.checkBoxActingSecretary.setChecked(true);
                binding.layoutActingSecretary.setVisibility(View.VISIBLE);
                binding.edtBeginDateActingSecretary.setText(user.getBeginDate());
                binding.edtBeginDateActingSecretary.setEnabled(false);
                binding.edtEndDateActingSecretary.setText(user.getEndDate());
                break;
        }

    }

    void updateUser(){
        if (!binding.edtStructureUserDetails.validateResult()){
            Toast.makeText(requireActivity(), "enter an existing structure", Toast.LENGTH_SHORT).show();
            binding.imgCheckUpdateDetails.setVisibility(View.VISIBLE);
            binding.progressUserDetails.setVisibility(View.GONE);
            enableComponent(true);
        }else{
            User user1=new User(struct.get(binding.edtStructureUserDetails.getPosition()),binding.edtNameUserDetails.getText().toString(),binding.edtFirstNameUserDetails.getText().toString()
                    ,binding.edtFunctionUser.getText().toString(),endDate);
            userViewModel.updateUserInfo(user.getId(),user1);
            userViewModel.getLiveUpdateUserInfo().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        Toast.makeText(requireActivity(), "updated", Toast.LENGTH_SHORT).show();
                        navControllerAdmin.popBackStack(R.id.userFragment, false);
                    } else {
                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                        binding.imgCheckUpdateDetails.setVisibility(View.VISIBLE);
                        binding.progressUserDetails.setVisibility(View.GONE);
                        enableComponent(true);
                    }
                }
            });
        }

    }

    private void enableComponent(boolean b){
        binding.edtNameUserDetails.setEnabled(b);
        binding.edtFirstNameUserDetails.setEnabled(b);
        binding.edtFunctionUser.setEnabled(b);
        binding.edtStructureUserDetails.getEditText().setEnabled(b);
        binding.relativeGeneratePassword.setEnabled(b);
    }

    private void getStructure(){
        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(getViewLifecycleOwner(), new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                List<String> list=new ArrayList<>();
                for(int i=0;i<structures.size();i++){
                    list.add(structures.get(i).getDesignation());
                    struct.add(structures.get(i));
                }
                binding.edtStructureUserDetails.setList(list);
            }
        });

    }
}