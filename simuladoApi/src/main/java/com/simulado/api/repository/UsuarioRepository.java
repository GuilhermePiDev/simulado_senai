package com.simulado.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulado.api.model.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByEmail(String email);
}

