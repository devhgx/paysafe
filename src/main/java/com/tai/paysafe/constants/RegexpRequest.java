package com.tai.paysafe.constants;

public class RegexpRequest {
    public static final String USERNAME = "^[a-zA-Z0-9_]{6,15}$";
    public static final String USER_PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,15}$";
}
