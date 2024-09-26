package com.simulado.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.simulado.api.model.ContaModel;
import com.simulado.api.service.ContaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios/{usuarioId}/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    // Criar uma nova conta para um usuário autenticado
    @PostMapping
    public ResponseEntity<ContaModel> criarConta(@PathVariable Long usuarioId, @RequestBody ContaModel conta) {
        try {
            ContaModel novaConta = contaService.criarConta(usuarioId, conta);
            return ResponseEntity.ok(novaConta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Usuário não encontrado
        }
    }

    // Listar todas as contas de um usuário autenticado
    @GetMapping
    public ResponseEntity<List<ContaModel>> listarContasPorUsuario(@PathVariable Long usuarioId) {
        List<ContaModel> contas = contaService.listarContasPorUsuario(usuarioId);
        return ResponseEntity.ok(contas);
    }

    // Buscar conta por ID
    @GetMapping("/{id}")
    public ResponseEntity<ContaModel> buscarContaPorId(@PathVariable Long usuarioId, @PathVariable Long id) {
        Optional<ContaModel> conta = contaService.buscarContaPorId(id);
        return conta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar uma conta
    @PutMapping("/{id}")
    public ResponseEntity<ContaModel> atualizarConta(@PathVariable Long usuarioId, @PathVariable Long id, @RequestBody ContaModel contaAtualizada) throws Exception {
        try {
            ContaModel conta = contaService.updateContaParcial(contaAtualizada, id);
            return ResponseEntity.ok(conta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Conta não encontrada
        }
    }

    // Deletar uma conta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long usuarioId, @PathVariable Long id) {
        contaService.deletarConta(id);
        return ResponseEntity.noContent().build();
    }
}

