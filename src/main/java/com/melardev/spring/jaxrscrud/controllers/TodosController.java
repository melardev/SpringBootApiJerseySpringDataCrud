package com.melardev.spring.jaxrscrud.controllers;


import com.melardev.spring.jaxrscrud.dtos.responses.ErrorResponse;
import com.melardev.spring.jaxrscrud.dtos.responses.SuccessResponse;
import com.melardev.spring.jaxrscrud.entities.Todo;
import com.melardev.spring.jaxrscrud.repositories.TodosRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Component
@Path("/todos")
public class TodosController {

    @Context
    HttpServletRequest request;

    @Context
    private TodosRepository todosRepository;

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response index() {

        List<Todo> todos = this.todosRepository.findAllSummary();
        return Response.ok().entity(todos)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Optional<Todo> todo = this.todosRepository.findById(id);
        if (todo.isPresent())
            return Response.ok().entity(todo.get()).build();
        else
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Todo not found")).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pending")
    public Response getNotCompletedTodos() {
        List<Todo> todos = this.todosRepository.findPendingTodos();
        return Response.ok().entity(todos).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/completed")
    public Response getCompletedTodos() {
        List<Todo> todos = todosRepository.findCompletedTodos();
        return Response.ok().entity(todos).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response create(Todo todo) {
        return Response.status(Response.Status.CREATED)
                .entity(todosRepository.save(todo))
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Todo todoInput) {
        Optional<Todo> optionalTodo = todosRepository.findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();

            String title = todoInput.getTitle();
            if (title != null)
                todo.setTitle(title);

            String description = todoInput.getDescription();
            if (description != null)
                todo.setDescription(description);

            todo.setCompleted(todoInput.isCompleted());

            return Response.ok()
                    .entity(todosRepository.save(optionalTodo.get()))
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Todo does not exist"))
                    .build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Optional<Todo> todo = todosRepository.findById(id);
        if (todo.isPresent()) {
            todosRepository.delete(todo.get());
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("This todo does not exist"))
                    .build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAll() {
        todosRepository.deleteAll();
        return Response.ok().entity(new SuccessResponse("Deleted all todos successfully")).build();
    }
}
