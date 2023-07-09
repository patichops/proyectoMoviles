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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    String url = "http://www.sistemaventasepe.somee.com/api/";
    private TextView resetContra;
    private EditText cedula;
    private EditText clave;
    private Button login;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        resetContra = findViewById(R.id.tv_forgot_password);
        cedula = findViewById(R.id.et_cedula_login);
        clave = findViewById(R.id.et_contraseña_login);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvocarServicio();
            }
        });
    }

    public void InvocarServicio(){

        String dni = cedula.getText().toString();
        String contra = clave.getText().toString();
        JSONObject obj = new JSONObject();
        Cliente cl;

        try {
            cl =  ApiHandler.LoginAsync(url + "Login/" + dni + "&" + contra, obj, new ApiHandler.OnPostDataListener() {
                @Override
                public void onPostDataSuccess(String response) {
                    //logica de login
                    try {
                        token = response;
                        JSONObject responseJson = new JSONObject(token);
                        boolean val = Boolean.parseBoolean(responseJson.getString("success"));

                        if (!val){
                            Toast.makeText(Login.this.getBaseContext(),"Login Incorrecto", Toast.LENGTH_SHORT).show();
                        } else {
                            token = responseJson.getString("message");
                        }

                    } catch (Exception e){
                        Toast.makeText(Login.this.getBaseContext(),"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPostDataError(IOException e) {
                    Toast.makeText(Login.this.getBaseContext(),"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            if (cl != null){
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            } else {
                Toast.makeText(Login.this.getBaseContext(),"Error: Inicio de sesión no valido", Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}