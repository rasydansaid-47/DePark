package com.example.depark;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Parking {
    public String author;
    public String lot;
    public String time;

    public Parking() {
    }

    public Parking(String author, String lot, String time) {
        this.author = author;
        this.lot = lot;
        this.time = time;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", author);
        result.put("lot", lot);
        result.put("time", time);

        return result;
    }
}
