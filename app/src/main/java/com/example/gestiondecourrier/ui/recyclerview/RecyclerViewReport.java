package com.example.gestiondecourrier.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.pojo.Report;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;

import java.util.List;

public class RecyclerViewReport extends RecyclerView.Adapter<RecyclerViewReport.viewHolderGmail>{
    List<Report> data;
    OnClick listener;

    public RecyclerViewReport(List<Report> data, OnClick listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolderGmail onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.container_report,null,false);
        return new viewHolderGmail(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderGmail holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Report> gmail){
        this.data=gmail;
        notifyDataSetChanged();
    }

    public  void clearData(){
        data.clear();
    }

    public class viewHolderGmail extends RecyclerView.ViewHolder {
        TextView txt_date,txt_structure;
        CardView cardView;
        public viewHolderGmail(@NonNull View itemView) {
            super(itemView);
            txt_date=itemView.findViewById(R.id.txt_date_report);
            txt_structure=itemView.findViewById(R.id.txt_type_report);
            cardView=itemView.findViewById(R.id.cardView_report);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(data.get(getAdapterPosition()).getPositionInTheViewModel());
                }
            });

        }
        void onBind(Report report){
            txt_date.setText(report.getDateToShow());
            txt_structure.setText(report.getType());
        }
    }
}
