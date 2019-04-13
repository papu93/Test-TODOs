package com.backendtodo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
	public long id;

    @Column(nullable = false, name="description")
	public String description;

    @Column(nullable = false, name="state", length = 50, updatable = true)
	public String state;
	
	@Lob
    @Column(nullable = false, name="img")
	public byte[] img;

	public Todo() {
		
	}
	
	public Todo(long id,String description, String state, byte[] img) {
		this.id = id;
		this.description = description;
		this.state = state;
		this.img = img;
	}

	public Todo(String description, String state, byte[] img) {
		this.description = description;
		this.state = state;
		this.img = img;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

}
