package com.example.depark;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Valet{
    public String author;
    public String typeCar;
    public String userCarPlate;
    public String time;

    public Valet(){
    }

    public Valet(String author, String typeCar, String userCarPlate, String time) {
        this.author = author;
        this.typeCar = typeCar;
        this.userCarPlate = userCarPlate;
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public String getTypeCar() {
        return typeCar;
    }

    public void setTypeCar(String typeCar) {
        this.typeCar = typeCar;
    }

    public String getUserCarPlate() {
        return userCarPlate;
    }

    public void setUserCarPlate(String userCarPlate) {
        this.userCarPlate= userCarPlate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", author);
        result.put("type", typeCar);
        result.put("carplate", userCarPlate);
        result.put("time", time);

        return result;
    }
}
