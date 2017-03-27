package com.enviosya.comentarios.dominio;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Comentario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    private int rating;
    
    @NotNull
    @Column(length = 300)
    private String comentario;
    
    private String estado;
    
    @NotNull
    private Long envio;
    
    @NotNull
    private Long autor;
    
    @NotNull
    private String cadete;

    
    private String sentimiento;

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

    public Long getId() {
        return id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += id != null ? id.hashCode() : 0;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Comentario)) {
            return false;
        }
        Comentario other = (Comentario) object;
        return !((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id)));
    }  
    
    @Override
    public String toString() {
        return "Comentario[ id=" + id + " ]";
    }
    
}
