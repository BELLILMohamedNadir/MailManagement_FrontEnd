package com.example.gestiondecourrier.ui.search;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.widget.Filter;

import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.pojo.ContactResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewContact;

import java.util.ArrayList;
import java.util.List;

public class SearchByName extends Filter {

    RecyclerViewContact recyclerViewContact;
    List<ContactResponse> originalList;
    List<ContactResponse> filteredList;

    public SearchByName(RecyclerViewContact recyclerViewContact, List<ContactResponse> originalList) {
        this.recyclerViewContact = recyclerViewContact;
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
                if (originalList.get(i).getName().concat(" "+originalList.get(i).getFirstName()).toLowerCase().contains(filterPattern))
                    filteredList.add(originalList.get(i));

        }

        results.values=filteredList;
        results.count= filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        if (recyclerViewContact!=null)
            recyclerViewContact.setData(filteredList);
    }

    public void setOriginalList(List<ContactResponse> originalList) {
        this.originalList = originalList;
    }
}
