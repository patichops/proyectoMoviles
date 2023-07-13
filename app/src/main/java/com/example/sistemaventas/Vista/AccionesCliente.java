package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Modelo.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Cliente;
import com.example.sistemaventas.R;

import java.util.concurrent.ExecutionException;

public class AccionesCliente extends AppCompatActivity {

    private static final String URL = "http://www.sistemaventasepe.somee.com/api/";
    private Cliente usuario;
    private Intent intentAcciones;

    private EditText nombre;
    private TextView cedula;
    private EditText correo;
    private EditText direccion;
    private EditText telefono;
    private EditText contrasenia;
    private Button volver;
    private Button guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones_cliente);
        intentAcciones = this.getIntent();
        usuario = traerCliente(
                Integer.parseInt(intentAcciones.getExtras().get("codigo").toString())
        );

        nombre = findViewById(R.id.editTextNombre);
        cedula = findViewById(R.id.textViewCedula);
        correo = findViewById(R.id.editTextCorreo);
        direccion = findViewById(R.id.editTextDireccion);
        telefono = findViewById(R.id.editTextTelefono);
        contrasenia = findViewById(R.id.editTextContraseña);
        volver = findViewById(R.id.buttonVolver);
        guardar = findViewById(R.id.buttonGuardarCliente);

        nombre.setText(usuario.nombre +" " +  usuario.apellido);
        cedula.setText(usuario.cedula);
        correo.setText(usuario.correo);
        direccion.setText(usuario.direccion);
        telefono.setText(usuario.telefono);
        contrasenia.setText(usuario.contrasenia);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAtras();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCliente();
            }
        });

    }

    private void guardarCliente() {

    }

    private void volverAtras() {
        Intent intent = new Intent(this, Ventas.class);
        startActivity(intent);
    }


    public Cliente traerCliente(int codigo){
        Cliente cl = new Cliente();

        try {
            cl = new ApiHandler.getClientesPorID().execute(URL + "Clientes/" + codigo).get();
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return cl;
    }
}