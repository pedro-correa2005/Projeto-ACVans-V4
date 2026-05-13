package com.fooengineers.projetoAcVansV4.specification;

import org.springframework.data.jpa.domain.Specification;

import com.fooengineers.projetoAcVansV4.entity.Cliente;

import jakarta.persistence.criteria.Predicate;

public class ClienteSpecification {
	public static Specification<Cliente> filtroGeral(String termo, Integer idOficina){
		return (root, query, cb) -> {
			if(termo == null || termo.isBlank()) {
				return cb.equal(root.get("oficina").get("id"), idOficina);
			}
			String like = "%" + termo.toLowerCase() + "%";
			
			Predicate porNome = cb.like(cb.lower(root.get("nome")), like);
			Predicate porCelular = cb.like(cb.lower(root.get("celular")), like);
			
			Predicate porOficina = cb.equal(root.get("oficina").get("id"), idOficina);
			
			return cb.and(porOficina, cb.or(porNome, porCelular));
		};
	}
}
