package com.ues.api_demo.models;

public class LoginRequest {
    private String user;
    private String pass;

    public LoginRequest(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
}
