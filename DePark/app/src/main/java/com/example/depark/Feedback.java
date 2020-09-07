package com.example.depark;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Feedback{

    public String author;
    public String body;
    public String typeFeedback;

    public Feedback() {
    }

    public Feedback(String author, String body, String typeFeedback) {
        this.author = author;
        this.body = body;
        this.typeFeedback = typeFeedback;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", author);
        result.put("body", body);
        result.put("type", typeFeedback);

        return result;
    }
}