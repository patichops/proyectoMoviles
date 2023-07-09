package com.example.sistemaventas.Modelo.Responses;

import com.example.sistemaventas.Modelo.Entidades.Cliente;

import java.util.List;

public class FacturaPost {
    public List<ProductPost> Productos;
    public Cliente Cliente;
    public double Total;
    public double SubTotal;
    public double Iva;

    public FacturaPost(List<ProductPost> productos, com.example.sistemaventas.Modelo.Entidades.Cliente cliente, double total, double subTotal, double iva) {
        Productos = productos;
        Cliente = cliente;
        Total = total;
        SubTotal = subTotal;
        Iva = iva;
    }
}
