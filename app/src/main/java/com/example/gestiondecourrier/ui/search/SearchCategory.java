package com.example.gestiondecourrier.ui.search;

import android.widget.Filter;

import com.example.gestiondecourrier.ui.pojo.Category;
import com.example.gestiondecourrier.ui.pojo.CategoryResponse;
import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewCategory;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewContact;

import java.util.ArrayList;
import java.util.List;

public class SearchCategory extends Filter {
    RecyclerViewCategory recyclerViewCategory;
    List<CategoryResponse> originalList;
    List<CategoryResponse> filteredList;

    public SearchCategory(RecyclerViewCategory recyclerViewCategory, List<CategoryResponse> originalList) {
        this.recyclerViewCategory = recyclerViewCategory;
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
                if (originalList.get(i).getDesignation().toLowerCase().contains(filterPattern))
                    filteredList.add(originalList.get(i));

        }

        results.values=filteredList;
        results.count= filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        if (recyclerViewCategory !=null)
            recyclerViewCategory.setData(filteredList);
    }

    public void setOriginalList(List<CategoryResponse> originalList) {
        this.originalList = originalList;
    }
}
