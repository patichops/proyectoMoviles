package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Modelo.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Cliente;
import com.example.sistemaventas.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texto = findViewById(R.id.textEjemplo);

        Intent intent = getIntent();
        texto.setText(intent.getExtras().get("token").toString());

/*        //GET
//        List<Cliente> res = null;
//        try {
//            res = new ApiHandler.getClientesTask().execute("http://www.sistemaVentasEPE.somee.com/api/Clientes").get();
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        texto.setText(res.get(0).nombre);*/

/*        //POST
//        try {
//            // Crea el objeto JSON con los datos a enviar
//            JSONObject postData = new JSONObject();
//            postData.put("nombre", "Ejemplo2");
//            postData.put("stock", 5);
//            postData.put("precio", 15.5);
//            postData.put("esActivo", true);
//
//            // Llama al método postData para enviar la solicitud POST
//            String response = ApiHandler.
//                    postDataAsync("http://www.sistemaVentasEPE.somee.com/api/Productos", postData, new ApiHandler.OnPostDataListener() {
//                        @Override
//                        public void onPostDataSuccess(String response) {
//                            // Manejar la respuesta exitosa
//                            texto.setText(response);
//                        }
//
//                        @Override
//                        public void onPostDataError(IOException e) {
//                            // Manejar el error
//                            texto.setText(e.getMessage());
//                        }
//                    });
//
//            // Maneja la respuesta como sea necesario
//            // Por ejemplo, puedes mostrarla en un TextView
//            texto.setText(response);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }*/

/*        //Delete
//        ApiHandler.deleteDataAsync("http://www.sistemaVentasEPE.somee.com/api/Productos/1006", new ApiHandler.OnDeleteDataListener() {
//            @Override
//            public void onDeleteDataSuccess(String response) {
//                // Manejar la respuesta exitosa
//                texto.setText(response);
//            }
//
//            @Override
//            public void onDeleteDataError(IOException e) {
//                // Manejar el error
//                texto.setText(e.getMessage());
//            }
//        });*/

/*        //UPDATE
        try {
            JSONObject updateData = new JSONObject();
            updateData.put("idProducto", 1005);
            updateData.put("nombre", "Ejemplo");
            updateData.put("stock", 30);
            updateData.put("precio", 2.00);
            updateData.put("esActivo", true);

            ApiHandler.updateDataAsync("http://www.sistemaVentasEPE.somee.com/api/Productos", updateData, new ApiHandler.OnUpdateDataListener() {
                @Override
                public void onUpdateDataSuccess(String response) {
                    // Manejar la respuesta exitosa
                    texto.setText(response);
                }

                @Override
                public void onUpdateDataError(IOException e) {
                    // Manejar el error
                    texto.setText(e.getMessage());
                }
            });
        }catch (JSONException e){
            texto.setText(e.getMessage());
        }*/

    }
}

