package com.example.gestiondecourrier.ui.search;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.widget.Filter;

import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewSearch;

import java.util.ArrayList;
import java.util.List;

public class Search extends Filter {

     List<String> originalList;
     List<String> filteredList;
     RecyclerViewSearch recyclerViewSearch;

    public Search(List<String> originalList, RecyclerViewSearch recyclerViewSearch) {
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
        this.recyclerViewSearch = recyclerViewSearch;
    }

    public void setOriginalList(List<String> originalList) {
        this.originalList = originalList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();
        if (constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();
            for (final String item : originalList) {
                if (item.toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        recyclerViewSearch.setData((List<String>) results.values);
    }
    public void search(CharSequence constraint){
        Log.d(TAG, "check this search: "+performFiltering(constraint).values);
        publishResults(constraint,performFiltering(constraint));
    }
}
