package com.kyljmeeski.vacanciesbot.importer;

public class TaskMessage {

    private final String source;

    public TaskMessage(String source) {
        this.source = source;
    }

    public int pages() {
        String[] parts = source.substring(1, source.length() - 1).split(":", 2);
        if (parts.length > 1) {
            return Integer.parseInt(parts[1].trim());
        }
        return 1;
    }

}
