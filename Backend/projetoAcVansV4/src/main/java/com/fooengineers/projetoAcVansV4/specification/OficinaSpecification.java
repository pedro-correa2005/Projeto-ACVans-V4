package com.fooengineers.projetoAcVansV4.specification;

import org.springframework.data.jpa.domain.Specification;

import com.fooengineers.projetoAcVansV4.entity.Oficina;

import jakarta.persistence.criteria.Predicate;

public class OficinaSpecification {
	public static Specification<Oficina> filtroGeral(String termo){
		return (root, query, cb) -> {
			String like = "%" + termo.toLowerCase() + "%";
			
			Predicate porNome = cb.like(cb.lower(root.get("nome")), like);
			Predicate porId = cb.like(root.get("id").as(String.class), like);
			
			return cb.or(porNome, porId);
		};
	}
}
