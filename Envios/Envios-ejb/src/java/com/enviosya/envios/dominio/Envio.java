package com.enviosya.envios.dominio;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Envio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(length = 300)
    private String descripcion;

    @NotNull
    private Long clienteEmisor;

    @NotNull
    private Long clienteReceptor;

    @NotNull
    private int formaPago;
    
    private double comision;
    
    private Long cadete;

    @NotNull
    @Column(length = 300)
    private String direccionRetiro;

    @NotNull
    @Column(length = 300)
    private String direccionRecibo;
    
    @NotNull
    private int estadoEnvio;

    public int getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(int estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    public Long getClienteEmisor() {
        return clienteEmisor;
    }

    public void setClienteEmisor(Long clienteEmisor) {
        this.clienteEmisor = clienteEmisor;
    }

    public Long getClienteReceptor() {
        return clienteReceptor;
    }

    public void setClienteReceptor(Long clienteReceptor) {
        this.clienteReceptor = clienteReceptor;
    }

    public Long getCadete() {
        return cadete;
    }

    public void setCadete(Long cadete) {
        this.cadete = cadete;
    }

    public String getDireccionRetiro() {
        return direccionRetiro;
    }

    public void setDireccionRetiro(String direccionRetiro) {
        this.direccionRetiro = direccionRetiro;
    }

    public String getDireccionRecibo() {
        return direccionRecibo;
    }

    public void setDireccionRecibo(String direccionRecibo) {
        this.direccionRecibo = direccionRecibo;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += id != null ? id.hashCode() : 0;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Envio)) {
            return false;
        }
        Envio other = (Envio) object;
        return !((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Envio[ id=" + id + " ]";
    }
    
}
