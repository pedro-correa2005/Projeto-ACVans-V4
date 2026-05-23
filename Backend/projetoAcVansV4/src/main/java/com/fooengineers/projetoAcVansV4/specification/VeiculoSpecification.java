package com.fooengineers.projetoAcVansV4.specification;

import org.springframework.data.jpa.domain.Specification;

import com.fooengineers.projetoAcVansV4.entity.Veiculo;

import jakarta.persistence.criteria.Predicate;

public class VeiculoSpecification {
	public static Specification<Veiculo> filtroGeral(String termo, Integer idOficina){
		return (root, query, cb) -> {
			if(termo == null || termo.isBlank()) {
				return cb.equal(root.get("oficina").get("id"), idOficina);
			}
			String like = "%" + termo.toLowerCase() + "%";
			
			Predicate porPlaca = cb.like(cb.lower(root.get("placa")), like);
			Predicate porMarca = cb.like(cb.lower(root.get("marca")), like);
			Predicate porModelo = cb.like(cb.lower(root.get("modelo")), like);
			
			Predicate porOficina = cb.equal(root.get("oficina").get("id"), idOficina);
			
			return cb.and(porOficina, cb.or(porPlaca, porMarca, porModelo));
		};
	}
	public static Specification<Veiculo> filtrarPorCliente(String termo, Long idCliente){
		return (root, query, cb) -> {
			if(termo == null || termo.isBlank()) {
				return cb.equal(root.get("cliente").get("id"), idCliente);
			}
			String like = "%" + termo.toLowerCase() + "%";
			
			Predicate porPlaca = cb.like(cb.lower(root.get("placa")), like);
			Predicate porMarca = cb.like(cb.lower(root.get("marca")), like);
			Predicate porModelo = cb.like(cb.lower(root.get("modelo")), like);
			
			Predicate porCliente = cb.equal(root.get("cliente").get("id"), idCliente);
			
			return cb.and(porCliente, cb.or(porPlaca, porMarca, porModelo));
		};
	}
}
