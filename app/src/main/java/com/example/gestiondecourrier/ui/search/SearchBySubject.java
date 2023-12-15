package com.example.gestiondecourrier.ui.search;

import android.widget.Filter;

import com.example.gestiondecourrier.ui.pojo.Gmail;
import com.example.gestiondecourrier.ui.pojo.GmailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewGmail;

import java.util.ArrayList;
import java.util.List;

public class SearchBySubject extends Filter {

    RecyclerViewGmail recyclerViewGmail;
    List<GmailResponse> originalList;
    List<GmailResponse> filteredList;

    public SearchBySubject(RecyclerViewGmail recyclerViewGmail, List<GmailResponse> originalList) {
        this.recyclerViewGmail = recyclerViewGmail;
        this.originalList = originalList;
        filteredList=new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {

        final FilterResults results = new FilterResults();
        filteredList.clear();
        if (charSequence.length()==0)
            filteredList.addAll(originalList);
        else{
            final String filterPattern = charSequence.toString().toLowerCase().trim();
            for (int i=0;i<originalList.size();i++)
                if (originalList.get(i).getSubject().toLowerCase().contains(filterPattern))
                    filteredList.add(originalList.get(i));

        }

        results.values=filteredList;
        results.count= filteredList.size();
        return results;

    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        if (recyclerViewGmail!=null)
            recyclerViewGmail.setData(filteredList);
    }

    public void setRecyclerViewGmail(RecyclerViewGmail recyclerViewGmail) {
        this.recyclerViewGmail = recyclerViewGmail;
    }

    public void setOriginalList(List<GmailResponse> originalList) {
        this.originalList = originalList;
    }
}
