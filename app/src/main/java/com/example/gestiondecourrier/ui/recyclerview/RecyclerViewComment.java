package com.example.gestiondecourrier.ui.recyclerview;

import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.features.Feature;
import com.example.gestiondecourrier.ui.pojo.CommentResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewComment extends RecyclerView.Adapter<RecyclerViewComment.viewHolder> {
    List<CommentResponse> data;
    Feature feature;

    public RecyclerViewComment(List<CommentResponse> data) {
        this.data = data;
        feature=new Feature();
    }

    @NonNull
    @Override
    public RecyclerViewComment.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comments,null,false);
        return new viewHolder(view);
    }
    public  void addComment(CommentResponse comment){
        data.add(comment);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewComment.viewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    public void setData(List<CommentResponse> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_user;
        TextView txt_name,txt_comment,txt_date;
        RelativeLayout relativeLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img_user=itemView.findViewById(R.id.img_comment_file);
            txt_name=itemView.findViewById(R.id.txt_name_comment_file);
            txt_comment=itemView.findViewById(R.id.txt_comment_file);
            txt_date=itemView.findViewById(R.id.txt_comment_date);
            relativeLayout=itemView.findViewById(R.id.relative_comment_);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (txt_date.getVisibility()==View.VISIBLE)
                        txt_date.setVisibility(View.GONE);
                    else
                        txt_date.setVisibility(View.VISIBLE);
                }
            });


        }
        void onBind(CommentResponse comment){
            if (comment.getBytes() != null)
                img_user.setImageBitmap(BitmapFactory.decodeByteArray(comment.getBytes(), 0, comment.getBytes().length));
            else
                img_user.setImageResource(feature.picture(comment.getName().charAt(0)));
            txt_name.setText(comment.getName().concat(" "+comment.getFirstName()));
            txt_comment.setText(comment.getComment());
            txt_date.setText(comment.getDate());

        }
    }
}
