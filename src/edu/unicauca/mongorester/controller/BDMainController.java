package edu.unicauca.mongorester.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.bson.BSON;
import org.bson.BSONLazyDecoder;
import org.bson.types.MaxKey;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
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
			list.add(new BackResponse(Template.ERROR_UNCLASIFIED, e
					.getMessage()).to_json());
		}
		return list;
	}

	public static Set<String> get_colls_by_db(String db) {
		ArrayList<String> list = new ArrayList<String>();
		Set<String> response;
		if (!isDB(db)) {
			Log.print("DB don't exist");
			list.add(new BackResponse(Template.DB_NO_FOUND,	"Base de datos no encontrada").to_json());
			response = new HashSet<String>(list);
			return response;
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			return mdbc.getMc().getDB(db).getCollectionNames();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			list.add(new BackResponse(Template.ERROR_UNCLASIFIED, e
					.getMessage()).to_json());
			response = new HashSet<String>(list);
			return response;
		}

	}

	public static String get_coll_by_name(String db, String coll) {
		if (!isCollInDB(db, coll)) {
			Log.print("Collection don't exist in DB");
			return new BackResponse(Template.COLL_NO_FOUND,"Collection don't exist in DB").to_json();
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			return new Gson().toJson(mdbc.getMc().getDB(db).getCollection(coll).find().toArray(10));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage())
					.to_json();
		}
	}

	public static BasicDBObject get_doc_by_id(String db, String coll, Long id) {
		DBCursor cursor;
		BasicDBObject dbo;
		try {
			mdbc = MongoDBConnection.getInstance();
			cursor = mdbc.getMc().getDB(db).getCollection(coll)
					.find(new BasicDBObject("id", id));
			while (cursor.hasNext()) {
				dbo = (BasicDBObject) cursor.next();
				return dbo;
			}
			dbo = new BasicDBObject().append("code", Template.COLL_NO_FOUND).append("comments",	"El doc con id (" + id + ") no existe en la coll ("+ coll + ") de la BD (" + db + ")");
			return dbo;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			dbo = new BasicDBObject()
					.append("code", Template.ERROR_UNCLASIFIED).append(
							"comments", e.getMessage());
			return dbo;
		}

	}

	public static BackResponse create_db(String db) {
		if (isDB(db)) {
			return new BackResponse(Template.DB_ALREADY_EXIST,
					"Base de datos: (" + db + ") ya existe");
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			mdbc.getMc().getDB(db).createCollection("test_",new BasicDBObject().append("test_", "_"));
			return new BackResponse(Template.DB_CREATED, "Base de datos: ("
					+ db + ") creada");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage());
		}

	}

	public static BackResponse create_coll_in_db(String db, String coll) {

		if (isCollInDB(db, coll)) {
			return new BackResponse(Template.COLL_ALREADY_EXIST, "Coleccion: ("
					+ coll + ") ya existe en la DB");
		} else {
			if (!isDB(db)) {
				return new BackResponse(Template.DB_NO_FOUND, "BD: (" + db
						+ ") no existe");
			}
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			mdbc.getMc()
					.getDB(db)
					.createCollection(coll,
							new BasicDBObject().append("_", "_"));

			return new BackResponse(Template.COLL_CREATED, "Coleccion: ("
					+ coll + ") creada");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage());
		}

	}

	public static BackResponse delete_db(String db) {
		if (!isDB(db)) {
			Log.print("DB don't exist");
			return new BackResponse(Template.DB_NO_FOUND, "Base de datos: ("
					+ db + ") no encontrada");
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			mdbc.getMc().dropDatabase(db);
			return new BackResponse(Template.DB_DELETED, "Base de datos: ("
					+ db + ") borrada");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage());
		}

	}

	public static BackResponse delete_coll_in_db(String db, String coll) {
		if (!isCollInDB(db, coll)) {
			Log.print("Coll don't exist in DB");
			return new BackResponse(Template.COLL_NO_FOUND, "Coleccion: ("
					+ coll + ") no encontrada en BD: (" + db + ")");
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			mdbc.getMc().getDB(db).getCollection(coll).drop();
			return new BackResponse(Template.COLL_DELETED, "Coleccion: ("
					+ coll + ") borrada en BD: (" + db + ")");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage());
		}

	}

	public static BackResponse create_doc_in_coll(String db, String coll,	String doc) {
		if (!isCollInDB(db, coll)) {
			Log.print("Coll don't exist in DB");
			return new BackResponse(Template.COLL_NO_FOUND, "Coleccion: ("
					+ coll + ") no encontrada en BD: (" + db + ")");
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			DBCollection coll_ = mdbc.getMc().getDB(db).getCollection(coll);
			DBObject dbo = (DBObject) JSON.parse(doc);
			Object id = dbo.get("id");
			boolean isId = false;
			if (id == null) {
				// return new
				// BackResponse(Template.ID_FIELD_NO_FOUND,"El campo (id) no fue encontrado en el documento a insertar");
				Log.print("El campo (id) no fue encontrado, procediendo a crear uno con el consecutivo");
			}
			if (!(coll_.find(new BasicDBObject("id", id)).size() == 0)) {
				return new BackResponse(Template.ID_FIELD_ALREADY_EXIST,
						"El doc con id (" + id + ") ya existe en la coll ("
								+ coll + ") de la BD (" + db + ")");
			}
			mdbc.getMc().getDB(db).getCollection(coll).insert(dbo);
			return new BackResponse(Template.DOC_CREATED, "documento: (" + doc
					+ ") en la coleccion:(" + coll + ") en BD: (" + db + ")");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage());
		}
	}

	public static BackResponse create_doc_in_coll(String db, String coll,String doc, Long id) {
		if (!isCollInDB(db, coll)) {
			Log.print("Coll don't exist in DB");
			return new BackResponse(Template.COLL_NO_FOUND, "Coleccion: ("+ coll + ") no encontrada en BD: (" + db + ")");
		}
		if (id < 0) {
			Log.print("Creacion como ultimo elemento respecto del ID");
			try {
				mdbc = MongoDBConnection.getInstance();
				DBCollection coll_ = mdbc.getMc().getDB(db).getCollection(coll);
				DBObject fields = new BasicDBObject("id",1);
				DBObject projection = new BasicDBObject("$project",fields);
				DBObject groupFields = new BasicDBObject( "_id", "maximo");
				groupFields.put("max", new BasicDBObject( "$max", "$id"));
				DBObject group = new BasicDBObject("$group", groupFields);
				AggregationOutput output = coll_.aggregate( projection, group );
				List <Object> result = (List) output.getCommandResult().toMap().get("result");
				HashMap<String, Long> maximo = (HashMap<String, Long>) result.get(0);
				Log.print("Maximo indice: "+maximo.get("max"));
				DBObject dbo = (DBObject) JSON.parse(doc);
				dbo.put("id", maximo.get("max")+1);
				Log.print("DBObject " + dbo.toString());
				coll_.insert(dbo);
				return new BackResponse(Template.DOC_CREATED_ID, "ID_CREATED_DOC:"+(maximo.get("max")+1)+",COLL:"+coll+",DB:"+db);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage());
			}
			
		} else {
			try {
				mdbc = MongoDBConnection.getInstance();
				DBCollection coll_ = mdbc.getMc().getDB(db).getCollection(coll);
				DBObject dbo = (DBObject) JSON.parse(doc);
				dbo.put("id", id);
				Log.print("DBObject" + dbo.toString());
				if (!(coll_.find(new BasicDBObject("id", id)).size() == 0)) {
					return new BackResponse(Template.ID_FIELD_ALREADY_EXIST,"El doc con id (" + id + ") ya existe en la coll ("+ coll + ") de la BD (" + db + ")");
				}
				mdbc.getMc().getDB(db).getCollection(coll).insert(dbo);
				return new BackResponse(Template.DOC_CREATED, "documento: ("+ doc + ") en la coleccion:(" + coll + ") en BD: ("+ db + ")");
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return new BackResponse(Template.ERROR_UNCLASIFIED,e.getMessage());
			}
		}
	}

	public static BackResponse delete_doc_in_coll(String db, String coll,Long id) {
		if (!isCollInDB(db, coll)) {
			Log.print("Coll don't exist in DB");
			return new BackResponse(Template.COLL_NO_FOUND, "Coleccion: ("
					+ coll + ") no encontrada en BD: (" + db + ")");
		}
		try {
			mdbc = MongoDBConnection.getInstance();
			DBCollection coll_ = mdbc.getMc().getDB(db).getCollection(coll);
			// DBCursor find = coll_.find(new BasicDBObject("id",id));
			if (coll_.find(new BasicDBObject("id", id)).size() == 0) {
				return new BackResponse(Template.DOC_NO_FOUND,	"El doc con id (" + id + ") no existe en la coll ("+ coll + ") de la BD (" + db + ")");
			}
			mdbc.getMc().getDB(db).getCollection(coll).findAndRemove(new BasicDBObject("id", id));
			return new BackResponse(Template.DOC_REMOVED, "documento con id: ("+ id + ") removido en la coleccion:(" + coll + ") en BD: ("+ db + ")");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return new BackResponse(Template.ERROR_UNCLASIFIED, e.getMessage());
		}
	}

	/************* Others *************/

	private static boolean isDB(String db) {
		List<String> dbs = get_databases();
		for (String db_ : dbs) {
			if (db_.equals(db)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isCollInDB(String db, String coll) {
		if (isDB(db)) {
			return mdbc.getMc().getDB(db).collectionExists(coll);
		} else {
			Log.print("DB don't exist");
		}
		return false;
	}
}
