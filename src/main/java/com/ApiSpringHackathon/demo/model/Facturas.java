package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Factura")

public class Facturas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;
//    private String C_ID_Factura;
    @Column(name = "Num_Serie")
    private String Num_Serie;
    @Column(name = "Folio")
    private String Folio;
    @Column(name = "Fecha_Emision")
    private Date Fecha_Emision;
    @Column(name = "Fecha_Timbrado")
    private Date Fecha_Timbrado;
    @Column(name = "FK_Integrante")
    private int FK_Integrante;
    @ManyToOne
    @JoinColumn(name = "FK_Usuarios", referencedColumnName = "id_Usuario", nullable = false)
    private Users usuario;



}
