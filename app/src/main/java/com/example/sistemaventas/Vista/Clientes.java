package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Modelo.Responses.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Cliente;
import com.example.sistemaventas.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Clientes extends AppCompatActivity {

    private static final String url = "https://dbventas-facturas-movil.somee.com/api/";
//    private static final String url = "https://www.sistemaventasepe.somee.com/api/";
    private TableLayout tablaClientes;
    private List<Cliente> listaClientes;
    private Intent intentCliente;
    private Button regresar, anterior, siguiente;

    private int currentPage = 1, pageSize = 5, totalPages;

    //menu
    private TextView logout, clientes, productos, ventas, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        intentCliente = this.getIntent();

        verificarUsuario(this, intentCliente);

        habilitarMenu();
        listaClientes = llenarDatos();
        tablaClientes = findViewById(R.id.tableLayoutClientes);
        anterior = findViewById(R.id.buttonAnterior);
        siguiente = findViewById(R.id.buttonSiguiente);
        totalPages = (int) Math.ceil((double) listaClientes.size() / pageSize);
        updateTable();

        regresar = findViewById(R.id.buttonProductos);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    voler();
                }
        });


        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1) {
                    currentPage--;
                    updateTable();
                }
            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < totalPages) {
                    currentPage++;
                    updateTable();
                }
            }
        });
    }

    private void verificarUsuario(Context pestaña, Intent intent){
        if (intent.getExtras().get("rol").toString().equals("usuario")){
            Intent ventas = new Intent(pestaña, Ventas.class);
            ventas.putExtra("codCliente", Integer.parseInt(intent.getExtras().get("codCliente").toString()));
            ventas.putExtra("nombre", intent.getExtras().get("nombre").toString());
            ventas.putExtra("rol", intent.getExtras().get("rol").toString());
            startActivity(ventas);
            this.finish();
        } else {
            Toast.makeText(pestaña,"Bienvenido usuario ADMINISTRADOR",Toast.LENGTH_SHORT).show();
        }
    }

    private void habilitarMenu() {
        //Habilitar el menu
        logout = findViewById(R.id.btMenuCerrarSesion);
        clientes = findViewById(R.id.btMenuClientes);
        productos = findViewById(R.id.btMenuProductos);
        ventas = findViewById(R.id.btMenuVentas);
        home = findViewById(R.id.btHome);

        if (intentCliente.getExtras().get("rol").toString().equals("usuario")){
            clientes.setVisibility(View.GONE);
            productos.setVisibility(View.GONE);
        } else {
            clientes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Clientes.this, Clientes.class);
                    intent.putExtra("codCliente",
                            Integer.parseInt(intentCliente.getExtras().get("codCliente").toString()));
                    intent.putExtra("rol", intentCliente.getExtras().get("rol").toString());
                    intent.putExtra("nombre", intentCliente.getExtras().get("nombre").toString());
                    startActivity(intent);
                    Clientes.this.finish();
                }
            });
            productos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Clientes.this, Productos.class);
                    intent.putExtra("codCliente", Integer.parseInt(intentCliente.getExtras().get("codCliente").toString()));
                    intent.putExtra("rol", intentCliente.getExtras().get("rol").toString());
                    intent.putExtra("nombre", intentCliente.getExtras().get("nombre").toString());
                    startActivity(intent);
                    Clientes.this.finish();
                }
            });
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Clientes.this, Ventas.class);
                intent.putExtra("codCliente", Integer.parseInt(intentCliente.getExtras().get("codCliente").toString()));
                intent.putExtra("nombre", intentCliente.getExtras().get("nombre").toString());
                intent.putExtra("rol", intentCliente.getExtras().get("rol").toString());
                startActivity(intent);
                Clientes.this.finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Clientes.this, Login.class);
                startActivity(intent);
                Clientes.this.finish();
            }
        });

        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Clientes.this, CarritoCompras.class);
                intent.putExtra("codCliente", Integer.parseInt(intentCliente.getExtras().get("codCliente").toString()));
                intent.putExtra("rol", intentCliente.getExtras().get("rol").toString());
                intent.putExtra("nombre", intentCliente.getExtras().get("nombre").toString());
                startActivity(intent);
                Clientes.this.finish();
            }
        });
    }

    private void voler() {
        Intent intent = new Intent(this, Productos.class);
        intent.putExtra("codCliente",
                Integer.parseInt(intentCliente.getExtras().get("codCliente").toString()));
        intent.putExtra("rol", intentCliente.getExtras().get("rol").toString());
        intent.putExtra("nombre", intentCliente.getExtras().get("nombre").toString());
        startActivity(intent);
    }

    public void eliminarCliente(int codigo){
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
                    ApiHandler.deleteAsync(url + "Clientes/" + codigo, new ApiHandler.OnDeleteDataListener() {
                        @Override
                        public void onDeleteDataSuccess(String response) {
                            Toast.makeText(Clientes.this,"Dato eliminado exitoso.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Clientes.this, Clientes.class);
                            intent.putExtra("codCliente",
                                    Integer.parseInt(intentCliente.getExtras().get("codCliente").toString()));
                            intent.putExtra("rol", intentCliente.getExtras().get("rol").toString());
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

    public void editarCliente(int cedula){
        Intent intent = new Intent(this, AccionesCliente.class);
        intent.putExtra("codCliente",
                Integer.parseInt(intentCliente.getExtras().get("codCliente").toString()));
        intent.putExtra("rol", intentCliente.getExtras().get("rol").toString());
        intent.putExtra("nombre", intentCliente.getExtras().get("nombre").toString());
        intent.putExtra("codigo",cedula);
        startActivity(intent);
    }

    public List<Cliente> llenarDatos(){
        List<Cliente> resp;
        try {
            if (intentCliente.getExtras().get("rol").toString().equals("admin")) {
                resp = new ApiHandler.getClientesTask().execute(url + "Clientes").get();
                return resp;
            } else {
                resp = new ApiHandler.getClientesActivosTask().execute(url + "Clientes/ACTIVOS").get();
                return resp;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateTable() {
        tablaClientes.removeAllViews();

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, listaClientes.size());

        TableRow encabezado = new TableRow(this);

        TextView h1 = new TextView(this);
        h1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        h1.setText("Cedula");
        h1.setTypeface(null, Typeface.BOLD);
        encabezado.addView(h1);

        TextView h2 = new TextView(this);
        h2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        h2.setText("Nombre");
        h2.setTypeface(null, Typeface.BOLD);
        encabezado.addView(h2);

        TextView h3 = new TextView(this);
        h3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        h3.setText("Direccion");
        h3.setTypeface(null, Typeface.BOLD);
        encabezado.addView(h3);

        TextView h4 = new TextView(this);
        h4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        h4.setText("Telefono");
        h4.setTypeface(null, Typeface.BOLD);
        encabezado.addView(h4);

        TextView campo5 = new TextView(this);
        campo5.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        campo5.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        campo5.setText("");
        encabezado.addView(campo5);

        TextView campo6 = new TextView(this);
        campo6.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        campo6.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        campo6.setText("");
        encabezado.addView(campo6);

        tablaClientes.addView(encabezado);

        for (int i = start; i < end; i++) {
            Cliente fac = listaClientes.get(i);

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

            Button editarCliente = new Button(this);
            editarCliente.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            editarCliente.setText("EDITAR");
            editarCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarCliente(fac.codigoCliente);
                }
            });
            tableRow.addView(editarCliente);

            Button eliminarCliente = new Button(this);
            eliminarCliente.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            eliminarCliente.setText("ELIMINAR");
            eliminarCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarCliente(fac.codigoCliente);
                }
            });
            tableRow.addView(eliminarCliente);

            tablaClientes.addView(tableRow);
        }
    }
}