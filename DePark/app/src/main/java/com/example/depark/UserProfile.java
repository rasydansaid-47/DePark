package com.example.depark;

public class UserProfile {
    public String userEmail;
    public String userName;

    public UserProfile(){
    }

    public UserProfile(String userName, String userEmail) {
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

class UserFeedback{
    public String userName;
    public String userEmail;
    public String userMessage;
    public String typeFeedback;

    public UserFeedback(){
    }

    public UserFeedback(String userName, String userEmail, String userMessage, String typeFeedback) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userMessage = userMessage;
        this.typeFeedback = typeFeedback;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getTypeFeedback(String type) {
        return typeFeedback;
    }

    public void setTypeFeedback(String typeFeedback) {
        this.typeFeedback = typeFeedback;
    }
}

class UserValet{
    public String typeCar;
    public String userCarPlate;
    public String time;

    public UserValet(){
    }

    public UserValet(String typeCar, String userCarPlate, String time) {
        this.typeCar = typeCar;
        this.userCarPlate = userCarPlate;
        this.time = time;
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
}