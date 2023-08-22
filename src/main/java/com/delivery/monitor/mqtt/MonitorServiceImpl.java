package com.delivery.monitor.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MonitorServiceImpl implements MonitorService {

    @Value("${mqtt.broker-url}")
    private String mqttBrokerUrl;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.topic}")
    private String topic;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    // MQTT 메시지를 발행하는 메서드
    public void publishMessage(String payload) {
        MqttClient client = null;
        try {
            // MQTT 클라이언트 생성
            client = new MqttClient(mqttBrokerUrl, clientId);

            // 연결 옵션 설정
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());

            // 브로커에 연결
            client.connect(options);

            // 발행할 메시지 생성 및 발행
            MqttMessage message = new MqttMessage(payload.getBytes());
            client.publish(topic, message);

            // 연결 해제
            client.disconnect();
        } catch (MqttException e) {
            // 발생한 MqttException에 대한 에러 로깅
            log.error("An error occurred while publishing the message.", e);
        } finally {
            // 클라이언트 연결 해제
            if (client != null && client.isConnected()) {
                try {
                    client.disconnect();
                } catch (MqttException e) {
                    // 연결 해제 중에 발생한 에러 로깅
                    log.error("An error occurred while disconnecting the client.", e);
                }
            }
        }
    }
}
