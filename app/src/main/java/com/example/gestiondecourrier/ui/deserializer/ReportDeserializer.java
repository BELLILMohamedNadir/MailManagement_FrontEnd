package com.example.gestiondecourrier.ui.deserializer;

import android.util.Base64;

import com.example.gestiondecourrier.ui.pojo.Report;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportDeserializer implements JsonDeserializer<Report> {

    @Override
    public Report deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        long id = jsonObject.get("id").getAsLong();
        boolean approved = jsonObject.get("approved").getAsBoolean();
        String date=jsonObject.get("date").isJsonNull() ? null : jsonObject.get("date").getAsString();
        String dateToShow=jsonObject.get("dateToShow").isJsonNull() ? null : jsonObject.get("dateToShow").getAsString();
        byte[] bytes=null;
        JsonElement jsonElement=jsonObject.get("bytes");
        if (jsonElement!=null && !jsonElement.isJsonNull())
             bytes = Base64.decode(jsonElement.getAsString(), Base64.DEFAULT);

        String type=jsonObject.get("type").isJsonNull() ? null : jsonObject.get("type").getAsString();

        Date date1=null;
        String dateToTrait="";
        if (date!=null) {
            try {
                date1=simpleDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (date1!=null){
            dateToTrait=simpleDateFormat.format(date1);
        }
        if (type!=null && type.equals("JOURNALIER") && !dateToTrait.isEmpty())
            dateToShow=dateToTrait;

        return new Report(id,bytes,date1,dateToTrait,dateToShow,type,approved);
    }
}

