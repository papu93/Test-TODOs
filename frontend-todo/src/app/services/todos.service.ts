import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap, count } from 'rxjs/operators';

import { Todo } from '../models/todo';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class TodosService {
  
  private todosUrl = 'http://localhost:8080/todos';  // URL to api

  constructor(
    private http: HttpClient) { }

  /** GET TODOs from the server */
  getTodos(): Observable<Todo[]> {
    return this.http.get<Todo[]>(this.todosUrl)
      .pipe(
        tap(_ => this.log('TODOs obtenidos.')),
        catchError(this.handleError<Todo[]>('getTodos', []))
      );
  }

  /** GET TODOs with filters from the server */
  searchTodos(idFilter: number, descriptionFilter:string, stateFilter:string): Observable<Todo[]> {    
    let params = new HttpParams();
    if(idFilter != undefined) {
      params = params.append("id", idFilter.toString());
    }
    if((descriptionFilter != undefined) && (descriptionFilter != "")) {
      params = params.append("description", descriptionFilter);
    }
    if((stateFilter != undefined) && (stateFilter != "")) {
      params = params.append("state", stateFilter);
    }

    return this.http.get<Todo[]>(this.todosUrl, { params: params }).pipe(
      tap(_ => this.log("TODOs filtrados")),
      catchError(this.handleError<Todo[]>('searchTodos', []))
    );
  }


  /** PATCH: Actualizacion del estado de un todo */
  /** La decision de utilizar PATCH y no PUT es debido al atributo img. Considero que no seria eficiente
   * realizar una actualizacion completa del objeto, cuando solo es posible actualizar el estado. */
  updateState (id: number, newState:string): Observable<any> {

    return this.http.patch(this.todosUrl + "/" + id, { "state": newState }, httpOptions)
    .pipe(
      tap(_ => this.log(`updated state`)),
      catchError(this.handleError('updateState', 1))
    );
  }

  /** POST: add a new todo to the server */
  addTodo (description: string, state: string, img: File): Observable<any> {
    var headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    let formData = new FormData(); 
    formData.append("description", description); 
    formData.append("state", state); 
    formData.append("img", img); 

    return this.http.post<any>(this.todosUrl, formData, {headers: headers })
    .pipe(
      tap((newTodo: Todo) => this.log(`TODO agregado id=${newTodo.id}`)),
      catchError(this.handleError<Todo>('addTodo'))
    );
  }

      /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a TodoService message in console */
  private log(message: string) {
    console.log(`TodoService: ${message}`);
  }
}
