package com.example.sistemaventas.Modelo.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.lang.reflect.Type;

public class ApiResponse {
    public String Message;

    public boolean Success;

    public Type Data;

    public ApiResponse(String message, boolean success, Type data) {
        Message = message;
        Success = success;
        Data = data;
    }

}
