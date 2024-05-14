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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kyljmeeski.plainhttps.BodyWrapper;
import com.kyljmeeski.plainhttps.MissingFieldException;
import com.kyljmeeski.plainhttps.Request;
import com.kyljmeeski.plainhttps.Response;
import com.kyljmeeski.plainhttps.request.GetRequest;
import com.kyljmeeski.rabbitmqwrapper.Producer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class TaskImportJob implements Consumer<String> {

    private final Producer producer;

    public TaskImportJob(Producer producer) {
        this.producer = producer;
    }

    @Override
    public void accept(String message) {
        TaskMessage taskMessage = new TaskMessage(message);
        int pages = taskMessage.pages();
        for (int i = 1; i <= pages; i++) {
            Request request = new GetRequest("devkg.com/api/pages/jobs?page=" + i);
            Response response = request.execute();
            if (response.status().code() == 200) {
                try {
                    JsonObject json = new BodyWrapper(response.body()).content(JsonObject.class);
                    JsonArray vacancies = json.getAsJsonObject("result").getAsJsonArray("list");
                    for (JsonElement element : vacancies) {
                        String slug = element.getAsJsonObject().get("slug").getAsString();
                        Vacancy vacancy = new Vacancy(slug);
                        try {
                            producer.produce(vacancy.toString());
                        } catch (IOException | TimeoutException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (MissingFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Consumer<String> andThen(Consumer<? super String> after) {
        return Consumer.super.andThen(after);
    }

}
