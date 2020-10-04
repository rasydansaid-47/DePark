package com.example.depark;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String ref;
    private String author;
    private String end;
    private String total;
    private String paymentState;

    public Request(){
    }

    public Request(String ref, String author, String time, String formatAmount, String state) {
        this.ref = ref;
        this.author = author;
        this.end = end;
        this.total = total;
        this.paymentState = paymentState;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ref", ref);
        result.put("author", author);
        result.put("time", end);
        result.put("total", total);
        result.put("paymentState", paymentState);

        return result;
    }
}
