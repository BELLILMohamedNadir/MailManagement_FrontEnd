package com.example.gestiondecourrier.ui.deserializer;

import android.util.Base64;

import com.example.gestiondecourrier.ui.pojo.GmailResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GmailDeserializer implements JsonDeserializer<GmailResponse> {

    @Override
    public GmailResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Gson gson = new Gson();

        long id = jsonObject.get("id").getAsLong();
        String recipient = jsonObject.get("recipient").isJsonNull() ? null : jsonObject.get("recipient").getAsString();
        String subject = jsonObject.get("subject").isJsonNull() ? null : jsonObject.get("subject").getAsString();
        String body = jsonObject.get("body").isJsonNull() ? null : jsonObject.get("body").getAsString();
        List<String> fileName = gson.fromJson(jsonObject.get("fileName"), new TypeToken<List<String>>(){}.getType());
        List<byte[]> bytes = new ArrayList<>();
        JsonElement jsonElement=jsonObject.get("bytes");
        if (!jsonElement.isJsonNull())
            if (jsonElement.isJsonArray()){
                // If the bytes field is an array of strings, decode each string and add the resulting byte array to the list
                JsonArray bytesArray = jsonElement.getAsJsonArray();
                for (JsonElement element : bytesArray) {
                    bytes.add(Base64.decode(element.getAsString(), Base64.DEFAULT));
                }
            }else{
                // If the bytes field is a single string, decode the entire string and add the resulting byte array to the list
                String bytesString = jsonElement.getAsString();
                bytes.add(Base64.decode(bytesString, Base64.DEFAULT));
            }

        return new GmailResponse(id,recipient,subject,body,fileName,bytes);
    }
}

