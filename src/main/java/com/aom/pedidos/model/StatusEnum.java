package com.aom.pedidos.model;

public enum StatusEnum {

    ENVIADO("Enviado"),
    AGUARDANDO_PROCESSO("Aguardando Processo"),
    SUCESSO("Sucesso"),
    FALHA("Falha");

    private final String nome;

    StatusEnum(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
