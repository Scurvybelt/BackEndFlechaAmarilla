package com.ApiSpringHackathon.demo.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "sat_impuestos")

public class sat_impuestos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Impuesto;
    private float Tasa_o_cuota;
    private String Tipo_factor;
    private String C_Impuesto;
    private String Bon_Translado;
}
