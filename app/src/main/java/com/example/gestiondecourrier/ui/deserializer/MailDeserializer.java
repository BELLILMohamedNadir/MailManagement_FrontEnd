package com.example.gestiondecourrier.ui.deserializer;

import android.util.Base64;

import com.example.gestiondecourrier.ui.pojo.MailResponse;
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

public class MailDeserializer implements JsonDeserializer<MailResponse> {

    @Override
    public MailResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        JsonObject jsonObject = json.getAsJsonObject();
        Gson gson = new Gson();

        long id = jsonObject.get("id").getAsLong();

        List<String> fileName = gson.fromJson(jsonObject.get("fileName"), new TypeToken<List<String>>(){}.getType());
        List<String> paths = gson.fromJson(jsonObject.get("paths"), new TypeToken<List<String>>(){}.getType());
        List<byte[]> bytes = new ArrayList<>();
        JsonElement jsonElement=jsonObject.get("bytes");
        if (!jsonElement.isJsonNull()) {
            if (jsonElement.isJsonArray()) {
                // If the bytes field is an array of strings, decode each string and add the resulting byte array to the list
                JsonArray bytesArray = jsonElement.getAsJsonArray();
                for (JsonElement element : bytesArray) {
                    bytes.add(Base64.decode(element.getAsString(), Base64.DEFAULT));
                }
            } else {
                // If the bytes field is a single string, decode the entire string and add the resulting byte array to the list
                String bytesString = jsonElement.getAsString();
                bytes.add(Base64.decode(bytesString, Base64.DEFAULT));
            }
        }

        String forStructure = jsonObject.get("forStructure").isJsonNull() ? null : jsonObject.get("forStructure").getAsString();
        String category = jsonObject.get("category").isJsonNull() ? null : jsonObject.get("category").getAsString();
        String receivedCategory = jsonObject.get("receivedCategory").isJsonNull() ? null : jsonObject.get("receivedCategory").getAsString();
        String object = jsonObject.get("object").isJsonNull() ? null : jsonObject.get("object").getAsString();
        String recipient = jsonObject.get("recipient").isJsonNull() ? null : jsonObject.get("recipient").getAsString();
        String priority = jsonObject.get("priority").isJsonNull() ? null : jsonObject.get("priority").getAsString();
        String type = jsonObject.get("type").isJsonNull() ? null : jsonObject.get("type").getAsString();
        String responseOf = jsonObject.get("responseOf").isJsonNull() ? null : jsonObject.get("responseOf").getAsString();
        String departureDate=jsonObject.get("departureDate").isJsonNull() ? null : jsonObject.get("departureDate").getAsString();
        String entryDate=jsonObject.get("entryDate").isJsonNull() ? null : jsonObject.get("entryDate").getAsString();
        String entryDateToShow=jsonObject.get("entryDateToShow").isJsonNull() ? null : jsonObject.get("entryDateToShow").getAsString();
        String internalReference=jsonObject.get("internalReference").isJsonNull() ? null : jsonObject.get("internalReference").getAsString();
        String mailDate=jsonObject.get("mailDate").isJsonNull() ? null : jsonObject.get("mailDate").getAsString();
        String mailReference=jsonObject.get("mailReference").isJsonNull() ? null : jsonObject.get("mailReference").getAsString();
        String objectReceived=jsonObject.get("objectReceived").isJsonNull() ? null : jsonObject.get("objectReceived").getAsString();
        String entryDateReceived=jsonObject.get("entryDateReceived").isJsonNull() ? null : jsonObject.get("entryDateReceived").getAsString();
        String entryDateReceivedToShow=jsonObject.get("entryDateReceivedToShow").isJsonNull() ? null : jsonObject.get("entryDateReceivedToShow").getAsString();
        String sender=jsonObject.get("sender").isJsonNull() ? null : jsonObject.get("sender").getAsString();
        boolean response=jsonObject.get("response").getAsBoolean();
        boolean favorite = jsonObject.get("favorite").getAsBoolean();
        boolean archive = jsonObject.get("archive").getAsBoolean();
        boolean classed = jsonObject.get("classed").getAsBoolean();
        boolean trait = jsonObject.get("trait").getAsBoolean();

        boolean initializedFavorite = jsonObject.get("initializedFavorite").getAsBoolean();
        boolean initializedArchive = jsonObject.get("initializedArchive").getAsBoolean();
        boolean initializedTrait = jsonObject.get("initializedTrait").getAsBoolean();

        Date entryDate1=null,departureDate1=null,mailDate1=null,entryDateReceived1=null;
        try {
            if (entryDate != null)
                entryDate1=simpleDateFormat.parse(entryDate);
            if (departureDate != null)
                departureDate1=simpleDateFormat.parse(departureDate);
            if (entryDateReceived != null)
                entryDateReceived1=simpleDateFormat.parse(entryDateReceived);
            if (mailDate != null)
                mailDate1=simpleDateFormat.parse(mailDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new MailResponse(id,fileName,paths,bytes,forStructure,sender,category,receivedCategory,entryDate1,departureDate1,entryDateReceived1
                ,mailDate1,entryDateToShow,entryDateReceivedToShow,internalReference,mailReference,objectReceived,object,recipient,priority,type,responseOf,favorite,archive,response,classed,trait
                ,initializedFavorite,initializedArchive,initializedTrait);
    }
}

