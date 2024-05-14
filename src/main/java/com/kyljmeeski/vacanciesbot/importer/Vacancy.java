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
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Vacancy {

    private final String slug;
    private final Document document;

    public Vacancy(String slug) {
        this.slug = slug;
        try {
            document = Jsoup.connect("https://devkg.com/ru/jobs/" + slug).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String id() {
        return slug;
    }

    public String title() {
        Element h1 = document.selectFirst("h1");
        if (h1 != null) {
            return h1.text();
        }
        return "";
    }

    public String company() {
        Element companyDiv = document.selectFirst("div.company");
        if (companyDiv != null) {
            Element company = companyDiv.selectFirst("strong");
            if (company != null) {
                return company.text();
            }
        }
        return "";
    }

    public String type() {
        Elements types = document.select("div.type");
        Element type = types.get(0).selectFirst("strong");
        if (type != null) {
            return type.text();
        }
        return "";
    }

    public String salary() {
        Element priceDiv = document.selectFirst("div.price");
        if (priceDiv != null) {
            return priceDiv.text();
        }
        return "";
    }

    public String description() {
        Element mainElement = document.selectFirst("main");
        if (mainElement != null) {
            return mainElement.html();
        }
        return "";
    }

    public Map<String, String> contacts() {
        Map<String, String> contacts = new HashMap<>();
        Elements types = document.select("div.type");
        for (int i = 1; i < types.size(); i++) {
            Element contactElement = types.get(i);
            try {
                String type = Objects.requireNonNull(contactElement.selectFirst("span")).text().trim();
                String contact;
                if (type.equals("E-Mail")) {
                    continue;
                } else {
                    contact = Objects.requireNonNull(contactElement.selectFirst("a")).text();
                }
                contacts.put(
                        type, contact
                );
            } catch (NullPointerException ignored) {}
        }
        return contacts;
    }

    public String link() {
        return "https://devkg.com/ru/jobs/" + slug;
    }

    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id());
        json.addProperty("company", company());
        json.addProperty("type", type());
        json.addProperty("salary", salary());
        json.addProperty("description", description());
        JsonArray contacts = new JsonArray();
        for (Map.Entry<String, String> entry : contacts().entrySet()) {
            JsonObject contact = new JsonObject();
            contact.addProperty("type", entry.getKey());
            contact.addProperty("contact", entry.getValue());
            contacts.add(contact);
        }
        json.add("contacts", contacts);
        return json.toString();
    }

}
