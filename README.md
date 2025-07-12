# Sistema de Pedidos - Backend (Spring Boot)

Este projeto é o serviço backend responsável por receber, processar e atualizar os pedidos de forma assíncrona, utilizando RabbitMQ. Ele expõe uma API REST que se comunica com a aplicação desktop feita em Java Swing.

## Funcionalidades

- Recebe novos pedidos via API REST.
- Publica os pedidos em uma fila RabbitMQ para processamento assíncrono.
- Processa os pedidos com delay simulado e possibilidade de falha (para testes).
- Atualiza o status do pedido conforme o resultado do processamento.
- Permite consultar o status de um pedido via endpoint.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web (REST)
- Spring AMQP (RabbitMQ)
- RabbitMQ
- Jackson (JSON)



