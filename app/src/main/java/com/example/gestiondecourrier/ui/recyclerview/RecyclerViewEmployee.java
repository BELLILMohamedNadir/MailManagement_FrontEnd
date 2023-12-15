package com.example.gestiondecourrier.ui.recyclerview;

import static com.example.gestiondecourrier.ui.ui.MainActivity.navController;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.EmployeeResponse;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.ui.viewmodel.EmailViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewEmployee extends RecyclerView.Adapter<RecyclerViewEmployee.ViewHolderEmployee> {

    List<EmployeeResponse> data;
    OnClick listener;
    Feature feature;

    public RecyclerViewEmployee(List<EmployeeResponse> data, OnClick listener) {
        this.data = data;
        this.listener = listener;
        feature=new Feature();
    }

    public void setData(List<EmployeeResponse> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewEmployee.ViewHolderEmployee onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_container,null,false);
        return new ViewHolderEmployee(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewEmployee.ViewHolderEmployee holder, int position) {
            holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolderEmployee extends RecyclerView.ViewHolder {
        CircleImageView img_user;
        TextView txt_name,txt_email, img_key;
        CardView card_contact;
        RelativeLayout relative_info;
        ImageView img_info,img_gmail;
        Bundle bundle=new Bundle();

        public ViewHolderEmployee(@NonNull View itemView) {
            super(itemView);

            card_contact=itemView.findViewById(R.id.card_employee);
            relative_info=itemView.findViewById(R.id.relative_info_employee);
            txt_name=itemView.findViewById(R.id.txt_name_employee);
            txt_email=itemView.findViewById(R.id.txt_email_employee);
            img_key =itemView.findViewById(R.id.txt_registration_key_employee);
            img_info=itemView.findViewById(R.id.img_info_employee);
            img_user=itemView.findViewById(R.id.img_user_employee);
            img_gmail=itemView.findViewById(R.id.img_gmail_employee);

            img_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(data.get(getAdapterPosition()).getPositionInTheViewModel());
                }
            });
            card_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putInt("employeeId",data.get(getAdapterPosition()).getPositionInTheViewModel());
                    navController.navigate(R.id.employeeAttendanceFragment,bundle);
                }
            });
            img_gmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("email",data.get(data.get(getAdapterPosition()).getPositionInTheViewModel()).getEmail());
                    bundle.putString("source","employee");
                    navController.navigate(R.id.sendEmailToEmployeeFragment,bundle);
                }
            });


        }

        public void onBind(EmployeeResponse employeeResponse) {
            txt_name.setText(employeeResponse.getName().toUpperCase().concat(" "+employeeResponse.getFirstName()));
            txt_email.setText(employeeResponse.getEmail());
            img_key.setText(employeeResponse.getRegistrationKey());
            img_user.setImageResource(feature.picture(employeeResponse.getName().charAt(0)));
        }
    }
}
