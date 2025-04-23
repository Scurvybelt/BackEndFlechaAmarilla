package com.ApiSpringHackathon.demo.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "sat_impuestos")

public class sat_impuestos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Impuesto;
    private float tasa_o_cuota;
    private String tipo_factor;
    private String c_Impuesto;
    private int Ban_Traslado ;

    public String getc_Impuesto() {
        return c_Impuesto;
    }

    public void setc_Impuesto(String c_Impuesto) {
        c_Impuesto = c_Impuesto;
    }

    public Long getId_Impuesto() {
        return id_Impuesto;
    }

    public void setId_Impuesto(Long id_Impuesto) {
        this.id_Impuesto = id_Impuesto;
    }

    public float getTasa_o_cuota() {
        return tasa_o_cuota;
    }

    public void setTasa_o_cuota(float tasa_o_cuota) {
        tasa_o_cuota = tasa_o_cuota;
    }

    public String getTipo_factor() {
        return tipo_factor;
    }

    public void settipo_factor(String tipo_factor) {
        tipo_factor = tipo_factor;
    }

}
