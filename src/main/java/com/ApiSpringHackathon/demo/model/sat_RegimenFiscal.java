package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sat_RegimenFiscal")

public class sat_RegimenFiscal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_RegimenFiscal;
    private String c_RegimenFiscal;
    private String descripcion;
    private boolean Ban_TipoPersona;

    public boolean isBan_TipoPersona() {
        return Ban_TipoPersona;
    }

    public void setBan_TipoPersona(boolean ban_TipoPersona) {
        Ban_TipoPersona = ban_TipoPersona;
    }

    public String getC_RegimenFiscal() {
        return c_RegimenFiscal;
    }

    public void setC_RegimenFiscal(String c_RegimenFiscal) {
        this.c_RegimenFiscal = c_RegimenFiscal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
