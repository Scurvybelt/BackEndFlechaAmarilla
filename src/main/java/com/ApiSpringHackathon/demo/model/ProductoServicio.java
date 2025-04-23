package com.ApiSpringHackathon.demo.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ProductoServicio")

public class ProductoServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID_Producto;
    @Column(name = "C_ProductoServicio")
    private int C_ProductoServicio;
    @Column(name = "Descripcion")
    private String Descripcion;

}
