package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Receptor")

public class Receptor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuarioReceptor;

    @Column(name = "Nombre_RazonSocial")
    private String Nombre_RazonSocial;
    @Column(name = "RFC")
    private String RFC;
    @Column(name = "Codigo_Postal")
    private String Codigo_Postal;
    @ManyToOne
    @JoinColumn(name = "UsoCFDI", referencedColumnName = "id_UsoCfdi", nullable = false)
    private sat_UsoCfdi usoCfdi;
    @ManyToOne
    @JoinColumn(name = "RegimenFiscal", referencedColumnName = "id_RegimenFiscal", nullable = false)
    private sat_RegimenFiscal regimenFiscal;


}
