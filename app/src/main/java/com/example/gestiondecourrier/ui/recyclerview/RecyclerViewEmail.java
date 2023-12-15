package com.example.gestiondecourrier.ui.recyclerview;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewEmail extends RecyclerView.Adapter<RecyclerViewEmail.ViewHolderEmail> {
    ArrayList<Uri> data;

    public RecyclerViewEmail(ArrayList<Uri> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewEmail.ViewHolderEmail onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.email_container,null,false);
        return new ViewHolderEmail(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewEmail.ViewHolderEmail holder, int position) {
            holder.onBind(++position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(Uri uri){
        data.add(uri);
        notifyDataSetChanged();
    }

    public void setData(ArrayList<Uri> data) {
        this.data = data;
    }

    public ArrayList<Uri> getData(){
        return this.data;
    }

    public class ViewHolderEmail extends RecyclerView.ViewHolder {
        ImageView img_cancel;
        TextView txt_number;
        Bundle bundle=new Bundle();
        public ViewHolderEmail(@NonNull View itemView) {
            super(itemView);
            img_cancel=itemView.findViewById(R.id.img_cancel);
            txt_number=itemView.findViewById(R.id.txt_pdf_number);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("pdfUri",String.valueOf(data.get(getAdapterPosition())));
                    navController.navigate(R.id.showPdfFragment,bundle);
                }
            });
            img_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
        void onBind(int cpt){
            txt_number.setText("("+String.valueOf(cpt)+")");
        }
    }
}
