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
    // Fluent interfaces
    System.out.println(service.path("rest").path("querier").accept(MediaType.TEXT_PLAIN).get(ClientResponse.class).toString());
    // Get plain text
    System.out.println(service.path("rest").path("querier").accept(MediaType.TEXT_PLAIN).get(String.class));
    // Get XML
    System.out.println(service.path("rest").path("querier").accept(MediaType.TEXT_XML).get(String.class));
    // The HTML
    System.out.println(service.path("rest").path("querier").accept(MediaType.TEXT_HTML).get(String.class));
    // POST dinamyc information path and attr
    
    Form f = new Form();
    f.add("firstAttr", "atributo 1");
    f.add("secondAttr", "atributo 2");
    
    System.out.println(service.path("rest").path("querier/firstPath").accept(MediaType.TEXT_PLAIN).post(String.class,f));
    
    System.out.println(service.path("rest").path("querier/perro1/gato1").accept(MediaType.TEXT_PLAIN).post(String.class,f));
    

  }

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost:8080/mongorester").build();
  }

} 