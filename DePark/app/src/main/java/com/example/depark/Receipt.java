package com.example.depark;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Receipt {

    private String ref;
    private String author;
    private String lot;
    private String start;
    private String end;
    private String total;
    private String paymentState;

    public Receipt(){
    }

    public Receipt(String ref, String author, String lot, String start, String end, String total, String paymentState) {
        this.ref = ref;
        this.author = author;
        this.lot = lot;
        this.start = start;
        this.end = end;
        this.total = total;
        this.paymentState = paymentState;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
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
        result.put("lot", lot);
        result.put("start", start);
        result.put("end", end);
        result.put("total", total);
        result.put("paymentState", paymentState);

        return result;
    }
}
