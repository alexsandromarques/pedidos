package com.aom.pedidos;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.aom.pedidos.model.Pedido;
import com.aom.pedidos.model.StatusEnum;
import com.aom.pedidos.service.PedidoService;

class PedidoServiceTest {

    private RabbitTemplate rabbitTemplate;
    private PedidoService pedidoService;

    private static final String FILA_TESTE = "pedidos.entrada.aom";

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        pedidoService = new PedidoService(rabbitTemplate, FILA_TESTE);
    }

    @Test
    void devePublicarPedidoNaFila() {
        Pedido pedido = new Pedido();
        UUID id = pedido.getId();

        pedidoService.enviarPedido(pedido);

        verify(rabbitTemplate, times(1)).convertAndSend(FILA_TESTE, pedido);
        assert StatusEnum.ENVIADO.getNome().equals(pedidoService.buscarStatus(id));
    }
}
