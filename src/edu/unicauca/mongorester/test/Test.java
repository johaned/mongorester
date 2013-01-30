package edu.unicauca.mongorester.test;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

public class Test {
  public static void main(String[] args) {
    ClientConfig config = new DefaultClientConfig();
    Client client = Client.create(config);
    WebResource service = client.resource(getBaseURI());

    
    Form f = new Form();
    f.add("firstAttr", "atributo 1");
    f.add("secondAttr", "atributo 2");
    /**BDs Disponibles**/ 
    System.out.println("DB disponibles: "+service.accept(MediaType.TEXT_PLAIN).get(String.class));
    /**Coleciones disponibles en la BD asociada**/
    System.out.println("Colecciones de la DB test: "+service.path("/test_").accept(MediaType.TEXT_PLAIN).get(String.class));
    /**Documentos en la colecion asociada**/
    System.out.println("Documentos de la collección test: "+service.path("/test/test").accept(MediaType.TEXT_PLAIN).get(String.class));
    /**Documento por ID en la colecion asociada**/
    System.out.println("Documento con id=1 de la collección test: "+service.path("/test/test/1").accept(MediaType.TEXT_PLAIN).get(String.class));
    /**Creacion de una BD**/
    System.out.println("Creacion de BD test_: "+service.path("/test_").accept(MediaType.TEXT_PLAIN).post(String.class));
    /**Eliminacion de una BD (No aconsejable)**/
    //System.out.println("Eliminacion de BD test_: "+service.path("/test_").accept(MediaType.TEXT_PLAIN).delete(String.class));
    /**Creacion de una colecion dentro de una BD**/
    //System.out.println("Creacion de la colecion test_: "+service.path("/test_/coll_").accept(MediaType.TEXT_PLAIN).post(String.class));
    /**Eliminación de una colecion dentro de una BD**/
    System.out.println("Creacion de la colecion test_: "+service.path("/test_/coll_").accept(MediaType.TEXT_PLAIN).post(String.class));

  }

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost:8080/mongorester").build();
  }

} 