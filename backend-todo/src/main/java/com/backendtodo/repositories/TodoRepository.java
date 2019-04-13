package com.backendtodo.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backendtodo.models.Todo;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {
	
    List<Todo> findAll(Specification<Todo> spec);
	
    @Transactional
	@Modifying
	@Query("UPDATE Todo t set t.state = ?1 where t.id = ?2")
	int setStateTodoById(String state, Long id);
}