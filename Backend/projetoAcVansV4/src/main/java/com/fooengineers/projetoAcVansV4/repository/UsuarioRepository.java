package com.fooengineers.projetoAcVansV4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
