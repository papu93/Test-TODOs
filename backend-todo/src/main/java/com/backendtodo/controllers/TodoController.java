package com.backendtodo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.backendtodo.exception.FileException;
import com.backendtodo.exception.TodoIdNotFoundException;
import com.backendtodo.models.Todo;
import com.backendtodo.services.TodoService;
import com.google.gson.Gson;

@Controller
@CrossOrigin(origins = "*")
public class TodoController {

	@Autowired
	TodoService todoService;

    //localhost:8080/todos?id=1&description=tarea&state=Resuelto
    @RequestMapping(method=RequestMethod.GET, value="/todos", 
					produces = "application/json; charset=utf-8")
    public ResponseEntity<List<Todo>> getFilterTodos(@RequestParam(value="id", required = false) Long id,
										    		@RequestParam(value="description", required = false) String description,
										    		@RequestParam(value="state", required = false) String state) {
        
    	List<Todo> todos = todoService.getFilterTodos(id, description, state);
        return new ResponseEntity<List<Todo>>(todos, HttpStatus.OK);
    }

    //localhost:8080/todos
	@RequestMapping(method = RequestMethod.POST, value = "/todos", produces = "application/json; charset=utf-8", consumes = "multipart/form-data; charset=utf-8")
	public ResponseEntity<?> addTodo(@RequestParam("description") String description,
			@RequestParam("state") String state, @RequestParam("img") MultipartFile file) {
		try {
			Todo added = todoService.addTodo(description, state, file);
			return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(added);
		} catch(FileException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .contentType(MediaType.APPLICATION_JSON)
            		.body(new Gson().toJson(e.getMessage()));
		} catch(Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .contentType(MediaType.APPLICATION_JSON)
            		.body(new Gson().toJson(e.getMessage()));
		}
	}

    //localhost:8080/todos/id
    @RequestMapping(method=RequestMethod.PATCH, value="/todos/{id}",
		            produces = "application/json; charset=utf-8",
            		consumes = "application/json; charset=utf-8")
	public ResponseEntity<String> changeState(@PathVariable long id, @RequestBody Map<String, String> body) {
		
		String newState = body.get("state");  
		if(newState == null) {	        
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
        		.body(new Gson().toJson("Debe especificar un valor para el parametro 'state'"));
		}
		try {
			int updated = todoService.changeState(id, newState);
	        return ResponseEntity.status(HttpStatus.OK)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(new Gson().toJson("Numero de TODOs afectados " + updated));
		} catch(TodoIdNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .contentType(MediaType.APPLICATION_JSON)
            		.body(new Gson().toJson(e.getMessage()));
		} catch(Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .contentType(MediaType.APPLICATION_JSON)
            		.body(new Gson().toJson(e.getMessage()));
		}
	}
}
