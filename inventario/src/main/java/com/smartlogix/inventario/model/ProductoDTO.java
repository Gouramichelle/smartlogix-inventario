package com.smartlogix.inventario.model;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String sku;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
}