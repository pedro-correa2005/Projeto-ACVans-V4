package com.fooengineers.projetoAcVansV4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fooengineers.projetoAcVansV4.entity.Oficina;

public interface OficinaRepository extends JpaRepository<Oficina, Integer>, JpaSpecificationExecutor<Oficina>{
	//Query para pesquisa por parâmetro id ou nome (barra de pesquisa)
	@Query(value="SELECT o FROM Oficina o WHERE STR(o.id) LIKE :param OR o.nome LIKE CONCAT('%', :param ,'%') ORDER BY o.id DESC")
	List<Oficina> listar(@Param("param") String param);
}
