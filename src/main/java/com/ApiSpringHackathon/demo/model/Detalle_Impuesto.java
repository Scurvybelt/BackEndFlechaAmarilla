package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;

public class Detalle_Impuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_DDImpuesto;
    private int FK_Detalle_PedidoProducto;

    @ManyToMany
    @JoinColumn(name = "FK_Impuesto", referencedColumnName = "id_Impuesto")
    private sat_impuestos impuesto;


}
