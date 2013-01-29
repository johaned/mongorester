package edu.unicauca.mongorester.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import edu.unicauca.mongorester.conn.MongoDBConnection;
import edu.unicauca.mongorester.miscellaneus.BackResponse;
import edu.unicauca.mongorester.miscellaneus.Log;
import edu.unicauca.mongorester.model.Template;

public class BDMainController {

	private static MongoDBConnection mdbc;

	public static List<String> get_databases() {
		try {
			mdbc = MongoDBConnection.getInstance();
			return mdbc.getMc().getDatabaseNames();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Set<String> get_colls_by_db(String db) {
		if(!isDB(db)){
			Log.print("DB don't exist");
			return null;
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			return mdbc.getMc().getDB(db).getCollectionNames();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DBCollection get_coll_by_name(String db, String coll) {
		if(!isCollInDB(db, coll)){
			Log.print("Collection don't exist in DB");
			return null;
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			return mdbc.getMc().getDB(db).getCollection(coll);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BasicDBObject get_doc_by_id(String db, String coll, Long id) {
		DBCursor cursor;
		BasicDBObject dbo=new BasicDBObject();
		try {
			mdbc = MongoDBConnection.getInstance();
			cursor = mdbc.getMc().getDB(db).getCollection(coll).find(new BasicDBObject("id", id));
			while (cursor.hasNext()) {
				dbo = (BasicDBObject) cursor.next();
				return dbo;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static BackResponse create_db(String db) {
		if(isDB(db)){
			return new BackResponse(Template.DB_ALREADY_EXIST,"Base de datos: ("+db+") ya existe");
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			mdbc.getMc().getDB(db);
			return new BackResponse(Template.DB_CREATED,"Base de datos: ("+db+") creada");
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
