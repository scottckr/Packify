package com.scottcrocker.packify.model;

/**
 * Created by mavve on 2016-11-11.
 */

public class User {
    public User() {

    }

    public User(int id, String password, String name, int telephone, boolean isAdmin) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.telephone = telephone;
        this.isAdmin = isAdmin;
    }

    public String toString() {
        String admin = "";
        if (isAdmin) {
            admin = ", ADMIN";
        }
        return "ID: " + getId() + ", namn: " + getName() + admin;
    }

    private int id;
    private String password;
    private String name;
    private int telephone;
    private boolean isAdmin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
