package com.example.gestiondecourrier.ui.search;

import android.widget.Filter;

import com.example.gestiondecourrier.ui.pojo.Contact;
import com.example.gestiondecourrier.ui.pojo.Employee;
import com.example.gestiondecourrier.ui.pojo.EmployeeResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewEmployee;

import java.util.ArrayList;
import java.util.List;

public class SearchByNameEmployee extends Filter {

    RecyclerViewEmployee recyclerViewEmployee;
    List<EmployeeResponse> originalList;
    List<EmployeeResponse> filteredList;

    public SearchByNameEmployee(RecyclerViewEmployee recyclerViewContact, List<EmployeeResponse> originalList) {
        this.recyclerViewEmployee = recyclerViewContact;
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
        if (recyclerViewEmployee !=null)
            recyclerViewEmployee.setData(filteredList);
    }

    public void setOriginalList(List<EmployeeResponse> originalList) {
        this.originalList = originalList;
    }
}
