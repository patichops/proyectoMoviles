package com.example.sistemaventas.Modelo.Responses;

public class ProductPost {
    public int cantidad;
    public int idProducto;
    public String nombre;
    public double precio;
    public double total;

    public ProductPost(int cantidad, int idProducto, String nombre, double precio, double total) {
        this.cantidad = cantidad;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.total = total;
    }

}
