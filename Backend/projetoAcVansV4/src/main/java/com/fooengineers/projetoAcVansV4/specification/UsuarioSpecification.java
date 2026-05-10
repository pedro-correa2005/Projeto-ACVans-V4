package com.fooengineers.projetoAcVansV4.specification;

import org.springframework.data.jpa.domain.Specification;

import com.fooengineers.projetoAcVansV4.entity.Usuario;

import jakarta.persistence.criteria.Predicate;

public class UsuarioSpecification {
	public static Specification<Usuario> filtroGeral(Long idOficina, String termo){
		return (root, query, cb) -> {
			if(termo == null || termo.isBlank()) {
				return cb.equal(root.get("oficina").get("id"), idOficina);
			}
			String like = "%" + termo.toLowerCase() + "";
			
			Predicate porEmail = cb.like(cb.lower(root.get("email")), like);
			
			Predicate porOficina = cb.equal(root.get("oficina").get("id"), idOficina);
			
			return cb.and(porOficina, porEmail);
		};
	}
}
