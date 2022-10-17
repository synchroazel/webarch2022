package it.unitn.padalino.assignment2;

import java.io.Serializable;

public class UserBean implements Serializable {

    private static final long serialVersionUID = 1;
    private String username;
    private String password;
    private int sessionPoints;
    private boolean active;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return sessionPoints;
    }

    public void setPoints(int sessionPoints) {
        this.sessionPoints = sessionPoints;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) { this.active = active; }


    public String toString(boolean withPass) {
        if (withPass) {
            return "User: " + username + " | Password: " + password;
        } else {
            return "User: " + username + " | Score: " + sessionPoints;
        }
    }

}

