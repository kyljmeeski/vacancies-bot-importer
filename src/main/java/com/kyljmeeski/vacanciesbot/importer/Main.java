package com.kyljmeeski.vacanciesbot.importer;

import com.kyljmeeski.rabbitmqwrapper.Exchanges;
import com.kyljmeeski.rabbitmqwrapper.Queues;
import com.rabbitmq.client.ConnectionFactory;

public class Main {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        Exchanges exchanges = new Exchanges(factory);
        Queues queues = new Queues(factory);

    }

}
