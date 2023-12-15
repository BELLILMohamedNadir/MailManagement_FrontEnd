package com.example.gestiondecourrier.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.pojo.CategoryResponse;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;

import java.util.List;

public class RecyclerViewCategory extends RecyclerView.Adapter<RecyclerViewCategory.ViewHolderCategory>{

    List<CategoryResponse> data;
    OnClick listener;

    public RecyclerViewCategory(List<CategoryResponse> data, OnClick listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewCategory.ViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_container,null,false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCategory.ViewHolderCategory holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<CategoryResponse> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class ViewHolderCategory extends RecyclerView.ViewHolder{

        TextView txt_designation,txt_code,txt_structure;

        public ViewHolderCategory(@NonNull View itemView) {
            super(itemView);
            txt_designation=itemView.findViewById(R.id.txt_designation_category);
            txt_code=itemView.findViewById(R.id.txt_code_category);
            txt_structure=itemView.findViewById(R.id.txt_structure_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(data.get(getAdapterPosition()).getPositionInTheViewModel());
                }
            });

        }
        void onBind(CategoryResponse category){
            txt_designation.setText(category.getDesignation());
            txt_code.setText(category.getCode());
            txt_structure.setText(category.getDesignation_struct());
        }
    }
}
