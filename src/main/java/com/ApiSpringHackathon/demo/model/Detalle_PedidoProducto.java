package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

public class Detalle_PedidoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    private int FK_pedido;
    private int id_PedodoProducto;
    private int cantidad;
    private float Valor_unitario;
    private float Descuento;
    private int FK_UnidadMedida;



}
