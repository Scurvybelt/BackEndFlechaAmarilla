package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Detalle_ProductoServicio")

public class Detalle_ProductoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedidoproducto;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_pedido_producto") // Asegúrate de que coincida con el nombre en la base de datos private Long id_PedidoProducto;

    @ManyToOne
    @JoinColumn(name = "FK_pedido", referencedColumnName = "ID_Pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "FK_producto", referencedColumnName = "ID_Producto", nullable = false)
    private ProductoServicio productoServicio;

    @Column(name = "cantidad")
    private float cantidad;
    @Column(name = "Valor_unitario")
    private float Valor_unitario;
    @Column(name = "Descuento")
    private float Descuento;

    @ManyToOne
    @JoinColumn(name = "FK_UnidadMedida", referencedColumnName = "id_ClaveUnidad", nullable = false)
    private sat_ClaveUnidad claveUnidad;






}
