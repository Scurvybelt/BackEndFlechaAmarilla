package com.ApiSpringHackathon.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Integrante")


public class Integrante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Integrante;
    @Column(name = "nombre")
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "c_regimenfiscal", referencedColumnName = "id_RegimenFiscal", nullable = false)
    private sat_RegimenFiscal regimenFiscal;

    private String rfc;
    private String codigoPostal;
    @Lob
    private String certificado_b64;
    private String num_certificado;
    @Lob
    private String pfx_base64;
    private String contraseña;



    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCertificado_b64() {
        return certificado_b64;
    }

    public void setCertificado_b64(String certificado_b64) {
        this.certificado_b64 = certificado_b64;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNum_certificado() {
        return num_certificado;
    }

    public void setNum_certificado(String num_certificado) {
        this.num_certificado = num_certificado;
    }

    public String getPfx_base64() {
        return pfx_base64;
    }

    public void setPfx_base64(String pfx_base64) {
        this.pfx_base64 = pfx_base64;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }





}
