package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.sistemaventas.Controllers.TablaAdapter;
import com.example.sistemaventas.Modelo.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Cliente;
import com.example.sistemaventas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Tabla extends AppCompatActivity {

    String url = "http://www.sistemaventasepe.somee.com/api/";
    private RecyclerView recyclerView;
    private TablaAdapter tablaAdapter;
    private List<Cliente> clienteList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla);
        clienteList = new ArrayList<>();

        try {
            clienteList = new ApiHandler.getClientesTask().execute(url + "Clientes").get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tablaAdapter = new TablaAdapter(clienteList);
        recyclerView.setAdapter(tablaAdapter);
    }
}