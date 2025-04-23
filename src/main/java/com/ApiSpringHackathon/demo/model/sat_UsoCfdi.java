package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sat_UsoCfdi")

public class sat_UsoCfdi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_UsoCfdi;
    private String c_UsoCfdi;
    private String descripcion;
    private int Ban_TipoPersona;

    public String getC_UsoCfdi() {
        return c_UsoCfdi;
    }

    public void setC_UsoCfdi(String c_UsoCfdi) {
        this.c_UsoCfdi = c_UsoCfdi;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getBan_TipoPersona() {
        return Ban_TipoPersona;
    }

    public void setBan_TipoPersona(int ban_TipoPersona) {
        Ban_TipoPersona = ban_TipoPersona;
    }
}
