package com.example.gestiondecourrier.ui.recyclerview;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

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
import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.pojo.ContactResponse;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewContact extends RecyclerView.Adapter<RecyclerViewContact.ViewHolderContact> {
    List<ContactResponse> data,list;
    OnClick listener;
    Feature feature;

    public RecyclerViewContact(List<ContactResponse> data, OnClick listener) {
        this.data = data;
        this.listener = listener;
        feature=new Feature();
    }

    @NonNull
    @Override
    public RecyclerViewContact.ViewHolderContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_container,null,false);
        return new ViewHolderContact(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewContact.ViewHolderContact holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void addContact(List<ContactResponse> contacts){
        this.data=contacts;
        list=data;
        notifyDataSetChanged();
    }

    public void setData(List<ContactResponse> contacts) {
        data=contacts;
        notifyDataSetChanged();
    }

    public class ViewHolderContact extends RecyclerView.ViewHolder {
        CircleImageView img_user;
        TextView txt_name,txt_email,txt_structure;
        CardView card_contact;
        RelativeLayout relative_info;
        ImageView img_info,img_gmail;
        Bundle bundle=new Bundle();
        public ViewHolderContact(@NonNull View itemView) {
            super(itemView);
            card_contact=itemView.findViewById(R.id.card_contact);
            relative_info=itemView.findViewById(R.id.relative_info_contact);
            txt_name=itemView.findViewById(R.id.txt_name_contact);
            txt_email=itemView.findViewById(R.id.txt_email_contact);
            txt_structure=itemView.findViewById(R.id.txt_structure_contact);
            img_info=itemView.findViewById(R.id.img_info_contact);
            img_gmail=itemView.findViewById(R.id.img_gmail);
            img_user=itemView.findViewById(R.id.img_user_contact);
            img_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(data.get(getAdapterPosition()).getPositionInViewModel());
                }
            });

            img_gmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("email",data.get(data.get(getAdapterPosition()).getPositionInViewModel()).getEmail());
                    bundle.putString("source","contact");
                    navController.navigate(R.id.sendEmailToEmployeeFragment,bundle);
                }
            });


        }
        void onBind(ContactResponse contact){
            txt_name.setText(contact.getName().toUpperCase().concat(" "+contact.getFirstName()));
            txt_structure.setText(contact.getStructure());
            txt_email.setText(contact.getEmail());
            img_user.setImageResource(feature.picture(contact.getName().charAt(0)));
        }
    }
}
