package com.example.gestiondecourrier.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.pojo.StructureResponse;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;

import java.util.List;

public class RecyclerViewStructure extends RecyclerView.Adapter<RecyclerViewStructure.ViewHolderStructure>{

    List<StructureResponse> data;
    OnClick listener;

    public RecyclerViewStructure(List<StructureResponse> data, OnClick listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewStructure.ViewHolderStructure onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.structure_container,null,false);
        return new ViewHolderStructure(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewStructure.ViewHolderStructure holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<StructureResponse> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class ViewHolderStructure extends RecyclerView.ViewHolder{

        TextView txt_designation,txt_code;

        public ViewHolderStructure(@NonNull View itemView) {
            super(itemView);
            txt_designation=itemView.findViewById(R.id.txt_designation);
            txt_code=itemView.findViewById(R.id.txt_code);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(data.get(getAdapterPosition()).getPositionInViewModel());
                }
            });

        }
        void onBind(StructureResponse structure){
            txt_designation.setText(structure.getDesignation());
            txt_code.setText(structure.getCode());
        }
    }
}
