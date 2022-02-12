package com.stortor.rabbitmq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ExchangerSenderApp {

    private static final String EXCHANGER_NAME = "directExchanger";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()){
            Channel channel = connection.createChannel();

            Scanner sc = new Scanner(System.in);
            System.out.println("Например, php some message");
            while (true) {
                String str = sc.nextLine();
                List<String> list = new ArrayList<>(Arrays.asList(str.split(" ")));
                String topic = list.get(0);
                list.remove(0);
                String message = String.join(" ", list);
                channel.basicPublish(EXCHANGER_NAME, topic, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("Sent to " + topic + ", message = " + message);
            }

        }
    }
}
