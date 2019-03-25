package com.example.firebasecurdapp;

public class Contacts {

    private String name;
    private String number;
    private String key;

    public Contacts(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public Contacts() {
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
