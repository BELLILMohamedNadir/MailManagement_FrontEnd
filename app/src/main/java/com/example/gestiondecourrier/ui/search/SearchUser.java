package com.example.gestiondecourrier.ui.search;

import android.widget.Filter;

import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewContact;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewUser;

import java.util.ArrayList;
import java.util.List;

public class SearchUser extends Filter {
    private RecyclerViewUser recyclerViewUser;
    private List<UserResponse> originalList;
    private List<UserResponse> filteredList;

    public SearchUser(RecyclerViewUser recyclerViewContact, List<UserResponse> originalList) {
        this.recyclerViewUser = recyclerViewContact;
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
        if (recyclerViewUser !=null)
            recyclerViewUser.setData(filteredList);
    }

    public void setOriginalList(List<UserResponse> originalList) {
        this.originalList = originalList;
    }
}
