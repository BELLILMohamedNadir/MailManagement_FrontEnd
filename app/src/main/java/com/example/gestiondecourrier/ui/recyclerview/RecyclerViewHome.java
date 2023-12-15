package com.example.gestiondecourrier.ui.recyclerview;

import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.pojo.ArchivePk;
import com.example.gestiondecourrier.ui.pojo.FavoritePk;
import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.pojo.TraitPk;
import com.example.gestiondecourrier.ui.recyclerview.interfaces.OnClick;
import com.example.gestiondecourrier.ui.ui.viewmodel.MailViewModel;

import java.util.List;

public class RecyclerViewHome extends RecyclerView.Adapter<RecyclerViewHome.viewHolderHome>{
    List<MailResponse> data;
    OnClick listener;
    String from="";
    MailViewModel mailViewModel;
    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    public RecyclerViewHome(List<MailResponse> data, RelativeLayout relativeLayout,RecyclerView recyclerView, String from, OnClick listener, FragmentActivity context) {
        this.data = data;
        this.listener = listener;
        this.from=from;
        this.relativeLayout=relativeLayout;
        this.recyclerView=recyclerView;
        mailViewModel=new MailViewModel();
        mailViewModel= ViewModelProviders.of(context).get(MailViewModel.class);
    }

    public RecyclerViewHome(List<MailResponse> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewHome.viewHolderHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.container,null,false);
        return new viewHolderHome(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHome.viewHolderHome holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<MailResponse> pdfs){
        if (pdfs!=null) {
            this.data = pdfs;
            notifyDataSetChanged();
        }
    }


    public class viewHolderHome extends RecyclerView.ViewHolder {
        TextView txt_source;
        CardView cardView;
        ImageView img_favorite,img_archive;
        View view;
        public viewHolderHome(@NonNull View itemView) {
            super(itemView);
            txt_source=itemView.findViewById(R.id.txt_source);
            cardView=itemView.findViewById(R.id.cardView);
            img_favorite=itemView.findViewById(R.id.img_star);
            img_archive=itemView.findViewById(R.id.img_archive_archive);
            view=itemView.findViewById(R.id.view_urgent);

            img_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (from.equals("to_trait_fragment") || (from.equals("received_fragment") &&
                            (data.get(getAdapterPosition()).getReceivedCategory()==null ||
                                    (data.get(getAdapterPosition()).getReceivedCategory()!=null && data.get(getAdapterPosition()).getReceivedCategory().isEmpty())))){
                        Toast.makeText(itemView.getContext(), "au début donner le courrier une réference", Toast.LENGTH_SHORT).show();
                    }else {
                            if (!data.get(getAdapterPosition()).isInitializedFavorite()){
                                img_favorite.setImageResource(R.drawable.ic_start_);
                                data.get(getAdapterPosition()).setFavorite(true);
                                data.get(getAdapterPosition()).setInitializedFavorite(true);
                                MailResponse mailResponse=data.get(getAdapterPosition());
                                FavoritePk favorite=new FavoritePk(mailResponse.getId(),userDetails.getId());
                                mailViewModel.insertFavorite(favorite);
                            }else{
                                if (data.get(getAdapterPosition()).isFavorite()) {
                                    img_favorite.setImageResource(R.drawable.ic_favorite);
                                    data.get(getAdapterPosition()).setFavorite(false);
                                    MailResponse mailResponse=data.get(getAdapterPosition());
                                    FavoritePk favorite=new FavoritePk(mailResponse.getId(),userDetails.getId());
                                    mailViewModel.updateFavorite(favorite);
                                    if (from.equals("favorite")) {
                                        data.remove(getAdapterPosition());
                                        notifyDataSetChanged();
                                    }
                                    //
                                } else {
                                    img_favorite.setImageResource(R.drawable.ic_start_);
                                    data.get(getAdapterPosition()).setFavorite(true);
                                    MailResponse mailResponse=data.get(getAdapterPosition());
                                    FavoritePk favorite=new FavoritePk(mailResponse.getId(),userDetails.getId());
                                    mailViewModel.updateFavorite(favorite);
                                }
                            }
                    }
                    if (data.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    } else {
                        relativeLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            });

            img_archive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((from.equals("to_trait_fragment") || (from.equals("received_fragment"))) &&
                            (data.get(getAdapterPosition()).getReceivedCategory()==null ||
                                    (data.get(getAdapterPosition()).getReceivedCategory()!=null && data.get(getAdapterPosition()).getReceivedCategory().isEmpty()))){
                        Toast.makeText(itemView.getContext(), "au début insérer le courrier comme arriver", Toast.LENGTH_SHORT).show();
                    }else {
                            if (!data.get(getAdapterPosition()).isInitializedArchive()){
                                img_archive.setImageResource(R.drawable.ic_save_fill);
                                data.get(getAdapterPosition()).setArchive(true);
                                data.get(getAdapterPosition()).setInitializedArchive(true);
                                MailResponse mailResponse=data.get(getAdapterPosition());
                                ArchivePk archive=new ArchivePk(mailResponse.getId(),userDetails.getId());
                                mailViewModel.insertArchive(archive);
                                if (!from.equals("favorite")) {
                                    data.remove(getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            }else {
                                if (data.get(getAdapterPosition()).isArchive()) {
                                    img_archive.setImageResource(R.drawable.ic_save);
                                    data.get(getAdapterPosition()).setArchive(false);
                                    MailResponse mailResponse = data.get(getAdapterPosition());
                                    ArchivePk archive=new ArchivePk(mailResponse.getId(),userDetails.getId());
                                    mailViewModel.updateArchive(archive);
                                    if (from.equals("archive")) {
                                        data.remove(getAdapterPosition());
                                        notifyDataSetChanged();
                                    }

                                } else {
                                    img_archive.setImageResource(R.drawable.ic_save_fill);
                                    data.get(getAdapterPosition()).setArchive(true);
                                    MailResponse mailResponse = data.get(getAdapterPosition());
                                    ArchivePk archive=new ArchivePk(mailResponse.getId(),userDetails.getId());
                                    mailViewModel.updateArchive(archive);
                                    if (!from.equals("favorite")) {
                                        data.remove(getAdapterPosition());
                                        notifyDataSetChanged();
                                    }
                                }
                            }
                    }
                    if (data.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    } else {
                        relativeLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!data.get(getAdapterPosition()).isInitializedTrait()){
                        data.get(getAdapterPosition()).setTrait(true);
                        MailResponse mailResponse=data.get(getAdapterPosition());
                        TraitPk trait=new TraitPk(mailResponse.getId(),userDetails.getId());
                        mailViewModel.insertTrait(trait);
                    }
                    listener.onClick(data.get(getAdapterPosition()).getPlaceInTheViewModel());
                }
            });

        }
        void onBind(MailResponse pdf){
            txt_source.setText(pdf.getInternalReference());
            // we make sure that the received mail that doesn't have received category doesn't archived or favorite
            if (pdf.isFavorite())
                img_favorite.setImageResource(R.drawable.ic_start_);
            else
                img_favorite.setImageResource(R.drawable.ic_favorite);
            if (pdf.isArchive())
                img_archive.setImageResource(R.drawable.ic_save_fill);
            else
                img_archive.setImageResource(R.drawable.ic_save);
            if (pdf.getPriority().equals("Trés urgent"))
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.GONE);
        }
    }
}
