package com.melardev.spring.jaxrscrud.repositories;


import com.melardev.spring.jaxrscrud.entities.Todo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TodosRepository extends CrudRepository<Todo, Long> {

    List<Todo> findByCompleted(boolean done);

    @Query("select new Todo(t.id, t.title, t.completed, t.createdAt, t.updatedAt) from Todo t")
    List<Todo> findAllSummary();

    List<Todo> findAll();

    @Query("select new Todo(t.id, t.title, t.completed, t.createdAt, t.updatedAt) from Todo t where t.completed = false")
    List<Todo> findPendingTodos();

    @Query("select new Todo(t.id, t.title, t.completed, t.createdAt, t.updatedAt) from Todo t where t.completed = true")
    List<Todo> findCompletedTodos();

    List<Todo> findByCompletedTrue();

    List<Todo> findByCompletedFalse();

    List<Todo> findByCompletedIsTrue();


    List<Todo> findByCompletedIsFalse();

    List<Todo> findByTitleContains(String title);

    List<Todo> findByDescriptionContains(String description);


    @Query("select t from Todo t where t.completed = :completed")
    List<Todo> findByHqlCompletedIs(boolean completed);

    @Query("select t from Todo t where t.title like %:title%")
    List<Todo> findByHqlTitleLike(@Param("title") String word);

    @Query("SELECT t FROM Todo t WHERE title = :title and description  = :description")
    List<Todo> findByHqlTitleAndDescription(String title, @Param("description") String description);

    @Query("select t FROM Todo t WHERE t.title like %:title%")
    List<Todo> findByHqlTitleContains(String title);

    @Query("select t FROM Todo t WHERE description like %:dscrptn%")
    List<Todo> findByHqlDescriptionContains(@Param("dscrptn") String dscr);

    // @Query("select t FROM Todo t WHERE lower(description) like %lower(:dscrptn)%") <-------- Will not work, use below
    // @Query("select t FROM Todo t WHERE lower(description) like  %lower(?0)%") <------- Neither
    @Query("select t FROM Todo t WHERE lower(description) like lower(concat('%', :dscrptn, '%'))")
    List<Todo> findByHqlDescriptionContainsIgnoreCase(@Param("dscrptn") String dscr);

    @Query("select t FROM Todo t WHERE title = ?0 and description  = ?1")
    List<Todo> findByTHqlTitleAndDescription(String title, String description);


}