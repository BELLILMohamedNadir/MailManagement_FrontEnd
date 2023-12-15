package com.example.gestiondecourrier.ui.search;

import android.widget.Filter;

import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.pojo.Structure;
import com.example.gestiondecourrier.ui.pojo.StructureResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewContact;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewStructure;

import java.util.ArrayList;
import java.util.List;

public class SearchStructure extends Filter {
    RecyclerViewStructure recyclerViewStructure;
    List<StructureResponse> originalList;
    List<StructureResponse> filteredList;

    public SearchStructure(RecyclerViewStructure recyclerViewStructure, List<StructureResponse> originalList) {
        this.recyclerViewStructure = recyclerViewStructure;
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
        if (recyclerViewStructure !=null)
            recyclerViewStructure.setData(filteredList);
    }

    public void setOriginalList(List<StructureResponse> originalList) {
        this.originalList = originalList;
    }
}
