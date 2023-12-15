package com.example.gestiondecourrier.ui.recyclerview;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClickString;


import java.util.List;

public class RecyclerViewSearch extends RecyclerView.Adapter<RecyclerViewSearch.viewHolder> {
    List<String> data;
    OnClickString  listener;
    public RecyclerViewSearch(List<String> data,OnClickString listener) {
        this.data = data;
        this.listener=listener;
    }

    @NonNull
    @Override
    public RecyclerViewSearch.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf,null,false);
        return new viewHolder(view);
    }
    public void add(String item){
        data.add(item);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewSearch.viewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name=itemView.findViewById(R.id.txt_pdf_name);
            txt_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(data.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
        void onBind(String item){
            txt_name.setText(item);
        }
    }
}
