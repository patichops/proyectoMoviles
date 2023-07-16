package com.example.sistemaventas.Controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemaventas.R;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MenuFlotante extends AppCompatActivity {

    private Button menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_layout_principal);
        menu = findViewById(R.id.btMenuFlotante);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getParent(), "Funcional", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
