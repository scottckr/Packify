package com.scottcrocker.packify.model;

/**
 * User model class.
 */
public class User {

    private int id;
    private String password;
    private String name;
    private String telephone;
    private boolean isAdmin;

    /**
     * Empty constructor for User class.
     */
    public User() {

    }

    /**
     * Constructor for User class which takes a few arguments.
     *
     * @param id        An id to put into the id variable.
     * @param password  A password to put into the password variable.
     * @param name      A name to put into the name variable.
     * @param telephone A telephone number to put into the telephone variable.
     * @param isAdmin   A boolean to determine if a User is an admin or not, put into the isAdmin variable.
     */
    public User(int id, String password, String name, String telephone, boolean isAdmin) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.telephone = telephone;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        String admin = "";
        if (isAdmin) {
            admin = ", ADMIN";
        }
        return "ID: " + getId() + ", namn: " + getName() + admin;
    }

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
