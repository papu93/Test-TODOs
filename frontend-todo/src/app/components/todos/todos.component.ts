import { Component, OnInit } from '@angular/core';
import { TodosService } from '../../services/todos.service';
import { Todo } from 'src/app/models/todo';

@Component({
  selector: 'app-todos',
  templateUrl: './todos.component.html',
  styleUrls: ['./todos.component.css']
})
export class TodosComponent implements OnInit {
  todos: Todo[] = [];
  newTodo: Todo = new Todo;
  filterTodo: Todo = new Todo;
  url = 'https://via.placeholder.com/200/000000/FFFFFF/?text=Subir%20imagen';

  constructor(private todosService: TodosService) { }

  ngOnInit() {
    this.getTodos();
  }

  getTodos(): void {
    this.todosService.getTodos()
    .subscribe(todos => this.todos = todos);
  }

  searchFilterTodos(): void {
    this.todosService.searchTodos(this.filterTodo.id, this.filterTodo.description, this.filterTodo.state)
    .subscribe(todos => this.todos = todos);

  }

  onSelectFile(event) {
    if (event.target.files && event.target.files[0]) {
      var reader = new FileReader();

      reader.readAsDataURL(event.target.files[0]); // read file as data url

      reader.onload = (event: any) => { // called once readAsDataURL is completed
        this.url = event.target.result;
      }
      
      this.newTodo.img = event.target.files[0]
    }
  }
  
  addTodo(): void {  
    this.todosService.addTodo(this.newTodo.description, this.newTodo.state, this.newTodo.img)
      .subscribe(todo => {
        this.todos.push(todo);
      });
    this.newTodo = new Todo();
    this.url = 'https://via.placeholder.com/200/000000/FFFFFF/?text=Subir%20imagen';
  }

  onChange(newState, id: number) {    
    this.todosService.updateState(id, newState)
      .subscribe(afectedRows => {
        if(afectedRows === "1") {
          console.log("Estado actualizado correctamente");
        }
      });
  }
}