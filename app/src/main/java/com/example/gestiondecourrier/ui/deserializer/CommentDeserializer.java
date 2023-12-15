package com.example.gestiondecourrier.ui.deserializer;

import android.util.Base64;

import com.example.gestiondecourrier.ui.pojo.CommentResponse;
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

public class CommentDeserializer implements JsonDeserializer<CommentResponse> {

    @Override
    public CommentResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        long id = jsonObject.get("id").getAsLong();
        long userId = jsonObject.get("userId").getAsLong();
        String comment = jsonObject.get("comment").isJsonNull() ? null : jsonObject.get("comment").getAsString();
        String name = jsonObject.get("name").isJsonNull() ? null : jsonObject.get("name").getAsString();
        String firstName = jsonObject.get("firstName").isJsonNull() ? null : jsonObject.get("firstName").getAsString();
        String date = jsonObject.get("date").isJsonNull() ? null : jsonObject.get("date").getAsString();
        byte[] bytes=null;
        JsonElement jsonElement=jsonObject.get("bytes");
        if (jsonElement!=null && !jsonElement.isJsonNull())
            bytes = Base64.decode(jsonElement.getAsString(), Base64.DEFAULT);
        return new CommentResponse(id,comment,name,firstName,date,bytes,userId);
    }
}

