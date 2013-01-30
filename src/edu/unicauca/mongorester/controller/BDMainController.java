package edu.unicauca.mongorester.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.sun.jersey.server.impl.template.TemplateViewProcessor;

import edu.unicauca.mongorester.conn.MongoDBConnection;
import edu.unicauca.mongorester.miscellaneus.BackResponse;
import edu.unicauca.mongorester.miscellaneus.Log;
import edu.unicauca.mongorester.model.Template;

public class BDMainController {

	private static MongoDBConnection mdbc;

	public static List<String> get_databases() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			mdbc = MongoDBConnection.getInstance();
			return mdbc.getMc().getDatabaseNames();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			list.add(new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage()).to_json());
		}
		return list;
	}

	public static Set<String> get_colls_by_db(String db) {
		ArrayList<String> list = new ArrayList<String>();
		Set<String> response;
		if(!isDB(db)){
			Log.print("DB don't exist");
			list.add(new BackResponse(Template.DB_NO_FOUND, "Base de datos no encontrada").to_json());
			response = new HashSet<String>(list);
			return response;
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			return mdbc.getMc().getDB(db).getCollectionNames();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			list.add(new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage()).to_json());
			response = new HashSet<String>(list);
			return response;
		}
		
	}

	public static String get_coll_by_name(String db, String coll) {
		if(!isCollInDB(db, coll)){
			Log.print("Collection don't exist in DB");
			return new BackResponse(Template.COLL_NO_FOUND, "Collection don't exist in DB").to_json();
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			return mdbc.getMc().getDB(db).getCollection(coll).toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage()).to_json();
		}
	}

	public static BasicDBObject get_doc_by_id(String db, String coll, Long id) {
		DBCursor cursor;
		BasicDBObject dbo;
		try {
			mdbc = MongoDBConnection.getInstance();
			cursor = mdbc.getMc().getDB(db).getCollection(coll).find(new BasicDBObject("id", id));
			while (cursor.hasNext()) {
				dbo = (BasicDBObject) cursor.next();
				return dbo;
			}
			dbo = new BasicDBObject().append("code", Template.COLL_NO_FOUND).append("comments", "Documento por Id no encontrado");
			return dbo;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			dbo = new BasicDBObject().append("code", Template.ERROR_UNCLASIFIED).append("comments", e.getMessage());
			return dbo;
		} 
		
	}
	
	public static BackResponse create_db(String db) {
		if(isDB(db)){
			return new BackResponse(Template.DB_ALREADY_EXIST,"Base de datos: ("+db+") ya existe");
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			mdbc.getMc().getDB(db).createCollection("test_", new BasicDBObject().append("test_", "_"));
			
			return new BackResponse(Template.DB_CREATED,"Base de datos: ("+db+") creada");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED,e.getMessage());
		} 
		
	}
	
	public static BackResponse create_coll_in_db(String db, String coll) {
		
		if(isCollInDB(db, coll)){
			return new BackResponse(Template.COLL_ALREADY_EXIST,"Coleccion: ("+coll+") ya existe en la DB");
		}else{
			if(!isDB(db)){
				return new BackResponse(Template.DB_NO_FOUND,"BD: ("+db+") no existe");
			}
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			mdbc.getMc().getDB(db).createCollection(coll, new BasicDBObject().append("test_", "_"));
			
			return new BackResponse(Template.COLL_CREATED,"Coleccion: ("+coll+") creada");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED,e.getMessage());
		} 
		
	}
	
	public static BackResponse delete_db(String db) {
		if(!isDB(db)){
			Log.print("DB don't exist");
			return new BackResponse(Template.DB_NO_FOUND,"Base de datos: ("+db+") no encontrada");
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			mdbc.getMc().dropDatabase(db);
			return new BackResponse(Template.DB_DELETED,"Base de datos: ("+db+") borrada");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED,e.getMessage());
		} 
		
	}
	
	/************* Others *************/
	
	private static boolean isDB(String db){
		List<String> dbs = get_databases();
		for (String db_ : dbs) {
			if(db_.equals(db)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean isCollInDB(String db, String coll){
		if(isDB(db)){
			Set<String> colls = get_colls_by_db(db);
			for (String coll_ : colls) {
				if(coll_.equals(coll)){
					return true;
				}		
			}
		}else{
			Log.print("DB don't exist");
		}
		return false;
	}
}
