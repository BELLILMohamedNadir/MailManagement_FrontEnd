package com.example.gestiondecourrier.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.pojo.TraceResponse;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RecyclerViewTrace extends RecyclerView.Adapter<RecyclerViewTrace.viewHolderTrace>{
    List<TraceResponse> data;
    OnClick listener;

    public RecyclerViewTrace(List<TraceResponse> data, OnClick listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolderTrace onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.container_trace,null,false);
        return new viewHolderTrace(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderTrace holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<TraceResponse> gmail){
        this.data=gmail;
        notifyDataSetChanged();
    }

    public  void clearData(){
        data.clear();
    }

    public class viewHolderTrace extends RecyclerView.ViewHolder {
        TextView txt_action,txt_time,txt_reference;
        CardView cardView;
        public viewHolderTrace(@NonNull View itemView) {
            super(itemView);
            txt_action=itemView.findViewById(R.id.txt_action_trace_container);
            txt_time=itemView.findViewById(R.id.txt_time_trace_container);
            txt_reference=itemView.findViewById(R.id.txt_reference_trace_container);
            cardView=itemView.findViewById(R.id.cardView_trace);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(data.get(getAdapterPosition()).getPositionInTheViewModel());
                }
            });

        }
        void onBind(TraceResponse traceResponse){
            txt_action.setText(traceResponse.getAction());
            txt_time.setText(traceResponse.getTimeToShow());
            txt_reference.setText(traceResponse.getReference());
        }
    }
}
