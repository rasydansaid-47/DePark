package com.example.depark;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Feedback{

    public String author;
    public String body;
    private String typeFeedback;

    public Feedback() {
    }

    public Feedback(String author, String body, String typeFeedback) {
        this.author = author;
        this.body = body;
        this.typeFeedback = typeFeedback;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getTypeFeedback() {
        return typeFeedback;
    }
    public void setTypeFeedback(String typeFeedback) {
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