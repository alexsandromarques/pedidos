package com.aom.pedidos.service;

import com.aom.pedidos.model.Pedido;
import com.aom.pedidos.model.StatusEnum;
import com.aom.pedidos.model.StatusPedido;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.logging.Logger;

@Component
public class ProcessamentoPedidoConsumer {

    private final RabbitTemplate rabbitTemplate;
    
    private final PedidoService pedidoService;
    
    private final Random random = new Random();
    
    private final Logger logger = Logger.getLogger(ProcessamentoPedidoConsumer.class.getName());

    public ProcessamentoPedidoConsumer(RabbitTemplate rabbitTemplate, PedidoService pedidoService) {
        this.rabbitTemplate = rabbitTemplate;
        this.pedidoService = pedidoService;
    }

    @RabbitListener(queues = "${app.rabbitmq.fila.pedidos}")
    public void processar(@Payload Pedido pedido, Message message) throws Exception {
        logger.info("Recebido pedido: " + pedido.getId());

        try {
            Thread.sleep((random.nextInt(3) + 1) * 1000);

            if (random.nextDouble() < 0.2) {
                throw new RuntimeException("Falha simulada no processamento");
            }

            pedidoService.atualizarStatus(pedido.getId(), StatusEnum.SUCESSO.getNome());

            StatusPedido status = new StatusPedido(
                    pedido.getId(),
                    StatusEnum.SUCESSO,
                    LocalDateTime.now(),
                    null
            );

            rabbitTemplate.convertAndSend("pedidos.status.sucesso.aom", status);
            logger.info("Pedido processado com sucesso: " + pedido.getId());

        } catch (Exception e) {
            pedidoService.atualizarStatus(pedido.getId(), StatusEnum.FALHA.getNome());

            StatusPedido status = new StatusPedido(
                    pedido.getId(),
                    StatusEnum.FALHA,
                    LocalDateTime.now(),
                    e.getMessage()
            );

            rabbitTemplate.convertAndSend("pedidos.status.falha.aom", status);
            logger.warning("Erro ao processar pedido: " + pedido.getId() + " - " + e.getMessage());

            throw e; // rejeita a mensagem → vai para DLQ
        }
    }
}
