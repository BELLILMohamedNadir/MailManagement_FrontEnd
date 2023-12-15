package com.example.gestiondecourrier.ui.search;

import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.sp;
import static com.example.gestiondecourrier.ui.ui.MainActivity.userDetails;

import android.util.Log;
import android.widget.Filter;

import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.pojo.Report;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewReport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SearchReport extends Filter {

    List<Report> originalList;
    List<Report> filteredList;
    RecyclerViewReport recyclerViewReport;
    String structure="",fromDate="",toDate="",type="";


    public SearchReport(List<Report> originalList, RecyclerViewReport recyclerViewReport) {
        this.originalList = originalList;
        this.recyclerViewReport = recyclerViewReport;
        this.filteredList =new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        final FilterResults results = new FilterResults();
        filteredList.clear();

        if (charSequence.length()==0)
            filteredList.addAll(originalList);
        else{
            final String filterPattern = charSequence.toString().toLowerCase().trim();
            boolean match;
            for ( Report item : originalList) {
                match =true;
                if (item.getDateToShow().toLowerCase().contains(filterPattern)) {
                    if (!structure.isEmpty() && !item.getType().equalsIgnoreCase(structure)) {
                            match = false;
                    }

                    if (!type.isEmpty() && !item.getType().equalsIgnoreCase(type)) {
                        match = false;
                    }

                    if (!fromDate.isEmpty() && !compareDate(fromDate,item.getDateToTrait())) {
                        match = false;
                    }
                    if (!toDate.isEmpty() && compareDate(toDate,item.getDateToTrait())) {
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
        if (recyclerViewReport!=null)
            recyclerViewReport.setData(filteredList);
    }

    public void setOriginalList(List<Report> originalList) {
        this.originalList = originalList;
    }

    public void setFilteredList(List<Report> filteredList) {
        this.filteredList = filteredList;
    }

    public void setRecyclerViewReport(RecyclerViewReport recyclerViewReport) {
        this.recyclerViewReport = recyclerViewReport;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    boolean compareDate(String d1, String d2){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(d1, formatter);
        LocalDate date2 = LocalDate.parse(d2, formatter);

        int comparisonResult = date1.compareTo(date2);

        return  comparisonResult>0;
    }
}
