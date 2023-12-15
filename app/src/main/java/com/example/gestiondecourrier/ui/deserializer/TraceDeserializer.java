package com.example.gestiondecourrier.ui.deserializer;

import android.util.Base64;

import com.example.gestiondecourrier.ui.pojo.TraceResponse;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TraceDeserializer implements JsonDeserializer<TraceResponse> {

    @Override
    public TraceResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        long id=jsonObject.get("id").getAsLong();

        JsonElement jsonElement = jsonObject.get("time");
        String time = (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : null;

        jsonElement = jsonObject.get("timeToShow");
        String timeToShow = (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : null;
        jsonElement = jsonObject.get("updateTimeToShow");
        String updateTimeToShow = (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : null;

        jsonElement = jsonObject.get("updateTime");
        String updateTime = (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : null;

        jsonElement = jsonObject.get("reference");
        String reference = (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : null;

        jsonElement = jsonObject.get("action");
        String action = (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : null;

        jsonElement = jsonObject.get("name");
        String name = (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : null;

        jsonElement = jsonObject.get("firstName");
        String firstName = (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : null;

        jsonElement = jsonObject.get("email");
        String email = (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : null;


        jsonElement = jsonObject.get("job");
        String job = (jsonElement != null && !jsonElement.isJsonNull()) ? jsonElement.getAsString() : null;

        jsonElement = jsonObject.get("bytes");
        byte[] bytes = null;
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            bytes = Base64.decode(jsonElement.getAsString(), Base64.DEFAULT);
        }

        Date time1=null,updateTime1=null;
        String timeToTrait="",updateTimeToTrait="";

        try {
            if (time!=null)
                time1=simpleDateFormat.parse(time);
            if (updateTime!=null)
                updateTime1=simpleDateFormat.parse(updateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (time1!=null)
            timeToTrait=simpleDateFormat.format(time1);
        if (updateTime1!=null)
            updateTimeToTrait=simpleDateFormat.format(updateTime1);



        return new TraceResponse(id,bytes,time1,updateTime1,timeToShow,updateTimeToShow,timeToTrait,updateTimeToTrait,action,reference,name,firstName,job,email);
    }
}

