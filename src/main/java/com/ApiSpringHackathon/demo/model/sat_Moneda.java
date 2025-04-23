package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sat_Moneda")

public class sat_Moneda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMoneda;
    private String c_Moneda;
    private String Descripcion;



}
