package com.backendtodo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.backendtodo.exception.TodoIdNotFoundException;
import com.backendtodo.models.Todo;
import com.backendtodo.repositories.TodoRepository;
import com.backendtodo.services.TodoServiceImpl;
import com.backendtodo.specifications.TodoSpecs;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
public class TodoServiceImplTest {
	
    private MockMvc mockMvc;

    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
	TodoServiceImpl todoService;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(todoService)
                .build();
    }
    
    // Get All Todos
    @Test
    public void shouldGetAllTodos() {
		List<Todo> todoList = new ArrayList<>();
		todoList.add(new Todo(1,"Tarea 1", "Pendiente", null));
		todoList.add(new Todo(2,"Tarea 2", "En proceso", null));

        Mockito.when(this.todoRepository.findAll(Mockito.any(TodoSpecs.class))).thenReturn(todoList);

        List<Todo> responseList = this.todoService.getFilterTodos(null, null, null);

        assertEquals(2, responseList.size());
        verify(todoRepository, times(1)).findAll(Mockito.any(TodoSpecs.class));
    }
    
    // Get Filtered Todos
    @Test
    public void shouldGetFilteredTodos() {
		List<Todo> todoList = new ArrayList<>();
		todoList.add(new Todo(1,"Tarea 1", "Pendiente", null));

        Mockito.when(this.todoRepository.findAll(Mockito.any(TodoSpecs.class))).thenReturn(todoList);

        List<Todo> responseList = this.todoService.getFilterTodos((long) 1, "Pendiente", null);

        assertEquals(1, responseList.size());
        verify(todoRepository, times(1)).findAll(Mockito.any(TodoSpecs.class));
    }
    
    // Add Todo
    @Test
    public void shouldAddTodo() {
    	MockMultipartFile img = new MockMultipartFile("img", "", null, new byte[10]);	
		Todo t = null;
		try {
			t = new Todo(1,"Nueva tarea a agregar", "Pendiente", img.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

        Mockito.when(this.todoRepository.save(Mockito.any(Todo.class))).thenReturn(t);

        Todo createdTodo = this.todoService.addTodo(t.getDescription(), t.getState(), img);

        assertEquals(1, createdTodo.getId());
        verify(todoRepository, times(1)).save(Mockito.any(Todo.class));
    }
    
    // Change state
    @Test
    public void shouldChangeStateTodo() {
		Todo t = new Todo(1,"Tarea 1", "Pendiente", null);		

        Mockito.when(todoRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(t));
        Mockito.when(todoRepository.setStateTodoById(Mockito.any(String.class), Mockito.any(Long.class))).thenReturn(1);
        
        int countUpdatedTodo = this.todoService.changeState(1, "En proceso");

        assertEquals(1, countUpdatedTodo);
        verify(todoRepository, times(1)).findById(Mockito.any(Long.class));
        verify(todoRepository, times(1)).setStateTodoById(Mockito.any(String.class), Mockito.any(Long.class));
    }
    
    // Change state
    @Test
    public void shouldNotChangeStateTodo() {
        Mockito.when(todoRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        
        try {        
        	this.todoService.changeState(1, "En proceso");
        } catch (TodoIdNotFoundException e) {
        	assertThat(e.getMessage()).isEqualTo("No hay tarea asociada al id=1");
        }
   	}
}
