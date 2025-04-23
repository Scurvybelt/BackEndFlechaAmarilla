package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Detalle_ProductoServicio")

public class Detalle_ProductoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedidoProducto;
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
