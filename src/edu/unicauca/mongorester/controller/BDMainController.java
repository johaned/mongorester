package edu.unicauca.mongorester.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import edu.unicauca.mongorester.conn.MongoDBConnection;
import edu.unicauca.mongorester.miscellaneus.Log;

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
				//System.out.println(dbo);
				return dbo;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static BasicDBObject create_db(String db) {
		try {
			mdbc = MongoDBConnection.getInstance();
			mdbc.getMc().getDB(db);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
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
		
		return false;
	}
}
