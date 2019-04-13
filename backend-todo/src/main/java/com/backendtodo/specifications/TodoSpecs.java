package com.backendtodo.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.backendtodo.models.Todo;

public class TodoSpecs implements Specification<Todo> {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String description;
	private String state;
	
	public TodoSpecs(Long id, String description, String state) {
		super();
		this.id = id;
		this.description = description;
		this.state = state;
	}
	
	public Predicate toPredicate(Root<Todo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		
		Predicate p = cb.conjunction();
		if (id != null) {
			p.getExpressions().add(cb.equal(root.get("id"), id));
		}
		if (description != null) {
			p.getExpressions().add(cb.like(root.get("description"), "%" + description + "%"));
		}
		if (state != null) {
			p.getExpressions().add(cb.equal(root.get("state"), state));
		}	
		return p;
	}
}
