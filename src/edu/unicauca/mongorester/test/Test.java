package edu.unicauca.mongorester.test;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

import edu.unicauca.mongorester.model.TestObject;

public class Test {
  public static void main(String[] args) {
    ClientConfig config = new DefaultClientConfig();
    Client client = Client.create(config);
    WebResource service = client.resource(getBaseURI());
    Form f = new Form();
    
    /**BDs Disponibles**/ 
    //System.out.println("BD disponibles: "+service.accept(MediaType.TEXT_PLAIN).get(String.class));
    
    /**Coleciones disponibles en la BD asociada**/
    //System.out.println("Colecciones de la BD (test): "+service.path("/test").accept(MediaType.TEXT_PLAIN).get(String.class));
    
    /**Documentos en la colecion asociada**/
    //System.out.println("Documentos de la coleccion (test) de la BD (test): "+service.path("/test/test").accept(MediaType.TEXT_PLAIN).get(String.class));
    
    /**Documento por ID en la colecion asociada**/
    //System.out.println("Documento con id=1 de la coleccion (test) en la BD (test): "+service.path("/test/test/1").accept(MediaType.TEXT_PLAIN).get(String.class));
    
    /**Creacion de una BD**/
    //System.out.println("Creacion de BD (test_): "+service.path("/test_").accept(MediaType.TEXT_PLAIN).post(String.class));
    
    /**Eliminacion de una BD (No aconsejable)**/
    //System.out.println("Eliminacion de BD (test_): "+service.path("/test_").accept(MediaType.TEXT_PLAIN).delete(String.class));
    
    /**Creacion de una colecion dentro de una BD**/
    //System.out.println("Creacion de la colecion (coll_) en la BD (test_): "+service.path("/test_/coll_").accept(MediaType.TEXT_PLAIN).post(String.class));
    
    /**Eliminación de una colecion dentro de una BD**/
    //System.out.println("Eliminacion de la colecion coll_: "+service.path("/test_/coll_").accept(MediaType.TEXT_PLAIN).delete(String.class));
    
    /**Creacion de un doc dentro de la coleccion (test_) de la BD (test_) con un ID especifico, si es -1, creara un doc con un ID consecutivo al ultimo doc**/
    f.clear();
    long id = -1;
    f.add("doc",(new TestObject(id,"test","test description",new Date(System.currentTimeMillis()))).to_json());
    //System.out.println("Creacion de un doc en la colecion coll_: "+service.path("/test_/coll_/"+id).accept(MediaType.TEXT_PLAIN).post(String.class,f));
    
    /**Eliminación de un doc por ID dentro de una coll**/
    //System.out.println("Documento con id=1 de la coleccion (coll_) en la BD (test_): "+service.path("/test_/coll_/1").accept(MediaType.TEXT_PLAIN).delete(String.class));

  }

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost:8080/mongorester").build();
  }

} 