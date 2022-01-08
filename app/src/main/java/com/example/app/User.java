package com.example.app;

public class User {
    public String fullName, email, type, number;
    public String rating;

    public User()
    {

    }

    public User(String fullName, String email, String type, String number)
    {
        this.fullName = fullName;
        this.email = email;
        this.type = type;
        this.number = number;
        this.rating = "0.0";
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }
}
