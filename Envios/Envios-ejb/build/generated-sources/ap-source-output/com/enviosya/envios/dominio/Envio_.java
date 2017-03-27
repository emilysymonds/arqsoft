package com.enviosya.envios.dominio;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-01T00:02:34")
@StaticMetamodel(Envio.class)
public class Envio_ { 

    public static volatile SingularAttribute<Envio, String> descripcion;
    public static volatile SingularAttribute<Envio, Long> clienteReceptor;
    public static volatile SingularAttribute<Envio, Long> cadete;
    public static volatile SingularAttribute<Envio, Long> clienteEmisor;
    public static volatile SingularAttribute<Envio, String> direccionRecibo;
    public static volatile SingularAttribute<Envio, Double> comision;
    public static volatile SingularAttribute<Envio, Integer> estadoEnvio;
    public static volatile SingularAttribute<Envio, Long> id;
    public static volatile SingularAttribute<Envio, Integer> formaPago;
    public static volatile SingularAttribute<Envio, String> direccionRetiro;
    public static volatile SingularAttribute<Envio, String> fotoPaquete;

}