package com.example.finalprojectmobile.models;

public class Item {
    private User user;
    private String name, description;
    private int quantity;
    private byte[] image;

    public Item(User user, String name, String description, int quantity, byte[] image) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
