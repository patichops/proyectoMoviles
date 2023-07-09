package com.example.sistemaventas.Controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemaventas.Modelo.Entidades.Cliente;
import com.example.sistemaventas.R;

import java.util.List;

public class TablaAdapter extends RecyclerView.Adapter<TablaAdapter.ViewHolder> {

    private List<Cliente> clienteList;

    public TablaAdapter(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_tabla, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente cliente = clienteList.get(position);
        holder.textNombre.setText(cliente.nombre);
        holder.textApellido.setText(cliente.apellido);
        holder.textTelefono.setText(cliente.telefono);
    }

    @Override
    public int getItemCount() {
        return clienteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre;
        TextView textApellido;
        TextView textTelefono;

        public ViewHolder(View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.text_nombre);
            textApellido = itemView.findViewById(R.id.text_apellido);
            textTelefono = itemView.findViewById(R.id.text_telefono);
        }
    }
}
