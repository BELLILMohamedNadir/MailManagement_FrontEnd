package com.example.gestiondecourrier.ui.recyclerview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClickString;
import com.example.gestiondecourrier.ui.search.Search;

import java.util.ArrayList;
import java.util.List;

public class EditTextWithResponseRecyclerView extends LinearLayout {

    private EditText editText;
    private RecyclerView recyclerView;
    List<String> list;
    RecyclerViewSearch recyclerViewSearch;
    Search filter;


    public EditTextWithResponseRecyclerView(Context context) {
        super(context);
        init();
    }

    public EditTextWithResponseRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithResponseRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        editText = new EditText(getContext());
        editText.setBackgroundResource(R.color.gray);
        editText.setHint(getResources().getString(R.string.response));

        recyclerView = new RecyclerView(getContext());
        recyclerView.setVisibility(GONE);

        list=new ArrayList<>();
        // Set up the RecyclerView adapter and layout manager here
        recyclerViewSearch=new RecyclerViewSearch(list, new OnClickString() {
            @Override
            public void onClick(String item,int position) {
                recyclerView.setVisibility(GONE);
                editText.setText(item);
            }
        });
        recyclerView.setAdapter(recyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);

        filter=new Search(list,recyclerViewSearch);

        //check the error


        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                recyclerView.setVisibility(VISIBLE);

                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter.search(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        addView(editText);
        addView(recyclerView);
    }

    public EditText getEditText() {
        return editText;
    }

    public void setList(List<String> list) {
        this.list = list;
        filter.setOriginalList(list);
    }

    public void setVisibility(){
        recyclerView.setVisibility(GONE);
    }
}

