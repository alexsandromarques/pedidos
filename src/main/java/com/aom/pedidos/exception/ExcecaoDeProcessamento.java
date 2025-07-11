package com.aom.pedidos.exception;

public class ExcecaoDeProcessamento extends RuntimeException {

	public ExcecaoDeProcessamento(String mensagem) {
		super(mensagem);
	}

	public ExcecaoDeProcessamento(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
