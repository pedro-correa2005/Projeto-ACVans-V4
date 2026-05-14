package com.fooengineers.projetoAcVansV4.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.fooengineers.projetoAcVansV4.entity.HistoricoEtapa;

import jakarta.persistence.criteria.Predicate;

public class HistoricoEtapaSpecification {
	public static Specification<HistoricoEtapa> filtroMesAno(LocalDateTime inicio, LocalDateTime fim, Integer idOficina){
		return(root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			predicates.add(
					cb.equal(root.get("oficina").get("id"), idOficina)
					);
			if(inicio != null && fim != null) {
				predicates.add(
						cb.between(root.get("dataInicio"), inicio, fim)
						);
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
