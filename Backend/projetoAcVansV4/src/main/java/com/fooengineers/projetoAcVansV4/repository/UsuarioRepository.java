package com.fooengineers.projetoAcVansV4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	//Query para busca por email
	Optional<Usuario> findByEmail(String email);
	
	//Query para verificar se existe usuário com email
	boolean existsByEmail(String email);
}
