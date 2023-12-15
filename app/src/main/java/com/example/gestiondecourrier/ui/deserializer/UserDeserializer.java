package com.example.gestiondecourrier.ui.deserializer;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Base64;
import android.util.Log;

import com.example.gestiondecourrier.ui.pojo.GmailResponse;
import com.example.gestiondecourrier.ui.pojo.User;
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
import java.util.ArrayList;
import java.util.List;

public class UserDeserializer implements JsonDeserializer<UserResponse> {

    @Override
    public UserResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        long id = jsonObject.get("id").getAsLong();
        long structure_id = jsonObject.get("structure_id").getAsLong();
        String name = jsonObject.get("name").isJsonNull() ? null : jsonObject.get("name").getAsString();
        String firstName = jsonObject.get("firstName").isJsonNull() ? null : jsonObject.get("firstName").getAsString();
        String fun = jsonObject.get("fun").isJsonNull() ? null : jsonObject.get("fun").getAsString();
        String email = jsonObject.get("email").isJsonNull() ? null : jsonObject.get("email").getAsString();
        String beginDate = jsonObject.get("beginDate").isJsonNull() ? null : jsonObject.get("beginDate").getAsString();
        String endDate = jsonObject.get("endDate").isJsonNull() ? null : jsonObject.get("endDate").getAsString();
        String status = jsonObject.get("status").isJsonNull() ? null : jsonObject.get("status").getAsString();
        String role = jsonObject.get("role").isJsonNull() ? null : jsonObject.get("role").getAsString();
        String job = jsonObject.get("job").isJsonNull() ? null : jsonObject.get("job").getAsString();
        String structureCode = jsonObject.get("structureCode").isJsonNull() ? null : jsonObject.get("structureCode").getAsString();
        String firebaseToken=jsonObject.get("firebaseToken").isJsonNull() ? null : jsonObject.get("firebaseToken").getAsString();
        String structureDesignation=jsonObject.get("structureDesignation").isJsonNull() ? null : jsonObject.get("structureDesignation").getAsString();
        boolean notification=jsonObject.get("notification").getAsBoolean();
        byte[] bytes=null;
        JsonElement jsonElement=jsonObject.get("bytes");
        if (jsonElement!=null && !jsonElement.isJsonNull())
             bytes = Base64.decode(jsonElement.getAsString(), Base64.DEFAULT);

        return new UserResponse(id,structure_id,structureDesignation,structureCode,name,firstName,fun,email,beginDate,endDate,status
                ,role,job,firebaseToken,notification,bytes);
    }
}

