package com.delivery.monitor.message;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  @Value("${rabbitmq.exchange.name}")
  private String exchangeName;

  @Value("${rabbitmq.routing.key}")
  private String routingKey;

  private final RabbitTemplate rabbitTemplate;

  /**
   * Queue로 메시지를 발행
   *
   * @param message 발행할 메시지의 DTO 객체
   */
  @Override
  public void sendMessage(String message) {
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
  }
}