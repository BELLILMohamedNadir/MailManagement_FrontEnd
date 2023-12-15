package com.example.gestiondecourrier.ui.pojo;

public class JwtToken {

    private String token;
    private UserResponse userResponse;

    public JwtToken() {
    }

    public JwtToken(String token, UserResponse userResponse) {
        this.token = token;
        this.userResponse = userResponse;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    @Override
    public String toString() {
        return "JwtToken{" +
                "token='" + token + '\'' +
                ", userResponse=" + userResponse +
                '}';
    }
}
