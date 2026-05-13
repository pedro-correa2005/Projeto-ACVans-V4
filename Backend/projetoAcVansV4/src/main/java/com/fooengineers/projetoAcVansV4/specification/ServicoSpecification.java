package com.fooengineers.projetoAcVansV4.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.fooengineers.projetoAcVansV4.entity.EtapaServico;
import com.fooengineers.projetoAcVansV4.entity.Servico;
import com.fooengineers.projetoAcVansV4.entity.StatusServico;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
import com.fooengineers.projetoAcVansV4.entity.Veiculo;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class ServicoSpecification {
	public static Specification<Servico> filtroGeral(String termo, Integer idOficina, Integer idStatusServico){
		return(root, query, cb) -> {
			query.distinct(true);
			
			Join<Servico, Veiculo> veiculo = root.join("veiculo");
			veiculo.join("cliente");
			
			root.join("tipoServico");
			root.join("etapaServico", JoinType.LEFT);
			
			List <Predicate> predicates = new ArrayList<>();
			
			predicates.add(
					cb.equal(root.get("oficina").get("id"), idOficina)
			);
			
			if(idStatusServico != null) {
				predicates.add(
						cb.equal(root.get("statusServico").get("id"), idStatusServico)
					);
			}
			
			if(termo != null && !termo.isBlank()) {
				String like = "%" + termo.toLowerCase() + "%";
				Predicate porIdServico = cb.like(
						root.get("id").as(String.class),
						like
				);
				Predicate porPlaca = cb.like(
						cb.lower(veiculo.get("placa")),
						like
				);
				Predicate porNomeCliente = cb.like(
						cb.lower(veiculo.get("nome")),
						like
				);
				predicates.add(
						cb.or(porIdServico, porPlaca, porNomeCliente)
				);
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
	public static Specification<Servico> filtrarPorVeiculo(String termo, Long idVeiculo){
		return(root, query, cb) -> {
			query.distinct(true);
			Join<Servico, TipoServico> tipoServico = root.join("tipoServico");
			Join<Servico, EtapaServico> etapaServico = root.join("etapaServico");
			Join<Servico, StatusServico> statusServico = root.join("statusServico");
			
			if(termo == null || termo.isBlank()) {
				return cb.equal(root.get("veiculo").get("id"), idVeiculo);
			}
			String like = "%" + termo.toLowerCase() + "%";
			
			Predicate porTipo = cb.like(cb.lower(tipoServico.get("descricao")), like);
			Predicate porStatus = cb.like(cb.lower(statusServico.get("descricao")), like);
			Predicate porEtapa = cb.like(cb.lower(etapaServico.get("descricao")), like);
			
			return cb.and(cb.equal(root.get("veiculo").get("id"), idVeiculo),
					cb.or(porTipo, porStatus, porEtapa));
		};
	}
}
