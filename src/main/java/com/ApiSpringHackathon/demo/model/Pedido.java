package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Pedio")

public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long ID_Pedido;
    private boolean Bon_Facturado;
    private int FK_Integrante;
    private int FK_TipoServicio;
    private Date Fecha_Pedido;
    private String Token_Ticket;
    private String C_Moneda;
    private float tipo_cambio;
    @ManyToOne
    @JoinColumn(name = "FIC_FormaPago", referencedColumnName = "c_formas_pago")
    private sat_formas_pago formaPago;

    public boolean isBon_Facturado() {
        return Bon_Facturado;
    }

    public void setBon_Facturado(boolean bon_Facturado) {
        Bon_Facturado = bon_Facturado;
    }

    public String getC_Moneda() {
        return C_Moneda;
    }

    public void setC_Moneda(String c_Moneda) {
        C_Moneda = c_Moneda;
    }

    public Date getFecha_Pedido() {
        return Fecha_Pedido;
    }

    public void setFecha_Pedido(Date fecha_Pedido) {
        Fecha_Pedido = fecha_Pedido;
    }


    public int getFK_Integrante() {
        return FK_Integrante;
    }

    public void setFK_Integrante(int FK_Integrante) {
        this.FK_Integrante = FK_Integrante;
    }

    public int getFK_TipoServicio() {
        return FK_TipoServicio;
    }

    public void setFK_TipoServicio(int FK_TipoServicio) {
        this.FK_TipoServicio = FK_TipoServicio;
    }

    public Long getID_Pedido() {
        return ID_Pedido;
    }

    public void setID_Pedido(Long ID_Pedido) {
        this.ID_Pedido = ID_Pedido;
    }

    public float getTipo_cambio() {
        return tipo_cambio;
    }

    public void setTipo_cambio(float tipo_cambio) {
        this.tipo_cambio = tipo_cambio;
    }

    public String getToken_Ticket() {
        return Token_Ticket;
    }

    public void setToken_Ticket(String token_Ticket) {
        Token_Ticket = token_Ticket;
    }

}
