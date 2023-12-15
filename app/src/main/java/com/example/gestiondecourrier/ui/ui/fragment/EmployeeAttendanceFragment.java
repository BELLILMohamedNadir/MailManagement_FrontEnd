package com.example.gestiondecourrier.ui.ui.fragment;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentEmployeeAttendanceBinding;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.Attendance;
import com.example.gestiondecourrier.ui.pojo.Employee;
import com.example.gestiondecourrier.ui.pojo.EmployeeResponse;
import com.example.gestiondecourrier.ui.ui.viewmodel.AttendanceViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.CommentViewModel;
import com.example.gestiondecourrier.ui.ui.viewmodel.EmployeeViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class EmployeeAttendanceFragment extends Fragment {

    FragmentEmployeeAttendanceBinding binding;
    EmployeeResponse employee;
    public static long id=-1;
    boolean clicked=false;
    String result="",finalResult="";
    int position=-1;
    int equation=0;
    AttendanceViewModel attendanceViewModel=new AttendanceViewModel();
    EmployeeViewModel employeeViewModel=new EmployeeViewModel();
    Feature feature=new Feature();
    public EmployeeAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        attendanceViewModel= ViewModelProviders.of(requireActivity()).get(AttendanceViewModel.class);
        employeeViewModel= ViewModelProviders.of(requireActivity()).get(EmployeeViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!isAdded())
            return null;
        // Inflate the layout for this fragment
        binding=FragmentEmployeeAttendanceBinding.inflate(inflater,container,false);

        if (getArguments()!=null){
            position=getArguments().getInt("employeeId");
            if (employeeViewModel.getLiveGetEmployees().getValue()!=null)
                employee=employeeViewModel.getLiveGetEmployees().getValue().get(position);
            id=employee.getId();
            binding.txtNameAttendance.setText(employee.getName().concat(" "+employee.getFirstName()));
            binding.imgImgAttendance.setImageResource(feature.picture(employee.getName().charAt(0)));
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        binding.txtAttendanceDate.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));
        binding.imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feature.showCalenderInTextView(requireActivity(),binding.txtAttendanceDate);
            }
        });

        binding.imgCheckAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result=binding.edtRecuperation.getText().toString();
                finalResult=result;
                // to avoid the case of  ex. 03 01
                if (!result.isEmpty() && result.length()>1 && result.charAt(0)=='0')
                    for (int i=1;i<result.length();i++)
                        finalResult=finalResult.concat(result.charAt(i)+"");

                if (!clicked) {
                    if (id != -1) {
                        binding.imgCheckAttendance.setVisibility(View.GONE);
                        binding.progressAddAttendance.setVisibility(View.VISIBLE);
                        clicked = true;

                        if (binding.txtStatAttendance.getText().toString().trim().equals("RÉCUPÉRATION")){
                            if (!binding.edtRecuperation.getText().toString().trim().isEmpty() && !finalResult.trim().equals("0")) {
                                if (!format()) {
                                    equation=employee.getRecuperation() - Integer.parseInt(finalResult.trim());
                                    if ( equation>= 0 &&  employeeViewModel.getLiveGetEmployees().getValue()!=null && position!=-1) {
                                        employeeViewModel.getLiveGetEmployees().getValue().get(position).setRecuperation(equation);
                                        attendanceViewModel.addAttendance(new Attendance(new Employee(id), binding.txtAttendanceDate.getText().toString(), binding.txtStatAttendance.getText().toString(),
                                                Integer.parseInt(finalResult.trim()), true));
                                    }else {
                                        binding.progressAddAttendance.setVisibility(View.GONE);
                                        binding.imgCheckAttendance.setVisibility(View.VISIBLE);
                                        Toast.makeText(requireActivity(), "you don't have recuperation enough", Toast.LENGTH_SHORT).show();
                                        clicked=false;
                                    }
                                }else {
                                    binding.progressAddAttendance.setVisibility(View.GONE);
                                    binding.imgCheckAttendance.setVisibility(View.VISIBLE);
                                    Toast.makeText(requireActivity(), "check the number format's", Toast.LENGTH_SHORT).show();
                                    clicked=false;
                                }
                            }
                        }else
                            attendanceViewModel.addAttendance(new Attendance(new Employee(id), binding.txtAttendanceDate.getText().toString(), binding.txtStatAttendance.getText().toString(),true));
                    }
                }else
                    Toast.makeText(requireActivity(), "wait", Toast.LENGTH_SHORT).show();

                attendanceViewModel.getLiveAddAttendance().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {

                        if (aBoolean) {
                            Toast.makeText(requireActivity(), "inserted", Toast.LENGTH_SHORT).show();
                            navController.popBackStack(R.id.employeeFragment, false);
                        }else {
                            binding.progressAddAttendance.setVisibility(View.GONE);
                            binding.imgCheckAttendance.setVisibility(View.VISIBLE);
                            Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
                            clicked=false;
                        }
                    }
                });
            }
        });


        cardViews();
        manageRecuperation();


        binding.txtRecuperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecuperationFragment recuperationFragment=new RecuperationFragment();
                recuperationFragment.show(getChildFragmentManager(),"recuperation_fragment");
            }
        });

        return binding.getRoot();
    }

    private boolean format() {
        for (int i = 0; i < finalResult.length(); i++)
            if (finalResult.equals(" "))
                return true;
        return false;
    }

    long att=0;
    private void manageRecuperation() {

        binding.imgAddAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtRecuperation.getText().toString().trim().equals(""))
                    att=0;
                else
                    att=Long.parseLong(binding.edtRecuperation.getText().toString().trim());
                binding.edtRecuperation.setText(String.valueOf(++att));
            }
        });
        binding.imgMinusAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtRecuperation.getText().toString().trim().equals(""))
                    att=0;
                else
                    att=Long.parseLong(binding.edtRecuperation.getText().toString().trim());
                binding.edtRecuperation.setText(String.valueOf(--att));
            }
        });
    }

    private void cardView(CardView cardView, String s){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s.equals("RÉCUPÉRATION"))
                    binding.linearAttendance.setVisibility(View.VISIBLE);
                else
                    binding.linearAttendance.setVisibility(View.GONE);
                binding.txtStatAttendance.setText(s);
            }
        });
    }

    private void cardViews(){
        cardView(binding.cardMut,"MUTATION");
        cardView(binding.cardSus,"SUSPENDU DE SES FONCTIONS");
        cardView(binding.cardFor,"FORMATION");
        cardView(binding.cardCa,"CONGÉ  ANNUEL");
        cardView(binding.cardCe,"CONGÉ   EXEPTIONNEL");
        cardView(binding.cardRc,"RÉCUPÉRATION");
        cardView(binding.cardCs,"CONGÉ  SANS SOLDE");
        cardView(binding.cardMis,"MISSION COMMANDEE");
        cardView(binding.cardAbr,"ABSENCE REMUNEREE");
        cardView(binding.cardF,"JOUR FÉRIER");
        cardView(binding.cardRup,"RUPTURE DE CONTRAT");
        cardView(binding.cardPa,"PRÉSENT DEMI JOURNEE");
        cardView(binding.cardAbi,"ABSENCE IRREGULIERE");
        cardView(binding.cardAnr,"ABSENCE  AUTORISEE");
        cardView(binding.cardMac,"MALADIE");
        cardView(binding.cardP,"PRÉSENT");
        cardView(binding.cardMat,"CONGÉ  MATERNITE");
        cardView(binding.cardFc,"FIN DE CONTRAT");
        cardView(binding.cardMd,"MISE EN DISPONIBILITE");
        cardView(binding.cardW,"WEEK –END");
        cardView(binding.cardDt,"DÉTACHE");
        cardView(binding.cardEl,"ÉLECTION");

    }

}