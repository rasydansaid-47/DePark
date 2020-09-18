package com.example.depark;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class StatusLot {
    public String status;

    public StatusLot(){
    }

    public StatusLot(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
