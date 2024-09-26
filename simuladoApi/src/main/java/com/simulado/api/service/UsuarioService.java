package com.simulado.api.service;

import org.aspectj.apache.bcel.classfile.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.simulado.api.model.UsuarioModel;
import com.simulado.api.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Criar um novo usuário
    public UsuarioModel criarUsuario(UsuarioModel usuario) {
        // Criptografando a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    // Autenticar o usuário com email e senha
    public Optional<UsuarioModel> autenticar(String email, String senha) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent() && passwordEncoder.matches(senha, usuarioOpt.get().getSenha())) {
            return usuarioOpt;
        }
        return Optional.empty();
    }

    // Listar todos os usuários
    public List<UsuarioModel> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Buscar usuário por ID
    public Optional<UsuarioModel> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Atualizar usuário
    public UsuarioModel updateUsuarioParcial(UsuarioModel usuarioPatch, long id) throws Exception {
        UsuarioModel usuarioExistente = usuarioRepository.findById(id).orElse(null);
        if (usuarioExistente == null) {
            throw new Exception("Usuário não encontrado.");
        }

        java.lang.reflect.Field[] fields = UsuarioModel.class.getDeclaredFields();

        for (java.lang.reflect.Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getName().equals("idUsuario")) { // Pular campo do ID
                    continue;
                }
                Object value = field.get(usuarioPatch);
                if (value != null) {
                    field.set(usuarioExistente, value); // Atualiza apenas os campos não nulos
                }
            } catch (IllegalAccessException e) {
                throw new Exception("Erro ao acessar o campo.");
            }
        }

        return usuarioRepository.save(usuarioExistente);
    }

    // Deletar usuário
    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public UsuarioModel atualizarUsuario(Long id, UsuarioModel usuarioAtualizado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarUsuario'");
    }
}

