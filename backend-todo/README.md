# Backend Todo Application

## Descripcion del sistema
El sistema permite guardar y listar Entidades TODOs con las siguientes características: <br />
TODO (To do, listado de tareas por hacer) <br />
- A) ID 
- B) DESCRIPCION (EL TODO a hacer)
- C) Estado
- D) Foto/Imagen adjunta a la DESCRIPCION


## Tecnologías
Proyecto construido a partir de Maven donde se utiliza Dependency Management para el manejo de dependencias. Basado en Java 8 donde se define Spring Boot para exponer los métodos REST, haciendo uso de las annotations. 
Se utiliza una BD en memoria hsqldb para que persistan los datos,utilizando JPA(javax.persistence). 
Se proveen test utilizando JUNIT y Mockito.

## Documentacion

La API REST presenta los siguientes endpoints: 

 | HTTP  | URL         | Descripcion                               |
 | ----- | ----------- | ----------------------------------------- |
 | GET   | /todos      | Devuelve todas las tareas.                |
 | POST  | /todos      | Crea una nueva tarea.                     |
 | PATCH | /todos/{id} | Modifica el estado de una tarea guardada. |
 
### GET
Utilizando este metodo, el sistema retorna una lista con las tareas guardadas, que cumplen con los filtros indicados. Existen tres filtros, los cuales son opcionales.
- id: Filtra aquella tarea cuya id coincide con este parametro.
- description: Filtra aquellas tareas, cuya descripcion contienen la palabra pasada en este parametro.
- state: Filtra las tareas segun el estado indicado. 

* **URL**
	
	GET /todos
  
* **URL Params**

   **Optional:**
 
   `id:[Number]`
   `description:[string]`
   `state:[string]`

* **Ejemplo**
	/todos?id=1&description=Tarea&state=Pendiente
 
* **Success Response:**
  
- **HTTP Status:** 200 OK

- **Content:**
```
[
    {
        "id": 0,
        "description": "Esta es la descripcion de la tarea.",
        "state": "En proceso",
        "img": "byte[] de la imagen"
    }
]
```

 
### POST
 Mediante este endpoint, es posible crear una nueva tarea. Para esto es necesario que cada uno de sus atributos esten cargadados.

* **URL**
	
	POST /todos
  
* **Data Params**

   **Required:**
 
   `todo:[Todo]`

* **Success Response:**
  
- **HTTP Status:** 201 CREATED

- **Content:**
```
[
    {
        "id": 0,
        "description": "Esta es la descripcion de la nueva tarea.",
        "state": "En proceso",
        "img": "byte[] de la imagen"
    }
] 
```

 * **Error Response:**

En el caso de que ocurra un error durante la transformacion de la imagen para guardarla en la BD:

  * **Code:** 500 INTERNAL SERVER ERROR <br />
    **Content:** `"Ha ocurrido un error al procesar la imagen."`
 
 
### PATCH
 Mediante este endpoint, es posible actualizar el estado de una tarea que se encuentre guardada en la base de datos.

* **URL**
	
	PATCH /todos/{id}
  
* **Data Params**

   **Required:**
 
   `state:[String]`

* **Success Response:**
  
- **HTTP Status:** 200 OK

- **Content:**

	"Numero de TODOs afectados 1"
 
 * **Error Response:**

Para poder actualizar el estado de la tarea, el id proveniente de la peticion debe corresponderse con una tarea ya creada en la BD.

  * **Code:** 400 BAD REQUEST <br />
    **Content:** `"No hay tarea asociada al id=N"`

  OR

  * **Code:** 400 BAD REQUEST <br />
    **Content:** `{ "Debe especificar un valor para el parametro 'state'" }`