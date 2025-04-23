package com.ApiSpringHackathon.demo.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "sat_formas_pago")


public class sat_formas_pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_FormaPago;
    private String c_FormaPago;
    private String descripcion;
    private String c_Metodopago;

    public String getC_Metodopago() {
        return c_Metodopago;
    }

    public void setC_Metodopago(String c_Metodopago) {
        c_Metodopago = c_Metodopago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getC_FormaPago() {
        return c_FormaPago;
    }

    public void setC_FormaPago(String c_FormaPago) {
        this.c_FormaPago = c_FormaPago;
    }
}
