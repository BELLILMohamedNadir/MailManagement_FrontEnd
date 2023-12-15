package com.example.gestiondecourrier.ui.features;

import static com.example.gestiondecourrier.ui.ui.MainActivity.drawerLayout;
import static com.example.gestiondecourrier.ui.ui.MainActivity.liveCategoryDesignation;
import static com.example.gestiondecourrier.ui.ui.MainActivity.liveItems;
import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnDateSelectedListener;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsNoStructure;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsReceivedMails;
import com.example.gestiondecourrier.ui.search.SearchWithOptionsSendMails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Feature {
    private String date="";
    private Calendar calender;
    private int year;
    private int month;
    private int day;
    private DatePickerDialog datePickerDialog;

    public  int picture(char c){
        switch (c){
            case 'a':
            case 'A':return R.drawable.ic_a;
            case 'b':
            case 'B':return R.drawable.ic_b;
            case 'c':
            case 'C':return R.drawable.ic_c;
            case 'd':
            case 'D':return R.drawable.ic_d;
            case 'e':
            case 'E':return R.drawable.ic_e;
            case 'f':
            case 'F':return R.drawable.ic_f;
            case 'g':
            case 'G':return R.drawable.ic_g;
            case 'h':
            case 'H':return R.drawable.ic_h;
            case 'i':
            case 'I':return R.drawable.ic_i;
            case 'j':
            case 'J':return R.drawable.ic_j;
            case 'k':
            case 'K':return R.drawable.ic_k;
            case 'l':
            case 'L':return R.drawable.ic_l;
            case 'm':
            case 'M':return R.drawable.ic_m;
            case 'n':
            case 'N':return R.drawable.ic_n;
            case 'o':
            case 'O':return R.drawable.ic_o;
            case 'p':
            case 'P':return R.drawable.ic_p;
            case 'q':
            case 'Q':return R.drawable.ic_q;
            case 'r':
            case 'R':return R.drawable.ic_r;
            case 's':
            case 'S':return R.drawable.ic_s;
            case 't':
            case 'T':return R.drawable.ic_t;
            case 'u':
            case 'U':return R.drawable.ic_u;
            case 'v':
            case 'V':return R.drawable.ic_v;
            case 'w':
            case 'W':return R.drawable.ic_w;
            case 'x':
            case 'X':return R.drawable.ic_x;
            case 'y':
            case 'Y':return R.drawable.ic_y;
            default: return R.drawable.ic_z;
        }
    }

    public  void profilePicture(Bitmap bitmap, char c, CircleImageView img){
        if (bitmap!=null)
            img.setImageBitmap(bitmap);
        else
            img.setImageResource(picture(c));
    }

    public  void showCalender(Context context, OnDateSelectedListener listener){
        calender= Calendar.getInstance();
        day=calender.get(Calendar.DAY_OF_MONTH);
        month=calender.get(Calendar.MONTH);
        year=calender.get(Calendar.YEAR);
        datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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

    public  void showCalenderInTextView(Context context, TextView txt){
        calender= Calendar.getInstance();
        day=calender.get(Calendar.DAY_OF_MONTH);
        month=calender.get(Calendar.MONTH);
        year=calender.get(Calendar.YEAR);
        datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String m=""+(i1 + 1);
                if (i1/10==0)
                    m="0"+m;
                date=i+"-"+m+"-"+i2;
                txt.setText(date);
            }
        },year,month,day);
        datePickerDialog.show();
    }

    public  void actionBarBehavior(ImageView menu, CircleImageView profile){
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.profileFragment);
            }
        });
    }

    public void getInfoFromOptionLayout(Context context, CardView entryDate, CardView departureDate, Spinner structure, Spinner category,SearchWithOptionsReceivedMails filter){

        entryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(context, new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String date) {
                        filter.setEntryDate(date);
                    }
                });
            }
        });

        departureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(context, new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String date) {
                        filter.setDepartureDate(date);
                    }
                });
            }
        });


        structure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                    filter.setStructure("");
                else
                    filter.setStructure((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                filter.setStructure("");
            }
        });


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                    filter.setCategory("");
                else
                    filter.setCategory((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                filter.setCategory("");
            }
        });
    }

    public void getInfoFromOptionLayout(Context context, CardView entryDate, CardView departureDate, Spinner structure, Spinner category,SearchWithOptionsSendMails filter){

        entryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(context, new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String date) {
                        filter.setEntryDate(date);
                    }
                });
            }
        });

        departureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(context, new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String date) {
                        filter.setDepartureDate(date);
                    }
                });
            }
        });


        structure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                    filter.setStructure("");
                else
                    filter.setStructure((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                filter.setStructure("");
            }
        });


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                    filter.setCategory("");
                else
                    filter.setCategory((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                filter.setCategory("");
            }
        });
    }

    public void getInfoFromOptionLayout(Context context, CardView entryDate, CardView departureDate,Spinner category,SearchWithOptionsNoStructure filter){

        entryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(context, new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String date) {
                        filter.setEntryDate(date);
                    }
                });
            }
        });

        departureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(context, new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String date) {
                        filter.setDepartureDate(date);
                    }
                });
            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                    filter.setCategory("");
                else
                    filter.setCategory((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                filter.setCategory("");
            }
        });
    }

    public  void initializeStructureSpinner(Spinner structure, Context context){
        liveItems.observe((LifecycleOwner) context, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> s) {
                List<String> list=new ArrayList<>();
                list.add("Structure");
                list.addAll(s);
                ArrayAdapter<String> adapterStructure = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
                adapterStructure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                structure.setAdapter(adapterStructure);
            }
        });
    }

    public void initializeCategorySpinner(Spinner categories,Context context){
        liveCategoryDesignation.observe((LifecycleOwner) context, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (strings!=null){
                    List<String> list=new ArrayList<>();
                    list.add("Catégorie");
                    list.addAll(strings);
                    ArrayAdapter<String> adapterStructure = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
                    adapterStructure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categories.setAdapter(adapterStructure);
                }
            }
        });
    }

    public  void search(SearchWithOptionsNoStructure filter, EditText editText){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter.filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public  void search(SearchWithOptionsSendMails filter, EditText editText){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter.filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public  void search(SearchWithOptionsReceivedMails filter, EditText editText){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter.filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public  void refreshOptions(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView){
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                refreshLayout.setEnabled(i2 <= 2);
            }
        });
    }

}
