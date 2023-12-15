package com.example.gestiondecourrier.ui.search;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.gestiondecourrier.ui.ui.AuthenticationActivity.sp;

import android.os.Trace;
import android.util.Log;
import android.widget.Filter;

import com.example.gestiondecourrier.ui.pojo.MailResponse;
import com.example.gestiondecourrier.ui.pojo.TraceResponse;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewHome;
import com.example.gestiondecourrier.ui.recyclerview.RecyclerViewTrace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchTrace extends Filter {

    List<TraceResponse> originalList;
    List<TraceResponse> filteredList;
    RecyclerViewTrace recyclerViewTrace;
    String action="";
    String fromDate="",toDate="";


    public SearchTrace(List<TraceResponse> originalList, RecyclerViewTrace recyclerViewTrace) {
        this.originalList = originalList;
        this.recyclerViewTrace = recyclerViewTrace;
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
            for ( TraceResponse item : originalList) {
                match =true;
                if (item.getReference().toLowerCase().contains(filterPattern)) {

                    if (!action.isEmpty() && !item.getAction().equalsIgnoreCase(action)) {
                        match = false;
                    }

                    if (!action.isEmpty() && action.equals("Modifier un courrier") ) {
                        if (item.getUpdateTimeToTrait() != null && item.getUpdateTimeToTrait().isEmpty()) {
                            if (!fromDate.isEmpty() && !compareDate(item.getUpdateTimeToTrait(), fromDate)) {
                                match = false;
                            }
                            if (!toDate.isEmpty() && compareDate(item.getUpdateTimeToTrait(), toDate)) {
                                match = false;
                            }
                        }
                    }else{
                        if (!fromDate.isEmpty() && !compareDate(item.getTimeToTrait(), fromDate)) {
                            match = false;
                        }
                        if (!toDate.isEmpty() && compareDate(item.getTimeToTrait(), toDate)) {
                            match = false;
                        }
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
        if (recyclerViewTrace!=null)
            recyclerViewTrace.setData(filteredList);
    }
    public void setOriginalList(List<TraceResponse> originalList) {
        this.originalList = originalList;
    }

    public void setRecyclerViewHome(RecyclerViewTrace recyclerViewTrace) {
        this.recyclerViewTrace = recyclerViewTrace;
    }

    public RecyclerViewTrace getRecyclerViewHome() {
        return recyclerViewTrace;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    boolean compareDate(String d1, String d2){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(d1, formatter);
        LocalDate date2 = LocalDate.parse(d2, formatter);

        int comparisonResult = date1.compareTo(date2);

        return  comparisonResult>0;
    }
}
