package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Pedido")

public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long ID_Pedido;
    @Column(columnDefinition = "boolean default false")
    private boolean Ban_Facturado;
    @ManyToOne
    @JoinColumn(name = "FK_Integrante", referencedColumnName = "id_Integrante", nullable = false)
    private Integrante integrante;
    private int FK_TipoServicio;
    private Date Fecha_Pedido;
    private String Token_Ticket;
    @ManyToOne
    @JoinColumn(name = "C_Moneda", referencedColumnName = "idMoneda", nullable = false)
    private sat_Moneda moneda;
    private float tipo_cambio;

    @ManyToOne
    @JoinColumn(name = "FK_FormaPago", referencedColumnName = "id_FormaPago", nullable = false)
    private sat_formas_pago formaPago;

    @ManyToOne
    @JoinColumn(name = "FK_Factura", referencedColumnName = "idFactura", nullable = false)
    private Facturas factura;

    public boolean isBon_Facturado() {
        return Ban_Facturado;
    }

    public void setBon_Facturado(boolean bon_Facturado) {
        Ban_Facturado = bon_Facturado;
    }


    public Date getFecha_Pedido() {
        return Fecha_Pedido;
    }

    public void setFecha_Pedido(Date fecha_Pedido) {
        Fecha_Pedido = fecha_Pedido;
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
