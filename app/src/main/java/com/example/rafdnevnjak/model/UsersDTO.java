package com.example.rafdnevnjak.model;

import com.google.gson.Gson;

import java.util.ArrayList;

public class UsersDTO {
    private ArrayList<User> users;

    public UsersDTO() {
        users = new ArrayList<>();
    }

    /**
     * @param users JSON String that contains a list of User objects.
     * @return Returns a Java ArrayList of User objects.
     */
    public ArrayList<User> getUsersFromJSON(String users){
        Gson gs = new Gson();
        UsersDTO usersDTO = gs.fromJson(users, UsersDTO.class);

        return usersDTO.users;
    }
}
