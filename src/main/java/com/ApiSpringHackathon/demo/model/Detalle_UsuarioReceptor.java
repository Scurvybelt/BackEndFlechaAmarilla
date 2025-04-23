package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Detalle_UsuarioReceptor")


public class Detalle_UsuarioReceptor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuarioReceptor;

    @ManyToOne
    @JoinColumn(name = "c_regimenfiscal", referencedColumnName = "id_RegimenFiscal", nullable = false)
    private sat_RegimenFiscal regimenFiscal;

    @ManyToOne
    @JoinColumn(name = "FK_Receptor", referencedColumnName = "idUsuarioReceptor", nullable = false)
    private Receptor receptor;

}
