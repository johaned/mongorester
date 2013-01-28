package edu.unicauca.mongorester.controller;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import edu.unicauca.mongorester.conn.MongoDBConnection;

public class BDMainController {

	private static MongoDBConnection mdbc;
	
	public BDMainController() {
		super();
	}
	
	public static List<String> get_databases(){
		try {
			mdbc = MongoDBConnection.getInstance();
			return mdbc.getMc().getDatabaseNames();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Set<String> get_colls_by_db(String db){
		try {
			mdbc = MongoDBConnection.getInstance();
			return mdbc.getMc().getDB(db).getCollectionNames();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
}
