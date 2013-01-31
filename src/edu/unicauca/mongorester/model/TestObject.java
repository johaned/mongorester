package edu.unicauca.mongorester.model;

import java.util.Date;

import com.google.gson.Gson;

public class TestObject {
	private long id;
	private String name;
	private String desc;
	private Date tmSt;

	public TestObject() {
		super();
	}

	public TestObject(long id, String name, String desc, Date tmSt) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.tmSt = tmSt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getTmSt() {
		return tmSt;
	}

	public void setTmSt(Date tmSt) {
		this.tmSt = tmSt;
	}
	public String to_json(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
