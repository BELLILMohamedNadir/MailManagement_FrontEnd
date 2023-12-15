package com.example.gestiondecourrier.ui.recyclerview;


import static com.example.gestiondecourrier.ui.ui.AdminActivity.navControllerAdmin;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewUser extends RecyclerView.Adapter<RecyclerViewUser.ViewHolderContact> {
    List<UserResponse> data;
    OnClick listener;
    Feature feature;

    public RecyclerViewUser(List<UserResponse> data, OnClick listener) {
        this.data = data;
        this.listener = listener;
        feature=new Feature();
    }

    @NonNull
    @Override
    public RecyclerViewUser.ViewHolderContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_container,null,false);
        return new ViewHolderContact(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewUser.ViewHolderContact holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setData(List<UserResponse> users) {
        data=users;
        notifyDataSetChanged();
    }

    public class ViewHolderContact extends RecyclerView.ViewHolder {
        CircleImageView img_user;
        ImageView img_info_gmail;
        TextView txt_name,txt_email,txt_structure;
        CardView card_user;
        RelativeLayout relative_info;
        ImageView img_info;
        Bundle bundle=new Bundle();
        public ViewHolderContact(@NonNull View itemView) {
            super(itemView);
            card_user =itemView.findViewById(R.id.card_user);
            relative_info=itemView.findViewById(R.id.relative_info_user);
            txt_name=itemView.findViewById(R.id.txt_name_user);
            txt_email=itemView.findViewById(R.id.txt_email_user);
            img_info_gmail =itemView.findViewById(R.id.img_info_gmail);
            txt_structure=itemView.findViewById(R.id.txt_structure_user);
            img_info=itemView.findViewById(R.id.img_info_user);
            img_user=itemView.findViewById(R.id.img_user_user);
            img_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(data.get(getAdapterPosition()).getPositionInTheViewModel());
                }
            });
            img_info_gmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("email",data.get(data.get(getAdapterPosition()).getPositionInTheViewModel()).getEmail());
                    bundle.putString("source","user");
                    navControllerAdmin.navigate(R.id.sendEmailToEmployeeFragment2,bundle);
                }
            });


        }
        void onBind(UserResponse user){
            txt_name.setText(user.getName().toUpperCase().concat(" "+user.getFirstName()));
            txt_structure.setText(user.getStructureDesignation());
            if (user.getBytes()!=null){
               img_user.setImageBitmap( BitmapFactory.decodeByteArray(user.getBytes(), 0, user.getBytes().length));
            }else
                img_user.setImageResource(feature.picture(user.getName().charAt(0)));
            txt_email.setText(user.getEmail());
        }
    }
}
