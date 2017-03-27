package com.enviosya.comentarios.dto;

public class ComentarioDto {
    
    private Long id;
    private int rating;
    private String comentario;
    private String estado;
    private Long envio;
    private Long autor;
    private String cadete;

    private String sentimiento;

    public Long getId() {
        return id;
    }

    public Long getEnvio() {
        return envio;
    }

    public void setEnvio(Long envio) {
        this.envio = envio;
    }

    public Long getAutor() {
        return autor;
    }

    public void setAutor(Long autor) {
        this.autor = autor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getSentimiento() {
        return sentimiento;
    }

    public void setSentimiento(String sentimiento) {
        this.sentimiento = sentimiento;
    }
    
    public String getCadete() {
        return cadete;
    }

    public void setCadete(String cadete) {
        this.cadete = cadete;
    }
    
}