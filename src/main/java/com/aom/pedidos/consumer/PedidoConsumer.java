package com.aom.pedidos.consumer;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aom.pedidos.exception.ExcecaoDeProcessamento;
import com.aom.pedidos.model.Pedido;
import com.aom.pedidos.model.StatusEnum;
import com.aom.pedidos.model.StatusPedido;
import com.aom.pedidos.service.PedidoService;

@Component
public class PedidoConsumer {

    private final PedidoService pedidoService;
    private final RabbitTemplate rabbitTemplate;
    private final String filaSucesso;
    private final String filaFalha;
    private static final Logger LOGGER = Logger.getLogger(PedidoConsumer.class.getName());

    public PedidoConsumer(PedidoService pedidoService,
                          RabbitTemplate rabbitTemplate,
                          @Value("${app.rabbitmq.fila.sucesso}") String filaSucesso,
                          @Value("${app.rabbitmq.fila.falha}") String filaFalha) {
        this.pedidoService = pedidoService;
        this.rabbitTemplate = rabbitTemplate;
        this.filaSucesso = filaSucesso;
        this.filaFalha = filaFalha;
    }

    @RabbitListener(queues = "${app.rabbitmq.fila.pedidos}")
    public void processarPedido(Pedido pedido) {
        final UUID id = pedido.getId();
        LOGGER.info(() -> String.format("[CONSUMER] Recebido pedido ID %s", id));
        pedidoService.atualizarStatus(id, StatusEnum.AGUARDANDO_PROCESSO.getNome());

        try {
            simularProcessamento();
            
            // TODO: Validação Desativada - Usei só para para simular falha
            // validarPedido();

            StatusPedido status = new StatusPedido(id, StatusEnum.SUCESSO, LocalDateTime.now(), null);
            rabbitTemplate.convertAndSend(filaSucesso, status);
            pedidoService.atualizarStatus(id, StatusEnum.SUCESSO.getNome());

            LOGGER.info(() -> String.format("[CONSUMER] Pedido %s processado com sucesso", id));

        } catch (ExcecaoDeProcessamento ex) {
            LOGGER.warning(() -> String.format("[CONSUMER] Falha no processamento do pedido %s: %s", id, ex.getMessage()));
            StatusPedido status = new StatusPedido(id, StatusEnum.FALHA, null, ex.getMessage());
            rabbitTemplate.convertAndSend(filaFalha, status);
            pedidoService.atualizarStatus(id, StatusEnum.FALHA.getNome());
            throw ex;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format("[CONSUMER] Erro inesperado no pedido %s", id), e);
            throw new RuntimeException(e);
        }
    }

    private void simularProcessamento() throws InterruptedException {
        Thread.sleep(new Random().nextInt(3000) + 1000);
    }

    private void validarPedido() {
        if (Math.random() < 0.2) {
            throw new ExcecaoDeProcessamento("Erro aleatório simulado durante o processamento");
        }
    }
}
