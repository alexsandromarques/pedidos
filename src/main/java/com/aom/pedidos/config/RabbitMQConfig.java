package com.aom.pedidos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Value("${app.rabbitmq.fila.pedidos}")
	private String nomeFilaPrincipal;

	@Bean
	public Queue filaPrincipal() {
		return QueueBuilder.durable(nomeFilaPrincipal).withArgument("x-dead-letter-exchange", "")
				.withArgument("x-dead-letter-routing-key", nomeFilaPrincipal + ".dlq").build();
	}

	@Bean
	public Queue filaDLQ() {
		return QueueBuilder.durable(nomeFilaPrincipal + ".dlq").build();
	}

	@Bean
	public DirectExchange exchangePedidos() {
		return new DirectExchange("pedidos.exchange");
	}

	@Bean
	public Binding bindFilaPrincipal() {
		return BindingBuilder.bind(filaPrincipal()).to(exchangePedidos()).with(nomeFilaPrincipal);
	}

	@Bean
	public Binding bindDLQ() {
		return BindingBuilder.bind(filaDLQ()).to(new DirectExchange("")).with(nomeFilaPrincipal + ".dlq");
	}

	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
			Jackson2JsonMessageConverter messageConverter) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(messageConverter);
		return template;
	}

	@Bean
	public Queue filaStatusSucesso() {
		return QueueBuilder.durable("pedidos.status.sucesso.aom").build();
	}

	@Bean
	public Queue filaStatusFalha() {
		return QueueBuilder.durable("pedidos.status.falha.aom").build();
	}

}
