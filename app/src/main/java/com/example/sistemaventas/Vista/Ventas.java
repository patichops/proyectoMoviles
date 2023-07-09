package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sistemaventas.Modelo.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Factura;
import com.example.sistemaventas.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Ventas extends AppCompatActivity {

    String url = "http://www.sistemaventasepe.somee.com/api/";
    private TableLayout tablaVentas;
    private List<Factura> listaVentas;
    private Intent intentVentas;
    private Button buttonProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        buttonProductos = findViewById(R.id.buttonProductos);
        intentVentas = this.getIntent();

        listaVentas = llenarDatos();
        tablaVentas = findViewById(R.id.tableLayoutVentas);

        for(Factura fac : listaVentas){
            TableRow tableRow = new TableRow(this);

            TextView campo1 = new TextView(this);
            campo1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            campo1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo1.setText(String.valueOf(fac.idVenta));
            tableRow.addView(campo1);

            TextView campo2 = new TextView(this);
            campo2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
            campo2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo2.setText(fac.fechaRegistro);
            tableRow.addView(campo2);

            TextView campo3 = new TextView(this);
            campo3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            campo3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo3.setText(String.valueOf(fac.total));
            tableRow.addView(campo3);

            Button verFactura = new Button(this);
            verFactura.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            verFactura.setText("VER");
            verFactura.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mirarFactura();
                }
            });
            tableRow.addView(verFactura);

            tablaVentas.addView(tableRow);
        }

        buttonProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mirarProductos();
            }
        });
    }

    public void mirarFactura(){
        Intent intent = new Intent(this, MainActivity.class);
        String token = "";
        intent.putExtra("token",token);
        startActivity(intent);
        this.finish();
    }

    public void mirarProductos(){
        Intent intent = new Intent(this, Productos.class);
        String token = "";
        intent.putExtra("token",token);
        startActivity(intent);
        this.finish();
    }

    public List<Factura> llenarDatos(){
        List<Factura> resp;
        try {
            resp = new ApiHandler.GetFacturasTask().execute(url + "Ventas").get();
            return resp;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}