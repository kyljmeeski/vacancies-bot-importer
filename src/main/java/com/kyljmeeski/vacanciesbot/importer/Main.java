/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 Amir Syrgabaev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.kyljmeeski.vacanciesbot.importer;

import com.kyljmeeski.rabbitmqwrapper.*;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        try {
            Connection connection = factory.newConnection();

            Exchanges exchanges = new Exchanges(connection);
            Queues queues = new Queues(connection);

            try {
                RabbitExchange exchange = exchanges.declare("vacancies", "direct");
                RabbitQueue tasksQueue = queues.declare(
                        "vacancy-import-tasks", false, false, false, null
                );
                queues.declare(
                        "vacancies-to-store", false, false, false, null
                ).bind(exchange, "to-store");

                Producer producer = new PlainProducer(connection, exchange, "to-store");

                Consumer consumer = new PlainConsumer(connection, tasksQueue, new TaskImportJob(producer));
                consumer.startConsuming();
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

}
