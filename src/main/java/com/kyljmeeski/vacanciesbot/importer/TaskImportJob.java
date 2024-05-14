package com.kyljmeeski.vacanciesbot.importer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kyljmeeski.plainhttps.BodyWrapper;
import com.kyljmeeski.plainhttps.MissingFieldException;
import com.kyljmeeski.plainhttps.Request;
import com.kyljmeeski.plainhttps.Response;
import com.kyljmeeski.plainhttps.request.GetRequest;

import java.util.function.Consumer;

public class TaskImportJob implements Consumer<String> {

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
                    }

                } catch (MissingFieldException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public Consumer<String> andThen(Consumer<? super String> after) {
        return Consumer.super.andThen(after);
    }

}
