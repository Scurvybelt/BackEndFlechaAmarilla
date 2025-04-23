package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Detalle_Impuesto")

public class Detalle_Impuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID_DDimpuesto;

    @ManyToOne
    @JoinColumn(name = "FK_PedidoProducto", referencedColumnName = "idPedidoProducto", nullable = false)
    private Detalle_ProductoServicio detalleProductoServicio;

    @ManyToOne
    @JoinColumn(name = "FK_impuesto", referencedColumnName = "id_Impuesto", nullable = false)
    private sat_impuestos impuesto;

}
