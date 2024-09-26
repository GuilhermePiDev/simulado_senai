package com.simulado.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulado.api.model.ContaModel;
import com.simulado.api.model.UsuarioModel;

import java.util.List;

public interface ContaRepository extends JpaRepository<ContaModel, Long> {
    List<ContaModel> findByUsuarioId(Long usuarioId); 
}

