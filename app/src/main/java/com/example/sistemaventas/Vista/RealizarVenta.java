package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Modelo.Entidades.Cliente;
import com.example.sistemaventas.Modelo.Entidades.Factura;
import com.example.sistemaventas.Modelo.Entidades.Producto;
import com.example.sistemaventas.Modelo.Responses.ApiHandler;
import com.example.sistemaventas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RealizarVenta extends AppCompatActivity {

    private static final String URL = "http://www.sistemaventasepe.somee.com/api/";

    private TextView nombre,
            direccion,
            telefono,
            subtotal,
            iva,
            total,
            fecha,
            numVenta;

    private Button cancelar, comprar;
    private TableLayout tablaProductos;
    private Cliente usuario;
    private Intent intentRealizarVenta;

    private float valorTotal;
    private ArrayList<ArrayList<String>> carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_venta);
        intentRealizarVenta = this.getIntent();

        nombre = findViewById(R.id.tv_cliente_factura);
        direccion = findViewById(R.id.tv_direccion_factura);
        telefono = findViewById(R.id.tv_telefono_factura);
        subtotal = findViewById(R.id.tv_subtotal_factura);
        iva = findViewById(R.id.tv_iva_factura);
        total = findViewById(R.id.tv_total_factura);
        fecha = findViewById(R.id.tv_fecha_factura);
        numVenta = findViewById(R.id.tv_numero_factura);
        cancelar = findViewById(R.id.buttonCancelarCompra);
        comprar = findViewById(R.id.buttonCrearCompra);
        tablaProductos = findViewById(R.id.tableLayoutProductos);

        usuario = traerCliente(Integer.parseInt(intentRealizarVenta.getExtras().get("codCliente").toString()));
        carrito = (ArrayList<ArrayList<String>>) intentRealizarVenta.getExtras().get("carrito");
        numVenta.setText(String.valueOf(traerUltimaVenta()));

        for (int i = 0; i < carrito.size(); i++) {
            TableRow tr = new TableRow(this);

            TextView t0 = new TextView(this);
            t0.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            t0.setText(carrito.get(i).get(0));
            t0.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(t0);

            TextView t1 = new TextView(this);
            t1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            t1.setText(carrito.get(i).get(1));
            t1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(t1);

            TextView t3 = new TextView(this);
            t3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            t3.setText(carrito.get(i).get(2));
            t3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(t3);

            TextView t2 = new TextView(this);
            t2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            t2.setText(carrito.get(i).get(3));
            t2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(t2);

            TextView t4 = new TextView(this);
            t4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            t4.setText(carrito.get(i).get(4));
            t4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(t4);

            tablaProductos.addView(tr);
        }

        Date fechaActual = Calendar.getInstance().getTime();
        fecha.setText(fechaActual.toString());
        nombre.setText(usuario.nombre);
        direccion.setText(usuario.direccion);
        telefono.setText(usuario.telefono);
        subtotal.setText(intentRealizarVenta.getExtras().get("subtotal").toString());

        float subto = Float.parseFloat(intentRealizarVenta.getExtras().get("subtotal").toString());
        valorTotal = (subto * 0.12f) + subto;

        total.setText(String.valueOf(valorTotal));
        iva.setText("0.12");


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarCompra();
            }
        });

        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                efectuarCompra();
            }
        });
    }

    public Cliente traerCliente(int codigo){
        Cliente res;
        try {
            res = new ApiHandler.getClientesPorID().execute(URL + "Clientes/" + codigo).get();
            return res;
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    public void cancelarCompra(){
        AlertDialog.Builder bd = new AlertDialog.Builder(this);
        bd
                .setMessage("¿Está seguro que desea cancelar el pedido?")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RealizarVenta.this, Ventas.class);
                        intent.putExtra("codCliente", intentRealizarVenta.getExtras().get("codCliente").toString());
                        intent.putExtra("rol", intentRealizarVenta.getExtras().get("rol").toString());
                        startActivity(intent);
                    }
                });
        bd.create();
        bd.show();
    }

    public void efectuarCompra(){
        try {
            JSONObject json = new JSONObject();
            JSONArray jsonProductos = new JSONArray();
            JSONObject jsonCliente = new JSONObject();

            for (int i = 0; i < carrito.size(); i++) {
                JSONObject producto = new JSONObject();

                producto.put("cantidad", carrito.get(i).get(2));
                producto.put("idProducto", carrito.get(i).get(0));
                producto.put("nombre", carrito.get(i).get(1));
                producto.put("precio", carrito.get(i).get(3));
                producto.put("total", carrito.get(i).get(4));

                jsonProductos.put(producto);
            }

            jsonCliente.put("codigoCliente", usuario.codigoCliente);
            jsonCliente.put("nombre", usuario.nombre);
            jsonCliente.put("apellido", usuario.apellido);
            jsonCliente.put("activo", usuario.activo);
            jsonCliente.put("direccion", usuario.direccion);
            jsonCliente.put("telefono", usuario.telefono);
            jsonCliente.put("correo", usuario.correo);
            jsonCliente.put("rol", usuario.rol);
            jsonCliente.put("contraseña", usuario.contrasenia);
            jsonCliente.put("cedula", usuario.cedula);

            json.put("productos",jsonProductos);
            json.put("cliente",jsonCliente);
            json.put("total",valorTotal);
            json.put("subTotal",Float.parseFloat(intentRealizarVenta.getExtras().get("subtotal").toString()));
            json.put("iva",0.12f); //

            ApiHandler.CrearDataAsync(URL + "Ventas", json, new ApiHandler.OnPostDataListener() {
                @Override
                public void onPostDataSuccess(String response) {
                    Toast.makeText(RealizarVenta.this,"Venta generada correctamente.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPostDataError(IOException e) {
                    Toast.makeText(RealizarVenta.this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Toast.makeText(RealizarVenta.this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public int traerUltimaVenta(){
        try {
            List<Factura> list = new ApiHandler.GetFacturasTask().execute(URL + "Ventas")
                    .get();
            int factura = list.get(list.size() - 1).idVenta + 1;
            return factura;
        } catch (ExecutionException | InterruptedException e) {
            return 0;
        }

    }
}

