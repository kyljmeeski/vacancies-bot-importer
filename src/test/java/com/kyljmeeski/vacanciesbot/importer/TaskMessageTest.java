package com.kyljmeeski.vacanciesbot.importer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskMessageTest {

    @Test
    public void test() {
        TaskMessage message = new TaskMessage("{\"pages\": 1}");
        assertEquals(1, message.pages());
    }

}