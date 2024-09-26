package com.simulado.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.simulado.api.model.UsuarioModel;
import com.simulado.api.service.UsuarioService;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;


@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Criar um novo usuário
    @PostMapping
    public ResponseEntity<UsuarioModel> criarUsuario(@RequestBody UsuarioModel usuario) {
        UsuarioModel novoUsuario = usuarioService.criarUsuario(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    // Login do usuário
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UsuarioModel usuario) {
        Optional<UsuarioModel> usuarioOpt = usuarioService.autenticar(usuario.getEmail(), usuario.getSenha());
        Map<String, Object> response = new HashMap<>();
        
        if (usuarioOpt.isPresent()) {
            response.put("message", "Login realizado com sucesso!");
            response.put("userId", usuarioOpt.get().getId());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Credenciais inválidas.");
            response.put("userId", null);
            return ResponseEntity.status(401).body(response);
        }
    }
    
    
    

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<UsuarioModel> usuario = usuarioService.buscarUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar um usuário
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioModel> atualizarUsuario(@PathVariable Long id,
            @RequestBody UsuarioModel usuarioAtualizado) throws Exception {
        try {
            UsuarioModel usuario = usuarioService.updateUsuarioParcial(usuarioAtualizado, id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
