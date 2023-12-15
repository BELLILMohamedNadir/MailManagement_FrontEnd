package com.example.gestiondecourrier.ui.recyclerview;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.pojo.PdfBitmap;
import com.jsibbold.zoomage.ZoomageView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFile extends RecyclerView.Adapter<RecyclerViewFile.viewHolderFile>{
    List<Bitmap> data;
    TextView textView;
    ZoomageView zoomageView;



    public RecyclerViewFile(List<Bitmap> data, TextView textView) {
        this.data = data;
        this.textView=textView;
    }

    @NonNull
    @Override
    public RecyclerViewFile.viewHolderFile onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.file,null,false);
        return new viewHolderFile(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewFile.viewHolderFile holder, int position) {
        holder.onBind(data.get(position));
    }

    public void setData(List<Bitmap> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    public void clearData(){
        data.clear();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolderFile extends RecyclerView.ViewHolder {
        Bundle bundle=new Bundle();
        ImageView img_zoom;
        public viewHolderFile(@NonNull View itemView) {
            super(itemView);
            zoomageView=itemView.findViewById(R.id.zoom_document_file);
            img_zoom=itemView.findViewById(R.id.img_zoom);
            img_zoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putParcelable("BitmapObject",data.get(getAdapterPosition()));
                    navController.navigate(R.id.showPageFragment,bundle);
                }
            });
        }
        void onBind(Bitmap bitmap){
            zoomageView.setImageBitmap(bitmap);
        }

    }

}
