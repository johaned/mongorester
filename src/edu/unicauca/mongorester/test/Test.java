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
    
    System.out.println("DB disponibles: "+service.accept(MediaType.TEXT_PLAIN).get(String.class));
    System.out.println("Colecciones de la DB test: "+service.path("/test_").accept(MediaType.TEXT_PLAIN).get(String.class));
    System.out.println("Documentos de la collección test: "+service.path("/test/test").accept(MediaType.TEXT_PLAIN).get(String.class));
    System.out.println("Documento con id=1 de la collección test: "+service.path("/test/test/1").accept(MediaType.TEXT_PLAIN).get(String.class));
    
    

  }

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost:8080/mongorester").build();
  }

} 