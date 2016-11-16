package com.scottcrocker.packify.model;

/**
 * Created by mavve on 2016-11-11.
 */

public class User {
    public User(int id, String name, int telephone, String password, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    private int id;
    private String name;
    private int telephone;
    private String password;
    private boolean isAdmin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
