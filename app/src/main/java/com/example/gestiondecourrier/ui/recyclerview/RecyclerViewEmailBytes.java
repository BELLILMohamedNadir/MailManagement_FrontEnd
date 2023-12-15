package com.example.gestiondecourrier.ui.recyclerview;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.pojo.Folder;
import com.example.gestiondecourrier.ui.ui.AdminActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewEmailBytes extends RecyclerView.Adapter<RecyclerViewEmailBytes.ViewHolderEmail> {
    List<Folder> data;
    String source="";
    boolean isChanged=false;
    List<Folder> delete;

    public RecyclerViewEmailBytes(List<Folder> data, String source) {
        this.data = data;
        this.source = source;
        delete=new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerViewEmailBytes.ViewHolderEmail onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.email_container,null,false);
        return new ViewHolderEmail(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewEmailBytes.ViewHolderEmail holder, int position) {
            holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void add(Folder folder){
        data.add(folder);
        notifyDataSetChanged();
    }
    public void clearData(){
        data.clear();
    }
    public List<Folder> getData(){
        return this.data;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public class ViewHolderEmail extends RecyclerView.ViewHolder {
        ImageView img_cancel;
        TextView txt_number;
        Bundle bundle=new Bundle();
        public ViewHolderEmail(@NonNull View itemView) {
            super(itemView);
            img_cancel=itemView.findViewById(R.id.img_cancel);
            txt_number=itemView.findViewById(R.id.txt_pdf_number);

            if (source.equals("gmailDetails") || source.equals("AdminActivity"))
                img_cancel.setVisibility(View.GONE);
            else
                img_cancel.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("source",source);
                    bundle.putInt("mailPosition",data.get(getAdapterPosition()).getPositionInTheViewModel());
                    bundle.putInt("folderPosition",getAdapterPosition());
                    if (source.equals("AdminActivity"))
                        AdminActivity.navControllerAdmin.navigate(R.id.showPdfFragment2,bundle);
                    else
                        navController.navigate(R.id.showPdfFragment,bundle);
                }
            });
            img_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete.add(data.get(getAdapterPosition()));
                    data.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    isChanged=true;
                }
            });
        }
        void onBind(Folder folder){
            txt_number.setText(folder.getName());
        }
    }

    public List<Folder> getDelete() {
        return delete;
    }
}
