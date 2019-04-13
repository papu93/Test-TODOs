package com.backendtodo.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.backendtodo.models.Todo;

public interface TodoService {

	public List<Todo> getFilterTodos(Long id, String description, String state);

	public Todo addTodo(String description, String state, MultipartFile file);
	
	public int changeState(long id, String state);

}
