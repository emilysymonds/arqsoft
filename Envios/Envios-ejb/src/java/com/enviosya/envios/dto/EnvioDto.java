package com.enviosya.envios.dto;

public class EnvioDto {

    private Long id;
    private String descripcion;
    private Long clienteEmisor;
    private Long clienteReceptor;
    private int formaPago;
    private double comision;
    private String fotoPaquete;
    private Long cadete;
    private String direccionRetiro;
    private String direccionRecibo;
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

    public String getFotoPaquete() {
        return fotoPaquete;
    }

    public void setFotoPaquete(String fotoPaquete) {
        this.fotoPaquete = fotoPaquete;
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
}
