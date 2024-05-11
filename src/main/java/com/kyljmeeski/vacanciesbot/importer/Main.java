package com.kyljmeeski.vacanciesbot.importer;

import com.kyljmeeski.rabbitmqwrapper.Exchanges;
import com.kyljmeeski.rabbitmqwrapper.Queues;
import com.kyljmeeski.rabbitmqwrapper.RabbitExchange;
import com.kyljmeeski.rabbitmqwrapper.RabbitQueue;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        Exchanges exchanges = new Exchanges(factory);
        Queues queues = new Queues(factory);

        try {
            RabbitExchange exchange = exchanges.declare("vacancies", "direct");
            RabbitQueue queue = queues.declare(
                    "vacancy-import-tasks", false, false, false, null
            );
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException("Check RabbitMQ");
        }

    }

}
