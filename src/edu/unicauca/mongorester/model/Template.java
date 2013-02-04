package edu.unicauca.mongorester.model;

public class Template {
	
	//Query Errors 
	public static String ERROR_UNCLASIFIED="E1";
	public static String DB_NO_FOUND="E2";
	public static String COLL_NO_FOUND="E3";
	public static String DOC_NO_FOUND="E4";
	public static String ID_FIELD_NO_FOUND = "E5";
	public static String ID_FIELD_ALREADY_EXIST = "E6";
			
	//Query warnings
	public static String DB_ALREADY_EXIST="W1";
	public static String COLL_ALREADY_EXIST="W2";

	//Query Success
	public static String DB_CREATED="S1";
	public static String DB_DELETED="S2";
	public static String COLL_CREATED = "S3";
	public static String COLL_DELETED="S4";
	public static String DOC_CREATED= "S5";
	public static  String DOC_REMOVED ="S6";
	
	//Query Info
	public static String DOC_CREATED_ID="I1";
}
