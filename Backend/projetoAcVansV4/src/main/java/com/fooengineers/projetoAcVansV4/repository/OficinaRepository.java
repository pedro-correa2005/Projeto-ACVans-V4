package com.fooengineers.projetoAcVansV4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fooengineers.projetoAcVansV4.entity.Oficina;

public interface OficinaRepository extends JpaRepository<Oficina, Long>{
}
