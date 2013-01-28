package edu.unicauca.mongorester.web.container;

import java.util.Set;

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
import edu.unicauca.mongorester.miscellaneus.BackResponse;
import edu.unicauca.mongorester.miscellaneus.Log;

@Path("/")
public class Querier {

	/********************************************************** GETS *********************************************************************/
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
		Set<String> colls= BDMainController.get_colls_by_db(db);
		if (colls==null){
			return new BackResponse(1,"Base de datos: ("+db+") no encontrada").to_json();
		}
		String response = colls.toString();
		Log.print(response);
		return response;
	}
	
	@GET
	@Path("/{db}/{coll}")
	@Produces(MediaType.TEXT_PLAIN)
	public String get_coll_by_db(@PathParam("db") String db,@PathParam("coll") String coll) {
		String response = BDMainController.get_coll_by_name(db, coll).toString();
		Log.print(response);
		return response;
	}
	
	@GET
	@Path("/{db}/{coll}/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String get_doc_by_db(@PathParam("db") String db,@PathParam("coll") String coll, @PathParam("id") Long id) {
		String response = BDMainController.get_doc_by_id(db, coll, id).toString();
		Log.print(response);
		return response;
	}
	
	/********************************************************** POSTS ********************************************************************/
	@POST
	@Path("/{db}")
	@Produces(MediaType.TEXT_PLAIN)
	public String create_db(@PathParam("db") String db) {
			return "Hello "+ db ;
	}
	
	/*************************************************************************************************************************************/

}