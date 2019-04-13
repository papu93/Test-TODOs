package com.backendtodo;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.backendtodo.controllers.TodoController;
import com.backendtodo.models.Todo;
import com.backendtodo.services.TodoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {
	
	private MockMvc mockMvc;

    @Mock
    private TodoService todoService;

	@InjectMocks
	private TodoController todoController;
	private Gson gson;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(todoController)
                .build();    
        
		this.gson = new GsonBuilder()
				.setPrettyPrinting()
				.excludeFieldsWithoutExposeAnnotation()
				.serializeNulls()
				.create();		
    }

    @Test
    public void testGetAllSuccess() throws Exception {
		List<Todo> todoList = new ArrayList<>();
		todoList.add(new Todo(1,"Tarea 1", "Pendiente", null));
		todoList.add(new Todo(2,"Tarea 2", "En proceso", null));
		
		Mockito.when(todoService.getFilterTodos(null, null,null))
				.thenReturn(todoList);
    	
        mockMvc.perform(get("/todos")
		        .contentType("application/json"))
        		.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Tarea 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].state", is("En proceso")));
        verify(todoService, times(1)).getFilterTodos(null,null,null);
        verifyNoMoreInteractions(todoService);
    }
    
    @Test
    public void testGetFilteredTodosSuccess() throws Exception {		
    	List<Todo> todoList = new ArrayList<>();
		todoList.add(new Todo(1,"Tarea 1", "Pendiente", null));
		
		Mockito.when(todoService.getFilterTodos(Mockito.any(Long.class), Mockito.any(String.class),Mockito.any(String.class)))
				.thenReturn(todoList);
	
        mockMvc.perform(get("/todos")
		        .contentType("application/json")
		        .param("id", "1")
		        .param("description", "Tarea")
		        .param("state", "Pendiente")
		).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Tarea 1")))
                .andExpect(jsonPath("$[0].state", is("Pendiente")));
        verify(todoService, times(1)).getFilterTodos((long) 1,"Tarea","Pendiente");
        verifyNoMoreInteractions(todoService);
    }
    
    @Test
    public void testAddTodoSuccess() throws Exception {
    	MockMultipartFile img = new MockMultipartFile("img", "", null, new byte[10]);	
		
    	Todo newTodo = new Todo(1, "Tarea a agregar", "En proceso", img.getBytes());
    	Mockito.when(todoService.addTodo(Mockito.any(String.class), Mockito.any(String.class),Mockito.any(MultipartFile.class)))
		.thenReturn(newTodo);
    	
    	mockMvc.perform(MockMvcRequestBuilders.multipart("/todos")
                .file("img",img.getBytes())
    			.param("description", "Tarea a agregar")
    			.param("state", "En proceso")
    		).andExpect(status().isCreated())
    			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			    .andExpect(jsonPath("$.id", is(1)))
			    .andExpect(jsonPath("$.description", is("Tarea a agregar")))
                .andExpect(jsonPath("$.state", is("En proceso")));   	
    }
    
	@Test
	public void testChangeStateSuccess() throws Exception {
		Mockito.when(todoService.changeState(Mockito.any(Long.class), Mockito.any(String.class)))
			.thenReturn(1);
		HashMap<String, Object> params = new HashMap<>();
		params.put("state", "Resuelto");
		
		mockMvc.perform(patch("/todos/" + 1)
		        .contentType("application/json")
		        .content(this.gson.toJson(params))
		).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(MockMvcResultMatchers.content().string("\"Numero de TODOs afectados 1\""));
	  }
	
	@Test
	public void testChangeStateBadRequest() throws Exception {
		Mockito.when(todoService.changeState(Mockito.any(Long.class), Mockito.any(String.class)))
			.thenReturn(1);
		HashMap<String, Object> params = new HashMap<>();
		
		mockMvc.perform(patch("/todos/" + 1)
		        .contentType("application/json")
		        .content(this.gson.toJson(params))
		).andExpect(status().isBadRequest());
	}
}


