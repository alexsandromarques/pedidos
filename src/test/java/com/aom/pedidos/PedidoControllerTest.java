package com.aom.pedidos;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.aom.pedidos.controller.PedidoController;
import com.aom.pedidos.model.Pedido;
import com.aom.pedidos.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void deveReceberPedidoValido() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setProduto("Camiseta");
        pedido.setQuantidade(5);

        doNothing().when(pedidoService).enviarPedido(pedido);

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.mensagem").value("Pedido recebido e será processado"));
    }

    @Test
    public void deveRejeitarPedidoInvalido() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setProduto(""); // inválido
        pedido.setQuantidade(0); // inválido

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isBadRequest());
    }
}
