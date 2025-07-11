package com.aom.pedidos.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aom.pedidos.model.Pedido;
import com.aom.pedidos.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> receberPedido(@Valid @RequestBody Pedido pedido) {
        pedidoService.enviarPedido(pedido);

        Map<String, Object> resposta = Map.of(
            "id", pedido.getId(),
            "mensagem", "Pedido recebido e será processado"
        );

        return ResponseEntity.accepted().body(resposta);
    }
    
    @GetMapping("/status/{id}")
    public ResponseEntity<String> consultarStatus(@PathVariable UUID id) {
        String status = pedidoService.buscarStatus(id);
        return ResponseEntity.ok(status);
    }
}
