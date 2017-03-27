package com.enviosya.cadete.dto;

import com.enviosya.cadete.dominio.Cadete;
import java.io.Serializable;

public class VehiculoDto implements Serializable {

    private Long id;
    private String matricula;
    private String descripcion;
    private Cadete cadete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Cadete getCadete() {
        return cadete;
    }

    public void setCadete(Cadete cadete) {
        this.cadete = cadete;
    }
}
