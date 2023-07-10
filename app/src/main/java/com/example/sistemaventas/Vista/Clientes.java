package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Modelo.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Cliente;
import com.example.sistemaventas.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Clientes extends AppCompatActivity {

    String url = "http://www.sistemaventasepe.somee.com/api/";
    private TableLayout tablaClientes;
    private List<Cliente> listaClientes;
    private Intent intentCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        intentCliente = this.getIntent();
        listaClientes = llenarDatos();
        tablaClientes = findViewById(R.id.tableLayoutProductos);

        for (Cliente fac : listaClientes) {
            TableRow tableRow = new TableRow(this);

            TextView campo1 = new TextView(this);
            campo1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo1.setText(String.valueOf(fac.cedula));
            tableRow.addView(campo1);

            TextView campo2 = new TextView(this);
            campo2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo2.setText(fac.nombre + " " + fac.apellido);
            tableRow.addView(campo2);

            TextView campo3 = new TextView(this);
            campo3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo3.setText(String.valueOf(fac.direccion));
            tableRow.addView(campo3);

            TextView campo4 = new TextView(this);
            campo4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo4.setText(String.valueOf(fac.telefono));
            tableRow.addView(campo4);

            Button editarProducto = new Button(this);
            editarProducto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            editarProducto.setText("EDITAR");
            editarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarCliente(fac.cedula);
                }
            });
            tableRow.addView(editarProducto);

            Button eliminarProducto = new Button(this);
            eliminarProducto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            eliminarProducto.setText("ELIMINAR");
            eliminarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarCliente(fac.cedula);
                }
            });
            tableRow.addView(eliminarProducto);

            tablaClientes.addView(tableRow);
        }
    }

    public void eliminarCliente(String cedula){
            AlertDialog.Builder bd = new AlertDialog.Builder(this);
            bd
                    .setMessage("¿Está seguro que desea eliminar el cliente?")
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            })
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ApiHandler.deleteAsync(url + cedula, new ApiHandler.OnDeleteDataListener() {
                        @Override
                        public void onDeleteDataSuccess(String response) {
                            Toast.makeText(Clientes.this,"Dato eliminado exitoso.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Clientes.this, Clientes.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onDeleteDataError(IOException e) {
                            Toast.makeText(Clientes.this,"Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            bd.create();
            bd.show();
    }

    public void editarCliente(String cedula){
        Intent intent = new Intent(this, RealizarVenta.class);
        startActivity(intent);
    }

    public List<Cliente> llenarDatos(){
        List<Cliente> resp;
        try {
            resp = new ApiHandler.getClientesTask().execute(url + "Clientes").get();
            return resp;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}