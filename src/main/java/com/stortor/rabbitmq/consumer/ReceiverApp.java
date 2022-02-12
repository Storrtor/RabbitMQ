package com.stortor.rabbitmq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ReceiverApp {
    private static final String EXCHANGE_NAME = "directExchanger";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // привязываемся к определенному Exchanger
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // получаем себе очередь
        String queueName = channel.queueDeclare().getQueue();

        Scanner sc = new Scanner(System.in);
        System.out.println("Введите интересующую вас тему рассылки.\nНапример, /set_topic php");
        String topic = "";
        while (true) {
            String str = sc.nextLine();
            String[] splittedStr = str.split(" ");
            if (str.startsWith("/set_topic")) {
                topic = splittedStr[1];
                channel.queueBind(queueName, EXCHANGE_NAME, topic);
            }
            if (str.startsWith("/unset_topic")) {
                topic = splittedStr[1];
                channel.queueUnbind(queueName, EXCHANGE_NAME, topic);
            }
            System.out.println("Waiting for messages...");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received message: '" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {

            });
        }
    }
}
