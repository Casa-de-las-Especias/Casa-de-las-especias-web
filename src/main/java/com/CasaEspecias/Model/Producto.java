package com.CasaEspecias.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    // 🔹 NUEVO: tipo de producto (ESPECIA o UNITARIO)
    @Column(nullable = false, length = 20)
    private String tipoProducto;

    private int minimoGramos;
    private double kilo;
    private double medio;
    private double cuarto;
    private double cien_g;
    private double cincuenta_g;
    private double veinticinco_g;
    private double quince_g;
    private double diez_g;

    // 🔹 NUEVOS CAMPOS PARA PRODUCTOS UNITARIOS
    private double precioUnitario;
    private double unidadCantidad;
    private String unidadMedida;

    @Column(nullable = false)
    private double stockCantidad;

    @Column(nullable = false, length = 20)
    private String stockUnidad;

    @Column(length = 255)
    private String imagenURL;

    public Producto() {
    }

    public void generarPresentaciones() {

        // 🔥 Primero redondeamos el kilo
        this.kilo = redondearCentenaSuperior(this.kilo);

        // 🔥 Inicializamos todos los precios a 0
        this.medio = 0;
        this.cuarto = 0;
        this.cien_g = 0;
        this.cincuenta_g = 0;
        this.veinticinco_g = 0;
        this.quince_g = 0;
        this.diez_g = 0;

        int[] gramajes = {1000, 500, 250, 100, 50, 25, 15, 10};

        for (int gramos : gramajes) {

            if (gramos >= this.minimoGramos) {

                switch (gramos) {

                    case 500 -> {
                        double precio = (this.kilo / 2) + (this.kilo * 0.01);
                        this.medio = redondearCentenaSuperior(precio);
                    }

                    case 250 -> {
                        double precio = (this.kilo / 4) + (this.kilo * 0.02) + 50;
                        this.cuarto = redondearCentenaSuperior(precio);
                    }

                    case 100 -> {
                        double precio = (this.kilo / 10) + (this.kilo * 0.025);
                        this.cien_g = redondearCentenaSuperior(precio);
                    }

                    case 50 -> {
                        double precio = (this.kilo / 20) + (this.kilo * 0.02);
                        this.cincuenta_g = redondearCentenaSuperior(precio);
                    }

                    case 25 -> {
                        double precio = (this.kilo / 40) + (this.kilo * 0.02);
                        this.veinticinco_g = redondearCentenaSuperior(precio);
                    }

                    case 15 -> {
                        double precio = (this.kilo / 60) + (this.kilo * 0.01);
                        this.quince_g = redondearCentenaSuperior(precio);
                    }

                    case 10 -> {
                        double precio = (this.kilo / 100) + (this.kilo * 0.008);
                        this.diez_g = redondearCentenaSuperior(precio);
                    }
                }
            }
            // 🔥 Si el gramaje es menor que el mínimo, ya quedó en 0 por la inicialización
        }
    }

    // 🔥 REDONDEO A CENTENA SUPERIOR
    private double redondearCentenaSuperior(double valor) {
        return Math.ceil(valor / 100.0) * 100;
    }

    // GETTERS Y SETTERS
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre != null) {
            this.nombre = nombre.toUpperCase();
        } else {
            this.nombre = null;
        }
    }

    public String getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(String tipoProducto) { this.tipoProducto = tipoProducto; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getUnidadCantidad() { return unidadCantidad; }
    public void setUnidadCantidad(double unidadCantidad) { this.unidadCantidad = unidadCantidad; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public double getStockCantidad() {
        return stockCantidad;
    }

    public void setStockCantidad(double stockCantidad) {
        this.stockCantidad = stockCantidad;
    }

    public String getStockUnidad() {
        return stockUnidad;
    }

    public void setStockUnidad(String stockUnidad) {
        this.stockUnidad = stockUnidad;
    }

    public double getKilo() { return kilo; }

    public void setKilo(double kilo) {this.kilo = kilo;}

    public int getMinimoGramos() {return minimoGramos;}
    public void setMinimoGramos(int minimoGramos) {this.minimoGramos = minimoGramos;}

    public double getMedio() { return medio; }
    public double getCuarto() { return cuarto; }
    public double getCien_g() { return cien_g; }
    public double getCincuenta_g() { return cincuenta_g; }
    public double getVeinticinco_g() { return veinticinco_g; }
    public double getQuince_g() { return quince_g; }
    public double getDiez_g() { return diez_g; }

    public String getImagenURL() { return imagenURL; }
    public void setImagenURL(String imagenURL) { this.imagenURL = imagenURL; }
}