package com.fooengineers.projetoAcVansV4.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario>{
	//Query para busca por email
	Optional<Usuario> findByEmail(String email);
	List<Usuario> findByOficina(Oficina oficina);
	//Query para verificar se existe usuário com email
	boolean existsByEmail(String email);
	
	Page<Usuario> findAll(Specification<Usuario> specification, Pageable pageable);
}
