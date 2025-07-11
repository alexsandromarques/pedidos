package com.aom.pedidos.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Pedido {
	
    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotBlank(message = "Produto não pode ser vazio")
    private String produto;

    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private int quantidade;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCriacao;

    public Pedido() {
        this.id = UUID.randomUUID();
        this.dataCriacao = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
