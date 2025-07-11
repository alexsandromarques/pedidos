package com.aom.pedidos.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StatusPedido {

    private static final long serialVersionUID = 1L;

    private UUID idPedido;
    
    private StatusEnum status;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataProcessamento;
    
    private String mensagemErro;

    public StatusPedido() {
    }

    public StatusPedido(UUID idPedido, StatusEnum status, LocalDateTime dataProcessamento, String mensagemErro) {
        this.idPedido = idPedido;
        this.status = status;
        this.dataProcessamento = dataProcessamento;
        this.mensagemErro = mensagemErro;
    }

    public UUID getIdPedido() {
        return idPedido;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public LocalDateTime getDataProcessamento() {
        return dataProcessamento;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }
    
    
}
