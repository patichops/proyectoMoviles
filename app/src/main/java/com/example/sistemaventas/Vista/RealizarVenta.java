package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sistemaventas.R;

public class RealizarVenta extends AppCompatActivity {

    private TextView texto;
    private Intent intentRealizarVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_venta);
        intentRealizarVenta = this.getIntent();



    }
}

