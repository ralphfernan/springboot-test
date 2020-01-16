package com.example.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
/*
 * https://docs.spring.io/spring-data/elasticsearch/docs/3.2.3.RELEASE/reference/html/#elasticsearch.mapping.meta-model
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
@Document(indexName = "person", type = "doc")
public class Person {
	@Id
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String name;
	private String email;
	private Class _class;
	public Class get_class() {
		return _class;
	}
	public void set_class(Class _class) {
		this._class = _class;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	

}
