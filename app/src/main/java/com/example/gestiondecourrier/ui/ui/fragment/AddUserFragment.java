package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.AdminActivity.navControllerAdmin;
import static com.example.gestiondecourrier.ui.ui.AdminActivity.navigationView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentAddUserBinding;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.pojo.User;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnDateSelectedListener;
import com.example.gestiondecourrier.ui.ui.viewmodel.StructureViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddUserFragment extends Fragment {

    FragmentAddUserBinding binding;
    Calendar calender;
    int day,month,year;
    DatePickerDialog datePickerDialog;
    String job="",beginDate="",endDate="",role="";
    List<Structure> struct;
    UserViewModel userViewModel=new UserViewModel();
    StructureViewModel structureViewModel=new StructureViewModel();
    String date="";

    public AddUserFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userViewModel= ViewModelProviders.of(requireActivity()).get(UserViewModel.class);
        structureViewModel= ViewModelProviders.of(requireActivity()).get(StructureViewModel.class);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddUserBinding.inflate(inflater,container,false);

        navigationView.observe(getViewLifecycleOwner(), new Observer<BottomNavigationView>() {
            @Override
            public void onChanged(BottomNavigationView bottomNavigationView) {
                if (bottomNavigationView!=null)
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        showCalenderInTextView(binding.txtBeginDateActingDirectory);
        showCalenderInTextView(binding.txtEndDateActingDirectory);
        showCalenderInTextView(binding.txtBeginDateActingSecretary);
        showCalenderInTextView(binding.txtEndDateActingSecretary);

        closedCheckBox();

        struct=new ArrayList<>();
        binding.imgCheckAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmailUser.getText().toString()).matches())
                    binding.edtEmailUser.setError(getResources().getString(R.string.valid_email));
                else{
                    if (!job.isEmpty()) {
                        if (job.equals("acting_director") || job.equals("acting_secretary")) {
                            if ((!binding.txtBeginDateActingDirectory.getText().toString().isEmpty() &&
                                    !binding.txtEndDateActingDirectory.getText().toString().isEmpty())
                                    || (!binding.txtBeginDateActingSecretary.getText().toString().isEmpty() &&
                                    !binding.txtEndDateActingSecretary.getText().toString().isEmpty()))
                                addUser();
                            else
                                Toast.makeText(requireActivity(), "fill dates", Toast.LENGTH_SHORT).show();
                        } else {
                            addUser();
                        }
                    } else
                        Toast.makeText(requireActivity(), "Choose a job", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.checkBoxActingDirector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.layoutActingDirectory.getVisibility()==View.VISIBLE)
                    binding.layoutActingDirectory.setVisibility(View.GONE);
                else
                    binding.layoutActingDirectory.setVisibility(View.VISIBLE);
            }
        });

        binding.checkBoxActingSecretary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.layoutActingSecretary.getVisibility()==View.VISIBLE)
                    binding.layoutActingSecretary.setVisibility(View.GONE);
                else
                    binding.layoutActingSecretary.setVisibility(View.VISIBLE);
            }
        });

        binding.imgBackAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navControllerAdmin.popBackStack(R.id.userFragment,false);
            }
        });


        getStructures();

        return binding.getRoot();
    }

    private void getStructures() {
        structureViewModel.getStructures();
        structureViewModel.getLiveGetStructures().observe(getViewLifecycleOwner(), new Observer<List<Structure>>() {
            @Override
            public void onChanged(List<Structure> structures) {
                List<String> list=new ArrayList<>();
                struct=structures;
                for(int i=0;i<structures.size();i++)
                    list.add(structures.get(i).getDesignation());
                binding.edtStructureUser.setList(list);
            }
        });
    }


    public  void showCalender(OnDateSelectedListener listener){
        calender= Calendar.getInstance();
        day=calender.get(Calendar.DAY_OF_MONTH);
        month=calender.get(Calendar.MONTH);
        year=calender.get(Calendar.YEAR);
        datePickerDialog=new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String m=""+(i1 + 1);
                if (i1/10==0)
                    m="0"+m;
                if (i2<10)
                    date=i+"-"+m+"-0"+i2;
                else
                    date=i+"-"+m+"-"+i2;
                listener.onDateSelected(date);
            }
        },year,month,day);
        datePickerDialog.show();
    }

    void showCalenderInTextView(TextView txt){
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String date) {
                        txt.setText(date);
                    }
                });
            }
        });
    }

    void closedCheckBox(){
        binding.checkBoxAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    job="admin";
                    binding.checkBoxDirector.setEnabled(false);
                    binding.checkBoxActingDirector.setEnabled(false);
                    binding.checkBoxSecretary.setEnabled(false);
                    binding.checkBoxActingSecretary.setEnabled(false);
                }else{
                    job="";
                    binding.checkBoxDirector.setEnabled(true);
                    binding.checkBoxActingDirector.setEnabled(true);
                    binding.checkBoxSecretary.setEnabled(true);
                    binding.checkBoxActingSecretary.setEnabled(true);
                }

            }
        });
        binding.checkBoxDirector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    job="director";
                    binding.checkBoxAdmin.setEnabled(false);
                    binding.checkBoxActingDirector.setEnabled(false);
                    binding.checkBoxSecretary.setEnabled(false);
                    binding.checkBoxActingSecretary.setEnabled(false);
                }else{
                    job="";
                    binding.checkBoxAdmin.setEnabled(true);
                    binding.checkBoxActingDirector.setEnabled(true);
                    binding.checkBoxSecretary.setEnabled(true);
                    binding.checkBoxActingSecretary.setEnabled(true);
                }
            }
        });
        binding.checkBoxActingDirector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    job="acting_director";
                    binding.checkBoxAdmin.setEnabled(false);
                    binding.checkBoxDirector.setEnabled(false);
                    binding.checkBoxSecretary.setEnabled(false);
                    binding.checkBoxActingSecretary.setEnabled(false);
                    binding.txtBeginDateActingSecretary.setText("");
                    binding.txtEndDateActingSecretary.setText("");
                }else{
                    job="";
                    binding.checkBoxAdmin.setEnabled(true);
                    binding.checkBoxDirector.setEnabled(true);
                    binding.checkBoxSecretary.setEnabled(true);
                    binding.checkBoxActingSecretary.setEnabled(true);
                    binding.txtBeginDateActingDirectory.setText("");
                    binding.txtEndDateActingDirectory.setText("");
                }
            }
        });
        binding.checkBoxSecretary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    job="secretary";
                    binding.checkBoxAdmin.setEnabled(false);
                    binding.checkBoxDirector.setEnabled(false);
                    binding.checkBoxActingDirector.setEnabled(false);
                    binding.checkBoxActingSecretary.setEnabled(false);
                }else{
                    job="";
                    binding.checkBoxAdmin.setEnabled(true);
                    binding.checkBoxDirector.setEnabled(true);
                    binding.checkBoxActingDirector.setEnabled(true);
                    binding.checkBoxActingSecretary.setEnabled(true);
                }
            }
        });
        binding.checkBoxActingSecretary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    job="acting_secretary";
                    binding.checkBoxAdmin.setEnabled(false);
                    binding.checkBoxDirector.setEnabled(false);
                    binding.checkBoxActingDirector.setEnabled(false);
                    binding.checkBoxSecretary.setEnabled(false);
                    binding.txtBeginDateActingDirectory.setText("");
                    binding.txtEndDateActingDirectory.setText("");
                }else{
                    binding.checkBoxAdmin.setEnabled(true);
                    binding.checkBoxDirector.setEnabled(true);
                    binding.checkBoxActingDirector.setEnabled(true);
                    binding.checkBoxSecretary.setEnabled(true);
                    binding.txtBeginDateActingSecretary.setText("");
                    binding.txtEndDateActingSecretary.setText("");
                }
            }
        });

    }

    void addUser(){
        binding.imgCheckAddUser.setVisibility(View.GONE);
        binding.progressAddUser.setVisibility(View.VISIBLE);
        enableComponent(false);
        if (job.equals("acting_director")) {
            beginDate = binding.txtBeginDateActingDirectory.getText().toString();
            endDate = binding.txtEndDateActingDirectory.getText().toString();
        } else {
            if (job.equals("acting_secretary")) {
                beginDate = binding.txtBeginDateActingSecretary.getText().toString();
                endDate = binding.txtEndDateActingSecretary.getText().toString();
            }
        }
        if (job.equals("admin"))
            role = "admin";
        else
            role = "user";

        if (!binding.edtStructureUser.validateResult()){
            Toast.makeText(requireActivity(), "enter an existing structure", Toast.LENGTH_SHORT).show();
            binding.imgCheckAddUser.setVisibility(View.VISIBLE);
            binding.progressAddUser.setVisibility(View.GONE);
            enableComponent(true);
        }else{
            User user = new User(struct.get(binding.edtStructureUser.getPosition()), binding.edtNameUser.getText().toString(), binding.edtFirstNameUser.getText().toString()
                    , binding.edtFunctionUser.getText().toString()
                    , binding.edtEmailUser.getText().toString(), beginDate, endDate, "active", null, role, job,true);
            userViewModel.createUser(user);
            userViewModel.getLiveCreateUser().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        Toast.makeText(requireActivity(), "created", Toast.LENGTH_SHORT).show();
                        navControllerAdmin.popBackStack(R.id.userFragment, false);
                    } else {
                        binding.imgCheckAddUser.setVisibility(View.VISIBLE);
                        binding.progressAddUser.setVisibility(View.GONE);
                        enableComponent(true);
                        Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void enableComponent(boolean b){
        binding.edtEmailUser.setEnabled(b);
        binding.edtStructureUser.getEditText().setEnabled(b);
        binding.edtFunctionUser.setEnabled(b);
        binding.edtNameUser.setEnabled(b);
        binding.edtFirstNameUser.setEnabled(b);
        binding.checkBoxAdmin.setEnabled(b);
        binding.checkBoxDirector.setEnabled(b);
        binding.checkBoxActingDirector.setEnabled(b);
        binding.checkBoxSecretary.setEnabled(b);
        binding.checkBoxActingSecretary.setEnabled(b);
        binding.txtBeginDateActingDirectory.setEnabled(b);
        binding.txtEndDateActingDirectory.setEnabled(b);
        binding.txtBeginDateActingSecretary.setEnabled(b);
        binding.txtEndDateActingSecretary.setEnabled(b);
    }

}