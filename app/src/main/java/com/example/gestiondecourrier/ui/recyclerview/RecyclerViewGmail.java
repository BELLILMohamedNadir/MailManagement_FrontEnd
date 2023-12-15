package com.example.gestiondecourrier.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.GmailResponse;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewGmail extends RecyclerView.Adapter<RecyclerViewGmail.viewHolderGmail>{
    List<GmailResponse> data;
    OnClick listener;
    Feature feature;

    public RecyclerViewGmail(List<GmailResponse> data, OnClick listener) {
        this.data = data;
        this.listener = listener;
        feature=new Feature();
    }

    @NonNull
    @Override
    public viewHolderGmail onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.container_gmail,null,false);
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

    public void setData(List<GmailResponse> gmail){
        this.data=gmail;
        notifyDataSetChanged();
    }


    public class viewHolderGmail extends RecyclerView.ViewHolder {
        TextView txt_source,txt_subject,txt_three_point,txt_three_point_source;
        CardView cardView;
        CircleImageView img_gmail;
        public viewHolderGmail(@NonNull View itemView) {
            super(itemView);
            txt_source=itemView.findViewById(R.id.txt_source_gmail);
            txt_subject=itemView.findViewById(R.id.txt_subject_gmail);
            txt_three_point=itemView.findViewById(R.id.txt_three_point_subject);
            txt_three_point_source=itemView.findViewById(R.id.txt_three_point_source);
            img_gmail=itemView.findViewById(R.id.img_gmail_);
            cardView=itemView.findViewById(R.id.cardView_gmail);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(data.get(getAdapterPosition()).getPositionInTheViewModel());
                }
            });

        }
        void onBind(GmailResponse gmailResponse){
            if (gmailResponse!=null) {
                txt_source.setText(gmailResponse.getSubject());
                if (gmailResponse.getSubject().length() > 18)
                    txt_three_point.setVisibility(View.VISIBLE);
                else
                    txt_three_point.setVisibility(View.GONE);
                if (gmailResponse.getBody().length() > 50)
                    txt_three_point_source.setVisibility(View.VISIBLE);
                else
                    txt_three_point_source.setVisibility(View.GONE);
                txt_subject.setText(gmailResponse.getBody());
                img_gmail.setImageResource(feature.picture(gmailResponse.getRecipient().charAt(0)));
            }

        }
    }
}
