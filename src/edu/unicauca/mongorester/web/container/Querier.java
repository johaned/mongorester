package edu.unicauca.mongorester.web.container;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.unicauca.mongorester.controller.BDMainController;
import edu.unicauca.mongorester.miscellaneus.Log;



// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/")
public class Querier {

	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String list_databases() {
		String response = BDMainController.get_databases().toString();
		Log.print(response);
		return response;
	}
	
	@GET
	@Path("/{db}")
	@Produces(MediaType.TEXT_PLAIN)
	public String list_coll_by_db(@PathParam("db") String db) {
		String response = BDMainController.get_colls_by_db(db).toString();
		Log.print(response);
		return response;
	}

	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
	}

	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello Jersey" + "</title>"
				+ "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	}

	@POST
	@Path("/firstPath")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String test(@FormParam("firstAttr") String firstAttr,@FormParam("secondAttr") String secondAttr) {
			return "Hello "+ firstAttr +" "+secondAttr;
	}
	
	@POST
	@Path("/{firstPath}/{secondPath}")
	@Produces(MediaType.TEXT_PLAIN)
	public String test(@PathParam("firstPath") String firstPath,@PathParam("secondPath") String secondPath,@FormParam("firstAttr") String firstAttr,@FormParam("secondAttr") String secondAttr) {
		return "Hello Paths: "+firstPath+" "+ secondPath +" Attrs: " +firstAttr +" "+secondAttr;
	}

}