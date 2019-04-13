package com.backendtodo.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backendtodo.exception.FileException;
import com.backendtodo.exception.TodoIdNotFoundException;
import com.backendtodo.models.Todo;
import com.backendtodo.repositories.TodoRepository;
import com.backendtodo.specifications.TodoSpecs;

@Service
public class TodoServiceImpl implements TodoService {
	
    @Autowired
    TodoRepository todoRepository;
    
	@Override
	public List<Todo> getFilterTodos(Long id, String description, String state) {
		TodoSpecs specs = new TodoSpecs(id, description, state);	    
		return todoRepository.findAll(specs);
	}
	
	@Override
	public Todo addTodo(String description, String state, MultipartFile file) {
    	Todo t;
    	try {
			t =  new Todo(description, state, file.getBytes());
			return todoRepository.save(t);
		} catch (IOException e) {
            throw new FileException("Ha ocurrido un error al procesar la imagen.");
		}
	}
	
	@Override
	public int changeState(long id, String state) { 
        Optional<Todo> originalTodo = todoRepository.findById(Long.valueOf(id));
        if(!originalTodo.isPresent()) {
            throw new TodoIdNotFoundException("No hay tarea asociada al id=" + id);
        }
        Todo todo = originalTodo.get();
        todo.setState(state);
		int countUpdatedTodo = todoRepository.setStateTodoById(state, id);
		return countUpdatedTodo;
	}
}
