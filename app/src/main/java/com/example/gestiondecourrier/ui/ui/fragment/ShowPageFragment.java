package com.example.gestiondecourrier.ui.ui.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.databinding.FragmentShowPageBinding;
import com.example.gestiondecourrier.ui.pojo.PdfBitmap;


public class ShowPageFragment extends Fragment {

    FragmentShowPageBinding binding;

    public ShowPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentShowPageBinding.inflate(inflater,container,false);

        if (getArguments()!=null && getArguments().getParcelable("BitmapObject")!=null){
            binding.zoomShowPage.setImageBitmap(getArguments().getParcelable("BitmapObject"));
        }



        return binding.getRoot();
    }

}