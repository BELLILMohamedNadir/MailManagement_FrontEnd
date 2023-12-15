package com.example.gestiondecourrier.ui.search;

import android.util.Log;
import android.widget.Filter;

import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchWithOptionsNoStructure extends Filter {

    List<MailResponse> originalList;
    List<MailResponse> filteredList;
    RecyclerViewHome recyclerViewHome;
    private String category="",entryDate="",departureDate="";



    public SearchWithOptionsNoStructure(List<MailResponse> originalList, RecyclerViewHome recyclerViewHome) {
        this.originalList = originalList;
        this.recyclerViewHome = recyclerViewHome;
        this.filteredList =new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        final FilterResults results = new FilterResults();
        filteredList.clear();
        if (charSequence.length()==0)
            filteredList.addAll(originalList);
        else{
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            final String filterPattern = charSequence.toString().toLowerCase().trim();
            boolean match;
            for ( MailResponse item : originalList) {
                match =true;
                if (item.getInternalReference().toLowerCase().contains(filterPattern)) {
                    if (!category.isEmpty() && !item.getCategory().trim().equalsIgnoreCase(category.trim())) {
                        match = false;
                    }
                    if (!entryDate.isEmpty() && !simpleDateFormat.format(item.getEntryDate()).trim().equalsIgnoreCase(entryDate.trim())) {
                        match = false;
                    }
                    if (!departureDate.isEmpty() && !simpleDateFormat.format(item.getDepartureDate()).trim().equalsIgnoreCase(departureDate.trim())) {
                        match = false;
                    }
                    if (match) {
                        filteredList.add(item);
                    }
                }
            }
        }
        results.values=filteredList;
        results.count= filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        if (recyclerViewHome!=null)
            recyclerViewHome.setData(filteredList);
    }
    public void setOriginalList(List<MailResponse> originalList) {
        this.originalList = originalList;
    }

    public void setRecyclerViewHome(RecyclerViewHome recyclerViewHome) {
        this.recyclerViewHome = recyclerViewHome;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public RecyclerViewHome getRecyclerViewHome() {
        return recyclerViewHome;
    }

}
