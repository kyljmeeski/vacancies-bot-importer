package com.kyljmeeski.vacanciesbot.importer;

import com.rabbitmq.client.ConnectionFactory;

public class Main {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
    }

}
