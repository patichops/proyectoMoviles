package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Modelo.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Factura;
import com.example.sistemaventas.Modelo.Entidades.Producto;
import com.example.sistemaventas.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Productos extends AppCompatActivity {

    String url = "http://www.sistemaventasepe.somee.com/api/";
    private TableLayout tablaProductos;
    private List<Producto> listaProductos;
    private Intent intentProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        intentProductos = this.getIntent();
        listaProductos = llenarDatos();
        tablaProductos = findViewById(R.id.tableLayoutProductos);

        for(Producto fac : listaProductos){
            TableRow tableRow = new TableRow(this);

            TextView campo1 = new TextView(this);
            campo1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo1.setText(String.valueOf(fac.idProducto));
            tableRow.addView(campo1);

            TextView campo2 = new TextView(this);
            campo2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo2.setText(fac.nombre);
            tableRow.addView(campo2);

            TextView campo3 = new TextView(this);
            campo3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo3.setText(String.valueOf(fac.precio));
            tableRow.addView(campo3);

            TextView campo4 = new TextView(this);
            campo4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo4.setText(String.valueOf(fac.stock));
            tableRow.addView(campo4);

            Button editarProducto = new Button(this);
            editarProducto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            editarProducto.setText("EDITAR");
            editarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarProducto();
                }
            });
            tableRow.addView(editarProducto);

            Button eliminarProducto = new Button(this);
            eliminarProducto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            eliminarProducto.setText("ELIMINAR");
            eliminarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarProducto();
                }
            });
            tableRow.addView(eliminarProducto);

            tablaProductos.addView(tableRow);
        }
    }

    public List<Producto> llenarDatos(){
        List<Producto> resp;
        try {
            resp = new ApiHandler.GetProductosTask().execute(url + "Productos").get();
            return resp;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminarProducto(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void editarProducto(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}