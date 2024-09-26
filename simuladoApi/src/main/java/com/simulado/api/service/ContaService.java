package com.simulado.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulado.api.model.ContaModel;
import com.simulado.api.model.UsuarioModel;
import com.simulado.api.repository.ContaRepository;
import com.simulado.api.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Criar uma nova conta para um usuário autenticado
    public ContaModel criarConta(Long usuarioId, ContaModel conta) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        conta.setUsuario(usuario);
        return contaRepository.save(conta);
    }

    // Listar contas por usuário autenticado
    public List<ContaModel> listarContasPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new EntityNotFoundException("Usuário não encontrado com ID: " + usuarioId);
        }
        return contaRepository.findByUsuarioId(usuarioId);
    }

    

    // Buscar conta por ID
    public Optional<ContaModel> buscarContaPorId(Long id) {
        return contaRepository.findById(id);
    }

    // Atualizar conta
    public ContaModel updateContaParcial(ContaModel contaPatch, long id) throws Exception {
        ContaModel contaExistente = contaRepository.findById(id).orElse(null);
        if (contaExistente == null) {
            throw new Exception("Conta não encontrada.");
        }

        java.lang.reflect.Field[] fields = ContaModel.class.getDeclaredFields();

        for (java.lang.reflect.Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getName().equals("idConta")) { // Pular campo do ID
                    continue;
                }
                Object value = field.get(contaPatch);
                if (value != null) {
                    field.set(contaExistente, value); // Atualiza apenas os campos não nulos
                }
            } catch (IllegalAccessException e) {
                throw new Exception("Erro ao acessar o campo.");
            }
        }

        return contaRepository.save(contaExistente);
    }

    // Deletar conta
    public void deletarConta(Long id) {
        contaRepository.deleteById(id);
    }
}
