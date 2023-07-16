package com.example.sistemaventas.Vista;

import androidx.annotation.GravityInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Controllers.GridAdapter;
import com.example.sistemaventas.Modelo.Entidades.Producto;
import com.example.sistemaventas.Modelo.Responses.ApiHandler;
import com.example.sistemaventas.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CarritoCompras extends AppCompatActivity {

    private static final String URL = "http://www.sistemaventasepe.somee.com/api/";
    private List<String[]> carrito;

    private GridView gridViewProductos;
    private Button cancelar;
    private Button continuar;
    private Button mostrarCarrito;
    private Intent intentComprar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compras);
        intentComprar = this.getIntent();
        gridViewProductos = findViewById(R.id.gridViewProductos);
        cancelar = findViewById(R.id.buttonCancelar);
        continuar = findViewById(R.id.buttonContinuar);
        mostrarCarrito = findViewById(R.id.buttonMostrarCarrito);

        //llenar datos
        List<Producto> lista = llenarDatos();
        GridAdapter gridAdapter = new GridAdapter(this, lista);
        gridViewProductos.setAdapter(gridAdapter);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _cancelar();
            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _continuar();
            }
        });

        gridViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                agregarProducto(
                        lista.get(position).nombre,
                        lista.get(position).stock,
                        lista.get(position).precio,
                        view);
            }
        });

        mostrarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCarrito();
            }
        });
    }

    public void _continuar(){
        if (carrito == null){
            Toast.makeText(CarritoCompras.this, "No ha seleccionado ningun producto.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, RealizarVenta.class);
            intent.putExtra("codCliente", intentComprar.getExtras().get("codCliente").toString());

            ArrayList<ArrayList<String>> listaExtra = new ArrayList<>();
            for (String[] array : carrito) {
                listaExtra.add(new ArrayList<>(Arrays.asList(array)));
            }

            intent.putExtra("rol", intentComprar.getExtras().get("rol").toString());
            intent.putExtra("carrito", listaExtra);
            startActivity(intent);
        }
    }

    public void mostrarCarrito(){

        if (carrito == null){
            Toast.makeText(CarritoCompras.this,"Tu carrito está vacio.",Toast.LENGTH_SHORT).show();
        } else {
            LinearLayout rl = new LinearLayout(this);
            rl.setPadding(5,5,5,5);
            TableLayout tl = new TableLayout(this);
            tl.setLayoutParams(new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, 1f));
            tl.setGravity(Gravity.CENTER);

            for (int i = 0; i < carrito.size(); i++) {
                TableRow tr = new TableRow(this);

                TextView t1 = new TextView(this);
                t1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                t1.setText(carrito.get(i)[0]);
                tr.addView(t1);

                TextView t3 = new TextView(this);
                t3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                t3.setText(carrito.get(i)[1]);
                tr.addView(t3);

                TextView t2 = new TextView(this);
                t2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                t2.setText(carrito.get(i)[2]);
                tr.addView(t2);

                TextView t4 = new TextView(this);
                t4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                t4.setText(carrito.get(i)[3]);
                tr.addView(t4);

                TextView t5 = new TextView(this);
                t5.setText(carrito.get(i)[4]);
                t5.setVisibility(View.GONE);
                tr.addView(t5);

                Button editarProducto = new Button(this);
                editarProducto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                editarProducto.setText("EDITAR");
                int finalI = i;
                editarProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agregarProducto(
                                carrito.get(finalI)[0],
                                Integer.parseInt(carrito.get(finalI)[4]),
                                Double.parseDouble(carrito.get(finalI)[2]),
                                v);

                    }
                });
                tr.addView(editarProducto);

                tl.addView(tr);
            }

            rl.addView(tl);
            AlertDialog.Builder bd = new AlertDialog.Builder(this);
            bd
                    .setView(rl)
                    .setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            bd.create();
            bd.show();
        }
    }

    public void _cancelar(){
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
                        Intent intent = new Intent(CarritoCompras.this, Ventas.class);
                        intent.putExtra("codCliente", intentComprar.getExtras().get("codCliente").toString());
                        intent.putExtra("rol", intentComprar.getExtras().get("rol").toString());
                        startActivity(intent);
                    }
                });
        bd.create();
        bd.show();
    }

    private List<Producto> llenarDatos() {
        List<Producto> lista = new ArrayList<>();

        try {
            lista = new ApiHandler.GetProductosActivosTask().execute(URL + "Productos/ACTIVOS").get();
            return lista;
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(CarritoCompras.this,"Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        return lista;
    }

    private void agregarProducto(String nombre, int max, double precio, View v){
        AlertDialog.Builder bd = new AlertDialog.Builder(this);
        EditText cantidad = new EditText(this);
        cantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
        cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                if (!input.isEmpty()) {
                    int value = Integer.parseInt(input);

                    if (value > max) {
                        value = max;
                        cantidad.setText(String.valueOf(value));
                    } else if (value <= 0) {
                        value = 1;
                        cantidad.setText(String.valueOf(value));
                    }
                }
            }
        });

        bd.setMessage("¿Cuanto(s) " + nombre +"(s) desea comprar?")
                .setView(cantidad)
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accionAgregarProducto(
                                nombre,
                                precio,
                                Integer.parseInt(cantidad.getText().toString()),
                                max);
                    }
                });
        bd.create();
        bd.show();
    }

    private void accionAgregarProducto(String nombre, double precio, int cantidad, int max){
        if (carrito != null){
            String[] valor = new String[] {
                    nombre,
                    String.valueOf(cantidad),
                    String.valueOf(precio),
                    String.valueOf(precio * cantidad),
                    String.valueOf(max)
            };

            boolean encontrado = false;
            for (int i = 0; i < carrito.size(); i++) {
                String[] elemento = carrito.get(i);
                if (elemento[0].equals(valor[0])) {
                    carrito.set(i, valor);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                carrito.add(valor);
            }
        } else {
            carrito = new ArrayList<>();
            String[] valor = new String[] {
                    nombre,
                    String.valueOf(cantidad),
                    String.valueOf(precio),
                    String.valueOf(precio * cantidad),
                    String.valueOf(max)
            };
            carrito.add(valor);
        }

        Toast.makeText(CarritoCompras.this, "Agregado al carrito.", Toast.LENGTH_SHORT).show();
    }
}