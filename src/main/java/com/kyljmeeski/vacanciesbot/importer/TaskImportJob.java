package com.kyljmeeski.vacanciesbot.importer;

import java.util.function.Consumer;

public class TaskImportJob implements Consumer<String> {

    @Override
    public void accept(String message) {
        System.out.println(message);
    }

    @Override
    public Consumer<String> andThen(Consumer<? super String> after) {
        return Consumer.super.andThen(after);
    }

}
