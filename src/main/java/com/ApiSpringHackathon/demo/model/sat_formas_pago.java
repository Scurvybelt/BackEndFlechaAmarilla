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

    private Long c_formas_pago;
    private String descripcion;
    private String C_Metodopago;

    public String getC_Metodopago() {
        return C_Metodopago;
    }

    public void setC_Metodopago(String c_Metodopago) {
        C_Metodopago = c_Metodopago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getID_Sat_Formas_Pago() {
        return c_formas_pago;
    }

    public void setc_formas_pago(Long c_formas_pago) {
        this.c_formas_pago = c_formas_pago;
    }
}
