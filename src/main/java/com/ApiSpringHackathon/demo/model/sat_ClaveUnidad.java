package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "sat_ClaveUnidad")


public class sat_ClaveUnidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ClaveUnidad;
    private String c_ClaveUnidad;
    private String descripcion;

    public String getC_ClaveUnidad() {
        return c_ClaveUnidad;
    }

    public void setC_ClaveUnidad(String c_ClaveUnidad) {
        this.c_ClaveUnidad = c_ClaveUnidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
