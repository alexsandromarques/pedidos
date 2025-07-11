package com.aom.pedidos.service;

import com.aom.pedidos.model.Pedido;
import com.aom.pedidos.model.StatusEnum;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
public class PedidoService {

    private final RabbitTemplate rabbitTemplate;
    
    private final String filaPedidos;
    
    private final Map<UUID, String> pedidosEmMemoria = new ConcurrentHashMap<>();
    
    private final Logger logger = Logger.getLogger(PedidoService.class.getName());

    public PedidoService(RabbitTemplate rabbitTemplate,
                         @Value("${app.rabbitmq.fila.pedidos}") String filaPedidos) {
        this.rabbitTemplate = rabbitTemplate;
        this.filaPedidos = filaPedidos;
    }

    public void enviarPedido(Pedido pedido) {
        pedidosEmMemoria.put(pedido.getId(), StatusEnum.ENVIADO.getNome());
        rabbitTemplate.convertAndSend(filaPedidos, pedido);
        logger.info("Pedido enviado para a fila: " + pedido.getId());
    }

    public void atualizarStatus(UUID id, String status) {
        pedidosEmMemoria.put(id, status);
    }

    public String buscarStatus(UUID id) {
        return pedidosEmMemoria.getOrDefault(id, "PEDIDO NÃO ENCONTRADO");
    }
}
