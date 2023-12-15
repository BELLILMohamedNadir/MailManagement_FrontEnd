package com.example.gestiondecourrier.ui.deserializer;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Base64;
import android.util.Log;

import com.example.gestiondecourrier.ui.pojo.JwtToken;
import com.example.gestiondecourrier.ui.pojo.UserResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class AuthenticationDeserializer implements JsonDeserializer<JwtToken> {

    @Override
    public JwtToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String token = jsonObject.get("token").isJsonNull() ? null : jsonObject.get("token").getAsString();

        JsonObject userResponseObject = jsonObject.getAsJsonObject("userResponse");
        long id = userResponseObject.get("id").getAsLong();
        long structure_id = userResponseObject.get("structure_id").getAsLong();
        String name = userResponseObject.get("name").isJsonNull() ? null : userResponseObject.get("name").getAsString();
        String firstName = userResponseObject.get("firstName").isJsonNull() ? null : userResponseObject.get("firstName").getAsString();
        String fun = userResponseObject.get("fun").isJsonNull() ? null : userResponseObject.get("fun").getAsString();
        String email = userResponseObject.get("email").isJsonNull() ? null : userResponseObject.get("email").getAsString();
        String beginDate = userResponseObject.get("beginDate").isJsonNull() ? null : userResponseObject.get("beginDate").getAsString();
        String endDate = userResponseObject.get("endDate").isJsonNull() ? null : userResponseObject.get("endDate").getAsString();
        String status = userResponseObject.get("status").isJsonNull() ? null : userResponseObject.get("status").getAsString();
        String role = userResponseObject.get("role").isJsonNull() ? null : userResponseObject.get("role").getAsString();
        String job = userResponseObject.get("job").isJsonNull() ? null : userResponseObject.get("job").getAsString();
        String structureCode = userResponseObject.get("structureCode").isJsonNull() ? null : userResponseObject.get("structureCode").getAsString();
        String structureDesignation = userResponseObject.get("structureDesignation").isJsonNull() ? null : userResponseObject.get("structureDesignation").getAsString();
        boolean notification = userResponseObject.get("notification").getAsBoolean();


        byte[] bytes=null;
        JsonElement bytesElement = userResponseObject.get("bytes");
        if (bytesElement != null && !bytesElement.isJsonNull()) {
            bytes = Base64.decode(bytesElement.getAsString(), Base64.DEFAULT);
        }
        UserResponse userResponse=new UserResponse(id,structure_id,structureDesignation,structureCode,name,firstName,fun,email,beginDate,endDate,status,role,job,null,notification,bytes);

        return new JwtToken(token,userResponse);
    }
}

